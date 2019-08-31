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

public class UserRegistrationMatrimonyResidenceFragment extends Fragment implements View.OnClickListener {
    private View fragmentview;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_address, et_city_town, et_state, et_country, et_primary_mobile, et_primary_mobile_two, et_primary_email;
    private EditText et_education, et_occupation_type, et_employer, et_job_profile, et_Annual_income;


    public static UserRegistrationMatrimonyResidenceFragment newInstance() {
        return new UserRegistrationMatrimonyResidenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_residence, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //Button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);


        //-------
        //edittext
        et_education = fragmentview.findViewById(R.id.et_education);
        et_occupation_type = fragmentview.findViewById(R.id.et_occupation_type);
        et_employer = fragmentview.findViewById(R.id.et_employer);
        et_job_profile = fragmentview.findViewById(R.id.et_job_profile);
        et_Annual_income = fragmentview.findViewById(R.id.et_Annual_income);
        et_address = fragmentview.findViewById(R.id.et_address);
        et_city_town = fragmentview.findViewById(R.id.et_city_town);
        et_state = fragmentview.findViewById(R.id.et_state);
        et_country = fragmentview.findViewById(R.id.et_country);
        et_primary_mobile = fragmentview.findViewById(R.id.et_primary_mobile);
        et_primary_mobile_two = fragmentview.findViewById(R.id.et_primary_mobile_two);
        et_primary_email = fragmentview.findViewById(R.id.et_primary_email);


        //set Listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);
        et_education.setOnClickListener(this);
        et_occupation_type.setOnClickListener(this);
        et_employer.setOnClickListener(this);
        et_job_profile.setOnClickListener(this);
        et_Annual_income.setOnClickListener(this);

        et_address.setOnClickListener(this);
        et_city_town.setOnClickListener(this);
        et_state.setOnClickListener(this);
        et_country.setOnClickListener(this);
        et_primary_mobile.setOnClickListener(this);
        et_primary_mobile_two.setOnClickListener(this);
        et_primary_email.setOnClickListener(this);

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
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(4);
                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(2);
                break;
        }
    }


}
