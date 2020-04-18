package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.form_component.MvUserNameResponseModel;
import com.octopusbjsindia.presenter.RegisterTrainerFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class RegisterTrainerFragment extends Fragment implements View.OnClickListener, APIDataListener {

    RegisterTrainerFragmentPresenter registerTrainerFragmentPresenter;
    View view;
    Button bt_next;
    public EditText tv_startdate,tv_enddate;
    public EditText et_name,et_select_mobile;
    String batchId;
    int registrationtype;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_regitser_to_training, container, false);
        registerTrainerFragmentPresenter = new RegisterTrainerFragmentPresenter(this);
        bt_next = view.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);


        et_name = view.findViewById(R.id.et_name);
        et_select_mobile = view.findViewById(R.id.et_select_mobile);


        et_select_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int count, int i2) {
                //Log.d("mvUserName_Phone", String.valueOf(charSequence));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int count, int i, int i2) {
                //Log.d("mvUserName_Phone", String.valueOf(charSequence));
                if (count >= 9) {

                    registerTrainerFragmentPresenter.GET_MV_USER_INFO(String.valueOf(charSequence));
                }else {
                    et_name.setText("");
                    et_name.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            batchId = getArguments().getString("batch_id");
             registrationtype =  getArguments().getInt("registrationtype");

            Log.d("batch_id received","-> "+batchId);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_next:
                if (registrationtype==1) {
                    if (et_select_mobile.getText().toString().trim().length() == 0) {
                        Toast.makeText(getActivity(), "Please enter valid mobile number.", Toast.LENGTH_LONG).show();
                    }else {
                        if (isAllInputsValid()) {
                            ((TrainerBatchListActivity) getActivity()).addTrainerTobatch(0, 1, new Gson().toJson(getAddTrainerReqJson(0)));
                        }
                    }
                }else {
                    if (et_select_mobile.getText().toString().trim().length() == 0) {
                        Toast.makeText(getActivity(), "Please enter valid mobile number.", Toast.LENGTH_LONG).show();
                    }else {
                        if (isAllInputsValid()) {
                            ((SmartGirlWorkshopListActivity) getActivity()).addBeneficiaryToWorkshop(0, 1, new Gson().toJson(getAddBeneficiaryReqJson(0)));
                        }
                    }

                }
                break;

        }
    }





        public JsonObject getAddTrainerReqJson(int pos) {
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("batch_id", batchId);
            requestObject.addProperty("phone", et_select_mobile.getText().toString());

            return requestObject;
        }

    public JsonObject getAddBeneficiaryReqJson(int pos) {
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("workshop_id", batchId);
        requestObject.addProperty("phone", et_select_mobile.getText().toString());

        return requestObject;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_name.getText().toString().trim().length() == 0) {
            msg = "Please enter the name.";//getResources().getString(R.string.msg_enter_name);
        } else if (et_select_mobile.getText().toString().trim().length() == 0) {
            msg = "Please enter the mobile number.";//getResources().getString(R.string.msg_enter_proper_date);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Log.d("mvUserName_Phone", response);
        Gson gson = new Gson();
        MvUserNameResponseModel mvUserNameResponseModel = gson.fromJson(response, MvUserNameResponseModel.class);
        if (mvUserNameResponseModel != null) {
            if (!TextUtils.isEmpty(mvUserNameResponseModel.getMvUserNameResponses().get(0).getName())) {
                et_name.setText(mvUserNameResponseModel.getMvUserNameResponses().get(0).getName());
                et_name.setEnabled(false);
            } else {
                et_name.setEnabled(true);
            }
        } else {
            et_name.setEnabled(true);
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }


}
