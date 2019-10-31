package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.presenter.MachineMouFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;

public class MachineMouFirstFragment extends Fragment  implements APIDataListener, CustomSpinnerListener, View.OnClickListener {
    private View machineMouFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineMouFragmentPresenter machineMouFragmentPresenter;
    String machineId;
    private ArrayList<CustomSpinnerObject> mOwnerTypeList = new ArrayList<>();
    private EditText editOwnerType,etUniqueIdNumber,etMachineDistrict,etMachineTaluka,etMachineType,etYear,
            etMachineMakeModel,etMeterWorking,etRtoNumber,etChasisNumber,etExcavationCapacity,etDieselCapacity;
    private Button btnFirstPartMou, btnEligilble, btnNotEligible;
    private LinearLayout llEligible;
    private int statusCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFragmentView = inflater.inflate(R.layout.fragment_machine_mou, container, false);
        return machineMouFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        statusCode = getActivity().getIntent().getIntExtra("statusCode",0);
        progressBarLayout = machineMouFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFragmentView.findViewById(R.id.pb_profile_act);
        editOwnerType = machineMouFragmentView.findViewById(R.id.et_owner_type);
        etUniqueIdNumber = machineMouFragmentView.findViewById(R.id.et_uniqueId_number);
        etMachineDistrict = machineMouFragmentView.findViewById(R.id.et_machine_district);
        etMachineTaluka = machineMouFragmentView.findViewById(R.id.et_machine_taluka);
        etMachineType = machineMouFragmentView.findViewById(R.id.et_machine_type);
        etYear = machineMouFragmentView.findViewById(R.id.et_year);
        etMachineMakeModel = machineMouFragmentView.findViewById(R.id.et_machine_make_model);
        etMeterWorking = machineMouFragmentView.findViewById(R.id.et_meter_working);
        etRtoNumber = machineMouFragmentView.findViewById(R.id.et_rto_number);
        etChasisNumber = machineMouFragmentView.findViewById(R.id.et_chasis_number);
        etExcavationCapacity = machineMouFragmentView.findViewById(R.id.et_excavation_capacity);
        etDieselCapacity = machineMouFragmentView.findViewById(R.id.et_diesel_capacity);

        btnFirstPartMou = machineMouFragmentView.findViewById(R.id.btn_first_part_mou);
        if(statusCode == Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
            btnEligilble = machineMouFragmentView.findViewById(R.id.btn_eligible);
            btnNotEligible = machineMouFragmentView.findViewById(R.id.btn_not_eligible);
            btnEligilble.setOnClickListener(this);
            btnNotEligible.setOnClickListener(this);
            btnFirstPartMou.setVisibility(View.GONE);
        } else {
            llEligible = machineMouFragmentView.findViewById(R.id.ll_eligible);
            llEligible.setVisibility(View.GONE);
            btnFirstPartMou.setOnClickListener(this);
        }
        machineMouFragmentPresenter = new MachineMouFragmentPresenter(this);
        if(statusCode == Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
            btnFirstPartMou.setText("Create Machine");
            setUIForMachineCreate();
        } else {
            setMachineFirstData();
        }
    }

    private void setMachineFirstData() {
        //((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOwnedBy("Sagar Mahajan");
        editOwnerType.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getOwnedBy());
        etUniqueIdNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode());
        etMachineDistrict.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getDistrict());
        etMachineTaluka.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getTaluka());
        etMachineType.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachinetype());
        etYear.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getManufacturedYear());
        etMachineMakeModel.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMakeModel());
//        etMeterWorking.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etRtoNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etChasisNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etExcavationCapacity.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
        etDieselCapacity.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getDiselTankCapacity());
    }

    private void setUIForMachineCreate() {
        editOwnerType.setFocusable(true);
        etUniqueIdNumber.setFocusable(true);
        etMachineDistrict.setFocusable(true);
        etMachineTaluka.setFocusable(true);
        etMachineType.setFocusable(true);
        etYear.setFocusable(true);
        etMachineMakeModel.setFocusable(true);
        etMeterWorking.setFocusable(true);
        etRtoNumber.setFocusable(true);
        etChasisNumber.setFocusable(true);
        etExcavationCapacity.setFocusable(true);
        etDieselCapacity.setFocusable(true);
    }

    private void setCreateMachineData() {
        //((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOwnedBy("Sagar Mahajan");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOwnedBy(editOwnerType.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMachineCode(etUniqueIdNumber.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setDistrict(etMachineDistrict.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setTaluka(etMachineTaluka.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMachinetype(etMachineType.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setManufacturedYear(etYear.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMakeModel(etMachineMakeModel.getText().toString().trim());
//        etMeterWorking.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etRtoNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etChasisNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
//        etExcavationCapacity.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().get);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setDiselTankCapacity(etDieselCapacity.getText().toString().trim());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFragmentPresenter != null) {
            machineMouFragmentPresenter.clearData();
            machineMouFragmentPresenter = null;
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

    @Override
    public void onCustomSpinnerSelection(String type) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_contract_date:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(getActivity(), this, "Select Owner Type",
                        mOwnerTypeList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_first_part_mou:
                if(statusCode == Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
                    if(isAllDataValid()) {

                        machineMouFragmentPresenter.createMachine(((MachineMouActivity) getActivity()).getMachineDetailData());
                    }
                } else {
                    ((MachineMouActivity) getActivity()).openFragment("MachineMouSecondFragment");
                }
                break;
            case R.id.btn_eligible:
                machineMouFragmentPresenter.updateMachineStructureStatus(((MachineMouActivity) getActivity()).getMachineDetailData()
                        .getMachine().getId(), ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode(),
                        Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
                break;
            case R.id.btn_not_eligible:
                machineMouFragmentPresenter.updateMachineStructureStatus(((MachineMouActivity) getActivity()).getMachineDetailData()
                        .getMachine().getId(), ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode(),
                        Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
                break;
        }
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(editOwnerType.getText().toString().trim())
                || TextUtils.isEmpty(etUniqueIdNumber.getText().toString().trim())
                || TextUtils.isEmpty(etMachineDistrict.getText().toString().trim())
                || TextUtils.isEmpty(etMachineTaluka.getText().toString().trim())
                || TextUtils.isEmpty(etMachineType.getText().toString().trim())
                || TextUtils.isEmpty(etYear.getText().toString().trim())
                || TextUtils.isEmpty(etMachineMakeModel.getText().toString().trim())
                || TextUtils.isEmpty(etMeterWorking.getText().toString().trim())
                || TextUtils.isEmpty(etRtoNumber.getText().toString().trim())
                || TextUtils.isEmpty(etChasisNumber.getText().toString().trim())
                || TextUtils.isEmpty(etExcavationCapacity.getText().toString().trim())
                || TextUtils.isEmpty(etDieselCapacity.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineMouFragmentPresenter.UPDATE_MACHINE_STATUS)){
            if(status == 200){
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
        if(requestId.equals(MachineMouFragmentPresenter.UPDATE_STRUCTURE_STATUS)){
            if(status == 200){

            }
        }
    }
}
