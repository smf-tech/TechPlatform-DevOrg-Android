package com.platform.view.fragments;


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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormStatusCallListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.adapters.ExpandableAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("EmptyMethod")
public class AllFormsFragment extends Fragment implements FormStatusCallListener {

    private static final String TAG = AllFormsFragment.class.getSimpleName();
    private final Map<String, List<ProcessData>> mChildList = new HashMap<>();
    private TextView mNoRecordsView;
    private Map<String, String> mCountList;
    private ExpandableAdapter adapter;

    public AllFormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllFormsFragment.
     */
    static AllFormsFragment newInstance() {
        return new AllFormsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoRecordsView = view.findViewById(R.id.no_records_view);
        mCountList = new HashMap<>();

        ExpandableListView expandableListView = view.findViewById(R.id.forms_expandable_list);
        adapter = new ExpandableAdapter(getContext(), mChildList, mCountList);
        expandableListView.setAdapter(adapter);

        getProcessData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_FORM_ADDED);
        filter.addAction(EVENT_FORM_SUBMITTED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = Objects.requireNonNull(intent.getAction());
                switch (action) {
                    case EVENT_FORM_ADDED:
                    case EVENT_FORM_SUBMITTED:
                        getProcessData();
                        break;
                }
            }
        }, filter);
    }

    private void getProcessData() {
        try {
            List<ProcessData> processDataArrayList = DatabaseManager.getDBInstance(getActivity()).getAllProcesses();
            if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
                Processes processes = new Processes();
                processes.setData(processDataArrayList);
                processResponse(processes);
            } else {
                if (Util.isConnected(getContext())) {
                    FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
                    presenter.getAllProcesses();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION : getProcessData()");
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "onErrorListener: " + error.getMessage());
        Util.showToast(error.getMessage(), getContext());
    }

    @Override
    public void onFormsLoaded(String response) {
        try {
            Processes json = new Gson().fromJson(response, Processes.class);
            if (json != null && json.getData() != null && !json.getData().isEmpty()) {
                for (ProcessData processData :
                        json.getData()) {
                    DatabaseManager.getDBInstance(getContext()).insertProcessData(processData);
                }
                processResponse(json);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void processResponse(final Processes json) {
        mCountList.clear();
        mChildList.clear();

        FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);

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

            List<String> localFormResults = DatabaseManager.getDBInstance(getActivity()).getAllFormResults(data.getId());

            String url;

            if (Util.isConnected(getContext()) && ((submitCount != null &&
                    !submitCount.equals("0")) && localFormResults.isEmpty())) {

                if (data.getMicroservice() != null && !TextUtils.isEmpty(data.getMicroservice().getBaseUrl())
                        && !TextUtils.isEmpty(data.getMicroservice().getRoute())) {

                    url = getResources().getString(R.string.form_field_mandatory, data.getMicroservice().getBaseUrl(),
                            data.getMicroservice().getRoute());
                    presenter.getSubmittedForms(data.getId(), url);
                }
            } else if ((submitCount == null || submitCount.equals("0")) &&
                    (localFormResults == null || localFormResults.isEmpty())) {

                if (!Util.isSubmittedFormsLoaded() && Util.isConnected(getContext())) {
                    if (data.getMicroservice() != null && !TextUtils.isEmpty(data.getMicroservice().getBaseUrl())
                            && !TextUtils.isEmpty(data.getMicroservice().getRoute())) {

                        url = getResources().getString(R.string.form_field_mandatory, data.getMicroservice().getBaseUrl(),
                                data.getMicroservice().getRoute());
                        presenter.getSubmittedForms(data.getId(), url);
                    }
                }
            }
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {
        try {
            String count;
            JSONObject metadataObj = null;
            if (new JSONObject(response).has(Constants.FormDynamicKeys.METADATA)) {
                JSONArray metadata = (JSONArray) new JSONObject(response).get(Constants.FormDynamicKeys.METADATA);
                if (metadata != null && metadata.length() > 0) {
                    metadataObj = metadata.getJSONObject(0);
                    count = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.SUBMIT_COUNT);
                    String formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM).getString(Constants.FormDynamicKeys.FORM_ID);
                    mCountList.put(formID, count);
                    DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                            .getApplicationContext()).updateProcessSubmitCount(formID, count);
                } else {
                    mCountList.put(formId, String.valueOf(0));
                    DatabaseManager.getDBInstance(Objects.requireNonNull(getActivity())
                            .getApplicationContext()).updateProcessSubmitCount(formId, String.valueOf(0));
                }
            }

            Util.setSubmittedFormsLoaded(true);

            if (new JSONObject(response).has(Constants.FormDynamicKeys.VALUES)) {
                JSONArray values = new JSONObject(response).getJSONArray(Constants.FormDynamicKeys.VALUES);

                for (int i = 0; i < values.length(); i++) {
                    SubmittedFormsFragment.FormResult formResult = PlatformGson.getPlatformGsonInstance().fromJson(String.valueOf(values.get(i)),
                            SubmittedFormsFragment.FormResult.class);

                    String uuid = UUID.randomUUID().toString();
                    String formID = formResult.formID;
                    if (TextUtils.isEmpty(formID)) {
                        if (metadataObj != null) {
                            formID = metadataObj.getJSONObject(Constants.FormDynamicKeys.FORM)
                                    .getString(Constants.FormDynamicKeys.FORM_ID);
                        }
                    }

                    com.platform.models.forms.FormResult result = new com.platform.models.forms.FormResult();
                    if (formResult.mOID.oid != null) {
                        result.set_id(formResult.mOID.oid);
                        result.setOid(formResult.mOID.oid);
                    } else {
                        result.set_id(uuid);
                    }
                    result.setFormId(formID);
                    result.setFormStatus(SyncAdapterUtils.FormStatus.SYNCED);
                    result.setCreatedAt(formResult.updatedDateTime);

                    JSONObject obj = (JSONObject) values.get(i);
                    if (obj == null) return;

                    if (!obj.has(Constants.FormDynamicKeys.FORM_ID)) {
                        obj.put(Constants.FormDynamicKeys.FORM_ID, formID);
                    }

                    List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                            .getAllFormResults(formID, SyncAdapterUtils.FormStatus.SYNCED);
                    if (!localFormResults.contains(obj.toString())) {
                        result.setResult(obj.toString());
                        DatabaseManager.getDBInstance(getActivity()).insertFormResult(result);
                    }

                }

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (!mChildList.isEmpty()) {
            setAdapter(mChildList);
            mNoRecordsView.setVisibility(View.GONE);
        } else {
            mNoRecordsView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(final Map<String, List<ProcessData>> data) {
        if (data != null && !data.isEmpty()) {
            adapter.notifyDataSetChanged();
            mNoRecordsView.setVisibility(View.GONE);
        }
    }
}
