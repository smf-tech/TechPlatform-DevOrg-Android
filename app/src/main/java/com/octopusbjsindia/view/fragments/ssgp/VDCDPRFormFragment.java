package com.octopusbjsindia.view.fragments.ssgp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.ssgp.GpStructureListModel;
import com.octopusbjsindia.models.ssgp.VdcDprRequestModel;
import com.octopusbjsindia.presenter.ssgp.VDCCMFormFragmentPresenter;
import com.octopusbjsindia.presenter.ssgp.VDCDPRFormFragmentPresenter;
import com.octopusbjsindia.presenter.ssgp.VDFFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.octopusbjsindia.utility.Util.getImageName;

public class VDCDPRFormFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomSpinnerListener {

    private static final String TAG = VDCDPRFormFragment.class.getCanonicalName();
    private View vdcdprFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private String selectedImageType ="";
    public static final String IMAGE_START_READING = "startMeterReading";
    public static final String IMAGE_END_READING = "endMeterReading";
    public static final String IMAGE_STRUCTURE_IMAGE = "structureImage";
    private Uri outputUri;
    private Uri finalUri;

    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineStatusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureStatusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineHaltReasonList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject>  gpMachineLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject>  gpStructureLists = new ArrayList<>();

    String selectedStructureTypeId, selectedStructureType, selectedIntervention, selectedInterventionId,
            selectedState = "", selectedStateId = "", selectedDistrict = "", selectedDistrictId = "",
            selectedTaluka = "", selectedTalukaId = "", selectedVillage = "", selectedVillageId = "",
            selectedBeneficiaryType, selectedBeneficiaryTypeId,selectedMachineStatus, selectedMachineStatusId,selectedStructureStatus, selectedStructureStatusId,
    selectedMachinecodename,selectedMachineId,selectedStructureCodename,selectedStructureId,selectedMachinehaltreason,selectedMachinehaltreasonId;


