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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.ssgp.StructureWorkType;
import com.octopusbjsindia.models.ssgp.VDFFRequest;
import com.octopusbjsindia.presenter.ssgp.VDFFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.octopusbjsindia.utility.Util.getImageName;

public class VDFFormFragment extends Fragment implements APIDataListener, CustomSpinnerListener,
        View.OnClickListener {
    private static final String TAG = VDFFormFragment.class.getCanonicalName();
    private View vdfFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    private String selectedImageType = "";
    public static final String IMAGE_DEMAND_LETTER1 = "imageofdemandletter1";
    public static final String IMAGE_DEMAND_LETTER2 = "imageofdemandletter2";
    public static final String IMAGE_DEMAND_LETTER3 = "imageofdemandletter3";
    public static final String IMAGE_GP_BOARD = "imageofgpboard";

    private Uri outputUri;
    private Uri finalUri;
    private ImageView iv_gpboard_photo, iv_demandletter_photo1,iv_demandletter_photo2,iv_demandletter_photo3;
    private String UrlDemandLetterPhoto1,UrlDemandLetterPhoto2,UrlDemandLetterPhoto3, UrlGPBoardPhoto = "", startWork = "Yes";

    private EditText etState, etDistrict, etTaluka, etVillage, etStructureType1, etStructCount1, etStructureType2,
            etStructCount2, etStructureType3, etStructCount3, etStructureType4, etStructCount4,
            etStructureType5, etStructCount5, etHours, etBackhoeCount, etExcavatorsCount, etNodalName,
            etNodalContact, etMachineTransport, etReason, etFutureWorkTime,
            etWorkableStructCount, etRemark, selectedEt;
    private Button btnSubmit;

    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> transportAgreeOptionsList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> workImmediateOptionsList = new ArrayList<>();
    private VDFFormFragmentPresenter presenter;
    private String selectedStateId, selectedStructureType1, selectedStructureTypeId1,
            selectedStructureType2, selectedStructureTypeId2, selectedStructureType3, selectedStructureTypeId3,
            selectedStructureType4, selectedStructureTypeId4, selectedStructureType5, selectedStructureTypeId5;
    private String userDistrictIds = "";
    private String userTalukaIds = "";
    private String userVillageIds = "";
    private boolean isStateFilter, isDistrictFilter, isTalukaFilter, isVillageFilter;
    private String selectedDistrictId = "", selectedTalukaId, selectedVillageId, selectedTaluka, selectedVillage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vdfFormFragmentView = inflater.inflate(R.layout.fragment_v_d_f_form, container, false);
        return vdfFormFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        ((GPActionsActivity) getActivity()).setTitle("VDF Form");
        progressBarLayout = vdfFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdfFormFragmentView.findViewById(R.id.pb_profile_act);
        presenter = new VDFFormFragmentPresenter(this);
        etState = vdfFormFragmentView.findViewById(R.id.et_state);
        etDistrict = vdfFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdfFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdfFormFragmentView.findViewById(R.id.et_village);
        etState.setFocusable(false);
        etDistrict.setFocusable(false);
        etTaluka.setFocusable(false);
        etStructureType1 = vdfFormFragmentView.findViewById(R.id.et_structure_type1);
        etStructCount1 = vdfFormFragmentView.findViewById(R.id.et_struct_count1);
        etStructureType2 = vdfFormFragmentView.findViewById(R.id.et_structure_type2);
        etStructCount2 = vdfFormFragmentView.findViewById(R.id.et_struct_count2);
        etStructureType3 = vdfFormFragmentView.findViewById(R.id.et_structure_type3);
        etStructCount3 = vdfFormFragmentView.findViewById(R.id.et_struct_count3);
        etStructureType4 = vdfFormFragmentView.findViewById(R.id.et_structure_type4);
        etStructCount4 = vdfFormFragmentView.findViewById(R.id.et_struct_count4);
        etStructureType5 = vdfFormFragmentView.findViewById(R.id.et_structure_type5);
        etStructCount5 = vdfFormFragmentView.findViewById(R.id.et_struct_count5);
        etHours = vdfFormFragmentView.findViewById(R.id.et_hours);
        etBackhoeCount = vdfFormFragmentView.findViewById(R.id.etBackhoeCount);
        etExcavatorsCount = vdfFormFragmentView.findViewById(R.id.etExcavatorsCount);
        etNodalName = vdfFormFragmentView.findViewById(R.id.et_nodal_name);
        etNodalContact = vdfFormFragmentView.findViewById(R.id.et_nodal_contact);
        etMachineTransport = vdfFormFragmentView.findViewById(R.id.et_machine_transport);
        etReason = vdfFormFragmentView.findViewById(R.id.et_reason);
        etFutureWorkTime = vdfFormFragmentView.findViewById(R.id.et_future_work_time);
        etWorkableStructCount = vdfFormFragmentView.findViewById(R.id.et_workable_struct_count);
//        etRemark = vdfFormFragmentView.findViewById(R.id.et_remark);
        iv_demandletter_photo1 = vdfFormFragmentView.findViewById(R.id.iv_demandletter_photo1);
        iv_demandletter_photo2 = vdfFormFragmentView.findViewById(R.id.iv_demandletter_photo2);
        iv_demandletter_photo3 = vdfFormFragmentView.findViewById(R.id.iv_demandletter_photo3);
        iv_gpboard_photo = vdfFormFragmentView.findViewById(R.id.iv_gpboard_photo);
        CheckBox cbBackhoe = vdfFormFragmentView.findViewById(R.id.cbBackhoe);
        cbBackhoe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vdfFormFragmentView.findViewById(R.id.etBackhoeCount).setEnabled(true);
                } else {
                    vdfFormFragmentView.findViewById(R.id.etBackhoeCount).setEnabled(false);
                    etBackhoeCount.setText("");
                }
            }
        });

        CheckBox cbExcavators = vdfFormFragmentView.findViewById(R.id.cbExcavators);
        cbExcavators.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vdfFormFragmentView.findViewById(R.id.etExcavatorsCount).setEnabled(true);
                } else {
                    vdfFormFragmentView.findViewById(R.id.etExcavatorsCount).setEnabled(false);
                    etExcavatorsCount.setText("");
                }
            }
        });
        RadioGroup rb = vdfFormFragmentView.findViewById(R.id.rgStartWork);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbYes:
                        startWork = "Yes";
                        vdfFormFragmentView.findViewById(R.id.tlyReason).setVisibility(View.GONE);
                        vdfFormFragmentView.findViewById(R.id.tly_future_work_time).setVisibility(View.GONE);
                        vdfFormFragmentView.findViewById(R.id.tly_workable_struct_count).setVisibility(View.GONE);
                        break;
                    case R.id.rbNo:
                        startWork = "No";
                        vdfFormFragmentView.findViewById(R.id.tlyReason).setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbPartially:
                        startWork = "Partially";
                        vdfFormFragmentView.findViewById(R.id.tly_future_work_time).setVisibility(View.VISIBLE);
                        vdfFormFragmentView.findViewById(R.id.tly_workable_struct_count).setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        etStructureType1.setOnClickListener(this);
        etStructureType1.setFocusable(false);
        etStructureType2.setOnClickListener(this);
        etStructureType2.setFocusable(false);
        etStructureType3.setOnClickListener(this);
        etStructureType3.setFocusable(false);
        etStructureType4.setOnClickListener(this);
        etStructureType4.setFocusable(false);
        etStructureType5.setOnClickListener(this);
        etStructureType5.setFocusable(false);
        vdfFormFragmentView.findViewById(R.id.btn_submit).setOnClickListener(this);
        iv_demandletter_photo1.setOnClickListener(this);
        iv_demandletter_photo2.setOnClickListener(this);
        iv_demandletter_photo3.setOnClickListener(this);
        iv_gpboard_photo.setOnClickListener(this);

