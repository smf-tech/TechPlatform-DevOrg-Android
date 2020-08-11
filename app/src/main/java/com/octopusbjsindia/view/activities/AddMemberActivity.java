package com.octopusbjsindia.view.activities;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.user.RoleData;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.AddMamberActivityPresenter;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener {

    private EditText etUserFirstName, etUserMiddleName, etUserLastName, etUserBirthDate,
            etUserMobileNumber, etUserEmailId;
    private RadioGroup radioGroup;
    private String userGender = Constants.Login.MALE;

    AddMamberActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        presenter = new AddMamberActivityPresenter(this);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Create Subordinate");
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);

        etUserFirstName = findViewById(R.id.et_user_first_name);
        etUserMiddleName = findViewById(R.id.et_user_middle_name);
        etUserLastName = findViewById(R.id.et_user_last_name);
        etUserBirthDate = findViewById(R.id.et_user_birth_date);
        etUserMobileNumber = findViewById(R.id.et_user_mobile_number);
        etUserEmailId = findViewById(R.id.et_user_email_id);
        radioGroup = findViewById(R.id.user_gender_group);
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

        findViewById(R.id.user_profile_pic).setOnClickListener(this);
        findViewById(R.id.btn_profile_submit).setOnClickListener(this);
        findViewById(R.id.et_user_birth_date).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.btn_profile_submit:
                submitProfileDetails();
                break;
            case R.id.et_user_birth_date:
                Util.showDateDialog(AddMemberActivity.this, findViewById(R.id.et_user_birth_date));
                break;

        }
    }

    private void submitProfileDetails() {

        if (isAllInputsValid()) {
            UserInfo userInfo = Util.getUserObjectFromPref();
            userInfo.setUserFirstName(String.valueOf(etUserFirstName.getText()).trim());
            userInfo.setUserMiddleName(String.valueOf(etUserMiddleName.getText()).trim());
            userInfo.setUserLastName(String.valueOf(etUserLastName.getText()).trim());
            userInfo.setUserBirthDate(Util.getDateInLong(String.valueOf(etUserBirthDate.getText()).trim()));
            userInfo.setUserMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            userInfo.setUserEmailId(String.valueOf(etUserEmailId.getText()).trim());
            userInfo.setUserGender(userGender);
            RoleData role = new RoleData();
            role.setId("12233");
            userInfo.setRoleIds(role);
            presenter.submitProfile(userInfo);
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
        } else if (etUserBirthDate.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_birth_date);
        } else if (etUserEmailId.getText().toString().trim().length() != 0 &&
                !Patterns.EMAIL_ADDRESS.matcher(etUserEmailId.getText().toString().trim()).matches()) {
            msg = getResources().getString(R.string.msg_enter_valid_email_id);
        } else if (TextUtils.isEmpty(userGender)) {
            msg = getString(R.string.msg_select_gender);
        }
        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message,this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(),this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
        if(responseOBJ.getStatus() == 200){
            Util.showToast(responseOBJ.getMessage(),this);
            finish();
        } else {
            Util.showToast(responseOBJ.getMessage(),this);
        }
    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }
}
