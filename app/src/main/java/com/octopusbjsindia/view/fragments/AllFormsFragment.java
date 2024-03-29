package com.octopusbjsindia.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.FormStatusCallListener;
import com.octopusbjsindia.listeners.FormTaskListener;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.models.pm.Processes;
import com.octopusbjsindia.presenter.FormStatusFragmentPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.ExpandableAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;

@SuppressWarnings("EmptyMethod")
public class AllFormsFragment extends Fragment implements FormStatusCallListener, FormTaskListener {

    private static final String TAG = AllFormsFragment.class.getSimpleName();
    private final Map<String, List<ProcessData>> mChildList = new HashMap<>();
    private ImageView imgNoData;
    private Map<String, String> mCountList;
    private ExpandableAdapter adapter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    private static int mSubmittedFormsCount = 0;
    private static int mSubmittedFormsDownloadedCount = 0;

    public AllFormsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }

        AppEvents.trackAppEvent(getString(R.string.event_all_forms_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgNoData = view.findViewById(R.id.img_no_data);
        mCountList = new HashMap<>();

        progressBarLayout = FormsFragment.getProgressBarView();
        progressBar = FormsFragment.getProgressBar();

        ExpandableListView expandableListView = view.findViewById(R.id.forms_expandable_list);
        adapter = new ExpandableAdapter(getContext(), mChildList, mCountList);
        expandableListView.setAdapter(adapter);

        getProcessData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_FORM_SUBMITTED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()))
                .registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(final Context context, final Intent intent) {
                        String action = Objects.requireNonNull(intent.getAction());
                        if (EVENT_FORM_SUBMITTED.equals(action)) {
                            getProcessData();
                        }
                    }
                }, filter);
    }

    private void getProcessData() {
        try {
            List<ProcessData> processDataArrayList =
                    DatabaseManager.getDBInstance(getActivity()).getAllProcesses();

            if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
                Processes processes = new Processes();
                processes.setData(processDataArrayList);
                processResponse(processes);
            } else {
                imgNoData.setVisibility(View.VISIBLE);
                if (Util.isConnected(getContext())) {
                    FormStatusFragmentPresenter presenter
                            = new FormStatusFragmentPresenter(this, this);
                    presenter.getAllProcesses();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION : getProcessData()");
        }
    }

    @Override
    public void onFailureListener(String message) {
        hideProgressBar();
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        hideProgressBar();
        Log.e(TAG, "onErrorListener: " + error.getMessage());

        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                if (error.networkResponse.data != null) {
                    String json = new String(error.networkResponse.data);
                    json = Util.trimMessage(json);
                    if (json != null) {
                        Util.showToast(json, this.getActivity());
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                this.getActivity());
                    }
                } else {
                    Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                            this.getActivity());
                }
            } else {
                Util.showToast(this.getString(R.string.unexpected_error_occurred), this.getActivity());
                Log.e("onErrorListener",
                        "Unexpected response code " + error.networkResponse.statusCode);
            }
        }
    }

    @Override
    public void onFormsLoaded(String response) {
        try {
            Processes json = new Gson().fromJson(response, Processes.class);
            if (json != null && json.getData() != null && !json.getData().isEmpty()) {
                imgNoData.setVisibility(View.GONE);
                for (ProcessData processData : json.getData()) {
                    DatabaseManager.getDBInstance(getContext()).insertProcessData(processData);
                }
                //processResponse(json);
                List<ProcessData> processDataArrayList =
                        DatabaseManager.getDBInstance(getActivity()).getAllProcesses();

                if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
                    Processes processes = new Processes();
                    processes.setData(processDataArrayList);
                    processResponse(processes);
                }
            }

            hideProgressBar();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            hideProgressBar();
            Util.showToast(this.getString(R.string.unexpected_error_occurred), this.getActivity());
        }
    }

    private void processResponse(final Processes json) {
        mCountList.clear();
        mChildList.clear();

        FormStatusFragmentPresenter presenter
                = new FormStatusFragmentPresenter(this, this);

        for (ProcessData data : json.getData()) {
            String categoryName = data.getCategory().getName().getLocaleValue();
            if (mChildList.containsKey(categoryName)) {
                List<ProcessData> processData = mChildList.get(categoryName);
                if (processData != null) {
                    processData.add(data);
                    mChildList.put(categoryName, processData);
                }
            } else {
                List<ProcessData> processData = new ArrayList<>();
                processData.add(data);
                mChildList.put(categoryName, processData);
            }

            ProcessData processData = DatabaseManager.getDBInstance(
                    Objects.requireNonNull(getContext()))
                    .getProcessData(data.getId());

            String submitCount = processData.getSubmitCount();
            if (!TextUtils.isEmpty(submitCount)) {
                mCountList.put(data.getId(), submitCount);
            }

            // For now, we dont need submitted forms, so commenting below code.
//            List<String> localFormResults
//                    = DatabaseManager.getDBInstance(getActivity()).getAllFormResults(data.getId());

            setSubmittedFormsCount();

            if (Util.isConnected(getActivity())) {
                presenter.getSubmittedForms(data.getId(), BuildConfig.BASE_URL +
                        String.format(Urls.PM.GET_SUBMITTED_FORMS, data.getId()));
            }
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            imgNoData.setVisibility(View.GONE);
        } else {
            imgNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
        hideProgressBar();
        mSubmittedFormsDownloadedCount++;
        try {
            String count;
            if (new JSONObject(response).has(Constants.FormDynamicKeys.DATA)) {
                JSONObject dataObject = new JSONObject(response).getJSONObject(Constants.FormDynamicKeys.DATA);
                JSONObject metadataObj = null;
                if (dataObject.has(Constants.FormDynamicKeys.METADATA)) {
                    JSONArray metadata = (JSONArray) dataObject.get(Constants.FormDynamicKeys.METADATA);
                    if (metadata != null && metadata.length() > 0) {
                        metadataObj = metadata.getJSONObject(0);

                        count = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
                                .getString(Constants.FormDynamicKeys.SUBMIT_COUNT);

                        String formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
                                .getString(Constants.FormDynamicKeys.FORM_ID);

                        mCountList.put(formID, count);
                        DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                                .getApplicationContext()).updateProcessSubmitCount(formID, count);
                    } else {
                        mCountList.put(formId, String.valueOf(0));
                        DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                                .getApplicationContext()).updateProcessSubmitCount(formId, String.valueOf(0));
                    }
                }
                if (dataObject.has(Constants.FormDynamicKeys.VALUES)) {
                    JSONArray values = dataObject.getJSONArray(Constants.FormDynamicKeys.VALUES);

                    for (int i = 0; i < values.length(); i++) {
//                    SubmittedFormsFragment.FormResult formResult =
//                            PlatformGson.getPlatformGsonInstance().fromJson(String.valueOf(values.get(i)),
//                                    SubmittedFormsFragment.FormResult.class);
//
//                    String uuid = UUID.randomUUID().toString();
//                    String formID = formResult.formID;
//                    if (TextUtils.isEmpty(formID)) {
//                        if (metadataObj != null) {
//                            formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
//                                    .getString(Constants.FormDynamicKeys.FORM_ID);
//                        }
//                    }
//
//                    com.octopusbjsindia.models.forms.FormResult result = new com.octopusbjsindia.models.forms.FormResult();
//                    if (formResult.mOID.oid != null) {
//                        result.set_id(formResult.mOID.oid);
//                        result.setOid(formResult.mOID.oid);
//                    } else {
//                        result.set_id(uuid);
//                    }
//                    result.setFormId(formID);
//                    result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
//                    result.setCreatedAt(formResult.updatedDateTime);
//                    result.setFormApprovalStatus(formResult.formStatus);
//
//                    JSONObject obj = (JSONObject) values.get(i);
//                    if (obj == null) {
//                        return;
//                    }
//
//                    if (!obj.has(Constants.FormDynamicKeys.FORM_ID)) {
//                        obj.put(Constants.FormDynamicKeys.FORM_ID, formID);
//                    }
                        JSONObject resultObject = values.getJSONObject(i);
                        String resultId = resultObject.getString(Constants.FormDynamicKeys.OID);
                        com.octopusbjsindia.models.forms.FormResult tempResult = DatabaseManager.getDBInstance
                                (getActivity()).getFormResult(resultId);
                        if (tempResult != null) {
                            if (tempResult.getFormApprovalStatus().equalsIgnoreCase(resultObject.getString
                                    (Constants.FormDynamicKeys.STATUS))) {
                                continue;
                            } else {
                                tempResult.setFormApprovalStatus(resultObject.getString(Constants.FormDynamicKeys.STATUS));
                                tempResult.setRejectionReason(resultObject.getString(Constants.FormDynamicKeys.REJECTION_REASON));
                                tempResult.setCreatedAt(resultObject.getLong(Constants.FormDynamicKeys.UPDATED_DATE_TIME));
                                DatabaseManager.getDBInstance(getActivity()).updateFormResult(tempResult);
                                continue;
                            }
                        }
                        String uuid = UUID.randomUUID().toString();
                        String formID = resultObject.getString(Constants.FormDynamicKeys.FORM_ID);
                        if (TextUtils.isEmpty(formID)) {
                            if (metadataObj != null) {
                                formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
                                        .getString(Constants.FormDynamicKeys.FORM_ID);
                            }
                        }
                        com.octopusbjsindia.models.forms.FormResult result = new com.octopusbjsindia.models.forms.FormResult();
                        if (resultObject.getString(Constants.FormDynamicKeys.OID) != null) {
                            result.set_id(resultObject.getString(Constants.FormDynamicKeys.OID));
                            result.setOid(resultObject.getString(Constants.FormDynamicKeys.OID));
                        } else {
                            result.set_id(uuid);
                        }
                        result.setFormId(formID);
                        result.setFormTitle(resultObject.getString(Constants.FormDynamicKeys.FORM_TITLE));
                        result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                        result.setCreatedAt(resultObject.getLong(Constants.FormDynamicKeys.UPDATED_DATE_TIME));
                        result.setFormApprovalStatus(resultObject.getString(Constants.FormDynamicKeys.STATUS));
                        result.setRejectionReason(resultObject.getString(Constants.FormDynamicKeys.REJECTION_REASON));
                        result.setResult(resultObject.getString(Constants.FormDynamicKeys.RESULT));
                        DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            hideProgressBar();
            Util.showToast(this.getString(R.string.unexpected_error_occurred), this.getActivity());
        } finally {
            Util.setSubmittedFormsLoaded(true);
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            imgNoData.setVisibility(View.GONE);
        } else {
            imgNoData.setVisibility(View.VISIBLE);
        }

        if (mSubmittedFormsDownloadedCount == mSubmittedFormsCount) {
            hideProgressBar();
            mSubmittedFormsCount = 0;
            mSubmittedFormsDownloadedCount = 0;
        }
    }

    private void setAdapter(final Map<String, List<ProcessData>> data) {
        if (data != null && !data.isEmpty()) {
            adapter.notifyDataSetChanged();
            imgNoData.setVisibility(View.GONE);
        }
    }

    private static void setSubmittedFormsCount() {
        mSubmittedFormsCount++;
    }

    @Override
    public void showProgressBar() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null
                    && progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }
}
