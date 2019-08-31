package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.platform.widgets.MultiSelectBottomSheet;
import com.platform.widgets.SingleSelectBottomSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

public class UserRegistrationMatrimonyFragmentOne extends Fragment implements View.OnClickListener, SingleSelectBottomSheet.MultiSpinnerListener {
    private View fragmentview;
    private Button btn_load_next;
    private TextView tv_pagetitle;
    private EditText et_first_name, et_middle_name, et_last_name, et_birth_date, et_birth_time, et_birth_place,
            et_age, et_marital_status, et_height, et_weight, et_complexion, et_patrika_match, et_sampraday, et_drink, et_smoke, et_residance_status;
    private CheckBox checkbox_community_preference;
    private String userGender = Constants.Login.MALE;

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
        et_marital_status = fragmentview.findViewById(R.id.et_marital_status);
        et_height = fragmentview.findViewById(R.id.et_height);
        et_weight = fragmentview.findViewById(R.id.et_weight);
        et_complexion = fragmentview.findViewById(R.id.et_complexion);
        et_patrika_match = fragmentview.findViewById(R.id.et_patrika_match);
        et_sampraday = fragmentview.findViewById(R.id.et_sampraday);
        et_drink = fragmentview.findViewById(R.id.et_drink);
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

        //set Listeners
        et_birth_date.setOnClickListener(this);
        et_first_name.setOnClickListener(this);
        et_middle_name.setOnClickListener(this);
        et_last_name.setOnClickListener(this);
        et_birth_date.setOnClickListener(this);
        et_birth_time.setOnClickListener(this);
        et_birth_place.setOnClickListener(this);
        et_age.setOnClickListener(this);
        et_marital_status.setOnClickListener(this);
        et_height.setOnClickListener(this);
        et_weight.setOnClickListener(this);
        et_complexion.setOnClickListener(this);
        et_patrika_match.setOnClickListener(this);
        et_sampraday.setOnClickListener(this);
        et_drink.setOnClickListener(this);
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
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(2);
                break;
            case R.id.et_birth_date:
                selectStartDate(et_birth_date);
                break;
            case R.id.et_drink:

                showMultiSelectBottomsheet("et_drink",ListDrink);
                break;
            case R.id.et_sampraday:
                for (int i = 0; i <((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if(((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("sect")){
                        showMultiSelectBottomsheet("et_sampraday", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;

            case R.id.et_marital_status:
                for (int i = 0; i <((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if(((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("marital_status")){
                        showMultiSelectBottomsheet("et_marital_status", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_height:
                for (int i = 0; i <((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if(((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("height")){
                        showMultiSelectBottomsheet("et_height", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_complexion:
                for (int i = 0; i <((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if(((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("complexion")){
                        showMultiSelectBottomsheet("et_complexion", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;
            case R.id.et_residance_status:
                for (int i = 0; i <((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.size(); i++) {
                    if(((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getKey().equalsIgnoreCase("own_house")){
                        showMultiSelectBottomsheet("et_residance_status", (ArrayList<String>) ((UserRegistrationMatrimonyActivity) getActivity()).MasterDataArrayList.get(i).getValues());
                        break;
                    }
                }
                break;

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



    public void createTempArrayList(){
        ListBloodGroup.add("O+ve");
        ListBloodGroup.add("O-ve");

        ListDrink.add("NO,I don't drink.");
        ListDrink.add("YES,I drink.");
        ListDrink.add("SOMETIMES,I drink Sometimes");

        ListSmoke.add("NO,I don't drink.");
        ListSmoke.add("YES,I drink.");
        ListSmoke.add("SOMETIMES,I drink Sometimes");

        ListResidenceStatus.add("Owned");
        ListResidenceStatus.add("Rented");

        ListmatchPatrika.add("Yes,I want to match patrika");
        ListmatchPatrika.add("No,I don't want to match patrika");

        ListSkintone.add("Very fair");
        ListSkintone.add("Fair");
        ListSkintone.add("Dark");

        ListSampraday.add("Sampraday one");
        ListSampraday.add("Sampraday Two");

        ListMaritalStatus.add("Unmarried");
        ListMaritalStatus.add("Married");

        ListWeight.add("50");
        ListWeight.add("60");
        ListWeight.add("70");

        ListHeight.add("5 feet");
        ListHeight.add("6 feet");
        ListHeight.add("7 feet");

    }


    private void showMultiSelectBottomsheet(String selectedOption,ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(getActivity(),selectedOption,List,this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onValuesSelected(int selected, String spinnerName, String selectedValues) {
        switch (spinnerName){
            case "et_sampraday":
                et_sampraday.setText(selectedValues);
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



        }
        //et_drink.setText(ListDrink.get(selected));
    }
}

