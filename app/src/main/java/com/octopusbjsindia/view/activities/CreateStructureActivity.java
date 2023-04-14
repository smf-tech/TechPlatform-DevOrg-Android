package com.octopusbjsindia.view.activities;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.SujalamSuphalam.Structure;
import com.octopusbjsindia.models.SujalamSuphalam.StructureAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.SujalamSuphalam.StructurePripretionData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.CreateStructureActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateStructureActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener,
        CustomSpinnerListener {

    private RelativeLayout progressBar;
    private CreateStructureActivityPresenter presenter;
    private String structureId;
    private ImageView structureImg1, structureImg2;
    private EditText etState, etDistrict, etTaluka, etHostVillage,
    //etCatchmentVillage, etHostVillagePopulation, etCatchmentVillagePopulation,
    etGatNo, etWaterShedNo, etArea, etStructureName, etStructureType, /*etStructureWorkType,*/ etStructureOwnerDepartment,
    //etNotaDetail, etSubStructureOwnerDepartment,
    etAdministrativeApprovalNo, etAdministrativeApprovalDate,
            etTechnicalSanctionNo, etTechnicalSanctionDate, etIntervention, etAdministrativeEstimateAmount,
    //etApproximateWorkingHours, etApproximateDieselConsumptionAmount, etApproximateDieselLiters,
    etSanctionedSiltQuantity, etPotentialSiltQuantity, etRemark;
    private Button btSubmit;

    private String selectedStateId, selectedState, selectedDistrictId, selectedDistrict, selectedTalukaId, selectedTaluka,
            selectedHostVillageId, selectedHostVillage, selectedStructureTypeId, selectedIntervention, selectedInterventionId,
            selectedStructureType, /*selectedStructureWorkTypeId, selectedStructureWorkType,*/ selectedStructureOwnerDepartmentId,
            selectedStructureOwnerDepartment, selectedSubStructureOwnerDepartmentId, selectedSubStructureOwnerDepartment;

    private boolean isStateFilter, isDistrictFilter, isTalukaFilter;

    String userStateIds = "";
    String userDistrictIds = "";
    String userTalukaIds = "";

    //private ArrayList<String> selectedCatchmentVillageId = new ArrayList<String>();
    //private ArrayList<String> selectedCatchmentVillage = new ArrayList<String>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    //private ArrayList<CustomSpinnerObject> catchmentVillageList = new ArrayList<>();
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureDepartmentList = new ArrayList<>();
    //private ArrayList<CustomSpinnerObject> structureSubDepartmentList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureWorkTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> interventionList = new ArrayList<>();
    private Structure structureData;
    private GPSTracker gpsTracker;
    private Location location;
    private Uri imageUri1, imageUri2;
    private String currentPhotoPath;
    private boolean isFirstStructureImage = true;
    private Uri finalUri, outputUri;
    private ImageView selectedIV;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.CREATE_STRUCTURE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_structure);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CreateStructureActivityPresenter(this);

        structureId = getIntent().getStringExtra("structure_id");

        structureData = new Structure();

        if (structureId != null && !TextUtils.isEmpty(structureId)) {
            presenter.getStructureById(structureId);
            structureData.setStructureId(structureId);
        }

        setMasterData();
        initView();
        setTitle("Create Waterbody");

    }

    //in edit structure case
    public void setPreviousStructureData(String requestID, StructureAPIResponse structureApiData) {
        Structure structure = structureApiData.getData();
        if (structure == null) {
            return;
        }

        //state
        if (structure.getState() != null && structure.getStateId() != null) {
            etState.setText(structure.getState());
            selectedState = structure.getState();
            selectedStateId = structure.getStateId();
        }
        //district
        if (structure.getDistrict() != null && structure.getDistrictId() != null) {
            etDistrict.setText(structure.getDistrict());
            selectedDistrict = structure.getDistrict();
            selectedDistrictId = structure.getDistrictId();
        }
        //taluka
        if (structure.getTaluka() != null && structure.getTalukaId() != null) {
            etTaluka.setText(structure.getTaluka());
            selectedTaluka = structure.getTaluka();
            selectedTalukaId = structure.getTalukaId();
        }
        //host village
        if (structure.getVillage() != null && structure.getVillageId() != null) {
            etHostVillage.setText(structure.getVillage());
            selectedHostVillage = structure.getVillage();
            selectedHostVillageId = structure.getVillageId();
        }
        //gat no./survey no.
        if (structure.getGatNo() != null) {
            etGatNo.setText(structure.getGatNo());
        }
        //water shed no.
        if (structure.getWaterShedNo() != null) {
            etWaterShedNo.setText(structure.getWaterShedNo());
        }
        //area
        if (structure.getArea() != null) {
            etArea.setText(structure.getArea());
        }
        //structure name
        if (structure.getName() != null) {
            etStructureName.setText(structure.getName());
        }
        //struc owner depart.
        if (structure.getDepartmentId() != null && structure.getDepartmentName() != null) {
            etStructureOwnerDepartment.setText(structure.getDepartmentName());
            selectedStructureOwnerDepartment = structure.getDepartmentName();
            selectedStructureOwnerDepartmentId = structure.getDepartmentId();
        }
        //intervention
        if (structure.getInterventionName() != null && structure.getInterventionId() != null) {
            etIntervention.setText(structure.getInterventionName());
            selectedIntervention = structure.getInterventionName();
            selectedInterventionId = structure.getInterventionId();
        }
        //struc type
        if (structure.getStructureTypeName() != null && structure.getStructureType() != null) {
            etStructureType.setText(structure.getStructureTypeName());
            selectedStructureType = structure.getStructureTypeName();
            selectedStructureTypeId = structure.getStructureType();
        }
        //work type
       /* if (structure.getWorkTypeName() != null && structure.getWorkType() != null) {
            etStructureWorkType.setText(structure.getWorkTypeName());
            selectedStructureWorkType = structure.getWorkTypeName();
            selectedStructureWorkTypeId = structure.getWorkType();
        }*/
        //tech. sanction no.
        if (structure.getTechnicalSectionNumber() != null) {
            etTechnicalSanctionNo.setText(structure.getTechnicalSectionNumber());
        }
        //tech. sanc. date
        if (structure.getTechnicalSectionDate() != null) {
            etTechnicalSanctionDate.setText(structure.getTechnicalSectionDate());
        }
        //admin approval no.
        if (structure.getAdministrativeApprovalNo() != null) {
            etAdministrativeApprovalNo.setText(structure.getAdministrativeApprovalNo());
        }
        //admin approval date
        if (structure.getAdministrativeApprovalDate() != null) {
            etAdministrativeApprovalDate.setText(structure.getAdministrativeApprovalDate());
        }
        //admin estimate amt
        if (structure.getAdministrativeEstimateAmount() != null) {
            etAdministrativeEstimateAmount.setText(structure.getAdministrativeEstimateAmount());
        }
        //sanctioned silt quantity
        if (structure.getApprxEstimateQunty() != null) {
            etSanctionedSiltQuantity.setText(structure.getApprxEstimateQunty());
        }
        //approx. potential silt quanti.
        if (structure.getPotentialSiltQuantity() != null) {
            etPotentialSiltQuantity.setText(structure.getPotentialSiltQuantity());
        }
        //remark
        if (structure.getRemark() != null) {
            etRemark.setText(structure.getRemark());
        }
        //structure image 1
        if (structure.getStructureImage1() != null) {
            structureData.setStructureImage1(structure.getStructureImage1());
            Glide.with(this)
                    .load(structure.getStructureImage1())
                    .placeholder(R.drawable.ic_add_img)
                    .into(structureImg1);
        }
        //structure image 1
        if (structure.getStructureImage2() != null) {
            structureData.setStructureImage2(structure.getStructureImage2());
            Glide.with(this)
                    .load(structure.getStructureImage2())
                    .placeholder(R.drawable.ic_add_img)
                    .into(structureImg2);
        }

        //disable adding & updating structure image
        // below check is check on image on click listener
        /*if (structureId != null && !TextUtils.isEmpty(structureId)) {
            structureImg1.setEnabled(false);
            structureImg2.setEnabled(false);
        }*/
    }

    private void initView() {

        //get lat,long of location
        gpsTracker = new GPSTracker(this);
        if (Permissions.isLocationPermissionGranted(this, this)) {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
                    isStateFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
                    isDistrictFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
                    isTalukaFilter = true;
                    continue;
                }
            }
        }

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                if (i == 0) {
                    userStateIds = j.getId();
                } else {
                    userStateIds = userStateIds + "," + j.getId();
                }
            }
        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                if (i == 0) {
                    userDistrictIds = j.getId();
                } else {
                    userDistrictIds = userDistrictIds + "," + j.getId();
                }
            }
        }

        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                if (i == 0) {
                    userTalukaIds = j.getId();
                } else {
                    userTalukaIds = userTalukaIds + "," + j.getId();
                }
            }
        }



        etState = findViewById(R.id.et_state);
        etDistrict = findViewById(R.id.et_district);
        etTaluka = findViewById(R.id.et_taluka);
        etHostVillage = findViewById(R.id.et_host_village);
        structureImg1 = findViewById(R.id.structure_img1);
        structureImg2 = findViewById(R.id.structure_img2);
