package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MachineNonUtilizationFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class MachineNonUtilizationFragment extends Fragment implements View.OnClickListener, APIDataListener,
        CustomSpinnerListener {

    private View siltTransportationRecordFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineNonUtilizationFragmentPresenter machineNonUtilizationFragmentPresenter;
    String machineId, currentStructureId;
    private final String TAG = MachineNonUtilizationFragment.class.getName();
    private Button btnSubmit;
    private EditText etReason, etOtherReason;
    private String selectedReason="", selectedReasonId ="101", otherReason="";
    private ArrayList<CustomSpinnerObject> reasonsList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        siltTransportationRecordFragmentView = inflater.inflate(R.layout.fragment_machine_non_utilization,
                container, false);
        return siltTransportationRecordFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        init();
    }

    private void init() {
        progressBarLayout = siltTransportationRecordFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = siltTransportationRecordFragmentView.findViewById(R.id.pb_profile_act);
        machineNonUtilizationFragmentPresenter = new MachineNonUtilizationFragmentPresenter(this);
        etReason = siltTransportationRecordFragmentView.findViewById(R.id.et_reason);
        etReason.setOnClickListener(this);
        etOtherReason = siltTransportationRecordFragmentView.findViewById(R.id.et_other_reason);
        etOtherReason.setOnClickListener(this);
        btnSubmit = siltTransportationRecordFragmentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("SS");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());
        masterDataList.size();
        for(int i = 0; i<masterDataList.size(); i++) {
            if(masterDataList.get(i).getForm().equals("machine_nonutilisation") && masterDataList.get(i).
                    getField().equals("machineNonUtilisation")) {
                for(int j = 0; j<masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    reasonsList.add(customSpinnerObject);
                }
            }
        }
    }

    private boolean isAllDataValid() {
        if (selectedReason == "" || selectedReason.length() == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please Select Non-utilization reason.",
                    Snackbar.LENGTH_LONG);
            return false;
        } else {
            if(selectedReasonId.equals("5daeb32b5b3fc94c4866c343")) {
                otherReason = etOtherReason.getText().toString().trim();
                if(otherReason == "" || otherReason.length() == 0){
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please Select Non-utilization reason.",
                            Snackbar.LENGTH_LONG);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineNonUtilizationFragmentPresenter != null) {
            machineNonUtilizationFragmentPresenter.clearData();
            machineNonUtilizationFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.et_reason) {
            CustomSpinnerDialogClass cddReason = new CustomSpinnerDialogClass(getActivity(), this,
                    "Select Reason", reasonsList, false);
            cddReason.show();
            cddReason.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if(view.getId() == R.id.btn_submit) {
            if(isAllDataValid()){
                machineNonUtilizationFragmentPresenter.submitNonUtilization(machineId,
                        selectedReasonId, otherReason);
            }
        }
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineNonUtilizationFragmentPresenter.NON_UTILIZATION)){
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
        //your background thread is still running. By the time that thread reaches the getActivity().runOnUiThread()
        // code,the activity no longer exists. So check if the activity still exists.
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if(type.equals("Select Reason")) {
            for (CustomSpinnerObject reason : reasonsList) {
                if (reason.isSelected()) {
                    selectedReason = reason.getName();
                    selectedReasonId = reason.get_id();
                    break;
                }
            }
            if(selectedReasonId.equals("5daeb32b5b3fc94c4866c343")) {
                etOtherReason.setVisibility(View.VISIBLE);
            } else {
                etOtherReason.setVisibility(View.GONE);
            }
            etReason.setText(selectedReason);
        }
    }
}
