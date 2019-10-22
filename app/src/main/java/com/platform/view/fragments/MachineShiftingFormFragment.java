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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.presenter.MachineShiftingFormFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;

public class MachineShiftingFormFragment extends Fragment implements APIDataListener,
        View.OnClickListener, CustomSpinnerListener {
    private View machineShiftingFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    String machineId, currentStructureId, newStructureId, isDieselFilled , dieselProvidedBy;
    private EditText etIsDieselFilled, etProvideBy, etDieselQuantity, etstartMeterReading, etTravelDistance,
     etTravelTime;
    private Button btnSubmit;
    private MachineShiftingFormFragmentPresenter machineShiftingFormFragmentPresenter;
    private ArrayList<CustomSpinnerObject> isDieselFilledOptionsList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isDieselProvidedByList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineShiftingFormFragmentView = inflater.inflate(R.layout.fragment_machine_shifting_form,
                container, false);
        return machineShiftingFormFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        newStructureId = getActivity().getIntent().getStringExtra("newStructureId");
        init();
    }

    private void init() {
        progressBarLayout = machineShiftingFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineShiftingFormFragmentView.findViewById(R.id.pb_profile_act);
        machineShiftingFormFragmentPresenter = new MachineShiftingFormFragmentPresenter(this);
        etIsDieselFilled = machineShiftingFormFragmentView.findViewById(R.id.et_is_diesel_filled);
        etIsDieselFilled.setOnClickListener(this);
        etProvideBy = machineShiftingFormFragmentView.findViewById(R.id.et_provided_by);
        etProvideBy.setOnClickListener(this);
        etDieselQuantity = machineShiftingFormFragmentView.findViewById(R.id.et_diesel_filled_quantity);
        etDieselQuantity.setOnClickListener(this);
        etstartMeterReading = machineShiftingFormFragmentView.findViewById(R.id.et_start_meter_reading);
        etstartMeterReading.setOnClickListener(this);
        etTravelDistance = machineShiftingFormFragmentView.findViewById(R.id.et_travel_distance);
        etTravelDistance.setOnClickListener(this);
        etTravelTime = machineShiftingFormFragmentView.findViewById(R.id.et_travel_time);
        etTravelTime.setOnClickListener(this);
        btnSubmit = machineShiftingFormFragmentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineShiftingFormFragmentPresenter != null) {
            machineShiftingFormFragmentPresenter.clearData();
            machineShiftingFormFragmentPresenter = null;
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

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.et_is_diesel_filled :
                isDieselFilledOptionsList.clear();
                CustomSpinnerObject cDieselFilledYes = new CustomSpinnerObject();
                cDieselFilledYes.setName("Yes");
                cDieselFilledYes.set_id("1");
                cDieselFilledYes.setSelected(false);
                isDieselFilledOptionsList.add(cDieselFilledYes);
                CustomSpinnerObject cDieselFilledNo = new CustomSpinnerObject();
                cDieselFilledNo.setName("No");
                cDieselFilledNo.set_id("2");
                cDieselFilledNo.setSelected(false);
                isDieselFilledOptionsList.add(cDieselFilledNo);
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
                        getResources().getString(R.string.is_diesel_filled), isDieselFilledOptionsList,
                        false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            break;
            case R.id.et_provided_by :
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        getResources().getString(R.string.diesel_provided_by), isDieselProvidedByList,
                        false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_submit :
                if (isAllDataValid()) {
                    submitShiftingForm();
                }
                break;
        }
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(etIsDieselFilled.getText().toString().trim())
                || TextUtils.isEmpty(etProvideBy.getText().toString().trim())
                || TextUtils.isEmpty(etDieselQuantity.getText().toString().trim())
                || TextUtils.isEmpty(etstartMeterReading.getText().toString().trim())
                || TextUtils.isEmpty(etTravelDistance.getText().toString().trim())
                || TextUtils.isEmpty(etTravelTime.getText().toString().trim())) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void submitShiftingForm(){
        machineShiftingFormFragmentPresenter.shiftMachine(etIsDieselFilled.getText().toString().trim(),
                etProvideBy.getText().toString().trim(), etDieselQuantity.getText().toString().trim(),
                etstartMeterReading.getText().toString().trim(), etTravelDistance.getText().toString().trim(),
                etTravelTime.getText().toString().trim(), machineId, currentStructureId,
                newStructureId);
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineShiftingFormFragmentPresenter.SUBMIT_MACHINE_SHIFTING_FORM)){
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
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Has diesel filled for shifting purpose?":
                for (CustomSpinnerObject dieselFilled : isDieselFilledOptionsList) {
                    if (dieselFilled.isSelected()) {
                        isDieselFilled = dieselFilled.getName();
                        break;
                    }
                }
                etIsDieselFilled.setText(isDieselFilled);
                break;
            case "Diesel provided by":
                for (CustomSpinnerObject providedBy : isDieselProvidedByList) {
                    if(providedBy.isSelected()) {
                        dieselProvidedBy = providedBy.getName();
                        break;
                    }
                }
                etProvideBy.setText(dieselProvidedBy);
                break;
        }

    }
}
