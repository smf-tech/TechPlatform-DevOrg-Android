package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class MockTestOfTrainerFragment extends Fragment implements View.OnClickListener{

    View view;
    Button bt_next;
    public EditText  et_select_mobile,et_communication_skill,et_presentation_skill,et_program_seriousness,et_grasping_power,et_other_remark;
    String batchId,trainerId,trainerPhone;

    private int mYear, mMonth, mDay, mHour, mMinute,position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mocktest_for_trainer, container, false);
        bt_next = view.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);


        et_select_mobile = view.findViewById(R.id.et_select_mobile);
        et_communication_skill = view.findViewById(R.id.et_communication_skill);
        et_presentation_skill = view.findViewById(R.id.et_presentation_skill);
        et_program_seriousness = view.findViewById(R.id.et_program_seriousness);
        et_grasping_power = view.findViewById(R.id.et_grasping_power);
        et_other_remark = view.findViewById(R.id.et_other_remark);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            batchId = getArguments().getString("batch_id");
            trainerId = getArguments().getString("trainer_id");
            trainerPhone = getArguments().getString("phone");
            position = getArguments().getInt("position");
            Log.d("batch_id received","-> "+batchId);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_next:
                if (isAllInputsValid()) {
                    ((TrainerBatchListActivity) getActivity()).fillMockTestFormToBatch(0, 1, new Gson().toJson(getMockTestReqJson(0)));
                }
                break;

        }
    }



    public JsonObject getMockTestReqJson(int pos) {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("trainer_id", trainerId);
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("trainer_phone",trainerPhone);
        requestObject.addProperty("communication_skills", 2);
        requestObject.addProperty("presentation_skills", 2);
        requestObject.addProperty("program_seriousness", 1);
        requestObject.addProperty("grasping_power", 1);
        requestObject.addProperty("other", "The trainer is good.");


        return requestObject;
    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_communication_skill.getText().toString().trim().length() == 0) {
            msg = "Please enter Communication rating.";//getResources().getString(R.string.msg_enter_name);
        } else if (et_presentation_skill.getText().toString().trim().length() == 0) {
            msg = "Please enter presentation rating";//getResources().getString(R.string.msg_enter_name);
        } else if (et_select_mobile.getText().toString().trim().length() == 0) {
            msg = "Please enter your mobile.";//getResources().getString(R.string.msg_enter_proper_date);
        }else if (et_program_seriousness.getText().toString().trim().length()==0)
        {
            msg = "Please enter program seriousness rating";
        }else if (et_grasping_power.getText().toString().trim().length()==0)
        {
            msg = "Please enter grasping power";

        }else if (et_other_remark.getText().toString().trim().length()==0)
        {
            msg = "Please enter remark";
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

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
}
