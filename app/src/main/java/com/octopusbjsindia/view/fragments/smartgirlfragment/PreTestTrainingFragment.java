package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class PreTestTrainingFragment extends Fragment implements View.OnClickListener, CustomSpinnerListener {
    private ArrayList<CustomSpinnerObject> genderList = new ArrayList<>();
    String selectedGander;
    View view;
    Button bt_next;
    public EditText tv_startdate,tv_enddate;
    public EditText et_name, et_email, et_select_mobile, et_select_age, et_select_education, et_select_gender, et_select_occupation, et_select_experience,
            et_select_training_location, et_is_teacher, et_select_school_name, et_program_for_girls, et_program_for_parents;
    String batchId;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pretest_training, container, false);
        bt_next = view.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);
        tv_startdate = view.findViewById(R.id.tv_startdate);
        tv_enddate= view.findViewById(R.id.tv_enddate);


        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setText(Util.getCurrentDate());
        tv_enddate.setText(Util.getCurrentDate());
        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_select_mobile = view.findViewById(R.id.et_select_mobile);
        et_select_age = view.findViewById(R.id.et_select_age);
        et_select_education = view.findViewById(R.id.et_select_education);
        et_select_gender = view.findViewById(R.id.et_select_gender);
        et_select_occupation = view.findViewById(R.id.et_select_occupation);
        et_select_experience = view.findViewById(R.id.et_select_experience);
        et_select_training_location = view.findViewById(R.id.et_select_training_location);
        et_is_teacher = view.findViewById(R.id.et_is_teacher);
        et_select_school_name = view.findViewById(R.id.et_select_school_name);
        et_program_for_girls = view.findViewById(R.id.et_program_for_girls);
        et_program_for_parents = view.findViewById(R.id.et_program_for_parents);
        et_select_gender.setOnClickListener(this);
        //
        CustomSpinnerObject male = new CustomSpinnerObject();
        male.set_id("1");
        male.setName("Male");
        genderList.add(male);
        CustomSpinnerObject female = new CustomSpinnerObject();
        female.set_id("2");
        female.setName("Female");
        genderList.add(female);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            batchId = getArguments().getString("batch_id");
            Log.d("batch_id received","-> "+batchId);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_next:
                if (isAllInputsValid()) {
                    ((TrainerBatchListActivity) getActivity()).fillPreTestFormToBatch(0, 1, new Gson().toJson(getPreTestReqJson(0)));
                }
                break;
            case R.id.tv_startdate:
                selectStartDate(tv_startdate);
                break;
            case R.id.tv_enddate:
                selectStartDate(tv_enddate);
                break;
            case R.id.et_select_gender:
                CustomSpinnerDialogClass csdGander = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Gender", genderList, false);
                csdGander.show();
                csdGander.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;

        }
    }



    public JsonObject getPreTestReqJson(int pos) {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("trainer_name", et_name.getText().toString());
        requestObject.addProperty("email", et_email.getText().toString());
        //requestObject.addProperty("trainer_phone", et_select_mobile.getText().toString());
        requestObject.addProperty("age", et_select_age.getText().toString());
        requestObject.addProperty("education", et_select_education.getText().toString());
        requestObject.addProperty("gender", et_select_gender.getText().toString());
        requestObject.addProperty("occupation", et_select_occupation.getText().toString());


        //as per discussion with KB userId is TrainerId

        //requestObject.addProperty("trainer_name", "Kumood S Bongale");
        requestObject.addProperty("trainer_id", Util.getUserObjectFromPref().getId());
        requestObject.addProperty("trainer_phone",Util.getUserObjectFromPref().getUserMobileNumber());

        return requestObject;
    }

    /*    private boolean isAllInputsValid() {
        boolean isInputValid = true;
        if (TextUtils.isEmpty(getUserMobileNumber())) {
            Util.setError(etAnswer, getResources().getString(R.string.msg_mobile_number_is_empty));
            isInputValid = false;
        } else if (getUserMobileNumber().trim().length() < 10 || getUserMobileNumber().trim().length() > 10) {
            Util.setError(etAnswer, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid = false;
        } else if (et_answer_name.getText().toString().trim().length() == 0) {
            Util.setError(et_answer_name, "Please enter the name of user.");
            isInputValid = false;
        }
        return isInputValid;
    }

    private String getUserMobileNumber() {
        if (etAnswer != null) {
            return String.valueOf(etAnswer.getText());
        }
        return "";
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void selectStartDate(TextView textview) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                        textview.setText(selectedDateString);
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your name.";//getResources().getString(R.string.msg_enter_name);
        } else if (et_email.getText().toString().trim().length() != 0 &&
                !Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString().trim()).matches()) {
            msg = "Please enter valid email";//getResources().getString(R.string.msg_enter_name);
        } else if (et_select_mobile.getText().toString().trim().length() == 0) {
            msg = "Please enter your mobile.";//getResources().getString(R.string.msg_enter_proper_date);
        }else if (et_select_age.getText().toString().trim().length()==0)
        {
            msg = "Please enter your age.";
        }else if (et_select_education.getText().toString().trim().length()==0)
        {
            msg = "Please enter your education.";

        }else if (et_select_gender.getText().toString().trim().length()==0)
        {
            msg = "Please enter your gender.";
        }
        else if (et_select_occupation.getText().toString().trim().length() == 0) {
            msg = "Please enter your occupation.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Gender":
                for (CustomSpinnerObject obj : genderList) {
                    if (obj.isSelected()) {
                        selectedGander = obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                et_select_gender.setText(selectedGander);
                break;
        }
    }
}