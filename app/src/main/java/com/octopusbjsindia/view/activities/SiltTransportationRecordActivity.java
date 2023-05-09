package com.octopusbjsindia.view.activities;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.SujalamSuphalam.SiltTransportRecord;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.SiltTransportationRecordFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.fragments.MachineDieselRecordFragment;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SiltTransportationRecordActivity extends AppCompatActivity implements APIDataListener, 
        View.OnClickListener, CustomSpinnerListener {

    private View siltTransportationRecordFragmentView;
    private SiltTransportationRecordFragmentPresenter presenter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private String /*machineId,*/ currentStructureId;
    //private ImageView imgRegisterOne, clickedImageView; //imgRegisterTwo, imgRegisterThree, ;
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineDieselRecordFragment.class.getName();
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    private Button btnSubmit;
    private EditText etDate, etState, etDistrict, etTaluka, etVillage, etBType, etArea, etSurveyNo, etBFirstName, etBLastName, etBMobile,
            etTractorTripsCount, etTipperTripsCount, etTotalSilt;
    //etFarmersCount, etBeneficiariesCount;
    private String currentPhotoPath = "";
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> bTypeList = new ArrayList<>();
    private String selectedState, selectedStateId, selectedDistrict, selectedDistrictId, selectedTaluka,
            selectedTalukaId, selectedVillage, selectedVillageId, selectedBType, selectedBTypeId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silt_transportation_record);
        //machineId = getIntent().getStringExtra("machineId");
        currentStructureId = getIntent().getStringExtra("structureId");

        presenter = new SiltTransportationRecordFragmentPresenter(this);
        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        etDate = findViewById(R.id.et_date);
        etDate.setOnClickListener(this);
        etState = findViewById(R.id.et_state);
        etState.setOnClickListener(this);
        etDistrict = findViewById(R.id.et_district);
        etDistrict.setOnClickListener(this);
        etTaluka = findViewById(R.id.et_taluka);
        etTaluka.setOnClickListener(this);
        etVillage = findViewById(R.id.et_village);
        etVillage.setOnClickListener(this);
        etState.setFocusable(false);
        etState.setLongClickable(false);
        etDistrict.setFocusable(false);
        etDistrict.setLongClickable(false);
        etTaluka.setFocusable(false);
        etTaluka.setLongClickable(false);
        etVillage.setFocusable(false);
        etVillage.setLongClickable(false);
        etBType = findViewById(R.id.et_b_type);
        etBType.setOnClickListener(this);
        etBType.setFocusable(false);
        etBType.setLongClickable(false);
        etSurveyNo = findViewById(R.id.et_survey_no);
        etSurveyNo.setLongClickable(false);
        etArea = findViewById(R.id.et_area);
        etArea.setLongClickable(false);
        etBFirstName = findViewById(R.id.et_b_fname);
        etBFirstName.setLongClickable(false);
        etBLastName = findViewById(R.id.et_b_lname);
        etBLastName.setLongClickable(false);
        etBMobile = findViewById(R.id.et_b_mobile);
        etBMobile.setLongClickable(false);
        etBMobile.addTextChangedListener(textWatcher);
        etTractorTripsCount = findViewById(R.id.et_trolley_trips_count);
        etTractorTripsCount.setLongClickable(false);
        etTipperTripsCount = findViewById(R.id.et_tipper_trips_count);
        etTipperTripsCount.setLongClickable(false);
        TextView tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText("Silt Beneficiary Record");
        findViewById(R.id.iv_toobar_back).setOnClickListener(this);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        if (!Util.isConnected(this)) {
            Util.showToast(getResources().getString(R.string.msg_no_network), this);
        }

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            selectedState = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName();
            selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
            etState.setText(selectedState);
        }
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            selectedDistrict = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName();
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
            etDistrict.setText(selectedDistrict);
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            selectedTaluka = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName();
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
            etTaluka.setText(selectedTaluka);
        }

        CustomSpinnerObject customSpinnerObject1 = new CustomSpinnerObject();
        customSpinnerObject1.setName("Bricks Maker");
        customSpinnerObject1.set_id("624ae70c750ea48291611f07");
        customSpinnerObject1.setSelected(false);
        bTypeList.add(customSpinnerObject1);

        CustomSpinnerObject customSpinnerObject2 = new CustomSpinnerObject();
        customSpinnerObject2.setName("Farmer");
        customSpinnerObject2.set_id("624ae6dd750ea48291611f06");
        customSpinnerObject2.setSelected(false);
        bTypeList.add(customSpinnerObject2);

