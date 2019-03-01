package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.PMFragmentPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.adapters.PendingFormsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms1;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_FORM_ADDED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.utility.Constants.Form.EXTRA_FORM_ID;
import static com.platform.utility.Util.saveFormCategoryForSync;

@SuppressWarnings("CanBeFinal")
public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {

    private View pmFragmentView;
    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();
    private LinearLayout lnrOuter;
    private RecyclerView rvPendingForms;
    private RelativeLayout rltPendingForms;
    private PendingFormsAdapter pendingFormsAdapter;
    private List<FormResult> mSavedForms;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        pmFragmentView = inflater.inflate(R.layout.fragment_dashboard_pm, container, false);
        return pmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        PMFragmentPresenter pmFragmentPresenter = new PMFragmentPresenter(this);
        ArrayList<ProcessData> processDataArrayList = new ArrayList<>();
        List<FormData> formDataList = DatabaseManager.getDBInstance(getActivity()).getAllFormSchema();
        if (formDataList != null && !formDataList.isEmpty()) {
            for (final FormData data : formDataList) {
                ProcessData processData = new ProcessData(data);
                processDataArrayList.add(processData);
            }

            Processes processes = new Processes();
            processes.setData(processDataArrayList);

            populateData(processes);

        } else {
            if (Util.isConnected(getContext())) {
                pmFragmentPresenter.getAllProcess();
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);

        SyncAdapterBroadCastReceiver(filter);
    }

    private void SyncAdapterBroadCastReceiver(final IntentFilter filter) {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_SYNC_COMPLETED)) {
                    Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();

                    int formID = intent.getIntExtra(EXTRA_FORM_ID, 0);
                    updateAdapter(context, formID);
                } else if (Objects.requireNonNull(intent.getAction()).equals(EVENT_FORM_ADDED)) {
                    updateAdapter(context, 0);
                } else if (intent.getAction().equals(EVENT_SYNC_FAILED)) {
                    Log.e("PendingForms", "Sync failed!");
                    Toast.makeText(context, "Sync failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }, filter);
    }

    private void updateAdapter(final Context context, final int formID) {
        if (pendingFormsAdapter == null) {
            pendingFormsAdapter = (PendingFormsAdapter) rvPendingForms.getAdapter();
            if (mSavedForms == null) {
                mSavedForms = new ArrayList<>();
            }
        }

        if (formID == 0) {
            mSavedForms.clear();
            mSavedForms.addAll(getAllNonSyncedSavedForms1());
//        } else {
//            List<FormResult> list = new ArrayList<>(mSavedForms);
//            for (final FormResult form : mSavedForms) {
//                if (formID == form.id) {
//                    list.remove(form);
//                }
//            }
//            mSavedForms.clear();
//            mSavedForms.addAll(list);
        }

        if (mSavedForms != null && !mSavedForms.isEmpty()) {
            rltPendingForms.setVisibility(View.VISIBLE);
            if (pendingFormsAdapter != null) {
                pendingFormsAdapter.notifyDataSetChanged();
            }
        } else {
            rltPendingForms.setVisibility(View.GONE);
        }
    }

    private void init() {
        rvPendingForms = pmFragmentView.findViewById(R.id.rv_dashboard_pending_forms);
        lnrOuter = pmFragmentView.findViewById(R.id.lnr_dashboard_forms_category);
        rltPendingForms = pmFragmentView.findViewById(R.id.rlt_pending_forms);

        TextView txtViewAllForms = pmFragmentView.findViewById(R.id.txt_view_all_forms);
        txtViewAllForms.setOnClickListener(this);
    }

    private void setPendingForms() {
        mSavedForms = getAllNonSyncedSavedForms1();
        if (mSavedForms != null && !mSavedForms.isEmpty()) {
            rltPendingForms.setVisibility(View.VISIBLE);
            pmFragmentView.findViewById(R.id.view_forms_divider).setVisibility(View.VISIBLE);
            pmFragmentView.findViewById(R.id.sync_button).setOnClickListener(v -> {
                if (Util.isConnected(getContext())) {
                    Toast.makeText(getContext(), "Sync started...", Toast.LENGTH_SHORT).show();
                    // TODO: 14-02-2019 Category is not known for syncing
                    saveFormCategoryForSync("");
                    SyncAdapterUtils.manualRefresh();
                } else {
                    Toast.makeText(getContext(), "Internet is not available!", Toast.LENGTH_SHORT).show();
                }
            });

            pendingFormsAdapter = new PendingFormsAdapter(getActivity(), mSavedForms);
            rvPendingForms.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPendingForms.setAdapter(pendingFormsAdapter);
        } else {
            pmFragmentView.findViewById(R.id.view_forms_divider).setVisibility(View.GONE);
            rltPendingForms.setVisibility(View.GONE);
        }
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null) {
                    String categoryName = data.getCategory().getName();
                    if (!TextUtils.isEmpty(categoryName)) {
                        if (processMap.containsKey(categoryName)) {
                            List<ProcessData> processData = processMap.get(categoryName);
                            if (processData != null) {
                                processData.add(data);
                                processMap.put(categoryName, processData);

                                addProcessDataInDatabase(data);
                            }
                        } else {
                            List<ProcessData> processData = new ArrayList<>();
                            processData.add(data);
                            processMap.put(categoryName, processData);
                            processCategoryList.add(categoryName);

                            addProcessDataInDatabase(data);
                        }
                    }
                }
            }

            for (int index = 0; index < processMap.size(); index++) {
                if (processMap != null && !TextUtils.isEmpty(processCategoryList.get(index)) &&
                        processMap.get(processCategoryList.get(index)) != null) {

                    List<ProcessData> processData = processMap.get(processCategoryList.get(index));
                    if (processData != null) {
                        createCategoryLayout(processCategoryList.get(index), processData);
                    }
                }
            }
        }
    }

    private void addProcessDataInDatabase(final ProcessData data) {
        FormData formData = new FormData(data);
        DatabaseManager.getDBInstance(getContext()).insertFormSchema(formData);
    }

    private void createCategoryLayout(String categoryName, List<ProcessData> childList) {
        View formTitleView = getLayoutInflater().inflate(R.layout.row_dashboard_forms_category, lnrOuter, false);
        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        for (ProcessData data : childList) {
            if (!TextUtils.isEmpty(data.getName())) {
                View formTypeView = getLayoutInflater().inflate(R.layout.row_dashboard_forms_category_card_view, lnrInner, false);
                ((TextView) formTypeView.findViewById(R.id.txt_dashboard_form_title)).setText(data.getName().trim());

                if (!TextUtils.isEmpty(data.getId())) {
                    ImageButton imgCreateForm = formTypeView.findViewById(R.id.iv_create_form);
                    imgCreateForm.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra(Constants.PM.PROCESS_ID, data.getId());
                        startActivity(intent);
                    });
                }
                lnrInner.addView(formTypeView);
            }
        }
        lnrOuter.addView(lnrInner);
    }

    @Override
    public void onResume() {
        super.onResume();

        setPendingForms();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_view_all_forms:
                Util.launchFragment(new FormsFragment(), getContext(), getString(R.string.forms));
                break;
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        populateData((Processes) data);
    }

    @Override
    public void showErrorMessage(String result) {

    }

}
