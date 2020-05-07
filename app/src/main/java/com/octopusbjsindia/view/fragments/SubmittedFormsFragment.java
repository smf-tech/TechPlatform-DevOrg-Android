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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.FormStatusCallListener;
import com.octopusbjsindia.listeners.FormTaskListener;
import com.octopusbjsindia.models.LocaleData;
import com.octopusbjsindia.models.common.Microservice;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.models.pm.Processes;
import com.octopusbjsindia.presenter.FormStatusFragmentPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SubmittedFormsListAdapter;

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

import static com.octopusbjsindia.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_FORM_SUBMITTED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_REMOVED;
import static com.octopusbjsindia.utility.Constants.FORM_DATE_FORMAT;

@SuppressWarnings("CanBeFinal")
public class SubmittedFormsFragment extends Fragment implements FormStatusCallListener, FormTaskListener, View.OnClickListener {

    private static final String TAG = SubmittedFormsFragment.class.getSimpleName();

    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();
    private HashMap<String, List<ProcessData>> mProcessDataMap = new HashMap<>();
    private HashMap<String, List<ProcessData>> mFilteredProcessDataMap = new HashMap<>();
    private ArrayList<String> processFormList = new ArrayList<>();
    private ArrayList<String> processSyncStatus = new ArrayList<>();

    private boolean showNoDataText = true;
    private TextView mNoRecordsView;
    private ExpandableListView mExpandableListView;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private SubmittedFormsListAdapter adapter;
    private FloatingActionButton btnFilter;//,btnNoFilter,btnFilter1,btnFilter2,btnFilter3;
    private ExtendedFloatingActionButton btnNoFilter, btnFilter1, btnFilter2, btnFilter3;
    private boolean isFABOpen = false;
    private ArrayList<String> unSyncProcessNameList = new ArrayList<>();
    private HashMap<String, String> processSyncStatusHashmap = new HashMap<>();
    private int selectedFilter = 0;
    private Animation animFadeIn, animFadeOut;

    // Here we show unsync forms seperately, but now we are showing unsync forms and sync forms in same expandable list view and so
    // commenting this code for now.

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
        btnFilter = view.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(this);
        btnNoFilter = view.findViewById(R.id.btn_no_filter);
        btnNoFilter.setOnClickListener(this);
        btnFilter1 = view.findViewById(R.id.btn_filter1);
        btnFilter1.setOnClickListener(this);
        btnFilter2 = view.findViewById(R.id.btn_filter2);
        btnFilter2.setOnClickListener(this);
        btnFilter3 = view.findViewById(R.id.btn_filter3);
        btnFilter3.setOnClickListener(this);

        progressBarLayout = FormsFragment.getProgressBarView();
        progressBar = FormsFragment.getProgressBar();

        adapter = new SubmittedFormsListAdapter(getContext(), mFilteredProcessDataMap,processSyncStatusHashmap);
        mExpandableListView.setAdapter(adapter);

        // Here we show unsync forms seperately, but now we are showing unsync forms and sync forms in same expandable list view and so

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

        animFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
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
        mProcessDataMap.clear();
        List<com.octopusbjsindia.models.forms.FormResult> savedForms = getAllNonSyncedSavedForms(getContext());
        if (savedForms != null && !savedForms.isEmpty()) {

            showNoDataText = false;
            for (com.octopusbjsindia.models.forms.FormResult formResult : savedForms) {
                if (!unSyncProcessNameList.contains(formResult.getFormName())) {
                    unSyncProcessNameList.add(formResult.getFormName());
                }
            }

            for (String processName : unSyncProcessNameList) {

                List<ProcessData> unSyncProcessData = new ArrayList<>();

            for (com.octopusbjsindia.models.forms.FormResult formResult : savedForms) {
                if (formResult.getFormName().equalsIgnoreCase(processName)){
                    ProcessData object = new ProcessData();
                object.setId(formResult.getFormId());
                object.setFormTitle(formResult.getFormTitle());
                object.setName(new LocaleData(formResult.getFormName()));
                object.setFormApprovalStatus(Constants.PM.UNSYNC_STATUS);
                Microservice microservice = new Microservice();
                microservice.setUpdatedAt(formResult.getCreatedAt());
                microservice.setId(formResult.get_id());
                object.setMicroservice(microservice);

                if (getContext() == null) {
                    continue;
                }

                unSyncProcessData.add(object);
            }
            }
                if (!unSyncProcessData.isEmpty()) {
                    Util.sortProcessDataListByCreatedDate(unSyncProcessData);

                    mProcessDataMap.put(unSyncProcessData.get(0).getName().getLocaleValue(), unSyncProcessData);
                    processFormList.add(unSyncProcessData.get(0).getName().getLocaleValue());
                    processSyncStatus.add(Constants.PM.UNSYNC_STATUS);
                    processSyncStatusHashmap.put(unSyncProcessData.get(0).getName().getLocaleValue(),Constants.PM.UNSYNC_STATUS);
                    showNoDataText = false;
                }
        }

        }
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();
            mFilteredProcessDataMap.clear();

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

//                        List<String> localFormResults = DatabaseManager.getDBInstance(getActivity())
//                                .getAllFormResults(data.getId(), SyncAdapterUtils.FormStatus.SYNCED);

