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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.forms.FormResult;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.PMFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.PendingFormsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_ADDED;

@SuppressWarnings("CanBeFinal")
public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener,
        PendingFormsAdapter.FormListener {

    private View pmFragmentView;
    private ArrayList<String> processCategoryList = new ArrayList<>();
    private HashMap<String, List<ProcessData>> processMap = new HashMap<>();
    private LinearLayout lnrOuter;
    private RecyclerView rvPendingForms;
    private RelativeLayout rltPendingForms;
    private PendingFormsAdapter pendingFormsAdapter;
    private List<FormResult> mSavedForms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setSyncButtonVisibility(true);
        }

        AppEvents.trackAppEvent(getString(R.string.event_forms_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        pmFragmentView = inflater.inflate(R.layout.fragment_dashboard_pm, container, false);
        return pmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isSyncRequired = false;
        if (getArguments() != null) {
            isSyncRequired = getArguments().getBoolean("NEED_SYNC");
        }

        init();

        List<ProcessData> processDataArrayList = DatabaseManager.getDBInstance(getActivity()).getAllProcesses();
        if (processDataArrayList != null && !processDataArrayList.isEmpty() && !isSyncRequired) {
            Processes processes = new Processes();
            processes.setData(processDataArrayList);

            populateData(processes);

        } else {
            if (Util.isConnected(getContext())) {
                PMFragmentPresenter pmFragmentPresenter = new PMFragmentPresenter(this);
                pmFragmentPresenter.getAllProcess();
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_SYNC_COMPLETED);
        filter.addAction(EVENT_SYNC_FAILED);
        filter.addAction(PARTIAL_FORM_ADDED);

        SyncAdapterBroadCastReceiver(filter);
    }

    private void SyncAdapterBroadCastReceiver(final IntentFilter filter) {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()))
                .registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(final Context context, final Intent intent) {
                        if (context == null) {
                            return;
                        }

                        try {
                            String action = Objects.requireNonNull(intent.getAction());
                            switch (action) {
                                case EVENT_SYNC_COMPLETED:
                                    Util.showToast(getString(R.string.sync_completed), context);
                                    updateAdapter();
                                    break;

                                case PARTIAL_FORM_ADDED:
                                    Util.showToast(getString(R.string.partial_form_added), context);
                                    updateAdapter();
                                    break;

                                case EVENT_SYNC_FAILED:
                                    Log.e("PendingForms", "Sync failed!");
                                    Util.showToast(getString(R.string.sync_failed), context);
                                    break;
                            }
                        } catch (IllegalStateException e) {
                            Log.e("PMFragment", "SyncAdapterBroadCastReceiver", e);
                        }
                    }
                }, filter);
    }

    private void updateAdapter() {
        if (pendingFormsAdapter == null) {
            pendingFormsAdapter = (PendingFormsAdapter) rvPendingForms.getAdapter();
        }

        if (mSavedForms == null) {
            mSavedForms = new ArrayList<>();
        }

        mSavedForms.clear();
        List<FormResult> savedForms = DatabaseManager
                .getDBInstance(getContext()).getAllPartiallySavedForms();

        List<FormResult> partiallySavedForms = Util.sortFormResultListByCreatedDate(savedForms);

        mSavedForms.addAll(partiallySavedForms);

        if (mSavedForms != null && !mSavedForms.isEmpty()) {
            rltPendingForms.setVisibility(View.VISIBLE);
            if (pendingFormsAdapter != null) {
                pendingFormsAdapter.notifyDataSetChanged();
            }
        } else {
            rltPendingForms.setVisibility(View.GONE);
            pmFragmentView.findViewById(R.id.view_forms_divider2).setVisibility(View.GONE);
        }
    }

    private void init() {
        rvPendingForms = pmFragmentView.findViewById(R.id.rv_dashboard_pending_forms);
        lnrOuter = pmFragmentView.findViewById(R.id.lnr_dashboard_forms_category);
        rltPendingForms = pmFragmentView.findViewById(R.id.rlt_pending_forms);

        TextView txtViewAllForms = pmFragmentView.findViewById(R.id.txt_view_all_forms);
        txtViewAllForms.setOnClickListener(this);
    }

    private void getPartiallySavedForms() {
        List<FormResult> savedForms = DatabaseManager.getDBInstance(getContext()).getAllPartiallySavedForms();
        if (savedForms != null && !savedForms.isEmpty()) {

            mSavedForms = Util.sortFormResultListByCreatedDate(savedForms);

            rltPendingForms.setVisibility(View.VISIBLE);
            rltPendingForms.setVisibility(View.VISIBLE);
            pmFragmentView.findViewById(R.id.view_forms_divider2).setVisibility(View.VISIBLE);

            pendingFormsAdapter = new PendingFormsAdapter(getActivity(), mSavedForms, this);
            rvPendingForms.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPendingForms.setAdapter(pendingFormsAdapter);
        } else {
            pmFragmentView.findViewById(R.id.view_forms_divider2).setVisibility(View.GONE);
            rltPendingForms.setVisibility(View.GONE);
        }
    }

    private void populateData(Processes process) {
        if (process != null) {
            processCategoryList.clear();
            processMap.clear();

            for (ProcessData data : process.getData()) {
                if (data != null && data.getCategory() != null) {
                    String categoryName = data.getCategory().getName().getLocaleValue();
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
                if (!TextUtils.isEmpty(processCategoryList.get(index)) &&
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
        DatabaseManager.getDBInstance(getContext()).insertProcessData(data);
    }

    private void createCategoryLayout(String categoryName, List<ProcessData> childList) {
        if (getContext() == null) {
            return;
        }

        View formTitleView = LayoutInflater.from(getContext().getApplicationContext())
                .inflate(R.layout.row_dashboard_forms_category, lnrOuter, false);

        ((TextView) formTitleView.findViewById(R.id.txt_dashboard_form_category_name)).setText(categoryName);
        LinearLayout lnrInner = formTitleView.findViewById(R.id.lnr_inner);

        for (int i = 0; i < childList.size(); i++) {
            final ProcessData data = childList.get(i);
            if (i >= 2) break;

            if (!TextUtils.isEmpty(data.getName().getLocaleValue())) {
                View formTypeView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.row_dashboard_forms_category_card_view, lnrInner, false);

                ((TextView) formTypeView.findViewById(R.id.txt_dashboard_form_title))
                        .setText(data.getName().getLocaleValue().trim());

                if (!TextUtils.isEmpty(data.getId())) {
                    ImageButton imgCreateForm = formTypeView.findViewById(R.id.iv_create_form);
                    imgCreateForm.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), FormActivity.class);
                        intent.putExtra(Constants.PM.FORM_ID, data.getId());
                        startActivity(intent);
                    });
                }
                lnrInner.addView(formTypeView);

                if (childList.size() == 1 || i == 1) {
                    formTypeView.findViewById(R.id.dashboard_category_item_divider).setVisibility(View.GONE);
                }
            }
        }
        lnrOuter.addView(lnrInner);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPartiallySavedForms();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view_all_forms) {
            Util.launchFragment(new FormsFragment(), getContext(),
                    getString(R.string.forms), true);
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

    @Override
    public void onFormDeletedListener() {
        updateAdapter();
        if (getParentFragment() != null && getParentFragment() instanceof DashboardFragment) {
            ((DashboardFragment) getParentFragment()).updateBadgeCount();
        }
    }
}
