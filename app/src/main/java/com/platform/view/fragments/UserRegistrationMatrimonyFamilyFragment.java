package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;

public class UserRegistrationMatrimonyFamilyFragment extends Fragment implements View.OnClickListener {
    private View fragmentview;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_family_type, et_Shakha_self, et_shakha_mama, et_shakha_dada, et_shakha_nana, et_father_name, et_father_occupation, et_family_income,
            et_mothers_name, et_mothers_occupation, et_brother, et_sister;


    public static UserRegistrationMatrimonyFamilyFragment newInstance() {
        return new UserRegistrationMatrimonyFamilyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_family, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);
        //edit text
        et_family_type = fragmentview.findViewById(R.id.et_family_type);
        et_Shakha_self = fragmentview.findViewById(R.id.et_Shakha_self);
        et_shakha_mama = fragmentview.findViewById(R.id.et_shakha_mama);
        et_shakha_dada = fragmentview.findViewById(R.id.et_shakha_dada);
        et_shakha_nana = fragmentview.findViewById(R.id.et_shakha_nana);
        et_father_name = fragmentview.findViewById(R.id.et_father_name);
        et_father_occupation = fragmentview.findViewById(R.id.et_father_occupation);
        et_family_income = fragmentview.findViewById(R.id.et_family_income);
        et_mothers_name = fragmentview.findViewById(R.id.et_mothers_name);
        et_mothers_occupation = fragmentview.findViewById(R.id.et_mothers_occupation);
        et_brother = fragmentview.findViewById(R.id.et_brother);
        et_sister = fragmentview.findViewById(R.id.et_sister);

        //set listeners
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
        switch (v.getId()) {
            case R.id.btn_loadnext:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(3);
                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(1);
                break;
        }
    }
}