//        etHostVillagePopulation = findViewById(R.id.et_host_village_population);
//        etCatchmentVillage = findViewById(R.id.et_catchment_village);
//        etCatchmentVillagePopulation = findViewById(R.id.et_catchment_village_population);
        etGatNo = findViewById(R.id.et_gat_no);
        etWaterShedNo = findViewById(R.id.et_water_shed_no);
        etArea = findViewById(R.id.et_area);
        etStructureName = findViewById(R.id.et_structure_name);
        etStructureOwnerDepartment = findViewById(R.id.et_structure_owner_department);
//        etSubStructureOwnerDepartment = findViewById(R.id.et_sub_structure_owner_department);
//        etNotaDetail = findViewById(R.id.et_nota_detail);
        etStructureType = findViewById(R.id.et_structure_type);
       // etStructureWorkType = findViewById(R.id.et_structure_work_type);
        etAdministrativeApprovalNo = findViewById(R.id.et_administrative_approval_no);
        etAdministrativeApprovalDate = findViewById(R.id.et_administrative_approval_date);
        etTechnicalSanctionNo = findViewById(R.id.et_technical_sanction_no);
        etTechnicalSanctionDate = findViewById(R.id.et_technical_sanction_date);
        etIntervention = findViewById(R.id.et_intervention);
        etAdministrativeEstimateAmount = findViewById(R.id.et_administrative_estimate_amount);
