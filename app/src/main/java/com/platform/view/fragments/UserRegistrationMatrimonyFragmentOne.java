package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.platform.widgets.SingleSelectBottomSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

public class UserRegistrationMatrimonyFragmentOne extends Fragment implements View.OnClickListener, SingleSelectBottomSheet.MultiSpinnerListener {
    //temporary arrays
    ArrayList<String> ListBloodGroup = new ArrayList<>();
    ArrayList<String> ListMaritalStatus = new ArrayList<>();
    ArrayList<String> ListHeight = new ArrayList<>();
    ArrayList<String> ListWeight = new ArrayList<>();
    ArrayList<String> ListSkintone = new ArrayList<>();
    ArrayList<String> ListmatchPatrika = new ArrayList<>();
    ArrayList<String> ListSampraday = new ArrayList<>();
    ArrayList<String> ListDrink = new ArrayList<>();
    ArrayList<String> ListSmoke = new ArrayList<>();
    ArrayList<String> ListResidenceStatus = new ArrayList<>();
    private View fragmentview;
    private Button btn_load_next;
    private TextView tv_pagetitle;
    private EditText et_first_name, et_middle_name, et_last_name, et_birth_date, et_birth_time, et_birth_place, et_blood_group,et_special_case,
            et_age, et_marital_status, et_height, et_weight, et_complexion, et_patrika_match, et_sampraday, et_drink, et_smoke, et_residance_status;
    private CheckBox checkbox_community_preference;
    private String userGender = Constants.Login.MALE;
    private String userManglik ="dont know";
    private SingleSelectBottomSheet bottomSheetDialogFragment;

    public static UserRegistrationMatrimonyFragmentOne newInstance() {
        return new UserRegistrationMatrimonyFragmentOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_one, container, false);
        initViews();
        createTempArrayList();

        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_load_next.setOnClickListener(this);

        //Edit text
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

