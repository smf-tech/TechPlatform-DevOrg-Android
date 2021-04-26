package com.octopusbjsindia.view.fragments.MissionRahat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MissionRahat.CreateHospitalActivity;
import com.octopusbjsindia.view.activities.MissionRahat.CreateMachineActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineDailyReportActivity;
import com.octopusbjsindia.view.activities.MissionRahat.OxyMachineMouActivity;

import java.util.List;

public class MissionRahatFragment extends Fragment implements APIDataListener, View.OnClickListener {
    private View machineRahatFragmentView;
    private FloatingActionButton fbSelect;
    private ExtendedFloatingActionButton fbMachine, fbHospital, fbRequirementForm, fbApproval, fbMachineList;
    private boolean isMachineCreate, isHospitalCreate, isRequirementForm, isApprovalAllowed,
            isDailyReportAllowed, isMachineListAllowed;
    private boolean isFABOpen = false;

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
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        closeFABMenu();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_select:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;
            case R.id.fb_machine:
                Intent i = new Intent(getActivity(), CreateMachineActivity.class);
                getActivity().startActivity(i);
                break;
            case R.id.fb_hospital:
                Intent intent = new Intent(getActivity(), CreateHospitalActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.fb_requirement_form:
                break;
            case R.id.fb_approval:
                Intent intent1 = new Intent(getActivity(), OxyMachineMouActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.fb_machine_list:
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
        if (isApprovalAllowed) {
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
        if (isApprovalAllowed) {
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

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }
}