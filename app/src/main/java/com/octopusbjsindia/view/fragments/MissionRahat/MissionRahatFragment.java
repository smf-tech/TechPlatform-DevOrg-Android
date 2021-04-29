package com.octopusbjsindia.view.fragments.MissionRahat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsData;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.presenter.MissionRahat.MissionRahatFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.ConcentratorRequirementActivity;
import com.octopusbjsindia.view.activities.MissionRahat.CreateHospitalActivity;
import com.octopusbjsindia.view.activities.MissionRahat.CreateMachineActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineListActivity;
import com.octopusbjsindia.view.activities.MissionRahat.RequirementsListActivity;
import com.octopusbjsindia.view.adapters.SSAnalyticsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MissionRahatFragment extends Fragment implements APIDataListener, View.OnClickListener {
    private View machineRahatFragmentView;
    private FloatingActionButton fbSelect;
    private ExtendedFloatingActionButton fbMachine, fbHospital, fbRequirementForm, fbApproval, fbMachineList;
    private boolean isMachineCreate, isHospitalCreate, isRequirementForm, isApprovalAllowed,
            isDailyReportAllowed, isMachineListAllowed, isDownloadMOU, isNewPatient, isSubmitMOU, isRequirementList;
    private boolean isFABOpen = false;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private SSAnalyticsAdapter mrAnalyticsAdapter;
    private ArrayList<SSAnalyticsData> mrAnalyticsDataList = new ArrayList<>();
    private RecyclerView rvMRAnalytics;
    private MissionRahatFragmentPresenter missionRahatFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineRahatFragmentView = inflater.inflate(R.layout.fragment_mission_rahat, container, false);
        return machineRahatFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineRahatFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineRahatFragmentView.findViewById(R.id.pb_profile_act);
        fbSelect = machineRahatFragmentView.findViewById(R.id.fb_select);
        fbSelect.setOnClickListener(this);
        fbMachine = machineRahatFragmentView.findViewById(R.id.fb_machine);
        fbMachine.setOnClickListener(this);
        fbHospital = machineRahatFragmentView.findViewById(R.id.fb_hospital);
        fbHospital.setOnClickListener(this);
        fbRequirementForm = machineRahatFragmentView.findViewById(R.id.fb_requirement_form);
        fbRequirementForm.setOnClickListener(this);
        fbApproval = machineRahatFragmentView.findViewById(R.id.fb_approval);
        fbApproval.setOnClickListener(this);
        fbMachineList = machineRahatFragmentView.findViewById(R.id.fb_machine_list);
        fbMachineList.setOnClickListener(this);

        rvMRAnalytics = machineRahatFragmentView.findViewById(R.id.rv_mr_analytics);
        rvMRAnalytics.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mrAnalyticsAdapter = new SSAnalyticsAdapter(getActivity(), mrAnalyticsDataList, 1,
                "MR DataList", "MR");
        rvMRAnalytics.setAdapter(mrAnalyticsAdapter);
        mrAnalyticsAdapter.notifyDataSetChanged();

        missionRahatFragmentPresenter = new MissionRahatFragmentPresenter(this);

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_MACHINE_CREATE)) {
                    isMachineCreate = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_HOSPTAL_CREATE)) {
                    isHospitalCreate = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_REQUIREMENT_FORM)) {
                    isRequirementForm = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_REQUIREMENT_APPROVAL)) {
                    isApprovalAllowed = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_VIEW_MACHINE_LIST)) {
                    isMachineListAllowed = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_DOWNLOAD_MOU)) {
                    isDownloadMOU = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_NEW_PATIENT)) {
                    isNewPatient = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_SUBMIT_MOU)) {
                    isSubmitMOU = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_VIEW_REQUIREMENT_LIST)) {
                    isRequirementList = true;
                    continue;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Util.isConnected(getActivity())) {
//            missionRahatFragmentPresenter.getMRAnalyticsData("", "");
//        } else {
//            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
//        }
//        if (mrAnalyticsDataList.size() > 0) {
//            machineRahatFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
//        } else {
//            machineRahatFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
//        }
        closeFABMenu();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (missionRahatFragmentPresenter != null) {
            missionRahatFragmentPresenter.clearData();
            missionRahatFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fb_select:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;
            case R.id.fb_machine:
                intent = new Intent(getActivity(), CreateMachineActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.fb_hospital:
                intent = new Intent(getActivity(), CreateHospitalActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.fb_requirement_form:
                intent = new Intent(getActivity(), ConcentratorRequirementActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.fb_approval:
                intent = new Intent(getActivity(), RequirementsListActivity.class);
                intent.putExtra("isDownloadMOU",isDownloadMOU);
                intent.putExtra("isSubmitMOU",isSubmitMOU);
                intent.putExtra("isApprovalAllowed",isApprovalAllowed);
                getActivity().startActivity(intent);
                break;
            case R.id.fb_machine_list:
                Intent intent2 = new Intent(getActivity(), OxyMachineListActivity.class);
                getActivity().startActivity(intent2);
                break;
        }
    }

    private void showFABMenu() {
        isFABOpen = true;
        int height = 140;
        if (isMachineCreate) {
            fbMachine.show();
            fbMachine.animate().translationY(-height);
            height = height + 140;
        }
        if (isHospitalCreate) {
            fbHospital.show();
            fbHospital.animate().translationY(-height);
            height = height + 140;
        }
        if (isRequirementForm) {
            fbRequirementForm.show();
            fbRequirementForm.animate().translationY(-height);
            height = height + 140;
        }
        if (isRequirementList) {
            fbApproval.show();
            fbApproval.animate().translationY(-height);
            height = height + 140;
        }
        if (isMachineListAllowed) {
            fbMachineList.show();
            fbMachineList.animate().translationY(-height);
        }
        fbSelect.setRotation(45);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        if (isMachineCreate) {
            fbMachine.hide();
            fbMachine.animate().translationY(0);
        }
        if (isHospitalCreate) {
            fbHospital.hide();
            fbHospital.animate().translationY(0);
        }
        if (isRequirementForm) {
            fbRequirementForm.hide();
            fbRequirementForm.animate().translationY(0);
        }
        if (isRequirementList) {
            fbApproval.hide();
            fbApproval.animate().translationY(0);
        }
        if (isMachineListAllowed) {
            fbMachineList.hide();
            fbMachineList.animate().translationY(0);
        }
        fbSelect.setRotation(0);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Util.showToast(response, this);
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    public void populateAnalyticsData(SSAnalyticsAPIResponse analyticsData) {
        if (analyticsData != null) {
            mrAnalyticsDataList.clear();
            for (SSAnalyticsData data : analyticsData.getData()) {
                if (data != null) {
                    mrAnalyticsDataList.add(data);
                }
            }
        }
    }

    public void emptyResponse() {
        mrAnalyticsDataList.clear();
        machineRahatFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        rvMRAnalytics.setAdapter(mrAnalyticsAdapter);
        mrAnalyticsAdapter.notifyDataSetChanged();
    }
}