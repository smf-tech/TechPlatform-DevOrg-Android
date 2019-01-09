package com.platform.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.UserInfo;
import com.platform.models.login.LoginInfo;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.profile.State;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends BaseActivity implements ProfileTaskListener,
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
    private ImageView backButton;
    private Button btnProfileSubmit;

    private String userGender = "Male";
    private String selectedProjects = "";
    private ArrayList<String> projectsList = new ArrayList<>();

    private boolean[] projectSelection = null;

    private Uri outputUri;
    private Uri finalUri;
    private ProfileActivityPresenter profilePresenter;

    private List<Organization> orgs = new ArrayList<>();
    private List<State> states = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        profilePresenter = new ProfileActivityPresenter(this);
        profilePresenter.getOrganizations();
        profilePresenter.getStates();
    }

    private void initViews() {
        setActionbar(getString(R.string.registration_title));

        backButton = findViewById(R.id.toolbar_back_action);
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
                    userGender = getResources().getString(R.string.male);
                    break;

                case R.id.gender_female:
                    userGender = getResources().getString(R.string.female);
                    break;

                case R.id.gender_other:
                    userGender = getResources().getString(R.string.other);
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            LoginInfo loginInfo = bundle.getParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA);
            if (loginInfo != null) {
                etUserMobileNumber.setText(loginInfo.getMobileNumber());
            }
        } else {
            etUserMobileNumber.setText(Util.getUserMobileFromPref());
        }

        projectsList = new ArrayList<>();
        projectsList.add(getString(R.string.label_select));
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        etUserBirthDate.setOnClickListener(this);
        etUserProject.setOnClickListener(this);

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
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.et_user_birth_date:
                showDateDialog(ProfileActivity.this, findViewById(R.id.et_user_birth_date));
                break;

            case R.id.et_user_project:
                showMultiSelectDialogProject(projectsList);
                break;

            case R.id.user_profile_pic:
                onAddImageClick();
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

        DatePickerDialog dateDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    private void showMultiSelectDialogProject(ArrayList<String> projectList) {
        if (projectList != null) {
            projectList.remove(getString(R.string.label_select));

            String[] items = projectList.toArray(new String[projectList.size()]);
            projectSelection = new boolean[(items.length)];
            Arrays.fill(projectSelection, false);

            if (!selectedProjects.isEmpty()) {
                String[] projects = selectedProjects.split(";");
                for (String project : projects) {
                    if (projectList.contains(project.trim())) {
                        projectSelection[projectList.indexOf(project.trim())] = true;
                    }
                }
            }

            AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle(getString(R.string.title_select_project))
                    .setMultiChoiceItems(items, projectSelection, (dialog1, which, isChecked) -> {
                        if (projectSelection != null && which < projectSelection.length) {
                            projectSelection[which] = isChecked;
                            selectedProjects = buildStringArrayToString(items);
                        } else {
                            throw new IllegalArgumentException("Exception in showing projects");
                        }
                    })
                    .setPositiveButton(ProfileActivity.this.getString(R.string.ok), (dialog12, id) -> etUserProject.setText(selectedProjects))
                    .setNegativeButton(getString(R.string.cancel), (dialog13, id) -> {
                    }).create();

            dialog.show();
        }
    }

    private String buildStringArrayToString(String[] items) {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        if (items != null) {
            for (int i = 0; i < items.length; ++i) {
                if (projectSelection[i]) {
                    if (foundOne) {
                        sb.append(";");
                    }
                    foundOne = true;
                    sb.append(items[i]);
                }
            }
        }
        return sb.toString();
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
    }

    private void submitProfileDetails() {
        if (isAllInputsValid()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserFirstName(String.valueOf(etUserFirstName.getText()).trim());
            userInfo.setUserMiddleName(String.valueOf(etUserMiddleName.getText()).trim());
            userInfo.setUserLastName(String.valueOf(etUserLastName.getText()).trim());
            userInfo.setUserBirthDate(String.valueOf(etUserBirthDate.getText()).trim());
            userInfo.setUserMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            userInfo.setUserEmailId(String.valueOf(etUserEmailId.getText()).trim());
            userInfo.setUserGender(userGender);

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
//        } else if (etUserEmailId.getText().toString().trim().length() == 0 &&
//                !Patterns.EMAIL_ADDRESS.matcher(etUserEmailId.getText().toString().trim()).matches()) {
//            msg = getResources().getString(R.string.msg_enter_valid_email_id);
//        } else if (etUserProject.getText().toString().trim().equals("")) {
//            msg = getString(R.string.msg_select_project);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/MV/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(this, getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this,
                    getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                String imageFilePath = "/MV/Image/picture_crop.jpg";
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).asSquare().start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = "/MV/Image/picture_crop.jpg";
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).asSquare().start(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Glide.with(this)
                    .load(finalUri)
                    .into(imgUserProfilePic);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPictureDialog();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_user_organization:
                if (orgs != null && !orgs.isEmpty() && orgs.get(i) != null && !TextUtils.isEmpty(orgs.get(i).getId())) {
                    profilePresenter.getOrganizationProjects(orgs.get(i).getId());
                    profilePresenter.getOrganizationRoles(orgs.get(i).getId());
                }
                break;

            case R.id.sp_user_role:
                break;

            case R.id.sp_user_state:
                /*if (states != null && !states.isEmpty() && states.get(i) != null && states.get(i).getJurisdictions() != null
                        && !states.get(i).getJurisdictions().isEmpty()) {
                    profilePresenter.getJurisdictionLevelData(states.get(i).getJurisdictions().get(i).getStateId(),
                            states.get(i).getJurisdictions().get(i).getLevel());
                }*/
                break;

            case R.id.sp_user_district:
                break;

            case R.id.sp_user_taluka:
                break;

            case R.id.sp_user_cluster:
                break;

            case R.id.sp_user_village:
                break;

            case R.id.sp_user_structure:
                break;
        }
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

    @Override
    public <T> void showNextScreen(T data) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showOrganizations(List<Organization> organizations) {
        this.orgs = organizations;
        List<String> orgs = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            orgs.add(organizations.get(i).getOrgName());
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, orgs);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrganization.setAdapter(a);

    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {
        if (organizationProjects != null && !organizationProjects.isEmpty()) {
            for (OrganizationProject organizationProject :
                    organizationProjects) {
                projectsList.add(organizationProject.getOrgProjectName());
            }
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        List<String> orgRoles = new ArrayList<>();
        for (int i = 0; i < organizationRoles.size(); i++) {
            orgRoles.add(organizationRoles.get(i).getOrgName());
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, orgRoles);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(a);
    }

    @Override
    public void showStates(List<State> states) {
        this.states = states;
        List<String> stateNames = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            stateNames.add(states.get(i).getOrgName());
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_item, stateNames);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(a);
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    @Override
    public void onBackPressed() {
        try {
            hideProgressBar();
            setResult(RESULT_CANCELED);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
