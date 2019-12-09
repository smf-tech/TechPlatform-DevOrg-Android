package com.octopusbjsindia.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.forms.FormStatusCount;
import com.octopusbjsindia.models.forms.FormStatusCountData;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.PMFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.FormsDashboardAdapter;

import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
// We have updated this fragment. We are not going to show forms section over here.So all all the respective api and Ui code has been commented.
// Now we are showing forms dashboard data.
public class PMFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {

    private View pmFragmentView;
    private int unsynchCount, savedCount;
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
//        rvFormsStatusCount.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFormsStatusCount.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
        } else {
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

    private void populateOfflineData() {
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
