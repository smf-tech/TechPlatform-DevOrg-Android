package com.octopusbjsindia.matrimonyregistration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;
import com.sagar.selectiverecycleviewinbottonsheetdialog.model.SelectionListObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;
import static com.octopusbjsindia.utility.ExtensionKt.getFormattedHeightValue;


public class ProfileMatrimonyFragmentOne extends Fragment implements View.OnClickListener,
        SingleSelectBottomSheet.MultiSpinnerListener, CustomSpinnerListener {

    ArrayList<String> ListBloodGroup = new ArrayList<>();
    ArrayList<String> ListWeight = new ArrayList<>();
    ArrayList<String> ListmatchPatrika = new ArrayList<>();
    ArrayList<String> ListProfileFor = new ArrayList<>();
    ArrayList<String> ListDrink = new ArrayList<>();
    ArrayList<String> ListSmoke = new ArrayList<>();
    //ArrayList<String> listHeight = new ArrayList<>();
    ArrayList<CustomSpinnerObject> ListHeight = new ArrayList<>();

    //ArrayList<String> ListHeightTemp = new ArrayList<>();
    ArrayList<String> listChildrenCount = new ArrayList<>();
    private View fragmentview;
    private Button btn_load_next;
    private RadioGroup rgGander;
    private EditText et_profile_for, et_first_name, et_middle_name, et_last_name, et_birth_date, et_birth_time, et_birth_place,
            et_blood_group, et_special_case, et_age, et_marital_status, et_height, et_weight, et_complexion,
            et_patrika_match, et_sampraday, et_drink, et_smoke, et_residance_status, et_sub_case, etChildrenCount;
    private String userGender = "";//= Constants.Matrimony.MALE;
    private String userManglik = "";
    private String userDivorce = "Yes";
    private String haveChildren = "NotApplicable";
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private LinearLayout lyDivorce, lyChildren;
    private boolean alert = true;
    private TextInputLayout tilChildrenCount;

    private String selectedHeight, selectedHeightDisplay;

    public static ProfileMatrimonyFragmentOne newInstance() {
        return new ProfileMatrimonyFragmentOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_one_new, container, false);
        initViews();
        createTempArrayList();

        return fragmentview;
    }

    private void initViews() {
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_load_next.setOnClickListener(this);
        et_profile_for = fragmentview.findViewById(R.id.et_profile_for);
        et_first_name = fragmentview.findViewById(R.id.et_first_name);
        et_first_name = fragmentview.findViewById(R.id.et_first_name);
        et_middle_name = fragmentview.findViewById(R.id.et_middle_name);
        et_last_name = fragmentview.findViewById(R.id.et_last_name);
        et_birth_date = fragmentview.findViewById(R.id.et_birth_date);
        et_birth_time = fragmentview.findViewById(R.id.et_birth_time);
        et_birth_place = fragmentview.findViewById(R.id.et_birth_place);
        et_age = fragmentview.findViewById(R.id.et_age);
        et_blood_group = fragmentview.findViewById(R.id.et_blood_group);
        et_marital_status = fragmentview.findViewById(R.id.et_marital_status);
        et_height = fragmentview.findViewById(R.id.et_height);
        et_weight = fragmentview.findViewById(R.id.et_weight);
        et_complexion = fragmentview.findViewById(R.id.et_complexion);
        et_patrika_match = fragmentview.findViewById(R.id.et_patrika_match);
        et_sampraday = fragmentview.findViewById(R.id.et_sampraday);
        et_drink = fragmentview.findViewById(R.id.et_drink);
        et_special_case = fragmentview.findViewById(R.id.et_special_case);
        et_smoke = fragmentview.findViewById(R.id.et_smoke);
        et_residance_status = fragmentview.findViewById(R.id.et_residance_status);
        et_sub_case = fragmentview.findViewById(R.id.et_sub_case);
        lyDivorce = fragmentview.findViewById(R.id.ly_divorce);
        lyChildren = fragmentview.findViewById(R.id.ly_children);
        tilChildrenCount = fragmentview.findViewById(R.id.til_children_count);
        etChildrenCount = fragmentview.findViewById(R.id.et_children_count);

        //Radio group
        rgGander = fragmentview.findViewById(R.id.user_gender_group);
        rgGander.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {

                case R.id.gender_male:
                    userGender = Constants.MatrimonyModule.MALE;
                    break;
                case R.id.gender_female:
                    userGender = Constants.MatrimonyModule.FEMALE;
                    break;
//                case R.id.gender_other:
//                    userGender = Constants.Matrimony.OTHER;
//                    break;

//                break;
//            }
            }
        });
        //MANGLIK
        RadioGroup radioGroupManglik = fragmentview.findViewById(R.id.user_manglik_group);
        radioGroupManglik.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.manglik:
                    userManglik = "Yes";
                    break;
                case R.id.nonmanglik:
                    userManglik = "No";
                    break;
                case R.id.dontknowmanglik:
                    userManglik = "Dont know";
                    break;
            }
        });

        RadioGroup radioGroupDivorce = fragmentview.findViewById(R.id.user_divorce_legal);
        radioGroupDivorce.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.divorce_yes:
                    userDivorce = "Yes";
                    break;
                case R.id.divorce_no:
                    userDivorce = "No";
                    break;
            }
        });

        RadioGroup rgChildren = fragmentview.findViewById(R.id.rg_children);
        rgChildren.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_children_yes:
                    haveChildren = "Yes";
                    tilChildrenCount.setVisibility(View.VISIBLE);
                    break;
                case R.id.rb_children_no:
                    haveChildren = "No";
                    tilChildrenCount.setVisibility(View.GONE);
                    break;
            }
        });

        //set Listeners
        et_profile_for.setOnClickListener(this);
        et_first_name.setOnClickListener(this);
        et_middle_name.setOnClickListener(this);
        et_last_name.setOnClickListener(this);
        et_birth_date.setOnClickListener(this);
        et_birth_time.setOnClickListener(this);
        et_birth_place.setOnClickListener(this);
        et_age.setOnClickListener(this);
        et_blood_group.setOnClickListener(this);
        et_marital_status.setOnClickListener(this);
        et_height.setOnClickListener(this);
        et_weight.setOnClickListener(this);
        et_complexion.setOnClickListener(this);
        et_patrika_match.setOnClickListener(this);
        et_sampraday.setOnClickListener(this);
        et_drink.setOnClickListener(this);
        et_special_case.setOnClickListener(this);
        et_smoke.setOnClickListener(this);
        et_residance_status.setOnClickListener(this);
        etChildrenCount.setOnClickListener(this);

        if (((RegistrationActivity) getActivity()).matrimonialProfile != null &&
                ((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails() != null) {
            setData();
        } else {
            showDialog(getActivity(), "Alert",
                    " Registration process requires your Aadhar and Educational degree to be uploaded." +
                            " Please keep them handy.",
                    "Ok", "");
        }
        fragmentview.findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegistrationActivity) getActivity()).onBackPressed();
            }
        });
    }

    public void setData() {
        alert = false;
        et_profile_for.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getProfileFor());
        et_first_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getFirstName());
        et_middle_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getMiddleName());
        et_last_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getLastName());
        et_last_name.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getLastName());
        et_birth_date.setText(Util.getDateFromTimestamp(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getBirthDate(),
                "dd MMM yyyy"));
        //et_birth_date.setEnabled(false);
        et_birth_time.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getBirthTime());
        //et_birth_time.setEnabled(false);
        et_birth_place.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getBirthCity());
        //if ()
        et_age.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getAge().toString());
        et_blood_group.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getBloodGroup());
        //et_marital_status.setEnabled(false);
        et_marital_status.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getMaritalStatus());

        et_height.setText(Util.getFormattedHeightValue(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getHeight()));

        et_weight.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getWeight());
        et_complexion.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getComplexion());


        et_sampraday.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getSect());
        et_smoke.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getSmoke());
        et_drink.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getDrink());
        et_residance_status.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getOwnHouse());
        et_special_case.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getSpecialCase());
        et_sub_case.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getSubCast());
        if (!((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                getMaritalStatus().equalsIgnoreCase("Unmarried") && ((RegistrationActivity)
                getActivity()).matrimonialProfile.getPersonalDetails().getChildrenCount() != null) {
            etChildrenCount.setText(((RegistrationActivity) getActivity()).matrimonialProfile.
                    getPersonalDetails().getChildrenCount());
        }

        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails()
                .getMaritalStatus().equalsIgnoreCase("Divorcee")) {
            lyDivorce.setVisibility(View.VISIBLE);
        } else {
            lyDivorce.setVisibility(View.GONE);
        }

        if (!((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails()
                .getMaritalStatus().equalsIgnoreCase("Unmarried")) {
            lyChildren.setVisibility(View.VISIBLE);
        }

        //-------
        RadioButton gender_male, gender_female, gender_other, manglik, nonmanglik, dontknowmanglik;
        ((RegistrationActivity) getActivity()).personalDetails.setGender(userGender);

        gender_male = fragmentview.findViewById(R.id.gender_male);
        //gender_male.setEnabled(false);
        gender_female = fragmentview.findViewById(R.id.gender_female);
        //gender_female.setEnabled(false);

        switch (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getGender()) {
            case Constants.MatrimonyModule.MALE:
                userGender = Constants.MatrimonyModule.MALE;
                gender_male.setChecked(true);
                break;
            case Constants.MatrimonyModule.FEMALE:
                userGender = Constants.MatrimonyModule.FEMALE;
                gender_female.setChecked(true);
                break;
//            case Constants.Matrimony.OTHER:
//                userGender = Constants.Matrimony.OTHER;
//                gender_other = fragmentview.findViewById(R.id.gender_other);
//                gender_other.setChecked(true);
//                break;
        }

        switch (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getIsManglik()) {
            case "Yes":
                userManglik = "Yes";
                manglik = fragmentview.findViewById(R.id.manglik);
                manglik.setChecked(true);
                break;
            case "No":
                userManglik = "No";
                nonmanglik = fragmentview.findViewById(R.id.nonmanglik);
                nonmanglik.setChecked(true);
                break;
            case "Dont know":
                userManglik = "Dont know";
                dontknowmanglik = fragmentview.findViewById(R.id.dontknowmanglik);
                dontknowmanglik.setChecked(true);
                break;
        }

        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getIsDivorcedLegal() != null)
            switch (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getIsDivorcedLegal()) {
                case "Yes":
                    userDivorce = "Yes";
                    manglik = fragmentview.findViewById(R.id.divorce_yes);
                    manglik.setChecked(true);
                    break;
                case "No":
                    userDivorce = "No";
                    nonmanglik = fragmentview.findViewById(R.id.divorce_no);
                    nonmanglik.setChecked(true);
                    break;
            }

        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getHaveChildren() != null)
            switch (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().getHaveChildren()) {
                case "Yes":
                    haveChildren = "Yes";
                    RadioButton rbChildrenYes = fragmentview.findViewById(R.id.rb_children_yes);
                    rbChildrenYes.setChecked(true);
                    break;
                case "No":
                    haveChildren = "No";
                    RadioButton rbChildrenNo = fragmentview.findViewById(R.id.rb_children_no);
                    rbChildrenNo.setChecked(true);
                    break;
            }

        et_patrika_match.setText(((RegistrationActivity) getActivity())
                .matrimonialProfile.getPersonalDetails().getMatchPatrika() ? "YES" : "NO");

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
                if (alert) {
                    showDialog(getActivity(), "Alert",
                            " Once your profile is created, you will not be able to modify the" +
                                    " gender field and DOB, so please enter it very carefully.",
                            "Ok", "");
                } else {
                    setValuesInModel();
                }
                break;
            case R.id.et_birth_date:
                selectStartDate(et_birth_date, et_age);
                break;
            case R.id.et_birth_time:
                Util.showTimeDialog(getActivity(), fragmentview.findViewById(R.id.et_birth_time));
                break;
            case R.id.et_profile_for:
                showMultiSelectBottomsheet("Profile for", "et_profile_for", ListProfileFor);
                break;
            case R.id.et_drink:
                showMultiSelectBottomsheet("Drink", "et_drink", ListDrink);
                break;
            case R.id.et_special_case:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("special_case")) {
                        showMultiSelectBottomsheet("Special Case", "et_special_case", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_blood_group:
                showMultiSelectBottomsheet("Blood Group", "et_blood_group", ListBloodGroup);
                break;
            case R.id.et_weight:
//showMultiSelectBottomsheet("et_weight", ListWeight);
                break;
            case R.id.et_patrika_match:
                showMultiSelectBottomsheet("Patrika Match", "et_patrika_match", ListmatchPatrika);
                break;
            case R.id.et_smoke:
                showMultiSelectBottomsheet("Smoke", "et_smoke", ListSmoke);
                break;
            case R.id.et_sampraday:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("sect")) {
                        showMultiSelectBottomsheet("Sampraday", "et_sampraday", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_marital_status:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("marital_status")) {
                        showMultiSelectBottomsheet("Marital Status", "et_marital_status", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_height:
                if (!ListHeight.isEmpty()) {
                    CustomSpinnerDialogClass csdMinHeight = new CustomSpinnerDialogClass(getActivity(), this,
                            "Height (in feet)", ListHeight, false);
                    csdMinHeight.show();
                    csdMinHeight.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }else {
                    for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                        if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("height")) {
                            ListHeight.clear();
                            ArrayList<CustomSpinnerObject> tempHeightList = new ArrayList<>();
                            for (String height : ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues()) {
                                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                                customSpinnerObject.set_id(height);
                                customSpinnerObject.setName(Util.getFormattedHeightValue(height));
                                tempHeightList.add(customSpinnerObject);
                            }
                            ListHeight.addAll(tempHeightList);

                            CustomSpinnerDialogClass csdMinHeight = new CustomSpinnerDialogClass(getActivity(), this,
                                    "Height (in feet)", ListHeight, false);
                            csdMinHeight.show();
                            csdMinHeight.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT);

                            //listHeight = (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues();
                            // showMultiSelectBottomsheet("Height (in feet)", "et_height", listHeight);
                            break;
                        }
                    }
                }
                break;
            case R.id.et_complexion:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("complexion")) {
                        showMultiSelectBottomsheet("Complexion", "et_complexion", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_residance_status:
                for (int i = 0; i < ((RegistrationActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("own_house")) {
                        showMultiSelectBottomsheet("Residential status", "et_residance_status", (ArrayList<String>) ((RegistrationActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_children_count:
                showMultiSelectBottomsheet("Select children count", "et_children_count", listChildrenCount);
                break;
        }
    }

    private void setValuesInModel() {
        if (isAllInputsValid()) {
            if (((RegistrationActivity) getActivity()).matrimonialProfile != null) {
                if (((RegistrationActivity) getActivity()).personalDetails != null) {
                    ((RegistrationActivity) getActivity()).personalDetails.setProfileFor(et_profile_for.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setFirstName(et_first_name.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setMiddleName(et_middle_name.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setLastName(et_last_name.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setGender(userGender);
                    ((RegistrationActivity) getActivity()).personalDetails.setIsManglik(userManglik);
                    ((RegistrationActivity) getActivity()).personalDetails.setBirthDate(Util.getDateInepoch(et_birth_date.getText().toString()));
                    //   ((RegistrationActivity) getActivity()).personalDetails.setAge(Integer.parseInt(et_age.getText().toString()));
                    ((RegistrationActivity) getActivity()).personalDetails.setBirthTime(et_birth_time.getText().toString().trim());
                    ((RegistrationActivity) getActivity()).personalDetails.setBirthCity(et_birth_place.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setBloodGroup(et_blood_group.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setMaritalStatus(et_marital_status.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setIsDivorcedLegal(userDivorce);
                    if (!et_marital_status.getText().toString().equalsIgnoreCase("Unmarried")) {
                        ((RegistrationActivity) getActivity()).personalDetails.setHaveChildren(haveChildren);
                        if (haveChildren.equalsIgnoreCase("Yes")) {
                            ((RegistrationActivity) getActivity()).personalDetails.setChildrenCount(
                                    etChildrenCount.getText().toString().trim());
                        }
                    }
                    ((RegistrationActivity) getActivity()).personalDetails.setHeight(selectedHeight);
                    ((RegistrationActivity) getActivity()).personalDetails.setWeight(et_weight.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setComplexion(et_complexion.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setMatchPatrika(et_patrika_match.getText().toString().trim().equalsIgnoreCase("yes"));
                    ((RegistrationActivity) getActivity()).personalDetails.setSect(et_sampraday.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setSmoke(et_smoke.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setDrink(et_drink.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setOwnHouse(et_residance_status.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setSpecialCase(et_special_case.getText().toString());
                    ((RegistrationActivity) getActivity()).personalDetails.setSubCast(et_sub_case.getText().toString());

                    ((RegistrationActivity) getActivity()).matrimonialProfile.setPersonalDetails(((RegistrationActivity) getActivity()).personalDetails);

                } else {
                    Util.showToast(getActivity(), "null object getPersonal_details()");
                }
            } else {
                Util.showToast(getActivity(), "null object");
            }
            ((RegistrationActivity) getActivity()).loadNextScreen(2);
        }
    }

    //select start date and end date for filter
    private void selectStartDate(EditText et_birth_date_view, EditText et_age) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                        et_birth_date_view.setText(selectedDateString);
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        et_age.setText(getAge(year, monthOfYear, dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }

   /* private ArrayList<String> getlistHeight(ArrayList<String> values) {
        ListHeightTemp.clear();
        for (int i = 0; i < values.size(); i++) {
            int cm = Integer.parseInt(values.get(i));
            int feet = (int) (cm / 30.48);
            int inches = (int) ((cm / 2.54) - (feet * 12));
//            Util.logger("There are " + feet + " feet and " , inches + " inches in ");
            ListHeightTemp.add(feet + " feet " + inches + " inches");
        }
        return ListHeightTemp;
    }*/

    public void createTempArrayList() {
        ListBloodGroup.add("A+");
        ListBloodGroup.add("AB+");
        ListBloodGroup.add("AB-");
        ListBloodGroup.add("A-");
        ListBloodGroup.add("B+");
        ListBloodGroup.add("B-");
        ListBloodGroup.add("O+");
        ListBloodGroup.add("O-");

        ListDrink.add("No");
        ListDrink.add("Yes");
        ListDrink.add("Occasionally");

        ListSmoke.add("No");
        ListSmoke.add("Yes");
        ListSmoke.add("Occasionally");

        ListmatchPatrika.add("Yes");
        ListmatchPatrika.add("No");

        ListProfileFor.add("Self");
        ListProfileFor.add("Son/Daughter");
        ListProfileFor.add("Siblings");
        ListProfileFor.add("Other");

        listChildrenCount.add("1");
        listChildrenCount.add("2");
        listChildrenCount.add("3");
        listChildrenCount.add("4 or more");

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
            case "et_profile_for":
                et_profile_for.setText(selectedValues);
                break;
            case "et_sampraday":
                et_sampraday.setText(selectedValues);
                break;
            case "et_marital_status":
                et_marital_status.setText(selectedValues);
                if (!selectedValues.equalsIgnoreCase("Unmarried")) {
                    lyChildren.setVisibility(View.VISIBLE);
                } else {
                    lyChildren.setVisibility(View.GONE);
                    ((RegistrationActivity) getActivity()).otherMaritialInformation.setSupport_doc("");
                    haveChildren = "NotApplicable";
                }
                if (selectedValues.equalsIgnoreCase("Divorcee")) {
                    lyDivorce.setVisibility(View.VISIBLE);
                } else {
                    lyDivorce.setVisibility(View.GONE);
                }

                break;
            case "et_height":
                et_height.setText(selectedValues);
                break;
            case "et_complexion":
                et_complexion.setText(selectedValues);
                break;
            case "et_residance_status":
                et_residance_status.setText(selectedValues);
                break;

            case "et_drink":
                et_drink.setText(ListDrink.get(selected));
                break;
            case "et_smoke":
                et_smoke.setText(ListSmoke.get(selected));
                break;

            case "et_blood_group":
                et_blood_group.setText(ListBloodGroup.get(selected));
                break;
            case "et_weight":
                et_weight.setText(ListWeight.get(selected));
                break;
            case "et_patrika_match":
                et_patrika_match.setText(ListmatchPatrika.get(selected));
                break;
            case "et_special_case":
                et_special_case.setText(selectedValues);
                break;
            case "et_children_count":
                etChildrenCount.setText(selectedValues);
                break;
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Height (in feet)":
                selectedHeight = null;
                for (CustomSpinnerObject obj : ListHeight) {
                    if (obj.isSelected()) {
                        selectedHeight = obj.get_id();
                        selectedHeightDisplay = obj.getName();
                        break;
                    }
                }
                et_height.setText(selectedHeightDisplay);
                break;
        }
    }

    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_profile_for.getText().toString().trim().length() == 0) {
            msg = "Please select Profile created for.";
        } else if (et_first_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your first name.";
        } /*else if (et_middle_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your middle name.";
        } */ else if (et_last_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your last name.";
        } else if (userGender.trim().length() == 0) {
            msg = "Please select gender.";
        } else if (et_birth_date.getText().toString().trim().length() == 0) {
            msg = "Please enter your birth date.";
        } else if (et_birth_time.getText().toString().trim().length() == 0) {
            msg = "Please enter your birth time.";
        } else if (et_birth_place.getText().toString().trim().length() == 0) {
            msg = "Please enter your birth place.";
        } else if (et_blood_group.getText().toString().trim().length() == 0) {
            msg = "Please enter your blood group.";
        } else if (et_marital_status.getText().toString().trim().length() == 0) {
            msg = "Please enter your Marital status";
        } else if (et_height.getText().toString().trim().length() == 0) {
            msg = "Please enter your height.";
        } else if (!et_marital_status.getText().toString().equalsIgnoreCase("Unmarried")
                && haveChildren.equalsIgnoreCase("NotApplicable")) {
            msg = "Please select children related question.";
        } else if (haveChildren.equalsIgnoreCase("Yes") &&
                etChildrenCount.getText().toString().trim().length() == 0) {
            msg = "Please select children count.";
        } else if (et_weight.getText().toString().trim().length() == 0) {
            msg = "Please enter your weight";
        } else if (userManglik.length() == 0) {
            msg = "Please select your manglik";
        } else if (et_sampraday.getText().toString().trim().length() == 0) {
            msg = "Please enter your sampraday.";
        } else /*if (et_drink.getText().toString().trim().length() == 0) {
            msg = "Please enter your Drink status";
        } else if (et_smoke.getText().toString().trim().length() == 0) {
            msg = "Please enter your Smock status";
        } else */if (et_special_case.getText().toString().trim().length() == 0) {
            msg = "Please enter your special case";
        }

        switch (userGender) {
            case Constants.MatrimonyModule.MALE:
                if (et_age.getText().toString().trim().length() > 0
                        && Integer.parseInt(et_age.getText().toString()) < 21) {
                    msg = "You do not meet the age criteria.";
                }
                break;
            case Constants.MatrimonyModule.FEMALE:
            case Constants.MatrimonyModule.OTHER:
                if (et_age.getText().toString().trim().length() > 0
                        && Integer.parseInt(et_age.getText().toString()) < 18) {
                    msg = "You do not meet the age criteria.";
                }
                break;
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }
        Util.showToast(getActivity(), msg);
        return false;
    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                setValuesInModel();
                dialog.dismiss();

            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}