//        etApproximateWorkingHours = findViewById(R.id.et_approximate_working_hours);
//        etApproximateDieselConsumptionAmount = findViewById(R.id.et_approximate_diesel_consumption_amount);
//        etApproximateDieselLiters = findViewById(R.id.et_approximate_diesel_liters);
        etSanctionedSiltQuantity = findViewById(R.id.et_approximate_estimate_quantity);
        etPotentialSiltQuantity = findViewById(R.id.et_potential_silt_quantity);
        etRemark = findViewById(R.id.et_remark);
        btSubmit = findViewById(R.id.bt_submit);

//        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
//                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0 ){
//                if(Util.getUserObjectFromPref().getUserLocation().getStateId().size() == 1) {
//                    etState.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
//                    selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
//                    selectedState = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName();
//                } else {
//                    etState.setOnClickListener(this);
//                }
//        } else {
//            //get State
//            presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
//                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                    Constants.JurisdictionLevelName.STATE_LEVEL);
//            etDistrict.setOnClickListener(this);
//        }
//
//        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
//                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
//            if(Util.getUserObjectFromPref().getUserLocation().getStateId().size() == 1) {
//                etDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
//                selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
//                selectedDistrict = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName();
//            } else {
//                districtList.clear();
//
//                List<JurisdictionType> dist = new ArrayList<>();
//                dist.addAll(Util.getUserObjectFromPref().getUserLocation().getDistrictIds());
//
//                for(JurisdictionType obj : dist){
//                    CustomSpinnerObject spinnerObject = new CustomSpinnerObject();
//                    spinnerObject.set_id(obj.getId());
//                    spinnerObject.setName(obj.getName());
//                    spinnerObject.setSelected(false);
//                    districtList.add(spinnerObject);
//                }
//                etDistrict.setOnClickListener(this);
//            }
//        } else {
//            //get District
//            presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
//                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
//            etDistrict.setOnClickListener(this);
//        }
////        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
////                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
////            etTaluka.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
////            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
////            selectedTaluka = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName();
////        } else {
//            etTaluka.setOnClickListener(this);
////        }

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            selectedState = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName();
            etState.setText(selectedState);
            selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            selectedDistrict = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName();
            etDistrict.setText(selectedDistrict);
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            selectedTaluka = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName();
            etTaluka.setText(selectedTaluka);
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        }

        if (isStateFilter) {
            etState.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 1) {
                etState.setOnClickListener(this);
                stateList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                    CustomSpinnerObject customState = new CustomSpinnerObject();
                    customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
                    customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
                    stateList.add(customState);
                }
            }
        }

        if (isDistrictFilter) {
            etDistrict.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 1) {
                etDistrict.setOnClickListener(this);
                districtList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    CustomSpinnerObject customDistrict = new CustomSpinnerObject();
                    customDistrict.set_id(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getId());
                    customDistrict.setName(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getName());
                    districtList.add(customDistrict);
                }
            }
        }

        if (isTalukaFilter) {
            etTaluka.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 1) {
                etTaluka.setOnClickListener(this);
                talukaList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                    talukaList.add(customTaluka);
                }
            }
        }

        etHostVillage.setOnClickListener(this);
        etAdministrativeApprovalDate.setOnClickListener(this);
        etTechnicalSanctionDate.setOnClickListener(this);
        //etCatchmentVillage.setOnClickListener(this);
        etIntervention.setOnClickListener(this);
        etStructureType.setOnClickListener(this);
        //etStructureWorkType.setOnClickListener(this);
        etStructureOwnerDepartment.setOnClickListener(this);
        //etSubStructureOwnerDepartment.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        structureImg1.setOnClickListener(this);
        structureImg2.setOnClickListener(this);


        //get District