//        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
//        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
//        if (roleAccessList != null) {
//            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
//            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
//                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
//                    isStateFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
//                    isDistrictFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
//                    isTalukaFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VILLAGE)) {
//                    isVillageFilter = true;
//                    continue;
//                }
//            }
//        }

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            etState.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
            selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
        } else {
            etState.setOnClickListener(this);
        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            etDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
        } else {
            etDistrict.setOnClickListener(this);
        }

        etTaluka.setOnClickListener(this);
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                CustomSpinnerObject customState = new CustomSpinnerObject();
                customState.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                customState.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                talukaList.add(customState);
            }
        }

        etVillage.setOnClickListener(this);
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                CustomSpinnerObject customState = new CustomSpinnerObject();
                customState.set_id(Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i).getId());
                customState.setName(Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i).getName());
                villageList.add(customState);
            }
        }

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("GP");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (int i = 0; i < masterDataList.size(); i++) {
            if (masterDataList.get(i).getField().equals("structureType")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    structureTypeList.add(customSpinnerObject);
                }
            }
        }

        if (!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_demandletter_photo1:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_DEMAND_LETTER1;
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.iv_demandletter_photo2:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_DEMAND_LETTER2;
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.iv_demandletter_photo3:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_DEMAND_LETTER3;
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.iv_gpboard_photo:
                if (Util.isConnected(getActivity())) {
                    onAddImageClick();
                    selectedImageType = IMAGE_GP_BOARD;
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }
                break;
            case R.id.et_state:
                CustomSpinnerDialogClass cdd0 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State",
                        stateList,
                        false);
                cdd0.show();
                cdd0.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select District",
                            districtList,
                            false);
                    cdd6.show();
                    cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etState.getText() != null && etState.getText().toString().length() > 0) {
                            presenter.getLocationData(selectedStateId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
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
                            presenter.getLocationData(selectedDistrictId,
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
                                            ? selectedTalukaId : userTalukaIds,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_structure_type1:
                selectedEt = etStructureType1;
                selectStructureType();
                break;
            case R.id.et_structure_type2:
                selectedEt = etStructureType2;
                selectStructureType();
                break;
            case R.id.et_structure_type3:
                selectedEt = etStructureType3;
                selectStructureType();
                break;
            case R.id.et_structure_type4:
                selectedEt = etStructureType4;
                selectStructureType();
                break;
            case R.id.et_structure_type5:
                selectedEt = etStructureType5;
                selectStructureType();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(selectedDistrictId)) {
                    Util.showToast(getActivity(), "Please selected district");
                } else if (TextUtils.isEmpty(selectedTalukaId)) {
                    Util.showToast(getActivity(), "Please selected taluka");
                } else if (TextUtils.isEmpty(selectedVillageId)) {
                    Util.showToast(getActivity(), "Please selected village");
                } else {
                    VDFFRequest request = new VDFFRequest();
                    request.setStateId(selectedStateId);
                    request.setDistrictId(selectedDistrictId);
                    request.setTalukaId(selectedTalukaId);
                    request.setVillageId(selectedVillageId);

                    List<StructureWorkType> structureWorkTypeList = new ArrayList<StructureWorkType>();
                    StructureWorkType structure1 = new StructureWorkType();
                    structure1.setStructureType(etStructureType1.getText().toString().trim());
                    structure1.setNumberStructureType(etStructCount1.getText().toString().trim());
                    structureWorkTypeList.add(structure1);

                    StructureWorkType structure2 = new StructureWorkType();
                    structure2.setStructureType(etStructureType2.getText().toString().trim());
                    structure2.setNumberStructureType(etStructCount2.getText().toString().trim());
                    structureWorkTypeList.add(structure2);

                    StructureWorkType structure3 = new StructureWorkType();
                    structure3.setStructureType(etStructureType3.getText().toString().trim());
                    structure3.setNumberStructureType(etStructCount3.getText().toString().trim());
                    structureWorkTypeList.add(structure3);

                    StructureWorkType structure4 = new StructureWorkType();
                    structure4.setStructureType(etStructureType4.getText().toString().trim());
                    structure4.setNumberStructureType(etStructCount4.getText().toString().trim());
                    structureWorkTypeList.add(structure4);

                    StructureWorkType structure5 = new StructureWorkType();
                    structure5.setStructureType(etStructureType5.getText().toString().trim());
                    structure5.setNumberStructureType(etStructCount5.getText().toString().trim());
                    structureWorkTypeList.add(structure5);

                    ArrayList<String> demandLetterList = new ArrayList<String>();
                    if(!TextUtils.isEmpty(UrlDemandLetterPhoto1)){
                        demandLetterList.add(UrlDemandLetterPhoto1);
                    }
                    if(!TextUtils.isEmpty(UrlDemandLetterPhoto2)){
                        demandLetterList.add(UrlDemandLetterPhoto2);
                    }
                    if(!TextUtils.isEmpty(UrlDemandLetterPhoto3)){
                        demandLetterList.add(UrlDemandLetterPhoto3);
                    }
                    request.setDemandLetterImage(demandLetterList);
                    request.setGpImage(UrlGPBoardPhoto);
                    request.setTypeNWorkStructure(structureWorkTypeList);
                    request.setMachineDemandHr(etHours.getText().toString().trim());
                    request.setBackhoeCount(etBackhoeCount.getText().toString().trim());
                    request.setExcavatorsCount(etExcavatorsCount.getText().toString().trim());
                    request.setNodalPersonName(etNodalName.getText().toString().trim());
                    request.setNodalPersonNumber(etNodalContact.getText().toString().trim());
                    request.setMachineTransportation(etMachineTransport.getText().toString().trim());
                    request.setIsStartWorkImmediately(startWork);
                    request.setReasonNotStart(etReason.getText().toString().trim());
                    request.setFutureDate(etFutureWorkTime.getText().toString().trim());
                    request.setNoStructureWork(etWorkableStructCount.getText().toString().trim());
                    request.setComment(etRemark.getText().toString().trim());
                    presenter.submitVDFF(request);
                }
                break;
        }
    }

    private void selectStructureType() {
        CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(getActivity(), this,
                "Select Structure Type", structureTypeList, false);
        csdStructerType.show();
        csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(getActivity(), message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String message) {
        getActivity().finish();
        Util.showToast(getActivity(), message);
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

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if (requestId.equals(VDFFormFragmentPresenter.SUBMIT_VDF_FORM)) {
            if (status == 200) {
                getActivity().finish();
//                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
//                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
//                intent.putExtra("viewType", 2);
//                intent.putExtra("title", "Machine List");
//                getActivity().startActivity(intent);
            }
        }
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
            case "Select District":
                for (CustomSpinnerObject state : districtList) {
                    if (state.isSelected()) {
                        etDistrict.setText(state.getName());
                        selectedDistrictId = state.get_id();
                        break;
                    }
                }
                etTaluka.setText("");
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillageId = "";
                break;
            case "Select Taluka":
                for (CustomSpinnerObject state : talukaList) {
                    if (state.isSelected()) {
                        etTaluka.setText(state.getName());
                        selectedTalukaId = state.get_id();
                        break;
                    }
                }
                etVillage.setText("");
                selectedVillageId = "";
                break;
            case "Select Village":
                for (CustomSpinnerObject state : villageList) {
                    if (state.isSelected()) {
                        etVillage.setText(state.getName());
                        selectedVillageId = state.get_id();
                        break;
                    }
                }

                break;
            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        switch (selectedEt.getId()) {
                            case R.id.et_structure_type1:
                                selectedStructureType1 = obj.getName();
                                selectedStructureTypeId1 = obj.get_id();
                                selectedEt.setText(selectedStructureType1);
                                break;
                            case R.id.et_structure_type2:
                                selectedStructureType2 = obj.getName();
                                selectedStructureTypeId2 = obj.get_id();
                                selectedEt.setText(selectedStructureType2);
                                break;
                            case R.id.et_structure_type3:
                                selectedStructureType3 = obj.getName();
                                selectedStructureTypeId3 = obj.get_id();
                                selectedEt.setText(selectedStructureType3);
                                break;
                            case R.id.et_structure_type4:
                                selectedStructureType4 = obj.getName();
                                selectedStructureTypeId4 = obj.get_id();
                                selectedEt.setText(selectedStructureType4);
                                break;
                            case R.id.et_structure_type5:
                                selectedStructureType5 = obj.getName();
                                selectedStructureTypeId5 = obj.get_id();
                                selectedEt.setText(selectedStructureType5);
                                break;
                        }

                        break;
                    }
                }
                break;
        }
    }

    //image from camera and gallery
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
        if (imagetype.equalsIgnoreCase(IMAGE_DEMAND_LETTER1)) {
            UrlDemandLetterPhoto1 = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_demandletter_photo1);
                }
            });

        } else if (imagetype.equalsIgnoreCase(IMAGE_DEMAND_LETTER2)) {
            UrlDemandLetterPhoto2 = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_demandletter_photo2);
                }
            });

        } else if (imagetype.equalsIgnoreCase(IMAGE_DEMAND_LETTER3)) {
            UrlDemandLetterPhoto3 = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_demandletter_photo3);
                }
            });

        } else if (imagetype.equalsIgnoreCase(IMAGE_GP_BOARD)) {
            UrlGPBoardPhoto = imageUrl;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity())
                            //.applyDefaultRequestOptions(requestOptions)
                            .load(imageUrl)
                            .into(iv_gpboard_photo);
                }
            });
        }
    }
}