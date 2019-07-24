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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.forms.FormResult;
import com.platform.models.forms.FormStatusCount;
import com.platform.models.forms.FormStatusCountData;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.models.user.UserInfo;
import com.platform.presenter.PMFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.FormActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.FormsDashboardAdapter;
import com.platform.view.adapters.PendingFormsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_COMPLETED;
import static com.platform.syncAdapter.SyncAdapterUtils.EVENT_SYNC_FAILED;
import static com.platform.syncAdapter.SyncAdapterUtils.PARTIAL_FORM_ADDED;
import static com.platform.presenter.PMFragmentPresenter.getAllNonSyncedSavedForms;

@SuppressWarnings("CanBeFinal")
// We have updated this fragment. We are not going to show forms section over here.So all all the respective api and Ui code has been commented.
// Now we are showing forms dashboard data.
public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener
        {

    private View pmFragmentView;
    private int unsynchCount,savedCount;
    private ArrayList<FormStatusCountData> formStatusCountDataList = new ArrayList<>();
    private RecyclerView rvFormsStatusCount;
    private FormsDashboardAdapter formsDashboardAdapter;

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

        pmFragmentView = inflater.inflate(R.layout.fragment_dashboard_forms, container, false);
        return pmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        FloatingActionButton txtViewAllForms = pmFragmentView.findViewById(R.id.txt_view_all_forms);
        txtViewAllForms.setOnClickListener(this);
        rvFormsStatusCount = pmFragmentView.findViewById(R.id.rv_forms_dashboard);
        formsDashboardAdapter = new FormsDashboardAdapter(getActivity(), formStatusCountDataList);
        rvFormsStatusCount.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFormsStatusCount.setAdapter(formsDashboardAdapter);

        rvFormsStatusCount.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < -5 && txtViewAllForms.getVisibility() != View.VISIBLE) {
                    txtViewAllForms.setVisibility(View.VISIBLE);
                } else if (dy > 5 && txtViewAllForms.getVisibility() == View.VISIBLE) {
                    txtViewAllForms.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isConnected(getContext())) {
            UserInfo user = Util.getUserObjectFromPref();
            PMFragmentPresenter pmFragmentPresenter = new PMFragmentPresenter(this);
            pmFragmentPresenter.getAllFormsCount(user);
        }else{
            formStatusCountDataList.clear();
            populateOfflineData();
        }
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
        populateData((FormStatusCount) data);

    }

    @Override
    public void showErrorMessage(String result) {

    }
            private void populateData(FormStatusCount formCount) {
                if (formCount != null) {
                    formStatusCountDataList.clear();
                    for (FormStatusCountData data : formCount.getData()) {
                        if (data != null && data.getType() != null) {
                            formStatusCountDataList.add(data);
                        }
                    }
                    populateOfflineData();
                }
            }

            private void populateOfflineData(){
                unsynchCount = DatabaseManager.getDBInstance(this.getActivity()).getNonSyncedPendingForms().size();
                savedCount = DatabaseManager.getDBInstance(this.getActivity()).getAllPartiallySavedForms().size();
                FormStatusCountData unsyncCountData = new FormStatusCountData();
                FormStatusCountData savedCountData = new FormStatusCountData();
                unsyncCountData.setType(Constants.PM.UNSYNC_STATUS);
                unsyncCountData.setCount(unsynchCount);
                savedCountData.setType(Constants.PM.SAVED_STATUS);
                savedCountData.setCount(savedCount);
                formStatusCountDataList.add(unsyncCountData);
                formStatusCountDataList.add(savedCountData);
                formsDashboardAdapter.notifyDataSetChanged();
            }
}
