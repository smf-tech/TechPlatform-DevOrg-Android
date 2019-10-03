package com.platform.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Util;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.platform.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;

public class UserRegistrationMatrimonyResidenceFragment extends Fragment implements View.OnClickListener ,SingleSelectBottomSheet.MultiSpinnerListener {
    private View fragmentview;
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_address, et_city_town, et_state, et_country, et_primary_mobile, et_primary_mobile_two, et_primary_email;
    private EditText et_education,et_education_degree, et_occupation_type, et_employer, et_job_profile, et_Annual_income;


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
        et_education_degree = fragmentview.findViewById(R.id.et_education_degree);
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
        et_education_degree.setOnClickListener(this);
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
                setValuesInModel();

                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(2);
                break;
            case R.id.et_education:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("education_level")) {
                        showMultiSelectBottomsheet("Education","et_education", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_education_degree:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("qualification_degree")) {
                        showMultiSelectBottomsheet("Education Degree","et_education_degree", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;

            case R.id.et_occupation_type:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("occupation")) {
                        showMultiSelectBottomsheet("Occupation","et_occupation_type", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_Annual_income:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("income")) {
                        showMultiSelectBottomsheet("Annual Income","et_Annual_income", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_state:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("state")) {
                        showMultiSelectBottomsheet("State","et_state", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_country:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("country")) {
                        showMultiSelectBottomsheet("Country","et_country", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;



        }
    }

    private void setValuesInModel() {
        if (isAllInputsValid())
        {
            if (UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel != null) {
                if (UserRegistrationMatrimonyActivity.residentialDetails != null) {

                    UserRegistrationMatrimonyActivity.residentialDetails.setAddress(et_address.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setCity(et_city_town.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setState(et_state.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setCountry(et_country.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setPrimary_mobile(et_primary_mobile.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setSecondary_mobile(et_primary_mobile_two.getText().toString());
                    UserRegistrationMatrimonyActivity.residentialDetails.setPrimary_email_address(et_primary_email.getText().toString());

                    UserRegistrationMatrimonyActivity.educationalDetails.setEducation_level(et_education.getText().toString());
                    UserRegistrationMatrimonyActivity.educationalDetails.setQualification_degree(et_education_degree.getText().toString());
                    UserRegistrationMatrimonyActivity.educationalDetails.setIncome(et_Annual_income.getText().toString());


                    UserRegistrationMatrimonyActivity.occupationalDetails.setOccupation(et_occupation_type.getText().toString());
                    UserRegistrationMatrimonyActivity.occupationalDetails.setEmployer_company(et_employer.getText().toString());
                    UserRegistrationMatrimonyActivity.occupationalDetails.setBusiness_description(et_job_profile.getText().toString());


                    UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setResidential_details(UserRegistrationMatrimonyActivity.residentialDetails);
                    UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setEducational_details(UserRegistrationMatrimonyActivity.educationalDetails);
                    UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setOccupational_details(UserRegistrationMatrimonyActivity.occupationalDetails);


                } else {
                    Util.showToast("null object getPersonal_details()", getActivity());
                }
            } else {
                Util.showToast("null object", getActivity());
            }
            ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(4);
        }
    }


    //------------

    private void showMultiSelectBottomsheet(String Title,String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(getActivity(), selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selected, String spinnerName, String selectedValues) {
        switch (spinnerName) {
            case "et_education":
                et_education.setText(selectedValues);
                break;
            case "et_education_degree":
                et_education_degree.setText(selectedValues);
                break;
            case "et_occupation_type":
                et_occupation_type.setText(selectedValues);
                break;
            case "et_Annual_income":
                et_Annual_income.setText(selectedValues);
                break;
            case "et_state":
                et_state.setText(selectedValues);
                break;
            case "et_country":
                et_country.setText(selectedValues);
                break;


//            case "et_father_occupation":
//                et_father_occupation.setText(selectedValues);
//                break;
//            case "et_mothers_occupation":
//                et_mothers_occupation.setText(selectedValues);
//                break;
//            case "et_family_income":
//                et_family_income.setText(selectedValues);
//                break;
//                case "et_marital_status":
//                    et_marital_status.setText(selectedValues);
//                    break;
//                case "et_height":
//                    et_height.setText(selectedValues);
//                    break;
//                case "et_complexion":
//                    et_complexion.setText(selectedValues);
//                    break;
//                case "et_residance_status":
//                    et_residance_status.setText(selectedValues);
//                    break;
//
//                case "et_drink":
//                    et_drink.setText(ListDrink.get(selected));
//                    break;
//                case "et_smoke":
//                    et_smoke.setText(ListSmoke.get(selected));
//                    break;
//
//                case "et_blood_group":
//                    et_blood_group.setText(ListBloodGroup.get(selected));
//                    break;
//                case "et_weight":
//                    et_weight.setText(ListWeight.get(selected));
//                    break;
//                case "et_patrika_match":
//                    et_patrika_match.setText(ListmatchPatrika.get(selected));
//                    break;


        }
        //et_drink.setText(ListDrink.get(selected));
    }
    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_education.getText().toString().trim().length() == 0) {
            msg = "Please enter education category";//getResources().getString(R.string.msg_enter_name);
            et_education.requestFocus();
        } else
        if (et_education_degree.getText().toString().trim().length() == 0) {
            msg = "Please enter education degree";//getResources().getString(R.string.msg_enter_name);
            et_education.requestFocus();
        } else
        if (et_occupation_type.getText().toString().trim().length() == 0) {
            msg = "Please select the occupation type.";//getResources().getString(R.string.msg_enter_proper_date);
            et_occupation_type.requestFocus();
        } else if (et_occupation_type.getText().toString().trim().length() == 0) {
            msg = "Please select the occupation type.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_Annual_income.getText().toString().trim().length() == 0) {
            msg = "Please select the annual income.";//getResources().getString(R.string.msg_enter_proper_date);
            et_Annual_income.requestFocus();
        } else if (et_job_profile.getText().toString().trim().length() == 0) {
            msg = "Please enter job description.";//getResources().getString(R.string.msg_enter_proper_date);
            et_job_profile.requestFocus();
        } else if (et_address.getText().toString().trim().length() == 0) {
            msg = "Please enter your address.";//getResources().getString(R.string.msg_enter_proper_date);
            et_address.requestFocus();
        } else if (et_city_town.getText().toString().trim().length() == 0) {
            msg = "Please enter your city.";//getResources().getString(R.string.msg_enter_proper_date);
            et_city_town.requestFocus();
        } else if (et_state.getText().toString().trim().length() == 0) {
            msg = "Please enter your city.";//getResources().getString(R.string.msg_enter_proper_date);
            et_state.requestFocus();
        }
        else if (et_country.getText().toString().trim().length() == 0) {
            msg = "Please enter your city.";//getResources().getString(R.string.msg_enter_proper_date);
            et_country.requestFocus();
        }
        else if (et_primary_mobile.getText().toString().trim().length() != 10 ) {
            msg = "Please enter the valid mobile number";//getResources().getString(R.string.msg_enter_name);
            et_primary_mobile.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(et_primary_email.getText().toString().trim()).matches()) {
            msg = getResources().getString(R.string.msg_enter_valid_email_id);
            et_primary_email.requestFocus();
        } else if (et_country.getText().toString().trim().length() == 0) {
            msg = "Please mention the Country"; //getResources().getString(R.string.msg_enter_name);
        }
        /*else if (et_education.getText().toString().trim().length() == 0) {
            msg = "Please enter the qualification.";//getResources().getString(R.string.msg_enter_proper_date);
        }*/

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}