//        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
//                getSSMasterDatabaseDao().getSSMasterData("SS");
//        String masterDbString = list.get(0).getData();
//        Gson gson = new Gson();
//        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
//        };
//        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());
//
//        for (int i = 0; i < masterDataList.size(); i++) {
//            if (masterDataList.get(i).getForm().equals("silt_transport") && masterDataList.get(i).
//                    getField().equals("beneficiaryType")) {
//                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
//                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
//                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
//                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
//                    customSpinnerObject.setSelected(false);
//                    bTypeList.add(customSpinnerObject);
//                }
//            }
//        }
        
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etBMobile.getText().toString().length() == 10) {
                // check if this mobile number is already registered in beneficiary master
                if(Util.isConnected(SiltTransportationRecordActivity.this)) {
                    presenter.getBeneficiaryDetails(etBMobile.getText().toString());
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                Util.showDateDialog(this, etDate);
                break;
            case R.id.iv_toobar_back:
                closeCurrentActivity();
                break;
            case R.id.et_state:
                if (stateList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(SiltTransportationRecordActivity.this, this,
                            "Select State", stateList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        presenter.getLocationData("",
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.STATE_LEVEL);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(SiltTransportationRecordActivity.this, this,
                            "Select District", districtList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        if (etState.getText() != null && etState.getText().toString().length() > 0) {
                            presenter.getLocationData(selectedStateId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else {
                            Util.showToast("Please select state.", this);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_taluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka", talukaList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        if (etDistrict.getText() != null && etDistrict.getText().toString().length() > 0) {
                            presenter.getLocationData(selectedDistrictId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else {
                            Util.showToast("Please select district.", this);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_village:
                if (villageList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(this, this,
                            "Select Village", villageList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        if (etTaluka.getText() != null && etTaluka.getText().toString().length() > 0) {
                            presenter.getLocationData(selectedTalukaId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        } else {
                            Util.showToast("Please select taluka.", this);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_b_mobile:
                break;
            case R.id.et_b_type:
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(this, this,
                        "Select Beneficiary Type", bTypeList, false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
//            case R.id.img_register_one:
//                clickedImageView = imgRegisterOne;
//                onAddImageClick();
//                break;
//            case R.id.img_register_two:
//                clickedImageView = imgRegisterTwo;
//                onAddImageClick();
//                break;
//            case R.id.img_register_three:
//                clickedImageView = imgRegisterThree;
//                onAddImageClick();
//                break;
            case R.id.btn_submit:
                if (Util.isConnected(this)) {
                    if (isAllDataValid()) {
                        SiltTransportRecord siltTransportRecord = new SiltTransportRecord();
                        siltTransportRecord.setStructureId(currentStructureId);
                        //siltTransportRecord.setMachineId(machineId);
                        siltTransportRecord.setSiltTransportDate(Util.dateTimeToTimeStamp(etDate.getText().toString(),
                                "00:00"));
                        siltTransportRecord.setStateId(selectedStateId);
                        siltTransportRecord.setStateName(selectedState);
                        siltTransportRecord.setDistrictId(selectedDistrictId);
                        siltTransportRecord.setDistrictName(selectedDistrict);
                        siltTransportRecord.setTalukaId(selectedTalukaId);
                        siltTransportRecord.setTalukaName(selectedTaluka);
                        siltTransportRecord.setVillageId(selectedVillageId);
                        siltTransportRecord.setVillageName(selectedVillage);
                        siltTransportRecord.setbTypeId(selectedBTypeId);
                        siltTransportRecord.setSurveyNo(etSurveyNo.getText().toString());
                        siltTransportRecord.setArea(etArea.getText().toString());
                        siltTransportRecord.setbFirstName(etBFirstName.getText().toString());
                        siltTransportRecord.setbLastName(etBLastName.getText().toString());
                        siltTransportRecord.setbMobile(etBMobile.getText().toString());
                        siltTransportRecord.setTractorTripsCount(etTractorTripsCount.getText().toString());
                        siltTransportRecord.setTipperTripsCount(etTipperTripsCount.getText().toString());
//                        siltTransportRecord.setFarmersCount(etFarmersCount.getText().toString());
//                        siltTransportRecord.setBeneficiariesCount(etBeneficiariesCount.getText().toString());
                        uploadData(siltTransportRecord);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }
                break;
        }
    }

    private boolean isAllDataValid() {
        //if (imageCount == 0) {
        if (TextUtils.isEmpty(etDate.getText().toString().trim())
                || TextUtils.isEmpty(etState.getText().toString().trim())
                || TextUtils.isEmpty(etDistrict.getText().toString().trim())
                || TextUtils.isEmpty(etTaluka.getText().toString().trim())
                || TextUtils.isEmpty(etVillage.getText().toString().trim())
                || TextUtils.isEmpty(etBType.getText().toString().trim())
                || TextUtils.isEmpty(etBFirstName.getText().toString().trim())
                || TextUtils.isEmpty(etBLastName.getText().toString().trim())
                || TextUtils.isEmpty(etBMobile.getText().toString().trim())
                || TextUtils.isEmpty(etTractorTripsCount.getText().toString().trim())
                || TextUtils.isEmpty(etTipperTripsCount.getText().toString().trim())) {

            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);

            return false;
        }
//            Util.snackBarToShowMsg(this.getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Please, click image of register.",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        }
        return true;
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    stateList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject state = new CustomSpinnerObject();
                        state.set_id(location.getId());
                        state.setName(location.getName());
                        state.setSelected(false);
                        stateList.add(state);
                    }
                }
                CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(this, this,
                        "Select State",
                        stateList,
                        false);
                cdd7.show();
                cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    districtList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        districtList.add(district);
                    }
                }
                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(this, this,
                        "Select District", districtList, false);
                cddDistrict.show();
                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    talukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject taluka = new CustomSpinnerObject();
                        taluka.set_id(location.getId());
                        taluka.setName(location.getName());
                        taluka.setSelected(false);
                        talukaList.add(taluka);
                    }
                }
                CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(this, this,
                        "Select Taluka", talukaList, false);
                cddTaluka.show();
                cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    villageList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject village = new CustomSpinnerObject();
                        village.set_id(location.getId());
                        village.setName(location.getName());
                        village.setSelected(false);
                        villageList.add(village);
                    }
                }
                CustomSpinnerDialogClass cddVillage = new CustomSpinnerDialogClass(this, this,
                        "Select Village", villageList, false);
                cddVillage.show();
                cddVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    public void updateBeneficiaryDetails(SiltTransportRecord record) {
        etSurveyNo.setText(record.getSurveyNo());
        etArea.setText(record.getArea());
        etBFirstName.setText(record.getbFirstName());
        etBLastName.setText(record.getbLastName());
        if(selectedStateId == null ||!selectedStateId.equals(record.getStateId())) {
            selectedStateId = record.getStateId();
            selectedState = record.getStateName();
            etState.setText(selectedState);
            etDistrict.setText("");
            selectedDistrict = "";
            selectedDistrictId = "";
            etTaluka.setText("");
            selectedTaluka = "";
            selectedTalukaId = "";
        }
        if(selectedDistrictId == null ||!selectedDistrictId.equals(record.getDistrictId())) {
            selectedDistrictId = record.getDistrictId();
            selectedDistrict = record.getDistrictName();
            etDistrict.setText(selectedDistrict);
            etTaluka.setText("");
            selectedTaluka = "";
            selectedTalukaId = "";
            etVillage.setText("");
            selectedVillage = "";
            selectedVillageId = "";
        }
        if(selectedTalukaId == null || !selectedTalukaId.equals(record.getTalukaId())) {
            selectedTalukaId = record.getTalukaId();
            selectedTaluka = record.getTalukaName();
            etTaluka.setText(selectedTaluka);
            etVillage.setText("");
            selectedVillage = "";
            selectedVillageId = "";
        }
        if (selectedVillageId ==null || !selectedVillageId.equals(record.getTalukaId())) {
            selectedVillageId = record.getVillageId();
            selectedVillage = record.getVillageName();
            etVillage.setText(selectedVillage);
        }
        for (CustomSpinnerObject bType: bTypeList) {
            if(bType.get_id().equals(record.getbTypeId())) {
                selectedBType = bType.getName();
                selectedBTypeId = bType.get_id();
                break;
            }
        }
        etBType.setText(selectedBType);
    }

    private void uploadData(SiltTransportRecord siltTransportRecord) {
        showProgressBar();
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.SILT_TRANSPORT_BENEFICIARY_FORM;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse responseOBJ = new Gson().fromJson(jsonString, CommonResponse.class);
                            hideProgressBar();
                            if (responseOBJ.getStatus() == 200) {
                                Util.showToast(responseOBJ.getMessage(), SiltTransportationRecordActivity.this);
                                closeCurrentActivity();
                            } else {
                                Util.showToast(responseOBJ.getMessage(), SiltTransportationRecordActivity.this);
                            }
                            Log.d("response -", jsonString);

                        } catch (UnsupportedEncodingException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            Toast.makeText(SiltTransportationRecordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(SiltTransportationRecordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(siltTransportRecord));
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));//add string parameters
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
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
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
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select State":
                for (CustomSpinnerObject state : stateList) {
                    if (state.isSelected()) {
                        selectedState = state.getName();
                        selectedStateId = state.get_id();
                        break;
                    }
                }
                etState.setText(selectedState);
                etDistrict.setText("");
                selectedDistrict = "";
                selectedDistrictId = "";
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
            case "Select District":
                for (CustomSpinnerObject district : districtList) {
                    if (district.isSelected()) {
                        selectedDistrict = district.getName();
                        selectedDistrictId = district.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select Taluka":
                for (CustomSpinnerObject taluka : talukaList) {
                    if (taluka.isSelected()) {
                        selectedTaluka = taluka.getName();
                        selectedTalukaId = taluka.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select Village":
                for (CustomSpinnerObject village : villageList) {
                    if (village.isSelected()) {
                        selectedVillage = village.getName();
                        selectedVillageId = village.get_id();
                        break;
                    }
                }
                etVillage.setText(selectedVillage);
                break;
            case "Select Beneficiary Type":
                for (CustomSpinnerObject BType : bTypeList) {
                    if (BType.isSelected()) {
                        selectedBType = BType.getName();
                        selectedBTypeId = BType.get_id();
                        break;
                    }
                }
                etBType.setText(selectedBType);
                break;
        }
    }
}