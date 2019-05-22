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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.FormStatusCallListener;
import com.platform.listeners.FormTaskListener;
import com.platform.models.LocaleData;
import com.platform.models.common.Microservice;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.FormStatusFragmentPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.adapters.SubmittedFormsListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_REMOVED;
import static com.platform.utility.Constants.FORM_DATE_FORMAT;

@SuppressWarnings("CanBeFinal")
public class SubmittedFormsFragment extends Fragment implements FormStatusCallListener, FormTaskListener {

    private static final String TAG = SubmittedFormsFragment.class.getSimpleName();

    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();
    private HashMap<String, List<ProcessData>> mProcessDataMap = new HashMap<>();

    private boolean showNoDataText = true;
    private View dividerView;
    private TextView mNoRecordsView;
    private ExpandableListView mExpandableListView;
    private RelativeLayout mPendingFormsView;
    private RelativeLayout progressBarLayout;
    private LinearLayout mPendingFormsContainer;
    private TextView mSubmittedFormsTitleView;
    private ProgressBar progressBar;

    public SubmittedFormsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoRecordsView = view.findViewById(R.id.no_records_view);
        mExpandableListView = view.findViewById(R.id.forms_expandable_list);

        progressBarLayout = FormsFragment.getProgressBarView();
        progressBar = FormsFragment.getProgressBar();

        dividerView = view.findViewById(R.id.submitted_forms_divider);
        mPendingFormsView = view.findViewById(R.id.pending_forms_view);
        mPendingFormsContainer = view.findViewById(R.id.pending_forms_container);

        ((TextView) view.findViewById(R.id.pending_form_category_name))
                .setText(getString(R.string.syncing_pending));

        mSubmittedFormsTitleView = view.findViewById(R.id.submitted_form_category_name);
        mSubmittedFormsTitleView.setText(getString(R.string.submitted_and_synced));

        view.findViewById(R.id.sync_button).setOnClickListener(v -> {
            if (Util.isConnected(getContext())) {
                Util.showToast(getString(R.string.sync_started), getContext());
                SyncAdapterUtils.manualRefresh();
            } else {
                Util.showToast(getString(R.string.msg_no_network), getContext());
            }
        });

