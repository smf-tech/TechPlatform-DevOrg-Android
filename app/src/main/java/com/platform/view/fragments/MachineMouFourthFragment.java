package com.platform.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.OperatorDetails;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.events.CommonResponse;
import com.platform.presenter.MachineDetailsFragmentPresenter;
import com.platform.presenter.MachineMouFourthFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MachineMouFourthFragment extends Fragment implements View.OnClickListener, APIDataListener,
        CustomSpinnerListener {
    private View machineMouFourthFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnFourthPartMou, btnPreviousMou;;
    private MachineMouFourthFragmentPresenter machineMouFourthFragmentPresenter;
    private EditText etOperatorName, etOperatorLastName, etOperatorContact, etLicenseNumber, etOperatorTraining,
            etAppInstalled;
    private ArrayList<CustomSpinnerObject> isTrainingDoneList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isAppInstalledList = new ArrayList<>();
    private String selectedtrainingOption, selectedAppInstalledOption;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFourthFragmentView = inflater.inflate(R.layout.fragment_machine_mou_fourth, container, false);
        return machineMouFourthFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineMouFourthFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFourthFragmentView.findViewById(R.id.pb_profile_act);
        btnFourthPartMou = machineMouFourthFragmentView.findViewById(R.id.btn_fourth_part_mou);
        btnFourthPartMou.setOnClickListener(this);
        btnPreviousMou = machineMouFourthFragmentView.findViewById(R.id.btn_previous_mou);
        btnPreviousMou.setOnClickListener(this);
        etOperatorName = machineMouFourthFragmentView.findViewById(R.id.et_operator_name);
        etOperatorLastName = machineMouFourthFragmentView.findViewById(R.id.et_operator_last_name);
        etOperatorContact = machineMouFourthFragmentView.findViewById(R.id.et_operator_contact);
        etLicenseNumber = machineMouFourthFragmentView.findViewById(R.id.et_license_number);
        etOperatorTraining = machineMouFourthFragmentView.findViewById(R.id.et_operator_training);
        etOperatorTraining.setOnClickListener(this);
        etAppInstalled = machineMouFourthFragmentView.findViewById(R.id.et_app_installed);
        etAppInstalled.setOnClickListener(this);
        CustomSpinnerObject optionYes = new CustomSpinnerObject();
        optionYes.setName("Yes");
        optionYes.set_id("1");
        optionYes.setSelected(false);
        isTrainingDoneList.add(optionYes);
        isAppInstalledList.add(optionYes);
        CustomSpinnerObject optionNo = new CustomSpinnerObject();
        optionNo.setName("No");
        optionNo.set_id("2");
        optionNo.setSelected(false);
        isTrainingDoneList.add(optionNo);
        isAppInstalledList.add(optionNo);
        machineMouFourthFragmentPresenter = new MachineMouFourthFragmentPresenter(this);
    }

    private void setMachineFourthData() {
        OperatorDetails operatorDetails = new OperatorDetails();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setOperatorDetails(operatorDetails);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setFirstName
                (etOperatorName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setLastName
                (etOperatorLastName.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setAddress
//                (etProviderFirstName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setContactNumnber
                (etOperatorContact.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setLicenceNumber
                (etLicenseNumber.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().
                setIsTrainingDone(selectedtrainingOption);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().
                setIsAppInstalled(selectedAppInstalledOption);
        List<String> operatorImages  = new ArrayList();
        operatorImages.add("www.google.com");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setOperatorImages(operatorImages);
        machineMouFourthFragmentPresenter.submitMouData(((MachineMouActivity) getActivity()).getMachineDetailData());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFourthFragmentPresenter != null) {
            machineMouFourthFragmentPresenter.clearData();
            machineMouFourthFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fourth_part_mou:
                if(isAllDataValid()) {
                    setMachineFourthData();
                }
                break;
            case R.id.btn_previous_mou:
                break;
            case R.id.et_operator_training:
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Is Training Completed?", isTrainingDoneList,
                        false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_app_installed:
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Is App Installed?", isAppInstalledList,
                        false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(etOperatorName.getText().toString().trim())
                || TextUtils.isEmpty(etOperatorContact.getText().toString().trim())
                || TextUtils.isEmpty(etLicenseNumber.getText().toString().trim())
                || TextUtils.isEmpty(etOperatorTraining.getText().toString().trim())
                || TextUtils.isEmpty(etAppInstalled.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void showResponseDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
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
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineMouFourthFragmentPresenter.SUBMIT_MOU)){
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
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (requestID.equalsIgnoreCase(MachineMouFourthFragmentPresenter.SUBMIT_MOU)) {
            CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
            showResponseDialog("Confirmation", responseOBJ.getMessage(), "OK", "");
        }
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
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Is Training Completed?":
                for (CustomSpinnerObject trainingOption : isTrainingDoneList) {
                    if (trainingOption.isSelected()) {
                        selectedtrainingOption = trainingOption.getName();
                        break;
                    }
                }
                etOperatorTraining.setText(selectedtrainingOption);
                break;
            case "Is App Installed?":
                for (CustomSpinnerObject appInstallationOption : isAppInstalledList) {
                    if (appInstallationOption.isSelected()) {
                        selectedAppInstalledOption = appInstallationOption.getName();
                        break;
                    }
                }
                etAppInstalled.setText(selectedAppInstalledOption);
                break;
        }
    }
}
