package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;

public class UserRegistrationMatrimonyEducationFragment extends Fragment implements View.OnClickListener {
    private View fragmentview;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_education, et_occupation_type, et_employer, et_job_profile, et_Annual_income;


    public static UserRegistrationMatrimonyEducationFragment newInstance() {
        return new UserRegistrationMatrimonyEducationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_education, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);
        //Edittext
        et_education = fragmentview.findViewById(R.id.et_education);
        et_occupation_type = fragmentview.findViewById(R.id.et_occupation_type);
        et_employer = fragmentview.findViewById(R.id.et_employer);
        et_job_profile = fragmentview.findViewById(R.id.et_job_profile);
        et_Annual_income = fragmentview.findViewById(R.id.et_Annual_income);


        //set Listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);
        et_education.setOnClickListener(this);
        et_occupation_type.setOnClickListener(this);
        et_employer.setOnClickListener(this);
        et_job_profile.setOnClickListener(this);
        et_Annual_income.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tvmessage.setText("Fragment ONe");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loadnext:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(3);
                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(1);
                break;
            case R.id.et_education:
                //selectStartDate(et_birth_date);
                break;

        }
    }
}
