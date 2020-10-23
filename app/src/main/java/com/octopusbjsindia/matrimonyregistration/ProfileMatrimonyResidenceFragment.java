package com.octopusbjsindia.matrimonyregistration;

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

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;

import java.util.ArrayList;

public class ProfileMatrimonyResidenceFragment extends Fragment implements View.OnClickListener, SingleSelectBottomSheet.MultiSpinnerListener {
    private View fragmentview;
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private EditText et_address, et_city_town, et_state, et_country, et_primary_mobile, et_primary_mobile_two, et_primary_email;
    private EditText et_education, et_qualification_degree, et_occupation_type, et_employer, et_job_profile, et_Annual_income;


    public static ProfileMatrimonyResidenceFragment newInstance() {
        return new ProfileMatrimonyResidenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_residence_new, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //Button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);

        et_education = fragmentview.findViewById(R.id.et_education);
        et_qualification_degree = fragmentview.findViewById(R.id.et_qualification_degree);
        et_occupation_type = fragmentview.findViewById(R.id.et_occupation_type);
        et_employer = fragmentview.findViewById(R.id.et_employer);
        et_job_profile = fragmentview.findViewById(R.id.et_job_profile);
        et_Annual_income = fragmentview.findViewById(R.id.et_Annual_income);
        et_address = fragmentview.findViewById(R.id.et_address);
        et_city_town = fragmentview.findViewById(R.id.et_city_town);
        et_state = fragmentview.findViewById(R.id.et_state);
        et_country = fragmentview.findViewById(R.id.et_country);
        et_primary_mobile = fragmentview.findViewById(R.id.et_primary_mobile);
        //et_primary_mobile.setText(Util.getLoginObjectFromPref().getUserDetails().getPhone());
        et_primary_mobile_two = fragmentview.findViewById(R.id.et_primary_mobile_two);
        et_primary_email = fragmentview.findViewById(R.id.et_primary_email);


        //set Listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);
        et_education.setOnClickListener(this);
        et_qualification_degree.setOnClickListener(this);
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

        if (((RegistrationActivity) getActivity()).matrimonialProfile != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails() != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getEducationalDetails() != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getOccupationalDetails() != null) {
            setData();
        }
        fragmentview.findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegistrationActivity) getActivity()).onBackPressed();
            }
        });
    }

    private void setData() {

        et_address.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getAddress());
        et_city_town.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getCity());
        et_state.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getState());
        et_country.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getCountry());
        et_primary_mobile.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getPrimaryPhone());
        et_primary_mobile_two.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getSecondaryPhone());
        et_primary_email.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getResidentialDetails().getPrimaryEmailAddress());

        et_education.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getEducationalDetails().getEducationLevel());
        et_qualification_degree.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getEducationalDetails().getQualificationDegree());
        et_Annual_income.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getEducationalDetails().getIncome());
        et_Annual_income.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getEducationalDetails().getIncome());

