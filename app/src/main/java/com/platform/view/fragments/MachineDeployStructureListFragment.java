package com.platform.view.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.SujalamSuphalam.StructureListAPIResponse;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.home.RoleAccessAPIResponse;
import com.platform.models.home.RoleAccessList;
import com.platform.models.home.RoleAccessObject;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.MachineDeployStructureListFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.adapters.SSDataListAdapter;
import com.platform.view.adapters.StructureListAdapter;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MachineDeployStructureListFragment extends Fragment  implements APIDataListener,
        CustomSpinnerListener, View.OnClickListener {
    private View machineDeployStructureListFragmentView;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private RecyclerView rvStructureList;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureListAdapter structureListAdapter;
    private MachineDeployStructureListFragmentPresenter machineDeployStructureListFragmentPresenter;
    private final ArrayList<StructureData> structureListData = new ArrayList<>();
    private ArrayList<StructureData> filteredStructureListData = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTalukaList = new ArrayList<>();
    private String machineId;
    private String type, currentStructureId, selectedDistrict, selectedDistrictId, selectedTaluka, selectedTalukaId;
    private Button btnDeploy;
    private int selectedPosition;
    public boolean isStateFilter, isDistrictFilter, isTalukaFilter, isVillageFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDeployStructureListFragmentView = inflater.inflate(R.layout.fragment_machine_deploy_structure_list,
                container, false);
        return machineDeployStructureListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bundle bundle = this.getArguments();
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        type = getActivity().getIntent().getStringExtra("type");
        //viewType = bundle.getInt("viewType");
        init();
    }

    private void init() {
        progressBarLayout = machineDeployStructureListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDeployStructureListFragmentView.findViewById(R.id.pb_profile_act);
        btnDeploy = machineDeployStructureListFragmentView.findViewById(R.id.btn_deploy);
        btnDeploy.setOnClickListener(this);
        tvStateFilter = machineDeployStructureListFragmentView.findViewById(R.id.tv_state_filter);
        tvDistrictFilter = machineDeployStructureListFragmentView.findViewById(R.id.tv_district_filter);
        tvTalukaFilter = machineDeployStructureListFragmentView.findViewById(R.id.tv_taluka_filter);
        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            tvStateFilter.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
        }
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
        }
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
        for (RoleAccessObject roleAccessObject: roleAccessObjectList) {
            if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
                isStateFilter = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
                isDistrictFilter = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
                isTalukaFilter = true;
                continue;
            } else if(roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VILLAGE)) {
                isVillageFilter = true;
            }
        }
        machineDeployStructureListFragmentPresenter = new MachineDeployStructureListFragmentPresenter(this);
//        if(Util.getUserObjectFromPref().getRoleNames().equals(Constants.SSModule.DISTRICT_LEVEL)){
//            tvTalukaFilter.setOnClickListener(this);
//            if(tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length()>0) {
//                UserInfo userInfo = Util.getUserObjectFromPref();
//                machineDeployStructureListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
//                        "5c4ab05cd503a372d0391467",
//                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
//            }
//        }
        rvStructureList = machineDeployStructureListFragmentView.findViewById(R.id.rv_structure_list);
        rvStructureList.setLayoutManager(new LinearLayoutManager(getActivity()));
        structureListAdapter = new StructureListAdapter(getActivity(), this, filteredStructureListData, type);
        rvStructureList.setAdapter(structureListAdapter);

        if(type.equalsIgnoreCase("deployMachine")) {
            if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_HO_OPS)) {
                machineDeployStructureListFragmentPresenter.getDistrictDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        "machineDeployableStructures", "");
            } else if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_DM)) {
                machineDeployStructureListFragmentPresenter.getDistrictDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        "machineDeployableStructures", "");
            } else if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_TC)) {
                machineDeployStructureListFragmentPresenter.getTalukaDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId(),
                        "machineDeployableStructures", "");
            }
        } else if(type.equalsIgnoreCase("shiftMachine")) {
            if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_HO_OPS)) {
                machineDeployStructureListFragmentPresenter.getDistrictDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        "machineShiftStructures", "");
            } else if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_DM)) {
                machineDeployStructureListFragmentPresenter.getDistrictDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        "machineShiftStructures", "");
            } else if (Util.getUserObjectFromPref().getRoleCode() == (Constants.SSModule.ROLE_CODE_SS_TC)) {
                machineDeployStructureListFragmentPresenter.getTalukaDeployableStructuresList(
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                        Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId(),
                        "machineShiftStructures", "");
            }
        }

