package com.platform.view.fragments;

import android.content.Context;
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
import com.platform.presenter.MachineNonUtilizationFragmentPresenter;
import com.platform.presenter.SiltTransportationRecordFragmentPresenter;
import com.platform.utility.Util;

public class MachineNonUtilizationFragment extends Fragment implements View.OnClickListener, APIDataListener {

    private View siltTransportationRecordFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineNonUtilizationFragmentPresenter machineNonUtilizationFragmentPresenter;
    String machineId, currentStructureId;
    private final String TAG = MachineNonUtilizationFragment.class.getName();
    private Button btnSubmit;
    private EditText etReason, etOtherReason;
    private String selectedReason="", otherReason="";
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
    }

    private boolean isAllDataValid() {
        if (selectedReason == "" || selectedReason.length() == 0) {
            if(otherReason == "" || otherReason.length() == 0){
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Select reason or enter other reason.",
                        Snackbar.LENGTH_LONG);
                return false;
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

        }
        if(view.getId() == R.id.btn_submit) {
            if(isAllDataValid()){
                //machineNonUtilizationFragmentPresenter
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
}
