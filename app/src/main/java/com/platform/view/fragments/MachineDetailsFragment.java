package com.platform.view.fragments;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.presenter.MachineDetailsFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.SSActionsActivity;

public class MachineDetailsFragment extends Fragment implements View.OnClickListener, APIDataListener {
    private View machineDetailsFragmentView;
    MachineData machineData;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineDetailsFragmentPresenter machineDetailsFragmentPresenter;
    private Button btnEligilble, btnNotEligible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDetailsFragmentView = inflater.inflate(R.layout.fragment_machine_details, container, false);
        return machineDetailsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineData = (MachineData) getActivity().getIntent().getSerializableExtra("machineData");
        init();
    }

    private void init(){
        progressBarLayout = machineDetailsFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDetailsFragmentView.findViewById(R.id.pb_profile_act);
        machineDetailsFragmentPresenter = new MachineDetailsFragmentPresenter(this);
        btnEligilble = machineDetailsFragmentView.findViewById(R.id.btn_eligible);
        btnNotEligible = machineDetailsFragmentView.findViewById(R.id.btn_not_eligible);
        btnEligilble.setOnClickListener(this);
        btnNotEligible.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineDetailsFragmentPresenter != null) {
            machineDetailsFragmentPresenter.clearData();
            machineDetailsFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_eligible){
            machineDetailsFragmentPresenter.updateMachineStructureStatus(machineData.getId(), machineData.getMachineCode(),
                    Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
        } else if(view.getId() == R.id.btn_not_eligible){
            machineDetailsFragmentPresenter.updateMachineStructureStatus(machineData.getId(), machineData.getMachineCode(),
                    Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
        }
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineDetailsFragmentPresenter.UPDATE_MACHINE_STATUS)){
            if(status == 200){
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
//        if(requestId.equals(MachineDetailsFragmentPresenter.UPDATE_STRUCTURE_STATUS)){
//            if(status == 200){
//
//            }
//        }
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
}