        setPendingForms();
        getProcessData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(PARTIAL_FORM_REMOVED);
        filter.addAction(EVENT_FORM_SUBMITTED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = Objects.requireNonNull(intent.getAction());
                if (action.equals(EVENT_SYNC_COMPLETED)) {
                    Util.showToast(getString(R.string.sync_completed), context);
                    getProcessData();
                } else if (action.equals(PARTIAL_FORM_REMOVED) || action.equals(EVENT_FORM_SUBMITTED)) {
                    getProcessData();
                } else if (intent.getAction().equals(EVENT_SYNC_FAILED)) {
                    Log.e(TAG, "Sync failed!");
                    Util.showToast(getString(R.string.sync_failed), context);
                }
                setPendingForms();
            }
        }, filter);
    }

    private void getProcessData() {
        try {
            List<ProcessData> processDataArrayList = DatabaseManager
                    .getDBInstance(getActivity()).getAllProcesses();
            if (processDataArrayList != null && !processDataArrayList.isEmpty()) {
                Processes processes = new Processes();
                processes.setData(processDataArrayList);
                populateData(processes);
            } else {
                showNoDataText = true;
                if (Util.isConnected(getContext())) {
                    FormStatusFragmentPresenter presenter = new FormStatusFragmentPresenter(this);
                    presenter.getAllProcesses();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }

        updateView();
    }

    private void setPendingForms() {
        List<com.platform.models.forms.FormResult> savedForms = getAllNonSyncedSavedForms(getContext());
        if (savedForms != null && !savedForms.isEmpty()) {

            mPendingFormsContainer.removeAllViews();
            mPendingFormsView.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.VISIBLE);

            showNoDataText = false;

            for (com.platform.models.forms.FormResult formResult : savedForms) {
                ProcessData object = new ProcessData();
                object.setId(formResult.getFormId());
                object.setFormTitle(formResult.getFormTitle());
                object.setName(new LocaleData(formResult.getFormName()));
                Microservice microservice = new Microservice();
                microservice.setUpdatedAt(formResult.getCreatedAt());
                microservice.setId(formResult.get_id());
                object.setMicroservice(microservice);

                if (getContext() == null) {
                    continue;
                }

                View formView = LayoutInflater.from(getContext())
                        .inflate(R.layout.form_sub_item, mPendingFormsContainer, false);

                ((TextView) formView.findViewById(R.id.form_title))
                        .setText(object.getName().getLocaleValue());

                ((TextView) formView.findViewById(R.id.form_date))
                        .setText(Util.getDateFromTimestamp(object.getMicroservice().getUpdatedAt()));

                int bgColor = getResources().getColor(R.color.red);
                formView.findViewById(R.id.form_status_indicator).setBackgroundColor(bgColor);

                formView.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), FormActivity.class);
                    intent.putExtra(Constants.PM.FORM_ID, object.getId());
                    intent.putExtra(Constants.PM.PROCESS_ID, object.getMicroservice().getId());
                    intent.putExtra(Constants.PM.EDIT_MODE, true);
                    intent.putExtra(Constants.PM.PARTIAL_FORM, true);
                    startActivity(intent);
                });
                mPendingFormsContainer.addView(formView);
            }
        } else {
            mPendingFormsView.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
        }
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();
            mProcessDataMap.clear();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null &&
                        !TextUtils.isEmpty(data.getCategory().getName().getLocaleValue())) {

                    String categoryName = data.getCategory().getName().getLocaleValue();
                    if (processMap.containsKey(categoryName) && processMap.get(categoryName) != null) {
                        List<ProcessData> processData = processMap.get(categoryName);
                        if (processData != null) {
                            processData.add(data);
                        }
                        processMap.put(categoryName, processData);
                    } else {
                        List<ProcessData> processData = new ArrayList<>();
                        processData.add(data);
                        processMap.put(categoryName, processData);
                        processCategoryList.add(categoryName);
                    }
                }
            }

            for (int index = 0; index < processMap.size(); index++) {

                List<ProcessData> pData = processMap.get(processCategoryList.get(index));
                if (!TextUtils.isEmpty(processCategoryList.get(index)) && pData != null) {

                    String formID;
                    for (final ProcessData data : pData) {

                        List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
                                .getAllFormResults(data.getId(), SyncAdapterUtils.FormStatus.SYNCED);

                        ProcessData pd = DatabaseManager.getDBInstance(
                                Objects.requireNonNull(getActivity()).getApplicationContext())
                                .getProcessData(data.getId());

                        String submitCount = pd.getSubmitCount();
                        if (submitCount != null && !submitCount.equals("0") && localFormResults.isEmpty()) {
                            if (Util.isConnected(getContext())) {
                                String url;
                                if (data.getMicroservice() != null
                                        && !TextUtils.isEmpty(data.getMicroservice().getBaseUrl())
                                        && !TextUtils.isEmpty(data.getMicroservice().getRoute())) {

                                    url = getResources().getString(R.string.form_field_mandatory,
                                            data.getMicroservice().getBaseUrl(),
                                            data.getMicroservice().getRoute());

                                    new FormStatusFragmentPresenter(this).getSubmittedForms(data.getId(), url);
                                }
                            }
                        } else {

                            if (localFormResults == null || localFormResults.isEmpty()) continue;

                            List<ProcessData> processData = new ArrayList<>();

                            for (final String result : localFormResults) {
                                FormResult formResult = PlatformGson.getPlatformGsonInstance()
                                        .fromJson(result, FormResult.class);

                                if (formResult.updatedDateTime != null) {
                                    if (isFormOneMonthOld(formResult.updatedDateTime)) {
                                        continue;
                                    }
                                }

                                formID = formResult.formID;

                                ProcessData object = new ProcessData();
                                if (formResult.mOID != null && formResult.mOID.oid != null) {
                                    object.setId(formResult.mOID.oid);
                                }

                                object.setFormTitle(data.getName().getLocaleValue());
                                object.setName(new LocaleData(formResult.formTitle));
                                Microservice microservice = new Microservice();
                                microservice.setUpdatedAt(formResult.updatedDateTime);
                                microservice.setId(formID);
                                object.setMicroservice(microservice);
                                processData.add(object);
                            }

                            if (!processData.isEmpty()) {
                                Util.sortProcessDataListByCreatedDate(processData);
                                mProcessDataMap.put(processData.get(0).getFormTitle(), processData);
                                showNoDataText = false;
                            }
                        }
                    }
                }
            }

            SubmittedFormsListAdapter adapter = new SubmittedFormsListAdapter(getContext(), mProcessDataMap);
            mExpandableListView.setAdapter(adapter);

            updateView();
        }
    }

    private void updateView() {
        mNoRecordsView.setVisibility(showNoDataText ? View.VISIBLE : View.GONE);
        if (showNoDataText) {
            mSubmittedFormsTitleView.setVisibility(View.GONE);
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
            if (error.networkResponse.statusCode == 504) {
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
        hideProgressBar();
        Processes json = new Gson().fromJson(response, Processes.class);
        if (json != null && json.getData() != null && !json.getData().isEmpty()) {
            showNoDataText = false;

            for (ProcessData processData : json.getData()) {
                DatabaseManager.getDBInstance(getContext()).insertProcessData(processData);
            }

            populateData(json);
        }
    }

    @Override
    public void onMastersFormsLoaded(final String response, final String formId) {

    }

    private boolean isFormOneMonthOld(final Long updatedAt) {
        if (updatedAt != null) {
            Date eventStartDate;
            DateFormat inputFormat = new SimpleDateFormat(FORM_DATE_FORMAT, Locale.getDefault());
            try {
                eventStartDate = inputFormat.parse(Util.getDateFromTimestamp(updatedAt));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                Date days30 = calendar.getTime();
                return eventStartDate.before(days30);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return false;
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

    static class FormResult {
        @SuppressWarnings("unused")
        @SerializedName("form_title")
        String formTitle;

        @SuppressWarnings("unused")
        @SerializedName("form_id")
        String formID;

        @SuppressWarnings("unused")
        @SerializedName("updatedDateTime")
        Long updatedDateTime;

        @SuppressWarnings("unused")
        @SerializedName("_id")
        OID mOID;
    }

    public static class OID {
        @SuppressWarnings("unused")
        @SerializedName("$oid")
        String oid;

        public OID(String defaultValue) {
            this.oid = defaultValue;
        }

        public String getOID() {
            return oid;
        }
    }
}
