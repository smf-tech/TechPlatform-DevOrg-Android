package com.platform.view.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.models.SujalamSuphalam.MachineListAPIResponse;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.leaves.LeaveData;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.StructureMachineListFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.MutiselectDialogAdapter;
import com.platform.view.adapters.SSDataListAdapter;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StructureMachineListFragment extends Fragment implements APIDataListener, View.OnClickListener, CustomSpinnerListener{
    private View structureMachineListFragmentView;
    int viewType;
    final Context context = getActivity();
    private RecyclerView rvDataList;
    private final ArrayList<MachineData> ssMachineListData = new ArrayList<>();
    private SSDataListAdapter ssDataListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureMachineListFragmentPresenter structureMachineListFragmentPresenter;
    private TextView tvDistrictFilter, tvTalukaFilter;
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaDeployList = new ArrayList<>();
    private String selectedTaluka, selectedTalukaId, selectedDeployTaluka, selectedDeployTalukaId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        structureMachineListFragmentView = inflater.inflate(R.layout.fragment_structure_machine_list, container, false);
        return structureMachineListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        viewType = bundle.getInt("viewType");
        init();
    }

    private void init(){
        progressBarLayout = structureMachineListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = structureMachineListFragmentView.findViewById(R.id.pb_profile_act);
        tvDistrictFilter = structureMachineListFragmentView.findViewById(R.id.tv_district_filter);
        tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
        tvTalukaFilter = structureMachineListFragmentView.findViewById(R.id.tv_taluka_filter);
        structureMachineListFragmentPresenter = new StructureMachineListFragmentPresenter(this);
        if(Util.getUserObjectFromPref().getRoleNames().equals(Constants.SSModule.DISTRICT_LEVEL)){
            tvTalukaFilter.setOnClickListener(this);
            if(tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length()>0) {
                UserInfo userInfo = Util.getUserObjectFromPref();
                structureMachineListFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                        "5c4ab05cd503a372d0391467",
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            }
        }
        rvDataList = structureMachineListFragmentView.findViewById(R.id.rv_data_list);
        rvDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ssDataListAdapter = new SSDataListAdapter(getActivity(), this, ssMachineListData);
        rvDataList.setAdapter(ssDataListAdapter);
        structureMachineListFragmentPresenter = new StructureMachineListFragmentPresenter(this);
        if(viewType == 1){
//            structureMachineListFragmentPresenter.getStrucuresList(Util.getUserObjectFromPref().getUserLocation().getDistrictIds()
//                    .get(0).getId(), Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
        } else {
//            structureMachineListFragmentPresenter.getMachinesList(Util.getUserObjectFromPref().getUserLocation().getDistrictIds()
//                    .get(0).getId(), Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId());
            if(Util.getUserObjectFromPref().getRoleNames().equals(Constants.SSModule.DISTRICT_LEVEL)) {
                structureMachineListFragmentPresenter.getDistrictMachinesList("5c66989ec7982d31cc6b86c3",
                        "5ced0c27d42f28124c0150ba");
            } else {
                structureMachineListFragmentPresenter.getMachinesList("5c66989ec7982d31cc6b86c3",
                        "5ced0c27d42f28124c0150ba", "5c66a53cd42f283b440013eb");
            }
        }
    }

    public void takeMouDoneAction(int position){
        showMouActionPopup(position);
    }

    public void showMouActionPopup(int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mou_action_layout);
        TextInputLayout tlyTerminateReason = dialog.findViewById(R.id.tly_terminate_reason);
        RecyclerView rvTaluka = dialog.findViewById(R.id.rv_taluka);

        RadioGroup rgMouAction = dialog.findViewById(R.id.rg_mou_action);
        RadioButton rbTerminate, rbDeploy;
        rbTerminate = dialog.findViewById(R.id.rb_terminate);
        rbDeploy = dialog.findViewById(R.id.rb_deploy);
        rgMouAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_terminate:
                        rvTaluka.setVisibility(View.GONE);
                        tlyTerminateReason.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_deploy:
                        machineTalukaDeployList.addAll(machineTalukaList);
                        tlyTerminateReason.setVisibility(View.GONE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rvTaluka.setLayoutManager(layoutManager);
                        MutiselectDialogAdapter mutiselectDialogAdapter  = new MutiselectDialogAdapter(getActivity(), machineTalukaDeployList, false);
                        rvTaluka.setAdapter(mutiselectDialogAdapter);
                        rvTaluka.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText("What would you like to do?");
            title.setVisibility(View.VISIBLE);

            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                for(CustomSpinnerObject mDeployTaluka: machineTalukaDeployList){
                    if(mDeployTaluka.isSelected()){
                        selectedDeployTaluka = mDeployTaluka.getName();
                        selectedDeployTalukaId = mDeployTaluka.get_id();
                        break;
                    }
                }
                if(selectedDeployTalukaId != null){
                    structureMachineListFragmentPresenter.terminateSubmitMou(ssMachineListData.get(position).getId(),
                            ssMachineListData.get(position).getMachineCode(), "Deployed", selectedDeployTalukaId);
                } else {

                }
                dialog.dismiss();
            });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (structureMachineListFragmentPresenter != null) {
            structureMachineListFragmentPresenter.clearData();
            structureMachineListFragmentPresenter = null;
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
//        if (getActivity() != null) {
//            getActivity().onBackPressed();
//        }
        getActivity().finish();
    }

    public void populateMachineData(String requestID, MachineListAPIResponse machineListData) {
        if (machineListData != null) {
            if (requestID.equals(StructureMachineListFragmentPresenter.GET_MACHINE_LIST)) {
                ssMachineListData.clear();
                for (MachineData machineData : machineListData.getData()) {
                    if (machineData != null) {
                        ssMachineListData.add(machineData);
                    }
                }
                rvDataList.setAdapter(ssDataListAdapter);
                ssDataListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineTalukaList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        if (tvDistrictFilter.getText().toString().equalsIgnoreCase(location.getDistrict().getName())) {
                            CustomSpinnerObject talukaList = new CustomSpinnerObject();
                            talukaList.set_id(location.getTalukaId());
                            talukaList.setName(location.getTaluka().getName());
                            talukaList.setSelected(false);
                            machineTalukaList.add(talukaList);
                        }
                    }

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_taluka_filter){
            if(tvDistrictFilter.getText()!= null && tvDistrictFilter.getText().length()>0) {
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(getActivity(), this, "Select Taluka", machineTalukaList,
                        false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Your District is not available in your profile." +
                                "Please update your profile.",
                        Snackbar.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if(type.equals("Select Taluka")){
            for(CustomSpinnerObject mTaluka: machineTalukaList){
                if(mTaluka.isSelected()){
                    selectedTaluka = mTaluka.getName();
                    selectedTalukaId = mTaluka.get_id();
                    break;
                }
            }
            tvTalukaFilter.setText(selectedTaluka);
        }
    }
}
