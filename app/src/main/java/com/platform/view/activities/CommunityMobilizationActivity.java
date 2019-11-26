package com.platform.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.CatchmentVillagesData;
import com.platform.models.SujalamSuphalam.CommunityMobilizationRequest;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.events.CommonResponse;
import com.platform.models.login.Login;
import com.platform.models.profile.JurisdictionLocation;
import com.platform.presenter.CommunityMobilizationActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.platform.utility.Util.getLoginObjectFromPref;
import static com.platform.utility.Util.getUserObjectFromPref;

public class CommunityMobilizationActivity extends AppCompatActivity implements View.OnClickListener,
        CustomSpinnerListener, APIDataListener {

    private final String TAG = StructurePripretionsActivity.class.getName();
    private final String STRUCTURE_DATA = "StructureData";

    EditText selecteETVillage, etActivity, etTask, etA1MeetingDate, etA1VillageName, etA1GrampanchayatName, etA1NoOfParticipant,
            etA1NameOfSarpanch, etA1ContactOfSarpanch, etA1NameOfOopSarpanch, etA1ContactOfOopSarpanch,
            etA2VillageName, etA2GpName, etA2Date, etA2NoOfParticipant, etA3TaskforceLeader, etA3MemberName,
            etA3Gender, etA3PhoneNoTaskForceLeader, etA3Education, etA3Occupation, etA3DateOfFormation,
            etA4TopicOfTraining, etA4DateOfTraining, etA4NameOfParticipant, etA4DurationInDays,
            etA5VillageName, etA5NameOfOfficerDept, etA5Date, etA5NameOfFarmer, etA5PhoneNoOfFarmers,
            etA5FarmerLandHolding;
    ImageView selectedIV, ivA1EntryLevel, ivA2EntryLevel, ivA4Photo, ivA5Photo;
    Button btSubmit;

    private RelativeLayout progressBar;
    private CommunityMobilizationActivityPresenter presenter;

    String selectedActivity, selectedActivityID, selectedGander, selectedTask, selectedTaskID, selectedVillage, selectedVillageID;
    private ArrayList<CustomSpinnerObject> statusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> taskList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> genderList = new ArrayList<>();
    CustomSpinnerDialogClass csdTask;

    private Uri outputUri;
    private Uri finalUri;

    final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.COMMUNITY_MOBILISATION;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    CommunityMobilizationRequest requestData = new CommunityMobilizationRequest();
    StructureData structureData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_mobilization);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CommunityMobilizationActivityPresenter(this);
        //get VIleges
        presenter.getCatchmentVillage(structureData.getStructureId());


        String[] activityList = getResources().getStringArray(R.array.activitys);
        for (int i = 0; i < activityList.length; i++) {
            CustomSpinnerObject obj = new CustomSpinnerObject();
            obj.set_id("" + (i + 1));
            obj.setName(activityList[i]);
            statusList.add(obj);
        }

        CustomSpinnerObject male = new CustomSpinnerObject();
        male.set_id("1");
        male.setName("Male");
        genderList.add(male);
        CustomSpinnerObject female = new CustomSpinnerObject();
        female.set_id("2");
        female.setName("Female");
        genderList.add(female);


        csdTask = new CustomSpinnerDialogClass(this, this,
                "Tasks", taskList, false);

        initView();
        setTitle("Community Mobilization");
    }

    private void initView() {

        etActivity = findViewById(R.id.et_activity);
        etTask = findViewById(R.id.et_tasks);

        //Activity 1
        etA1MeetingDate = findViewById(R.id.et_a1_meeting_date);
        etA1VillageName = findViewById(R.id.et_a1_village_name);
        etA1GrampanchayatName = findViewById(R.id.et_a1_grampanchayat_name);
        etA1NoOfParticipant = findViewById(R.id.et_a1_no_of_participant);
        etA1NameOfSarpanch = findViewById(R.id.et_a1_name_of_sarpanch);
        etA1ContactOfSarpanch = findViewById(R.id.et_a1_contact_of_sarpanch);
        etA1NameOfOopSarpanch = findViewById(R.id.et_a1_name_of_oopSarpanch);
        etA1ContactOfOopSarpanch = findViewById(R.id.et_a1_contact_of_oopSarpanch);
        ivA1EntryLevel = findViewById(R.id.iv_a1_entry_level);

        //Activity 2
        etA2VillageName = findViewById(R.id.et_a2_village_name);
        etA2GpName = findViewById(R.id.et_a2_gp_name);
        etA2Date = findViewById(R.id.et_a2_date);
        etA2NoOfParticipant = findViewById(R.id.et_a2_no_of_participant);
        ivA2EntryLevel = findViewById(R.id.iv_a2_entry_level);

        //Activity 3
        etA3TaskforceLeader = findViewById(R.id.et_a3_taskforce_leader);
        etA3MemberName = findViewById(R.id.et_a3_member_name);
        etA3Gender = findViewById(R.id.et_a3_gender);
        etA3PhoneNoTaskForceLeader = findViewById(R.id.et_a3_phone_no_task_force);
        etA3Education = findViewById(R.id.et_a3_education);
        etA3Occupation = findViewById(R.id.et_a3_occupation);
        etA3DateOfFormation = findViewById(R.id.et_a3_date_of_formation);

        //Activity 4
        etA4TopicOfTraining = findViewById(R.id.et_a4_topic_of_training);
        etA4DateOfTraining = findViewById(R.id.et_a4_date_of_training);
        etA4NameOfParticipant = findViewById(R.id.et_a4_name_of_participant);
        etA4DurationInDays = findViewById(R.id.et_a4_duration_in_days);
        ivA4Photo = findViewById(R.id.iv_a4_photo);

        //Activity 5
        etA5VillageName = findViewById(R.id.et_a5_village_name);
        etA5NameOfOfficerDept = findViewById(R.id.et_a5_name_of_officer_dept);
        etA5Date = findViewById(R.id.et_a5_date);
        etA5NameOfFarmer = findViewById(R.id.et_a5_name_of_farmer);
        etA5PhoneNoOfFarmers = findViewById(R.id.et_a5_phone_no_of_farmers);
        etA5FarmerLandHolding = findViewById(R.id.et_a5_farmer_land_holding);
        ivA5Photo = findViewById(R.id.iv_a5_photo);

        btSubmit = findViewById(R.id.bt_submit);

        etActivity.setOnClickListener(this);
        etTask.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        ivA1EntryLevel.setOnClickListener(this);
        ivA2EntryLevel.setOnClickListener(this);
        ivA4Photo.setOnClickListener(this);
        ivA5Photo.setOnClickListener(this);

        etA1MeetingDate.setOnClickListener(this);
        etA2Date.setOnClickListener(this);
        etA3DateOfFormation.setOnClickListener(this);
        etA3Gender.setOnClickListener(this);
        etA4DateOfTraining.setOnClickListener(this);
        etA5Date.setOnClickListener(this);
        etA1VillageName.setOnClickListener(this);
        etA2VillageName.setOnClickListener(this);
        etA5VillageName.setOnClickListener(this);

    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_activity:
                CustomSpinnerDialogClass csdStatus = new CustomSpinnerDialogClass(this, this,
                        "Activity", statusList, false);
                csdStatus.show();
                csdStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_tasks:
                csdTask.show();
                csdTask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    uploadImage();
                }
                break;
            case R.id.et_a1_meeting_date:
                Util.showDateDialog(this, etA1MeetingDate);
            case R.id.et_a2_date:
                Util.showDateDialog(this, etA2Date);
            case R.id.et_a3_date_of_formation:
                Util.showDateDialog(this, etA3DateOfFormation);
            case R.id.et_a4_date_of_training:
                Util.showDateDialog(this, etA4DateOfTraining);
                break;
            case R.id.et_a5_date:
                Util.showDateDialog(this, etA5Date);
                break;
            case R.id.et_a3_gender:
                CustomSpinnerDialogClass csdGander = new CustomSpinnerDialogClass(this, this,
                        "Select Gender", genderList, false);
                csdGander.show();
                csdGander.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_a1_village_name:
                selecteETVillage = etA1VillageName;
                CustomSpinnerDialogClass csdA1Village = new CustomSpinnerDialogClass(this, this,
                        "Select Village", villageList, false);
                csdA1Village.show();
                csdA1Village.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_a2_village_name:
                selecteETVillage = etA2VillageName;
                CustomSpinnerDialogClass csdA2Village = new CustomSpinnerDialogClass(this, this,
                        "Select Village", villageList, false);
                csdA2Village.show();
                csdA2Village.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_a5_village_name:
                selecteETVillage = etA5VillageName;
                CustomSpinnerDialogClass csdA5Village = new CustomSpinnerDialogClass(this, this,
                        "Select Village", villageList, false);
                csdA5Village.show();
                csdA5Village.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.iv_a1_entry_level:
                selectedIV = ivA1EntryLevel;
                onAddImageClick();
                break;
            case R.id.iv_a2_entry_level:
                selectedIV = ivA2EntryLevel;
                onAddImageClick();
                break;
            case R.id.iv_a4_photo:
                selectedIV = ivA4Photo;
                onAddImageClick();
                break;
            case R.id.iv_a5_photo:
                selectedIV = ivA5Photo;
                onAddImageClick();
                break;
            case R.id.toolbar_back_action:
                finish();
                break;

        }
    }


    private boolean isAllDataValid() {

//        GPSTracker gpsTracker = new GPSTracker(this);
//        Location location = null;
//        if (gpsTracker.isGPSEnabled(this, this)) {
//            location = gpsTracker.getLocation();
//        } else {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Location not available",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        }

        requestData.setStructureId(structureData.getStructureId());
        if (TextUtils.isEmpty(selectedActivityID)
                || TextUtils.isEmpty(selectedActivity)
                || TextUtils.isEmpty(selectedTask)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please fill proper information.",
                    Snackbar.LENGTH_LONG);
            return false;
        } else {
            requestData.setActivityCode(selectedActivityID);
            requestData.setActivityName(selectedActivity);
            requestData.setTask(selectedTask);
        }

        switch (Integer.parseInt(selectedActivityID)) {
            case 1:
                //Activity 1
                if (TextUtils.isEmpty(etA1MeetingDate.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Meeting Date.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(selectedVillageID)){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, select Village.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1GrampanchayatName.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Grampanchayat Name.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1NoOfParticipant.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter No Of Participant.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1NameOfSarpanch.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Name Of Sarpanch.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1ContactOfSarpanch.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Contact Of Sarpanch.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1NameOfOopSarpanch.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Name Of Oop-Sarpanch.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA1ContactOfOopSarpanch.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Contact Of Oop-Sarpanch.", Snackbar.LENGTH_LONG);
                    return false;
                } else {
                    requestData.setDate(etA1MeetingDate.getText().toString());
                    requestData.setVillageId(selectedVillageID);
                    requestData.setGrampanchayatName(etA1GrampanchayatName.getText().toString());
                    requestData.setNoParticipant(etA1NoOfParticipant.getText().toString());
                    requestData.setSarpanchName(etA1NameOfSarpanch.getText().toString());
                    requestData.setSarpanchPhoneNo(etA1ContactOfSarpanch.getText().toString());
                    requestData.setOopsarpanchName(etA1NameOfOopSarpanch.getText().toString());
                    requestData.setOopsarpanchPhoneNo(etA1ContactOfOopSarpanch.getText().toString());
                }
                if (imageCount == 0) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please click images of structure.",
                            Snackbar.LENGTH_LONG);
                    return false;
                }
                break;

            case 2:
                //Activity 2
                if (TextUtils.isEmpty(selectedVillageID)){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, select Village.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA2GpName.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Grampanchayt Name.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA2Date.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Date.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA2NoOfParticipant.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter No Of Participant.", Snackbar.LENGTH_LONG);
                    return false;
                } else {
                    requestData.setVillageId(selectedVillageID);
                    requestData.setGrampanchayatName(etA2GpName.getText().toString());
                    requestData.setDate(etA2Date.getText().toString());
                    requestData.setNoParticipant(etA2NoOfParticipant.getText().toString());
                }
                if (imageCount == 0) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please, click images of structure.",
                            Snackbar.LENGTH_LONG);
                    return false;
                }
                break;
            case 3:
                //Activity 3

                if (TextUtils.isEmpty(etA3TaskforceLeader.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Taskforce Leader.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA3MemberName.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Member Name.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA3Gender.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Gender.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA3PhoneNoTaskForceLeader.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Phone No. Task-force Leader.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA3Education.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Education.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA3Occupation.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Occupation.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA3DateOfFormation.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Date Of Formation.", Snackbar.LENGTH_LONG);
                    return false;
                } else {
                    requestData.setLeaderName(etA3TaskforceLeader.getText().toString());
                    requestData.setMemberName(etA3MemberName.getText().toString());
                    requestData.setMemberGander(etA3Gender.getText().toString());
                    requestData.setLeaderPhoneNo(etA3PhoneNoTaskForceLeader.getText().toString());
                    requestData.setEducation(etA3Education.getText().toString());
                    requestData.setEducation(etA3Occupation.getText().toString());
                    requestData.setDate(etA3DateOfFormation.getText().toString());
                }
                break;
            case 4:
                //Activity 4
                if (TextUtils.isEmpty(etA4TopicOfTraining.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Topic Of Training.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA4DateOfTraining.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Date Of Training.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA4NameOfParticipant.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Name Of Participant.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA4DurationInDays.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Duration In Days.", Snackbar.LENGTH_LONG);
                    return false;
                } else {
                    requestData.setTopicName(etA4TopicOfTraining.getText().toString());
                    requestData.setDate(etA4DateOfTraining.getText().toString());
                    requestData.setParticipantName(etA4NameOfParticipant.getText().toString());
                    requestData.setDuration(etA4DurationInDays.getText().toString());
                }
                if (imageCount == 0) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please, click images of structure.",
                            Snackbar.LENGTH_LONG);
                    return false;
                }
               break;
            case 5:
                //Activity 5
                if (TextUtils.isEmpty(selectedVillageID)){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, selected Village.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA5NameOfOfficerDept.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter name of officer oepartment.", Snackbar.LENGTH_LONG);
                    return false;
                } else if(TextUtils.isEmpty(etA5Date.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, selected Date.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA5NameOfFarmer.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter name of Farmer.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA5PhoneNoOfFarmers.getText().toString())){
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter phone of Farmer.", Snackbar.LENGTH_LONG);
                    return false;
                } else if( TextUtils.isEmpty(etA5FarmerLandHolding.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter Farmer Land Holding.", Snackbar.LENGTH_LONG);
                    return false;
                } else {
                    requestData.setVillageId(selectedVillageID);
                    requestData.setGovtOfficerDept(etA5NameOfOfficerDept.getText().toString());
                    requestData.setDate(etA5Date.getText().toString());
                    requestData.setFormerName(etA5NameOfFarmer.getText().toString());
                    requestData.setFormerPhoneNo(etA5PhoneNoOfFarmers.getText().toString());
                    requestData.setFormerLandHolding(etA5FarmerLandHolding.getText().toString());
                }
                if (imageCount == 0) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please, click images of structure.",
                            Snackbar.LENGTH_LONG);
                    return false;
                }
                break;
        }
        return true;
    }

    private void uploadImage() {
        showProgressBar();
        Log.d("url :", upload_URL);
        Log.d("request :", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        hideProgressBar();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                            if(commonResponse.getStatus()==200){
                                Util.showToast(commonResponse.getMessage(),this);
                                finish();
                            } else {
                                Util.showToast(commonResponse.getMessage(),this);
                            }
                            Log.d("response :", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(CommunityMobilizationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(CommunityMobilizationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));
                 return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId()!=null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds()!=null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds()!=null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0; i < imageHashmap.size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(this);
        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Activity":
                taskList.clear();
                for (CustomSpinnerObject obj : statusList) {
                    if (obj.isSelected()) {
                        selectedActivity = obj.getName();
                        selectedActivityID = obj.get_id();
                        break;
                    }
                }
                String[] activityList = {""};
                switch (Integer.parseInt(selectedActivityID)) {
                    case 1:
                        activityList = getResources().getStringArray(R.array.activitys1);
                        findViewById(R.id.ly_activity1).setVisibility(View.VISIBLE);
                        findViewById(R.id.ly_activity2).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity3).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity4).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity5).setVisibility(View.GONE);
                        break;
                    case 2:
                        activityList = getResources().getStringArray(R.array.activitys2);
                        findViewById(R.id.ly_activity1).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity2).setVisibility(View.VISIBLE);
                        findViewById(R.id.ly_activity3).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity4).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity5).setVisibility(View.GONE);
                        break;
                    case 3:
                        activityList = getResources().getStringArray(R.array.activitys3);
                        findViewById(R.id.ly_activity1).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity2).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity3).setVisibility(View.VISIBLE);
                        findViewById(R.id.ly_activity4).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity5).setVisibility(View.GONE);
                        break;
                    case 4:
                        activityList = getResources().getStringArray(R.array.activitys4);
                        findViewById(R.id.ly_activity1).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity2).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity3).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity4).setVisibility(View.VISIBLE);
                        findViewById(R.id.ly_activity5).setVisibility(View.GONE);
                        break;
                    case 5:
                        activityList = getResources().getStringArray(R.array.activitys5);
                        findViewById(R.id.ly_activity1).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity2).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity3).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity4).setVisibility(View.GONE);
                        findViewById(R.id.ly_activity5).setVisibility(View.VISIBLE);
                        break;
                }
                for (int i = 0; i < activityList.length; i++) {
                    CustomSpinnerObject obj = new CustomSpinnerObject();
                    obj.set_id("" + i + 1);
                    obj.setName(activityList[i]);
                    taskList.add(obj);
                }
                selectedTask="";
                selectedTaskID="";
                etTask.setText("");
                csdTask = new CustomSpinnerDialogClass(this, this, "Tasks", taskList, false);
                etActivity.setText(selectedActivity);
                break;
            case "Tasks":
                for (CustomSpinnerObject obj : taskList) {
                    if (obj.isSelected()) {
                        selectedTask = obj.getName();
                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etTask.setText(selectedTask);
                break;
            case "Select Gender":
                for (CustomSpinnerObject obj : genderList) {
                    if (obj.isSelected()) {
                        selectedGander = obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etA3Gender.setText(selectedGander);
                break;
            case "Select Village":
                for (CustomSpinnerObject obj : villageList) {
                    if (obj.isSelected()) {
                        selectedVillageID = obj.get_id();
                        selectedVillage = obj.getName();
                        break;
                    }
                }
                selecteETVillage.setText(selectedVillage);
                break;
        }
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
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
            Toast.makeText(this, getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(this, this.getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(imageFile)) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalUri);
                        selectedIV.setImageURI(finalUri);
                        imageHashmap.put("image" + imageCount, bitmap);
                        imageCount++;
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }

    public void showCattachmentVileges(List<CatchmentVillagesData> data) {
        if (data != null && !data.isEmpty()) {
            villageList.clear();
            Collections.sort(data, (j1, j2) -> j1.getName().compareTo(j2.getName()));

            for (int i = 0; i < data.size(); i++) {
                CatchmentVillagesData location = data.get(i);
                CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                meetCountry.set_id(location.getId());
                meetCountry.setName(location.getName());
                meetCountry.setSelected(false);
                villageList.add(meetCountry);
            }
        }
    }
}