//        ((RegistrationActivity)getActivity()).educationalDetails.setQualificationDegree("");
        et_occupation_type.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOccupationalDetails().getOccupation());
        et_employer.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOccupationalDetails().getEmployerCompany());
        et_job_profile.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOccupationalDetails().getBusinessDescription());

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
                ((RegistrationActivity) getActivity()).loadNextScreen(2);
                break;
            case R.id.et_education:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("education_level")) {
                        showMultiSelectBottomsheet("Education", "et_education", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_qualification_degree:
                if (!TextUtils.isEmpty(et_education.getText())) {
                    for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                        if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase(et_education.getText().toString())) {
                            showMultiSelectBottomsheet("Qualification degree", "et_qualification_degree", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                            break;
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Select Education level first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.et_occupation_type:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("occupation")) {
                        showMultiSelectBottomsheet("Occupation", "et_occupation_type", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_Annual_income:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("income")) {
                        showMultiSelectBottomsheet("Annual Income", "et_Annual_income", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_state:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("state")) {
                        showMultiSelectBottomsheet("State", "et_state", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_country:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("country")) {
                        showMultiSelectBottomsheet("Country", "et_country", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;


        }
    }

    private void setValuesInModel() {
        if (isAllInputsValid()) {
            if (((RegistrationActivity) getActivity()).matrimonialProfile != null) {
                if (((RegistrationActivity) getActivity()).residentialDetails != null) {

                    ((RegistrationActivity) getActivity()).residentialDetails.setAddress(et_address.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setCity(et_city_town.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setState(et_state.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setCountry(et_country.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setPrimaryPhone(et_primary_mobile.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setSecondaryPhone(et_primary_mobile_two.getText().toString());
                    ((RegistrationActivity) getActivity()).residentialDetails.setPrimaryEmailAddress(et_primary_email.getText().toString());

                    ((RegistrationActivity) getActivity()).educationalDetails.setEducationLevel(et_education.getText().toString());
                    ((RegistrationActivity) getActivity()).educationalDetails.setQualificationDegree(et_qualification_degree.getText().toString());
                    ((RegistrationActivity) getActivity()).educationalDetails.setIncome(et_Annual_income.getText().toString());

                    ((RegistrationActivity) getActivity()).occupationalDetails.setOccupation(et_occupation_type.getText().toString());
                    ((RegistrationActivity) getActivity()).occupationalDetails.setEmployerCompany(et_employer.getText().toString());
                    ((RegistrationActivity) getActivity()).occupationalDetails.setBusinessDescription(et_job_profile.getText().toString());

                    ((RegistrationActivity) getActivity())
                            .matrimonialProfile.setResidentialDetails(((RegistrationActivity) getActivity()).residentialDetails);
                    ((RegistrationActivity) getActivity())
                            .matrimonialProfile.setEducationalDetails(((RegistrationActivity) getActivity()).educationalDetails);
                    ((RegistrationActivity) getActivity())
                            .matrimonialProfile.setOccupationalDetails(((RegistrationActivity) getActivity()).occupationalDetails);


                } else {
                    Util.showToast(getActivity(), "null object getPersonal_details()");
                }
            } else {
                Util.showToast(getActivity(), "null object");
            }
            ((RegistrationActivity) getActivity()).aboutUsStr = "My name is " + ((RegistrationActivity) getActivity()).personalDetails.getFirstName() + " and I am in the profession of " + ((RegistrationActivity) getActivity()).occupationalDetails.getOccupation() + " in the " + ((RegistrationActivity) getActivity()).occupationalDetails.getEmployerCompany() + ". I have completed my " + ((RegistrationActivity) getActivity()).educationalDetails.getQualificationDegree() + "." + " I grew up in a " + ((RegistrationActivity) getActivity()).familyDetails.getFamilyType() + " family.";
            ((RegistrationActivity) getActivity()).loadNextScreen(4);
        }

    }

    private void showMultiSelectBottomsheet(String Title, String selectedOption, ArrayList<String> List) {
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
                et_qualification_degree.setText("");
                break;
            case "et_qualification_degree":
                et_qualification_degree.setText(selectedValues);
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
        }
        //et_drink.setText(ListDrink.get(selected));
    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_education.getText().toString().trim().length() == 0) {
            msg = "Please enter education";//getResources().getString(R.string.msg_enter_name);
        } else
        if (et_qualification_degree.getText().toString().trim().length() == 0) {
            msg = "Please enter qualification degree";//getResources().getString(R.string.msg_enter_name);
        } else if (et_occupation_type.getText().toString().trim().length() == 0) {
            msg = "Please select the occupation type.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_employer.getText().toString().trim().length() == 0) {
            msg = "Please enter employer.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_Annual_income.getText().toString().trim().length() == 0) {
            msg = "Please enter your annual income.";//getResources().getString(R.string.msg_enter_proper_date);
        } /*else if (et_address.getText().toString().trim().length() == 0) {
            msg = "Please enter your address.";//getResources().getString(R.string.msg_enter_proper_date);
        } */ else if (et_country.getText().toString().trim().length() == 0) {
            msg = "Please mention the Country"; //getResources().getString(R.string.msg_enter_name);
        } else if (et_state.getText().toString().trim().length() == 0) {
            msg = "Please enter your state.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_city_town.getText().toString().trim().length() == 0) {
            msg = "Please enter your city.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_primary_mobile_two.getText().toString().trim().length() == 0) {
            msg = "Please enter communication mobile number."; //getResources().getString(R.string.msg_enter_name);
        } else if (et_primary_mobile_two.getText().toString().trim().length() < 10) {
            msg = "Please enter valid mobile number for communication."; //getResources().getString(R.string.msg_enter_name);
        } else if (TextUtils.isEmpty(et_primary_email.getText().toString().trim())) {
            msg = "Please enter email address."; //getResources().getString(R.string.msg_enter_name);
        } else if (!TextUtils.isEmpty(et_primary_email.getText().toString().trim())
                && !Patterns.EMAIL_ADDRESS.matcher(et_primary_email.getText().toString().trim()).matches()) {
            msg = "Please enter valid email.";
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}