        //Radio group
        RadioGroup radioGroup = fragmentview.findViewById(R.id.user_gender_group);
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.gender_male:
                    userGender = Constants.Login.MALE;
                    break;
                case R.id.gender_female:
                    userGender = Constants.Login.FEMALE;
                    break;
                case R.id.gender_other:
                    userGender = Constants.Login.OTHER;
                    break;
            }
        });
        //MANGLIK
        RadioGroup radioGroupManglik = fragmentview.findViewById(R.id.user_manglik_group);
        radioGroupManglik.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.manglik:
                    userManglik = "yes";
                    break;
                case R.id.nonmanglik:
                    userGender = "no";
                    break;
                case R.id.dontknowmanglik:
                    userGender = "Dont know";
                    break;
            }
        });

        //set Listeners
        et_birth_date.setOnClickListener(this);
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

        // ListDrink = (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(0).getValues();
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
            case R.id.et_birth_date:
                selectStartDate(et_birth_date);
                break;
            case R.id.et_birth_time:
                Util.showTimeDialog(getActivity(), fragmentview.findViewById(R.id.et_birth_time));
                break;

            case R.id.et_drink:
                showMultiSelectBottomsheet("et_drink", ListDrink);
                break;
            case R.id.et_special_case:

                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("special_case")) {
                        showMultiSelectBottomsheet("et_special_case", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_blood_group:
                showMultiSelectBottomsheet("et_blood_group", ListBloodGroup);
                break;
            case R.id.et_weight:
                //showMultiSelectBottomsheet("et_weight", ListWeight);
                break;
            case R.id.et_patrika_match:
                showMultiSelectBottomsheet("et_patrika_match", ListmatchPatrika);
                break;
            case R.id.et_smoke:
                showMultiSelectBottomsheet("et_smoke", ListSmoke);
                break;


            case R.id.et_sampraday:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("sect")) {
                        showMultiSelectBottomsheet("et_sampraday", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;

            case R.id.et_marital_status:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("marital_status")) {
                        showMultiSelectBottomsheet("et_marital_status", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_height:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("height")) {
                        showMultiSelectBottomsheet("et_height", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_complexion:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("complexion")) {
                        showMultiSelectBottomsheet("et_complexion", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_residance_status:
                for (int i = 0; i < ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if (((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("own_house")) {
                        showMultiSelectBottomsheet("et_residance_status", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
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
                if (UserRegistrationMatrimonyActivity.personalDetails != null) {

                    UserRegistrationMatrimonyActivity.personalDetails.setFirst_name(et_first_name.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setMiddle_name(et_middle_name.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setLast_name(et_last_name.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setGender(userGender);
                    UserRegistrationMatrimonyActivity.personalDetails.setIs_manglik(userManglik);
                    UserRegistrationMatrimonyActivity.personalDetails.setBirth_date(Util.getDateInepoch(et_birth_date.getText().toString()));
                    UserRegistrationMatrimonyActivity.personalDetails.setBirth_time("11.15 PM");
                    UserRegistrationMatrimonyActivity.personalDetails.setBirth_city(et_birth_place.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setBlood_group(et_blood_group.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setMarital_status(et_marital_status.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setHeight(et_height.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setWeight(et_weight.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setComplexion(et_complexion.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setMatch_patrika(Boolean.parseBoolean("YES"));
                    UserRegistrationMatrimonyActivity.personalDetails.setSect(et_sampraday.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setSmoke(et_smoke.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setDrink(et_drink.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setOwn_house(et_residance_status.getText().toString());
                    UserRegistrationMatrimonyActivity.personalDetails.setSpecial_case(et_special_case.getText().toString());


                    UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setPersonal_details(UserRegistrationMatrimonyActivity.personalDetails);

                } else {
                    Util.showToast("null object getPersonal_details()", getActivity());
                }
            } else {
                Util.showToast("null object", getActivity());
            }
            ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(2);
        }
    }


    //select start date and end date for filter
    private void selectStartDate(EditText et_birth_date_view) {
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

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void createTempArrayList() {
        ListBloodGroup.add("A+");
        ListBloodGroup.add("AB+");
        ListBloodGroup.add("AB-");
        ListBloodGroup.add("A-");
        ListBloodGroup.add("B+");
        ListBloodGroup.add("B-");
        ListBloodGroup.add("O+");
        ListBloodGroup.add("O-");

        ListDrink.add("no");
        ListDrink.add("yes");
        ListDrink.add("occasionally");

        ListSmoke.add("no");
        ListSmoke.add("yes");
        ListSmoke.add("occasionally");

        ListmatchPatrika.add("Yes");
        ListmatchPatrika.add("No");


        ListWeight.add("30");
        ListWeight.add("40");
        ListWeight.add("50");
        ListWeight.add("60");
        ListWeight.add("70");
        ListWeight.add("80");
        ListWeight.add("90");
        ListWeight.add("100");
        ListWeight.add("110");
        ListWeight.add("120");
        ListWeight.add("130");
        ListWeight.add("140");
        ListWeight.add("150");

    }


    private void showMultiSelectBottomsheet(String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(getActivity(), selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selected, String spinnerName, String selectedValues) {
        switch (spinnerName) {
            case "et_sampraday":
                et_sampraday.setText(selectedValues);
                //  UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.getEducational_details().setEducation_level("");
                break;
            case "et_marital_status":
                et_marital_status.setText(selectedValues);
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


        }
        //et_drink.setText(ListDrink.get(selected));
    }


    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (et_first_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your first name.";//getResources().getString(R.string.msg_enter_name);
        } else if (et_last_name.getText().toString().trim().length() == 0) {
            msg = "Please enter your last name.";//getResources().getString(R.string.msg_enter_name);
        } else if (et_birth_date.getText().toString().trim().length() == 0) {
            msg = "Please enter your birth date.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        else if (et_birth_place.getText().toString().trim().length() == 0) {
            msg = "Please enter your birth place.";//getResources().getString(R.string.msg_enter_proper_date);
        }else if (et_height.getText().toString().trim().length() == 0) {
            msg = "Please enter your height.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        else if (et_sampraday.getText().toString().trim().length() == 0) {
            msg = "Please enter your sampradaay.";//getResources().getString(R.string.msg_enter_proper_date);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}

