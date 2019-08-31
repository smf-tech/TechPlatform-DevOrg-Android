package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Util;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;

public class UserRegistrationMatrimonyAboutmeFragment extends Fragment implements View.OnClickListener {
    private View fragmentview;
    private Button btn_load_next,btn_loadprevious;
    private TextView tv_pagetitle;
    private ImageView img_user_profle;
    private EditText et_education,et_about_me,et_partner_expectation,et_achivements,et_other_remark;
    private CheckBox checkbox_community_preference;

    public static UserRegistrationMatrimonyAboutmeFragment newInstance() {
        return new UserRegistrationMatrimonyAboutmeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_aboutme, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //Button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);

        //edittext
        et_education = fragmentview.findViewById(R.id.et_education);
        et_about_me = fragmentview.findViewById(R.id.et_about_me);
        et_partner_expectation = fragmentview.findViewById(R.id.et_partner_expectation);
        et_achivements = fragmentview.findViewById(R.id.et_achivements);
        et_other_remark = fragmentview.findViewById(R.id.et_other_remark);


        //add listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tvmessage.setText("Fragment ONe");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_loadnext:
                Util.showToast("Call Submit",getActivity());
                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity)getActivity()).loadNextScreen(3);
                break;
        }
    }
}
