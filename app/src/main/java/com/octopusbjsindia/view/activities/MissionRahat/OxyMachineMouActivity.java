package com.octopusbjsindia.view.activities.MissionRahat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.databinding.LayoutMouOxymachineBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.MachineModel;
import com.octopusbjsindia.models.MissionRahat.MouRequestModel;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineMouActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;

import java.util.ArrayList;
import java.util.Objects;

public class OxyMachineMouActivity extends AppCompatActivity implements APIDataListener,
        CustomSpinnerListener {
    private OxyMachineMouActivityPresenter presenter;
    LayoutMouOxymachineBinding mouOxymachineBinding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_mou_oxymachine);
        mouOxymachineBinding = LayoutMouOxymachineBinding.inflate(getLayoutInflater());
        View view = mouOxymachineBinding.getRoot();
        setContentView(view);
        activity = OxyMachineMouActivity.this;
        presenter = new OxyMachineMouActivityPresenter(this);
        initView();
    }

    private void initView() {

        setClickListners();
        mouOxymachineBinding.toolbar.toolbarTitle.setText("Machine MOU");


    }

    private void setClickListners() {
        mouOxymachineBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitData();
                if (mouOxymachineBinding.checkBoxTerms.isChecked()){

                    //submitData();
                    if (isAllDataValid()) {
                        showDialog(activity, "Alert", "Do you want to submit MOU?", "No", "Yes");
                    }
                }else {
                    Util.showToast(OxyMachineMouActivity.this,"Please check terms and conditions before submit.");
                }
            }
        });
        mouOxymachineBinding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setStartDate();
                Util.showDateDialogMin(OxyMachineMouActivity.this, mouOxymachineBinding.etStartDate);
            }
        });
        mouOxymachineBinding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showDateDialogMin(OxyMachineMouActivity.this, mouOxymachineBinding.etEndDate);
                //setEndDate(); not used for now keep open
                /*Util.showEndDateWithMonthDifference(OxyMachineMouActivity.this, mouOxymachineBinding.etEndDate,
                        Util.getDateInLong(mouOxymachineBinding.etStartDate.getText().toString()),2);*/
            }
        });
        mouOxymachineBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callSubmitMethod() {
        presenter.submitMouData(getMouRequestData());
    }

    private String getMouRequestData() {
        MouRequestModel mouRequestModel = new MouRequestModel();
        Gson gson = new GsonBuilder().create();
        mouRequestModel.setRequirementId("608588760bdd640b9538eab7");
        mouRequestModel.setStartDate(String.valueOf(Util.getDateInLong(mouOxymachineBinding.etStartDate.getText().toString())));
        mouRequestModel.setEndDate(String.valueOf(Util.getDateInLong(mouOxymachineBinding.etEndDate.getText().toString())));
        String paramjson = gson.toJson(mouRequestModel);
        return paramjson;
    }



    public void setMasterData() {

    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (mouOxymachineBinding.profileActProgressBar != null && mouOxymachineBinding.pbProfileAct != null) {
                mouOxymachineBinding.profileActProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (mouOxymachineBinding.profileActProgressBar != null && mouOxymachineBinding.pbProfileAct != null) {
                mouOxymachineBinding.profileActProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onCustomSpinnerSelection(String type) {

    }

    public void showSuccessResponse(String response) {
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
    }

    //submit button confirmation
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
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
                try {
                    dialog.dismiss();
                    callSubmitMethod();
                } catch (IllegalStateException e) {
                    Log.e("TAG", e.getMessage());
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }



    private boolean isAllDataValid() {
        if (mouOxymachineBinding.etStartDate.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter MOU start date.", Snackbar.LENGTH_LONG);
            return false;
        } else if (mouOxymachineBinding.etEndDate.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter MOU end date.", Snackbar.LENGTH_LONG);
            return false;
        } else {

        }
        return true;
    }

}