//        presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(),
//                Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
        //get Village
        presenter.getLocationData(selectedTalukaId,
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.VILLAGE_LEVEL);


        /*if (masterDataLists != null) {
            for (int i = 0; i < masterDataLists.size(); i++) {
                if (masterDataLists.get(i).getField().equalsIgnoreCase("work_type")) {
                    MasterDataValue data = masterDataLists.get(i).getData().get(0);
                    selectedStructureWorkType = data.getValue();
                    selectedStructureWorkTypeId = data.getId();
                    etStructureWorkType.setText(selectedStructureWorkType);
                }
            }
        }*/
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (gpsTracker.canGetLocation()) {
                    location = gpsTracker.getLocation();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                Util.openCropActivityFreeCrop(this, finalUri, finalUri);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
                    Util.openCropActivityFreeCrop(this, outputUri, finalUri);
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                final Uri resultUri = UCrop.getOutput(data);
                final File imageFile = new File(Objects.requireNonNull(resultUri).getPath());
                final File compressedImageFile = Util.compressFile(imageFile);
                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(compressedImageFile)) {
                        selectedIV.setImageURI(resultUri);
                        Bitmap bitmap = Util.compressImageToBitmap(imageFile);
                        if (isFirstStructureImage) {
                            imageUri1 = resultUri;
                            imageHashmap.put("structure_image_0" , bitmap);
                        }
                        else {
                            imageUri2 = resultUri;
                            imageHashmap.put("structure_image_1" , bitmap);
                        }
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        } else if (requestCode == 100) {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_state:
                CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(this, this,
                        "Select State",
                        stateList,
                        false);
                cdd6.show();
                cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                            "Select District", districtList, false);
                    csdDisttrict.show();
                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        presenter.getLocationData((!TextUtils.isEmpty(selectedStateId)) ? selectedStateId : userStateIds,
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_taluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka", talukaList, false);
                    csdTaluka.show();
                    csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        presenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId)) ? selectedDistrictId : userDistrictIds,
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL);

                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_host_village:
                CustomSpinnerDialogClass csdHostVillage = new CustomSpinnerDialogClass(this, this,
                        "Select Host Village", villageList, false);
                csdHostVillage.show();
                csdHostVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