    private VDCDPRFormFragmentPresenter presenter;
    private EditText etState,etDistrict, etTaluka, etVillage,et_remark,
            et_date,et_machine_code,et_machine_status,et_start_meter_reading,et_end_meter_reading,et_reason,et_struct_code,et_structure_status;
    private ImageView iv_start_meter,iv_end_meter,iv_structure_photo;
    private String UrlStartMeterPhoto,UrlEndMeterPhoto,UrlStructurePhoto;
    private Button btn_submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vdcdprFormFragmentView = inflater.inflate(R.layout.fragment_v_d_c_dpr_form, container, false);
        return vdcdprFormFragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        setMasterData();

        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
            CustomSpinnerObject customState = new CustomSpinnerObject();
            customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
            customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
            stateList.add(customState);
        }
        ((GPActionsActivity) getActivity()).setTitle("VDC DPR Form");
        progressBarLayout = vdcdprFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdcdprFormFragmentView.findViewById(R.id.pb_profile_act);
        presenter = new VDCDPRFormFragmentPresenter(this);
        etState = vdcdprFormFragmentView.findViewById(R.id.et_state);
        etDistrict = vdcdprFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdcdprFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdcdprFormFragmentView.findViewById(R.id.et_village);
        et_date  = vdcdprFormFragmentView.findViewById(R.id.et_date);
        et_machine_code = vdcdprFormFragmentView.findViewById(R.id.et_machine_code);
        et_machine_status = vdcdprFormFragmentView.findViewById(R.id.et_machine_status);
        et_start_meter_reading = vdcdprFormFragmentView.findViewById(R.id.et_start_meter_reading);
        et_end_meter_reading = vdcdprFormFragmentView.findViewById(R.id.et_end_meter_reading);
        et_reason = vdcdprFormFragmentView.findViewById(R.id.et_reason);
        et_struct_code = vdcdprFormFragmentView.findViewById(R.id.et_struct_code);
        et_structure_status = vdcdprFormFragmentView.findViewById(R.id.et_structure_status);
        et_remark = vdcdprFormFragmentView.findViewById(R.id.et_remark);

        et_start_meter_reading  = vdcdprFormFragmentView.findViewById(R.id.et_start_meter_reading);
        et_end_meter_reading  = vdcdprFormFragmentView.findViewById(R.id.et_end_meter_reading);
        iv_start_meter = vdcdprFormFragmentView.findViewById(R.id.iv_start_meter);
        iv_end_meter = vdcdprFormFragmentView.findViewById(R.id.iv_end_meter);
        iv_structure_photo = vdcdprFormFragmentView.findViewById(R.id.iv_structure_photo);
        btn_submit = vdcdprFormFragmentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        etState.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);
        et_date.setOnClickListener(this);
        et_machine_code.setOnClickListener(this);
        et_machine_status.setOnClickListener(this);
        et_reason.setOnClickListener(this);
        iv_start_meter.setOnClickListener(this);
        iv_end_meter.setOnClickListener(this);
        iv_structure_photo.setOnClickListener(this);
        et_structure_status.setOnClickListener(this);
        et_struct_code.setOnClickListener(this);

        presenter.GetGpMachineList();
        presenter.GetGpStrucureList();

    }

    //set master data list
    public void setMasterData() {

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("GP");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            /*if (obj.getForm().equalsIgnoreCase("structure_create") ||
                    obj.getForm().equalsIgnoreCase("structure_preparation"))*/
            {
                masterDataLists.add(obj);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_struct_code:
                CustomSpinnerDialogClass csdStructerCode = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Structure",
                        gpStructureLists,
                        false);
                csdStructerCode.show();
                csdStructerCode.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_code:
                CustomSpinnerDialogClass csdMachineCode = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Machine",
                        gpMachineLists,
                        false);
                csdMachineCode.show();
                csdMachineCode.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_status:
                machineStatusList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("machineStatus"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            machineStatusList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Machine Status", machineStatusList, false);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_status:
                structureStatusList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureStatus"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureStatusList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructerStatus = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Structure Status", structureStatusList, false);
                csdStructerStatus.show();
                csdStructerStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;

            case R.id.et_reason:
                machineHaltReasonList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("machineHaltedReason"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            machineHaltReasonList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdMachineIdleReason = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Machine halt reason", machineHaltReasonList, false);
                csdMachineIdleReason.show();
                csdMachineIdleReason.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_state:
                CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State",
                        stateList,
                        false);
                cdd6.show();
                cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select District",
                            districtList,
                            false);
                    cdd7.show();
                    cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etState.getText() != null && etState.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
                                            ? selectedStateId : selectedStateId, Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_taluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select Taluka", talukaList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etDistrict.getText() != null && etDistrict.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                            ? selectedDistrictId : selectedDistrictId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_village:
                if (villageList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select Village", villageList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etTaluka.getText() != null && etTaluka.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedTalukaId))
                                            ? selectedTalukaId : selectedTalukaId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_date:
                Util.showDateDialog(getActivity(), et_date);
                break;
            case R.id.iv_start_meter:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_START_READING;
                } else {
                    Util.showToast(getActivity(),getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.iv_end_meter:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_END_READING;
                } else {
                    Util.showToast(getActivity(),getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.iv_structure_photo:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_STRUCTURE_IMAGE;
                } else {
                    Util.showToast(getActivity(),getResources().getString(R.string.msg_no_network));
                }
                break;

            case R.id.btn_submit:
                if (isAllInputsValid())
                {
                    //Util.showToast(getActivity(),"data is valid call API here");
                    VdcDprRequestModel vdcDprRequestModel = new VdcDprRequestModel();
                    vdcDprRequestModel.setStateId(selectedStateId);
                    vdcDprRequestModel.setDistrictId(selectedDistrictId);
                    vdcDprRequestModel.setTalukaId(selectedTalukaId);
                    vdcDprRequestModel.setVillageId(selectedVillageId);
                    vdcDprRequestModel.setReportDate(Util.getDateInepoch(et_date.getText().toString()));
                    vdcDprRequestModel.setMachineId(selectedMachineId);
                    vdcDprRequestModel.setMachineStatus(selectedMachineStatusId);
                    vdcDprRequestModel.setStructureStatus(selectedStructureStatusId);
                    vdcDprRequestModel.setStartMeterReading(et_start_meter_reading.getText().toString());
                    vdcDprRequestModel.setStartMeterReadingImage(UrlStartMeterPhoto);
                    vdcDprRequestModel.setEndMeterReading(et_end_meter_reading.getText().toString());
                    vdcDprRequestModel.setEndMeterReadingImage(UrlEndMeterPhoto);
                    vdcDprRequestModel.setStructureId(selectedStructureId);
                    vdcDprRequestModel.setStructureImage(UrlStructurePhoto);
                    vdcDprRequestModel.setComment(et_remark.getText().toString());

                    presenter.submitDPR(vdcDprRequestModel);
                }
                break;
        }
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }
    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            Util.showToast(getString(R.string.msg_image_capture_not_support), this);
        } catch (SecurityException e) {
            Util.showToast(getString(R.string.msg_take_photo_error), this);
        }
    }
    private boolean isAllInputsValid() {
        String msg = "";

        if (etState.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_state);
        } else if (etDistrict.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_district);
        } else if (etTaluka.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_select_taluka);
        } else if (et_machine_code.getText().toString().trim().length() == 0) {
            msg =getString(R.string.msg_select_struct_code);
        } else if (et_machine_status.getText().toString().trim().length() == 0) {
            msg = getString(R.string.select_stuct_type);
        } else if (et_struct_code.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (UrlStartMeterPhoto.trim().length() == 0) {
            msg = "Please upload start meter reading photo";
        } else if (UrlEndMeterPhoto.trim().length() == 0) {
            msg = "Please upload end meter reading photo";
        } else if (UrlStructurePhoto.trim().length() == 0) {
            msg = "Please upload structure photo";
        } else if (et_remark.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_enter_remark);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
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
                CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District",
                        districtList,
                        false);
                cdd7.show();
                cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    talukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        this.talukaList.add(talukaList);
                    }
                }
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Taluka", talukaList, false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    villageList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject villageList = new CustomSpinnerObject();
                        villageList.set_id(location.getId());
                        villageList.setName(location.getName());
                        villageList.setSelected(false);
                        this.villageList.add(villageList);
                    }
                }
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Village", villageList, false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
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
                break;
            case "Select District":
                for (CustomSpinnerObject state : districtList) {
                    if (state.isSelected()) {
                        selectedDistrict = state.getName();
                        selectedDistrictId = state.get_id();
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
                for (CustomSpinnerObject state : talukaList) {
                    if (state.isSelected()) {
                        selectedTaluka = state.getName();
                        selectedTalukaId = state.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select Village":
                for (CustomSpinnerObject state : villageList) {
                    if (state.isSelected()) {
                        selectedVillage = state.getName();
                        selectedVillageId = state.get_id();
                        break;
                    }
                }
                etVillage.setText(selectedVillage);
                break;

            case "Select Machine Status":
                for (CustomSpinnerObject obj : machineStatusList) {
                    if (obj.isSelected()) {
                        selectedMachineStatus = obj.getName();
                        selectedMachineStatusId = obj.get_id();
                    }
                }
                et_machine_status.setText(selectedMachineStatus);
                break;
            case "Select Structure Status":
                for (CustomSpinnerObject obj : structureStatusList) {
                    if (obj.isSelected()) {
                         selectedStructureStatus = obj.getName();
                        selectedStructureStatusId = obj.get_id();
                    }
                }
                et_structure_status.setText(selectedStructureStatus);
                break;


            case "Select Machine":
                for (CustomSpinnerObject obj : gpMachineLists) {
                    if (obj.isSelected()) {
                        selectedMachinecodename = obj.getName();
                        selectedMachineId = obj.get_id();
                    }
                }
                et_machine_code.setText(selectedMachinecodename);
                break;
            case "Select Structure":
                for (CustomSpinnerObject obj : gpStructureLists) {
                    if (obj.isSelected()) {
                        selectedStructureCodename = obj.getName();
                        selectedStructureId = obj.get_id();
                    }
                }
                et_struct_code.setText(selectedStructureCodename);
                break;
            case "Select Machine halt reason":
                for (CustomSpinnerObject obj : machineHaltReasonList) {
                    if (obj.isSelected()) {
                        selectedMachinehaltreason = obj.getName();
                        selectedMachinehaltreasonId = obj.get_id();
                    }
                }
                et_reason.setText(selectedMachinehaltreason);
                break;




        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) {
                    return;
                }
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) {
                        return;
                    }

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                final File compressedImageFile = Util.compressFile(imageFile);
                if (Util.isConnected(getContext())) {
                    if (Util.isValidImageSize(compressedImageFile)) {
                        uploadImage(compressedImageFile,
                                Constants.Image.IMAGE_TYPE_FILE, selectedImageType);
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


    //upload images
    public void uploadImage(File file, String type, final String formName) {
        presenter.uploadImage(file,
                Constants.Image.IMAGE_TYPE_FILE, formName);
    }

    public void onImageUploaded(String imagetype, String imageUrl) {
        if (imagetype.equalsIgnoreCase(IMAGE_START_READING)){
            UrlStartMeterPhoto = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_start_meter);
                }
            });

        }else if (imagetype.equalsIgnoreCase(IMAGE_END_READING)){
            UrlEndMeterPhoto = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_end_meter);
                }
            });
        }else  if (imagetype.equalsIgnoreCase(IMAGE_STRUCTURE_IMAGE)){
            UrlStructurePhoto = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_structure_photo);
                }
            });
        }
    }


    public void setMachinelist(String response) {
        Util.logger("GPMachineList",response);
        GpStructureListModel gpStructureListModel = new Gson().fromJson(response,GpStructureListModel.class);
        if (gpStructureListModel!=null && gpStructureListModel.getGpStructureList().size()>0){

            for (int i = 0; i <gpStructureListModel.getGpStructureList().size(); i++) {
                CustomSpinnerObject customState = new CustomSpinnerObject();
                customState.set_id(gpStructureListModel.getGpStructureList().get(i).getId());
                customState.setName(gpStructureListModel.getGpStructureList().get(i).getCode());
                gpMachineLists.add(customState);
            }

        }else {
            Util.showToast(getActivity(),"structure not available." );
        }
    }

    public void setStructurelist(String response) {
        Util.logger("GPStructureList",response);
        GpStructureListModel gpStructureListModel = new Gson().fromJson(response,GpStructureListModel.class);
        if (gpStructureListModel!=null && gpStructureListModel.getGpStructureList().size()>0){

            for (int i = 0; i <gpStructureListModel.getGpStructureList().size(); i++) {
                CustomSpinnerObject customState = new CustomSpinnerObject();
                customState.set_id(gpStructureListModel.getGpStructureList().get(i).getId());
                customState.setName(gpStructureListModel.getGpStructureList().get(i).getCode());
                gpStructureLists.add(customState);
            }

        }else {
            Util.showToast(getActivity(),"structure not available." );
        }
    }

    public void showResponse(String message, String requestId, int code) {
        Util.showToast(getActivity(),message);

        if (requestId.equals(VDCDPRFormFragmentPresenter.DAILY_PROGRESS_REPORT)) {
            if (code == 200) {
                getActivity().finish();
            }
        }
    }
}