                        List<com.octopusbjsindia.models.forms.FormResult> localFormResults = DatabaseManager.getDBInstance(getActivity())
                                .getFormResults(data.getId(), SyncAdapterUtils.FormStatus.SYNCED);

                        ProcessData pd = DatabaseManager.getDBInstance(
                                Objects.requireNonNull(getActivity()).getApplicationContext())
                                .getProcessData(data.getId());

//                        String submitCount = pd.getSubmitCount();
//                        if (submitCount != null && !submitCount.equals("0") && localFormResults.isEmpty()) {
//                            if (Util.isConnected(getContext())) {
//                                String url;
//                                if (data.getApi_url() != null
//                                        && !TextUtils.isEmpty(data.getApi_url())) {
//
////                                    url = getResources().getString(R.string.form_field_mandatory,
////                                            data.getMicroservice().getBaseUrl(),
////                                            data.getMicroservice().getRoute());
//                                    url = data.getApi_url() + "/" + data.getId();
//
//                                    //new FormStatusFragmentPresenter(this).getSubmittedForms(data.getId(), url);
//                                }
//                            }
//                        } else {

                            if (localFormResults == null || localFormResults.isEmpty()) continue;

                            List<ProcessData> processData = new ArrayList<>();

//                            for (final String result : localFormResults) {
//                                FormResult formResult = PlatformGson.getPlatformGsonInstance()
//                                        .fromJson(result, FormResult.class);
//
//                                if (formResult.updatedDateTime != null) {
//                                    if (isFormOneMonthOld(formResult.updatedDateTime)) {
//                                        continue;
//                                    }
//                                }
//
//                                formID = formResult.formID;
//
//                                ProcessData object = new ProcessData();
//                                if (formResult.mOID != null && formResult.mOID.oid != null) {
//                                    object.setId(formResult.mOID.oid);
//                                }
//
//                                object.setFormTitle(data.getName().getLocaleValue());
//                                object.setName(new LocaleData(formResult.formTitle));
//                                object.setFormApprovalStatus(formResult.formStatus);
//                                Microservice microservice = new Microservice();
//                                microservice.setUpdatedAt(formResult.updatedDateTime);
//                                microservice.setId(formID);
//                                object.setMicroservice(microservice);
//                                processData.add(object);
//                            }

                            for (com.octopusbjsindia.models.forms.FormResult formResult : localFormResults) {
//                                if (formResult.getCreatedAt() != null) {
//                                    if (isFormOneMonthOld(formResult.getCreatedAt())) {
//                                        continue;
//                                    }
//                                }
                                ProcessData object = new ProcessData();
                                object.setId(formResult.getFormId());
                                object.setFormTitle(data.getName().getLocaleValue());
                                object.setName(new LocaleData(formResult.getFormTitle()));
                                object.setFormApprovalStatus(formResult.getFormApprovalStatus());

                                Microservice microservice = new Microservice();
                                microservice.setUpdatedAt(formResult.getCreatedAt());
                                microservice.setId(formResult.get_id());
                                object.setMicroservice(microservice);
                                processData.add(object);
                            }

                            if (!processData.isEmpty()) {
                                Util.sortProcessDataListByCreatedDate(processData);

                                if(mProcessDataMap.containsKey(processData.get(0).getFormTitle())){
                                    processData.addAll(mProcessDataMap.get(processData.get(0).getFormTitle()));
                                }else{
                                    processSyncStatus.add(Constants.PM.SYNC_STATUS);
                                    processSyncStatusHashmap.put(processData.get(0).getFormTitle(),Constants.PM.SYNC_STATUS);
                                }
                                mProcessDataMap.put(processData.get(0).getFormTitle(), processData);
                                if(!processFormList.contains(processData.get(0).getFormTitle())){
                                    processFormList.add(processData.get(0).getFormTitle());
                                }
                                showNoDataText = false;
                            }
                        //}
                    }
                }
            }

            mFilteredProcessDataMap.putAll(mProcessDataMap);