//            case R.id.et_catchment_village:
//                CustomSpinnerDialogClass csdCatchmentVillage = new CustomSpinnerDialogClass(this, this,
//                        "Select Catchment Village", catchmentVillageList, true);
//                csdCatchmentVillage.show();
//                csdCatchmentVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                break;
            case R.id.et_structure_owner_department:
                structureDepartmentList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureDept"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureDepartmentList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select Structure owner department", structureDepartmentList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
//            case R.id.et_sub_structure_owner_department:
//                structureSubDepartmentList.clear();
//                for (int i = 0; i < masterDataLists.size(); i++) {
//                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureSubDept"))
//                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
//                            CustomSpinnerObject temp = new CustomSpinnerObject();
//                            temp.set_id(obj.getId());
//                            temp.setName(obj.getValue());
//                            temp.setSelected(false);
//                            structureSubDepartmentList.add(temp);
//                        }
//                }
//                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
//                        "Select Sub Structure owner department", structureSubDepartmentList, false);
//                cddCity.show();
//                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//                break;
            case R.id.et_intervention:
                interventionList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("intervention"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setTypeCode(obj.getTypeCode());
                            temp.setSelected(false);
                            interventionList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdIntervention = new CustomSpinnerDialogClass(this, this,
                        "Select Intervention", interventionList, false);
                csdIntervention.show();
                csdIntervention.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_type:
//                structureTypeList.clear();
//                for (int i = 0; i < masterDataLists.size(); i++) {
//                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType"))
//                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
//                            CustomSpinnerObject temp = new CustomSpinnerObject();
//                            temp.set_id(obj.getId());
//                            temp.setName(obj.getValue());
//                            temp.setSelected(false);
//                            structureTypeList.add(temp);
//                        }
//                }
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(this, this,
                        "Select Structure Type", structureTypeList, false);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
           /* case R.id.et_structure_work_type:
                structureWorkTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("work_type"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureWorkTypeList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructerWorkType = new CustomSpinnerDialogClass(this, this,
                        "Select Structure Work Type", structureWorkTypeList, false);
                csdStructerWorkType.show();
                csdStructerWorkType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;*/
            case R.id.et_administrative_approval_date:
                Util.showDateDialog(this, etAdministrativeApprovalDate);
                break;
            case R.id.et_technical_sanction_date:
                Util.showDateDialog(this, etTechnicalSanctionDate);
                break;
            case R.id.structure_img1:
                if (structureId != null && !TextUtils.isEmpty(structureId)) {
                    Snackbar.make(view,"Structure image edit not allowed",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                selectedIV = findViewById(R.id.structure_img1);
                isFirstStructureImage = true;
                onAddImageClick();
                break;
            case R.id.structure_img2:
                if (structureId != null && !TextUtils.isEmpty(structureId)) {
                    Snackbar.make(view,"Structure image edit not allowed",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                selectedIV = findViewById(R.id.structure_img2);
                isFirstStructureImage = false;
                onAddImageClick();
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                   // presenter.submitStructure(structureData);
                    uploadImage(structureData);
                }
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
            Util.showToast(getString(R.string.msg_error_in_photo_gallery), this);
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private File getImageFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.Image.IMAGE_STORAGE_DIRECTORY);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        currentPhotoPath = file.getPath();
        return file;
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedDistrict)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select District.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedTaluka)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Taluka.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedHostVillage)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Host Village.", Snackbar.LENGTH_LONG);
            return false;
        }
