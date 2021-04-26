package com.octopusbjsindia.view.activities.MissionRahat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.databinding.LayoutMouOxymachineBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.MachineModel;
import com.octopusbjsindia.models.MissionRahat.MouRequestModel;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineMouActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.fragments.formComponents.CheckboxFragment;

import java.util.ArrayList;

public class OxyMachineMouActivity extends AppCompatActivity implements APIDataListener,
        CustomSpinnerListener {
    private OxyMachineMouActivityPresenter presenter;
    LayoutMouOxymachineBinding mouOxymachineBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_mou_oxymachine);
        mouOxymachineBinding = LayoutMouOxymachineBinding.inflate(getLayoutInflater());
        View view = mouOxymachineBinding.getRoot();
        setContentView(view);
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
                    callSubmitMethod();
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
        //mouRequestModel.set
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
    }
}