//        if(type.equalsIgnoreCase("deployMachine")) {
//            machineDeployStructureListFragmentPresenter.getDeployableStructuresList("5c669d13c7982d31cc6b86cd",
//                    "5c66a468d42f283b440013e3","5c66a588d42f283b44001447",
//                    "machineDeployableStructures", "");
//        } else if(type.equalsIgnoreCase("shiftMachine")) {
//            machineDeployStructureListFragmentPresenter.getDeployableStructuresList("5c669d13c7982d31cc6b86cd",
//                    "5c66a468d42f283b440013e3","5c66a588d42f283b44001447",
//                    "machineShiftStructures", currentStructureId);
//        }
        if(isStateFilter) {
            tvStateFilter.setOnClickListener(this);
        } else {
            tvStateFilter.setEnabled(false);
        }
        if(isDistrictFilter) {
            tvDistrictFilter.setOnClickListener(this);
        } else {
            tvDistrictFilter.setEnabled(false);
        }
        if(isTalukaFilter) {
            tvTalukaFilter.setOnClickListener(this);
        } else {
            tvTalukaFilter.setEnabled(false);
        }
    }

    public void deployMachine(int position) {
        selectedPosition = position;
        if(btnDeploy.getVisibility()!=View.VISIBLE) {
            btnDeploy.setVisibility(View.VISIBLE);
        }
    }

    public void ShiftMachine(int position) {
        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "MachineShiftingFormFragment");
        intent.putExtra("title", "Machine Shifting");
        intent.putExtra("machineId", machineId);
        intent.putExtra("currentStructureId", currentStructureId);
        intent.putExtra("newStructureId", structureListData.get(position).getStructureId());
        startActivity(intent);
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineDeployStructureListFragmentPresenter.DEPLOY_MACHINE)){
            if(status == 200){
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineDeployStructureListFragmentPresenter != null) {
            machineDeployStructureListFragmentPresenter.clearData();
            machineDeployStructureListFragmentPresenter = null;
        }
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
        getActivity().finish();
    }

    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    structureTalukaList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        if (tvDistrictFilter.getText().toString().equalsIgnoreCase(location.getDistrict().getName())) {
                            CustomSpinnerObject talukaList = new CustomSpinnerObject();
                            talukaList.set_id(location.getTalukaId());
                            talukaList.setName(location.getTaluka().getName());
                            talukaList.setSelected(false);
                            structureTalukaList.add(talukaList);
                        }
                    }
                }
                CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(),
                        this, "Select Taluka", structureTalukaList,
                        false);
                cddTaluka.show();
                cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    structureDistrictList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getDistrict().getName().
                            compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        if (tvStateFilter.getText().toString().equalsIgnoreCase(location.getState().getName())) {
                            CustomSpinnerObject districtList = new CustomSpinnerObject();
                            districtList.set_id(location.getDistrictId());
                            districtList.setName(location.getDistrict().getName());
                            districtList.setSelected(false);
                            structureDistrictList.add(districtList);
                        }
                    }
                }
                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District", structureDistrictList,
                        false);
                cddDistrict.show();
                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            default:
                break;
        }
    }

    public void populateStructureData(String requestID, StructureListAPIResponse structureList) {
        if (structureList != null) {
            if (requestID.equals(MachineDeployStructureListFragmentPresenter.GET_MACHINE_DEPLOY_STRUCTURE_LIST)) {
                structureListData.clear();
                filteredStructureListData.clear();
                for (StructureData structureData : structureList.getData()) {
                    if (structureData != null) {
                        structureListData.add(structureData);
                    }
                }
                filteredStructureListData.addAll(structureListData);
                rvStructureList.setAdapter(structureListAdapter);
                structureListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if(type.equals("Select Taluka")){
            for(CustomSpinnerObject sTaluka: structureTalukaList){
                if(sTaluka.isSelected()){
                    selectedTaluka = sTaluka.getName();
                    selectedTalukaId = sTaluka.get_id();
                    break;
                }
            }
            tvTalukaFilter.setText(selectedTaluka);
            filteredStructureListData.clear();
            for (StructureData structureData : structureListData) {
                if (structureData.getTalukaId().equalsIgnoreCase(selectedTalukaId)) {
                    filteredStructureListData.add(structureData);
                }
            }
            rvStructureList.setAdapter(structureListAdapter);
            structureListAdapter.notifyDataSetChanged();
        } else if(type.equals("Select District")){
            for(CustomSpinnerObject sDistrict: structureDistrictList){
                if(sDistrict.isSelected()){
                    selectedDistrict = sDistrict.getName();
                    selectedDistrictId = sDistrict.get_id();
                    break;
                }
            }
            tvDistrictFilter.setText(selectedDistrict);
            filteredStructureListData.clear();
            for (StructureData structureData : structureListData) {
                if (structureData.getDistrictId().equalsIgnoreCase(selectedDistrictId)) {
                    filteredStructureListData.add(structureData);
                }
            }
            rvStructureList.setAdapter(structureListAdapter);
            structureListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_taluka_filter){
//            if(tvDistrictFilter.getText()!= null && tvDistrictFilter.getText().length()>0) {
//                CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(), this, "Select Taluka", machineTalukaList,
//                        false);
//                cddTaluka.show();
//                cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
//                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                                .findViewById(android.R.id.content), "Your District is not available in your profile." +
//                                "Please update your profile.",
//                        Snackbar.LENGTH_LONG);
//            }
            if(tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length()>0) {
                UserInfo userInfo = Util.getUserObjectFromPref();
                machineDeployStructureListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                        "5c4ab05cd503a372d0391467",
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Your District is not available in your profile." +
                                "Please update your profile.",
                        Snackbar.LENGTH_LONG);
            }
        } else if(view.getId() == R.id.tv_district_filter) {
            if(tvStateFilter.getText() != null && tvStateFilter.getText().toString().length()>0) {
                UserInfo userInfo = Util.getUserObjectFromPref();
                machineDeployStructureListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                        "5c4ab05cd503a372d0391467",
                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Your State is not available in your profile." +
                                "Please update your profile.",
                        Snackbar.LENGTH_LONG);
            }
        } else if(view.getId() == R.id.btn_deploy) {
            showDeployDialog(getContext(), "CONFIRM", "Are you sure you want to deploy " +
                    "machine( "+machineId+ " ) on structure( "+structureListData.get(selectedPosition)
                    .getStructureId()+") ?", "Yes", "No");
        }
    }

    public void showDeployDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                machineDeployStructureListFragmentPresenter.deployMachine(structureListData.get
                        (selectedPosition).getStructureId(), machineId);
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