//        else if (TextUtils.isEmpty(etHostVillagePopulation.getText().toString())) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please, fill Host Village Population.", Snackbar.LENGTH_LONG);
//            return false;
//        } else if (TextUtils.isEmpty(android.text.TextUtils.join(",", selectedCatchmentVillage))) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please, select Catchment Village.", Snackbar.LENGTH_LONG);
//            return false;
//        } else if (TextUtils.isEmpty(etCatchmentVillagePopulation.getText().toString())) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please, fill Catchment Village Population.", Snackbar.LENGTH_LONG);
//            return false;
//        }
        else if (TextUtils.isEmpty(etGatNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Gat No.", Snackbar.LENGTH_LONG);
            return false;
        } /*else if (TextUtils.isEmpty(etWaterShedNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Water Shed No.", Snackbar.LENGTH_LONG);
            return false;
        }*/
        else if (TextUtils.isEmpty(etArea.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill structure area.", Snackbar.LENGTH_LONG);
            return false;
        }
        else if (TextUtils.isEmpty(etStructureName.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Structure Name.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedStructureOwnerDepartmentId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Structure Owner Department.", Snackbar.LENGTH_LONG);
            return false;
        }
//        else if (TextUtils.isEmpty(selectedSubStructureOwnerDepartmentId)) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please, select Sub-structure Owner DepartmentId.", Snackbar.LENGTH_LONG);
//            return false;
//        }
//        else if (TextUtils.isEmpty(etNotaDetail.getText().toString())) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please, fill Nota Detail.", Snackbar.LENGTH_LONG);
//            return false;
//        }
        else if (TextUtils.isEmpty(selectedStructureTypeId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Structure Type.", Snackbar.LENGTH_LONG);
            return false;
        } /*else if (TextUtils.isEmpty(selectedStructureWorkType)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Structure Work Type.", Snackbar.LENGTH_LONG);
            return false;
        }*/
        /*else if (TextUtils.isEmpty(etAdministrativeApprovalNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Administrative Approval No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etAdministrativeApprovalDate.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Administrative Approval Date.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etTechnicalSanctionNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Technical Sanction No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etAdministrativeEstimateAmount.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Administrative Estimate Amount.", Snackbar.LENGTH_LONG);
            return false;
        }*/
      /*  else if (TextUtils.isEmpty(etApproximateWorkingHours.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Approximate Working Hours.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateDieselConsumptionAmount.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill Approximate Diesel Consumption Amount.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateDieselLiters.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, fill proper information.", Snackbar.LENGTH_LONG);
            return false;
        }*/
       /* else if (TextUtils.isEmpty(etSanctionedSiltQuantity.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, Fill Approximate Estimate Quantity.", Snackbar.LENGTH_LONG);
            return false;
        }*/
        else if (imageHashmap.isEmpty() && structureData.getStructureImage1()==null
                &&  structureData.getStructureImage2()==null) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, upload at least one structure image", Snackbar.LENGTH_LONG);
            return false;
        } else {
            structureData.setStateId(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId());
            structureData.setDistrictId(selectedDistrictId);
            structureData.setDistrict(selectedDistrict);
            structureData.setTalukaId(selectedTalukaId);
            structureData.setTaluka(selectedTaluka);
            structureData.setVillageId(selectedHostVillageId);
            structureData.setVillage(selectedHostVillage);
//            structureData.setVillagePopulation(etHostVillagePopulation.getText().toString());
//            structureData.setCatchmentVillagesIds(android.text.TextUtils.join(",", selectedCatchmentVillageId));
//            structureData.setCatchmentVillages(android.text.TextUtils.join(",", selectedCatchmentVillage));
//            structureData.setTotalPopulation(etCatchmentVillagePopulation.getText().toString());
            structureData.setGatNo(etGatNo.getText().toString());
            structureData.setWaterShedNo(etWaterShedNo.getText().toString());
            structureData.setArea(etArea.getText().toString());
            structureData.setName(etStructureName.getText().toString());
            structureData.setDepartmentId(selectedStructureOwnerDepartmentId);
            structureData.setSubDepartmentId(selectedSubStructureOwnerDepartmentId);
            //structureData.setNotaDetail(etNotaDetail.getText().toString());
            structureData.setStructureType(selectedStructureTypeId);
            //structureData.setWorkType(selectedStructureWorkTypeId);
            structureData.setAdministrativeApprovalNo(etAdministrativeApprovalNo.getText().toString());
            structureData.setAdministrativeApprovalDate(etAdministrativeApprovalDate.getText().toString());
            structureData.setTechnicalSectionNumber(etTechnicalSanctionNo.getText().toString());
            structureData.setTechnicalSectionDate(etTechnicalSanctionDate.getText().toString());
            structureData.setInterventionId(selectedInterventionId);
            structureData.setAdministrativeEstimateAmount(etAdministrativeEstimateAmount.getText().toString());
//            structureData.setApprxWorkingHrs(etApproximateWorkingHours.getText().toString());
//            structureData.setApprxDieselConsumptionRs(etApproximateDieselConsumptionAmount.getText().toString());
//            structureData.setApprxDieselConsumptionLt(etApproximateDieselLiters.getText().toString());
            structureData.setApprxEstimateQunty(etSanctionedSiltQuantity.getText().toString());
            structureData.setPotentialSiltQuantity(etPotentialSiltQuantity.getText().toString());

            //set location
            if (location != null) {
                structureData.setLat(location.getLatitude());
                structureData.setLog(location.getLongitude());
            } else {
                if (gpsTracker.canGetLocation()) {
                    location = gpsTracker.getLocation();
                    if (location != null) {
                        structureData.setLat(location.getLatitude());
                        structureData.setLog(location.getLongitude());
                    }
                } else {
                    Toast.makeText(this, "Not able to get location.", Toast.LENGTH_LONG).show();
                }
            }

            structureData.setFfId(Util.getUserObjectFromPref().getId());
            structureData.setRemark(etRemark.getText().toString());

        }
        return true;
    }

    private void uploadImage(Structure structure) {

        showProgressBar();
        Log.d("request -", new Gson().toJson(structure));
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        hideProgressBar();
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                            Log.d("response -", jsonString);
                            if (commonResponse.getStatus() == 200) {
                                Toast.makeText(CreateStructureActivity.this,
                                        commonResponse.getMessage(), Toast.LENGTH_SHORT).show();                                finish();
                            } else {
                                Toast.makeText(CreateStructureActivity.this,
                                        commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateStructureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                error -> {
                    hideProgressBar();
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(structure));
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
                    Log.d("TAG", "getByteData: "+params);
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


    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select State":
                for (CustomSpinnerObject obj : stateList) {
                    if (obj.isSelected()) {
                        selectedState = obj.getName();
                        selectedStateId = obj.get_id();
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
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";
                districtList.clear();
                break;
            case "Select District":
                for (CustomSpinnerObject obj : districtList) {
                    if (obj.isSelected()) {
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";
                talukaList.clear();
                villageList.clear();
                break;
            case "Select Taluka":
                for (CustomSpinnerObject obj : talukaList) {
                    if (obj.isSelected()) {
                        selectedTaluka = obj.getName();
                        selectedTalukaId = obj.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";
                villageList.clear();
                //get Taluka
                if (!TextUtils.isEmpty(selectedTalukaId)) {
                    presenter.getLocationData(selectedTalukaId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                }

                break;
            case "Select Host Village":
                for (CustomSpinnerObject obj : villageList) {
                    if (obj.isSelected()) {
                        selectedHostVillage = obj.getName();
                        selectedHostVillageId = obj.get_id();
                        break;
                    }
                }
                etHostVillage.setText(selectedHostVillage);
                break;
//            case "Select Catchment Village":
//                selectedCatchmentVillage.clear();
//                selectedCatchmentVillageId.clear();
//                for (CustomSpinnerObject obj : catchmentVillageList) {
//                    if (obj.isSelected()) {
//                        selectedCatchmentVillage.add(obj.getName());
//                        selectedCatchmentVillageId.add(obj.get_id());
//                    }
//                }
//                etCatchmentVillage.setText(android.text.TextUtils.join(",", selectedCatchmentVillage));
//                break;
            case "Select Structure owner department":
                for (CustomSpinnerObject obj : structureDepartmentList) {
                    if (obj.isSelected()) {
                        selectedStructureOwnerDepartment = obj.getName();
                        selectedStructureOwnerDepartmentId = obj.get_id();
                    }
                }
                etStructureOwnerDepartment.setText(selectedStructureOwnerDepartment);
                break;
//            case "Select Sub Structure owner department":
//                for (CustomSpinnerObject obj : structureSubDepartmentList) {
//                    if (obj.isSelected()) {
//                        selectedSubStructureOwnerDepartment = obj.getName();
//                        selectedSubStructureOwnerDepartmentId = obj.get_id();
//                    }
//                }
//                etSubStructureOwnerDepartment.setText(selectedSubStructureOwnerDepartment);
//                break;
            case "Select Intervention":
                int selectedType = -1;
                for (CustomSpinnerObject obj : interventionList) {
                    if (obj.isSelected()) {
                        selectedIntervention = obj.getName();
                        selectedInterventionId = obj.get_id();
                        selectedType = obj.getTypeCode();
                    }
                }
                selectedStructureType = "";
                selectedStructureTypeId = "";
                etStructureType.setText("");
                structureTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType")
                            && masterDataLists.get(i).getStructureTypeCode() == selectedType) {
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureTypeList.add(temp);
                        }
                        break;
                    }

                }
                etIntervention.setText(selectedIntervention);
                break;
            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureType = obj.getName();
                        selectedStructureTypeId = obj.get_id();
                    }
                }
                etStructureType.setText(selectedStructureType);
                break;
            /*case "Select Structure Work Type":
                for (CustomSpinnerObject obj : structureWorkTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureWorkType = obj.getName();
                        selectedStructureWorkTypeId = obj.get_id();
                    }
                }
                etStructureWorkType.setText(selectedStructureWorkType);
                break;*/
        }
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

    public void setMasterData() {

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("SS");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            if (obj.getForm().equalsIgnoreCase("structure_create")) {
                masterDataLists.add(obj);
            }
        }
    }

    public void logOutUser() {
        // remove user related shared pref data

        Util.saveLoginObjectInPref("");

        try {
            Intent startMain = new Intent(this, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> data, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (data != null && !data.isEmpty()) {
                    districtList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < data.size(); i++) {
//                        if (Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId()
//                                .equalsIgnoreCase(data.get(i).getStateId())) {
                        JurisdictionLocationV3 location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        districtList.add(meetCountry);
                        //    }
                    }
                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                            "Select District", districtList, false);
                    csdDisttrict.show();
                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (data != null && !data.isEmpty()) {
                    talukaList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                        JurisdictionLocationV3 location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        talukaList.add(meetCountry);
                        //}
                    }
                    CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka", talukaList, false);
                    csdTaluka.show();
                    csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    villageList.clear();
                    //catchmentVillageList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedTaluka.equalsIgnoreCase(data.get(i).getTaluka().getName())) {

                        JurisdictionLocationV3 location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        villageList.add(meetCountry);
                        //catchmentVillageList.add(meetCountry);
                        //}
                    }
                }
                break;
        }
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
