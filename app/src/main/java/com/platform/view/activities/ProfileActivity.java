package com.platform.view.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.UserInfo;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.Calendar;

public class ProfileActivity extends BaseActivity implements PlatformTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText etUserFirstName;
    private EditText etUserMiddleName;
    private EditText etUserLastName;
    private EditText etUserBirthDate;
    private EditText etUserMobileNumber;
    private EditText etUserEmailId;
    private EditText etUserProject;

    private Spinner spOrganization;
    private Spinner spRole;
    private Spinner spState;
    private Spinner spDistrict;
    private Spinner spTaluka;
    private Spinner spCluster;
    private Spinner spVillage;
    private Spinner spStructure;

    private ImageView imgUserProfilePic;
    private Button btnProfileSubmit;

    private String userGender = "Male";

    private ProfileActivityPresenter profilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        profilePresenter = new ProfileActivityPresenter(this);
    }

    private void initViews() {
        setActionbar(getString(R.string.registration_title));

        etUserFirstName = findViewById(R.id.et_user_first_name);
        etUserMiddleName = findViewById(R.id.et_user_middle_name);
        etUserLastName = findViewById(R.id.et_user_last_name);
        etUserBirthDate = findViewById(R.id.et_user_birth_date);
        etUserMobileNumber = findViewById(R.id.et_user_mobile_number);
        etUserEmailId = findViewById(R.id.et_user_email_id);
        etUserProject = findViewById(R.id.et_user_project);

        RadioGroup radioGroup = findViewById(R.id.user_gender_group);
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.gender_male:
                    userGender = "Male";
                    break;

                case R.id.gender_female:
                    userGender = "Female";
                    break;

                case R.id.gender_other:
                    userGender = "Other";
                    break;
            }
        });

        spOrganization = findViewById(R.id.sp_user_organization);
        spRole = findViewById(R.id.sp_user_role);
        spState = findViewById(R.id.sp_user_state);
        spDistrict = findViewById(R.id.sp_user_district);
        spTaluka = findViewById(R.id.sp_user_taluka);
        spCluster = findViewById(R.id.sp_user_cluster);
        spVillage = findViewById(R.id.sp_user_village);
        spStructure = findViewById(R.id.sp_user_structure);

        imgUserProfilePic = findViewById(R.id.user_profile_pic);
        btnProfileSubmit = findViewById(R.id.btn_profile_submit);

        setListeners();

        if (Platform.getInstance().getAppMode().equals(Constants.App.BJS_MODE)) {
            findViewById(R.id.user_geo_location_view).setVisibility(View.GONE);
            findViewById(R.id.input_user_address).setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        etUserBirthDate.setOnClickListener(this);

        spOrganization.setOnItemSelectedListener(this);
        spRole.setOnItemSelectedListener(this);
        spState.setOnItemSelectedListener(this);
        spDistrict.setOnItemSelectedListener(this);
        spTaluka.setOnItemSelectedListener(this);
        spCluster.setOnItemSelectedListener(this);
        spVillage.setOnItemSelectedListener(this);
        spStructure.setOnItemSelectedListener(this);

        imgUserProfilePic.setOnClickListener(this);
        btnProfileSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_user_birth_date:
                showDateDialog(ProfileActivity.this, findViewById(R.id.et_user_birth_date));
                break;

            case R.id.user_profile_pic:
                break;

            case R.id.btn_profile_submit:
                submitProfileDetails();
                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    private void submitProfileDetails() {
        if (isAllInputsValid()) {
            UserInfo userInfo = new UserInfo();
            profilePresenter.submitProfile(userInfo);
        }
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (etUserFirstName.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (etUserMobileNumber.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_mobile_number);
        } else if (etUserMobileNumber.getText().toString().trim().length() != 10) {
            msg = getResources().getString(R.string.msg_enter_valid_mobile_no);
        } else if (etUserEmailId.getText().toString().trim().length() == 0 &&
                !android.util.Patterns.EMAIL_ADDRESS.matcher(
                        etUserEmailId.getText().toString().trim()).matches()) {
            msg = getResources().getString(R.string.msg_enter_valid_email_id);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