//            adapter = new SubmittedFormsListAdapter(getContext(), mFilteredProcessDataMap,processSyncStatusHashmap);
//            mExpandableListView.setAdapter(adapter);

            updateView();
        }
    }

    private void updateView() {
        mNoRecordsView.setVisibility(showNoDataText ? View.VISIBLE : View.GONE);
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
                eventStartDate = inputFormat.parse(Util.getDateTimeFromTimestamp(updatedAt));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_filter:
                selectedFilter = 0;
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
                break;
            case R.id.btn_no_filter:
                selectedFilter = 1;
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    filterData(Constants.PM.NO_FILTER);
                    closeFABMenu();
                }
                break;
            case R.id.btn_filter1:
                selectedFilter = 2;
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    filterData(Constants.PM.PENDING_STATUS);
                    closeFABMenu();
                }
                break;
            case R.id.btn_filter2:
                selectedFilter = 3;
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    filterData(Constants.PM.APPROVED_STATUS);
                    closeFABMenu();
                }
                break;
            case R.id.btn_filter3:
                selectedFilter = 4;
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    filterData(Constants.PM.REJECTED_STATUS);
                    closeFABMenu();
                }
                break;
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        btnFilter.show();
        btnFilter.setEnabled(true);
        btnNoFilter.show();
        btnNoFilter.setEnabled(true);
        btnFilter1.show();
        btnFilter1.setEnabled(true);
        btnFilter2.show();
        btnFilter2.setEnabled(true);
        btnFilter3.show();
        btnFilter3.setEnabled(true);
        btnNoFilter.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        btnFilter1.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        btnFilter2.animate().translationY(-getResources().getDimension(R.dimen.standard_180));
        btnFilter3.animate().translationY(-getResources().getDimension(R.dimen.standard_240));
        btnFilter.startAnimation(animFadeIn);
        btnNoFilter.startAnimation(animFadeIn);
        btnFilter1.startAnimation(animFadeIn);
        btnFilter2.startAnimation(animFadeIn);
        btnFilter3.startAnimation(animFadeIn);
        btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        btnNoFilter.animate().translationY(0);
        btnFilter1.animate().translationY(0);
        btnFilter2.animate().translationY(0);
        btnFilter3.animate().translationY(0);

        switch (selectedFilter){
            case 0:
                btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_icon_list));
                btnNoFilter.hide();
                btnNoFilter.setEnabled(false);
                btnFilter1.hide();
                btnFilter1.setEnabled(false);
                btnFilter2.hide();
                btnFilter2.setEnabled(false);
                btnFilter3.hide();
                btnFilter3.setEnabled(false);
                break;
            case 1:
                btnFilter.hide();
                btnFilter.setEnabled(false);
                btnFilter1.hide();
                btnFilter1.setEnabled(false);
                btnFilter2.hide();
                btnFilter2.setEnabled(false);
                btnFilter3.hide();
                btnFilter3.setEnabled(false);
                break;
            case 2:
                btnFilter.hide();
                btnFilter.setEnabled(false);
                btnNoFilter.hide();
                btnNoFilter.setEnabled(false);
                btnFilter2.hide();
                btnFilter2.setEnabled(false);
                btnFilter3.hide();
                btnFilter3.setEnabled(false);
                break;
            case 3:
                btnFilter.hide();
                btnFilter.setEnabled(false);
                btnNoFilter.hide();
                btnNoFilter.setEnabled(false);
                btnFilter1.hide();
                btnFilter1.setEnabled(false);
                btnFilter3.hide();
                btnFilter3.setEnabled(false);
                break;
            case 4:
                btnFilter.hide();
                btnFilter.setEnabled(false);
                btnNoFilter.hide();
                btnNoFilter.setEnabled(false);
                btnFilter1.hide();
                btnFilter1.setEnabled(false);
                btnFilter2.hide();
                btnFilter2.setEnabled(false);
                break;
        }
    }

    private void filterData(String status) {
        mFilteredProcessDataMap.clear();
        if (status.equalsIgnoreCase("NO_FILTER")) {
            mFilteredProcessDataMap.putAll(mProcessDataMap);
        } else {
            for (int i = 0; i < mProcessDataMap.size(); i++) {
                List<ProcessData> filterStatusProcessData = new ArrayList<ProcessData>();
                List<ProcessData> filterProcessData = new ArrayList<ProcessData>();
                filterProcessData.addAll(mProcessDataMap.get(processFormList.get(i)));
                for (ProcessData pd : filterProcessData) {
                    if (pd.getFormApprovalStatus() != null && pd.getFormApprovalStatus().equalsIgnoreCase(status)) {
                        filterStatusProcessData.add(pd);
                    }
                }
                if (filterProcessData.size() > 0 && filterStatusProcessData.size() > 0) {
                    mFilteredProcessDataMap.put(filterProcessData.get(0).getFormTitle(), filterStatusProcessData);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    //    static class FormResult {
//        @SuppressWarnings("unused")
//        @SerializedName("form_title")
//        String formTitle;
//
//        @SuppressWarnings("unused")
//        @SerializedName("form_id")
//        String formID;
//
//        @SuppressWarnings("unused")
//        @SerializedName("updatedDateTime")
//        Long updatedDateTime;
//
//        @SuppressWarnings("unused")
//        @SerializedName("status")
//        String formStatus;
//
//        @SuppressWarnings("unused")
//        @SerializedName("_id")
//        OID mOID;
//    }
//
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
