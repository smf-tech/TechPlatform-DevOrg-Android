package com.octopusbjsindia.view.fragments.formComponents;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.LocationFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomSpinnerListener {

    View view;
    RelativeLayout progressBar;
    LocationFragmentPresenter presenter;
    Elements element;
    ArrayList<String> jurisdictions = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();


    String selectedCountry = "", selectedCountryId = "", selectedState = "", selectedStateId = "",
            selectedDistrict = "", selectedDistrictId = "",selectedCity = "", selectedCityId = "",
            selectedTaluka = "", selectedTalukaId = "", selectedCluster = "", selectedClusterId = "",
            selectedVillage = "", selectedVillageId = "", selectedSchool = "", selectedSchoolId = "";
    ArrayList<CustomSpinnerObject> countryList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> cityList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> clusterList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> schoolList = new ArrayList<>();

    boolean isOffline = false;
    boolean isFirstpage =false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);
        presenter = new LocationFragmentPresenter(this);
        progressBar = view.findViewById(R.id.progress_bar);
        element = (Elements) getArguments().getSerializable("Element");
        isFirstpage = getArguments().getBoolean("isFirstpage");
        jurisdictions.clear();
        jurisdictions.addAll((ArrayList<String>) getArguments().getSerializable("jurisdictions"));

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_OFFLINE_LOCATION_ALLOWED)) {
                    isOffline = true;
                    continue;
                }
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean flag = true; // have in profile
        for (String str : jurisdictions) {
            if (str.equalsIgnoreCase(Util.getUserObjectFromPref().getMultipleLocationLevel().getName())) {
                flag = false;
                if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {

                    countryList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCountryId().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getCountryId().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        countryList.add(obj);
                    }

                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {

                    stateList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        stateList.add(obj);
                    }

                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
                    districtList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        districtList.add(obj);
                    }
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)) {
                    cityList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCityIds().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getCityIds().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        cityList.add(obj);
                    }

                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
                    talukaList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        talukaList.add(obj);
                    }

                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
                    clusterList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getClusterIds().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getClusterIds().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        clusterList.add(obj);
                    }
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
                    villageList.clear();
                    for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                        JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        villageList.add(obj);
                    }
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.SCHOOL_LEVEL)) {
                    schoolList.clear();
                    if(Util.getUserObjectFromPref().getUserLocation().getSchoolIds()!= null) {
                        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getSchoolIds().size(); i++) {
                            JurisdictionType location = Util.getUserObjectFromPref().getUserLocation().getSchoolIds().get(i);
                            CustomSpinnerObject obj = new CustomSpinnerObject();
                            obj.set_id(location.getId());
                            obj.setName(location.getName());
                            obj.setSelected(false);
                            schoolList.add(obj);
                        }
                    }
                }
            }
            if (flag) {
                if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
                    selectedStateId = Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId();
                    hashMap.put(Constants.JurisdictionLevelName.COUNTRY_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.COUNTRY_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
                    selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
                    hashMap.put(Constants.JurisdictionLevelName.STATE_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.STATE_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
                    selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
                    hashMap.put(Constants.JurisdictionLevelName.DISTRICT_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.DISTRICT_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)) {
                    selectedClusterId = Util.getUserObjectFromPref().getUserLocation().getClusterIds().get(0).getId();
                    hashMap.put(Constants.JurisdictionLevelName.CITY_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.CITY_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getCityIds().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
                    selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
                    hashMap.put(Constants.JurisdictionLevelName.TALUKA_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.TALUKA_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
                    hashMap.put(Constants.JurisdictionLevelName.CLUSTER_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.CLUSTER_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getClusterIds().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
                    hashMap.put(Constants.JurisdictionLevelName.VILLAGE_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.VILLAGE_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(0).getName());
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.SCHOOL_LEVEL)) {
                    hashMap.put(Constants.JurisdictionLevelName.SCHOOL_LEVEL + "Id",
                            Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId());
                    hashMap.put(Constants.JurisdictionLevelName.SCHOOL_LEVEL,
                            Util.getUserObjectFromPref().getUserLocation().getSchoolIds().get(0).getName());
                }
            } else {
                if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etCountry);
                    view.findViewById(R.id.ly_country).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.COUNTRY_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.COUNTRY_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etState);
                    view.findViewById(R.id.ly_state).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.STATE_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.STATE_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etDistrict);
                    view.findViewById(R.id.ly_district).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.DISTRICT_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.DISTRICT_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etCity);
                    view.findViewById(R.id.ly_city).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.CITY_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.CITY_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etTaluka);
                    view.findViewById(R.id.ly_taluka).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.TALUKA_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.TALUKA_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etCluster);
                    view.findViewById(R.id.ly_cluster).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.CLUSTER_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.CLUSTER_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etVillage);
                    view.findViewById(R.id.ly_village).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.VILLAGE_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.VILLAGE_LEVEL));
                    }
                    continue;
                } else if (str.equalsIgnoreCase(Constants.JurisdictionLevelName.SCHOOL_LEVEL)) {
                    EditText editText = view.findViewById(R.id.etSchool);
                    view.findViewById(R.id.ly_school).setVisibility(View.VISIBLE);
                    editText.setOnClickListener(this);
                    if (!TextUtils.isEmpty(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.SCHOOL_LEVEL))) {
                        editText.setText(((FormDisplayActivity) getActivity()).formAnswersMap.get(Constants.JurisdictionLevelName.SCHOOL_LEVEL));
                    }
                    continue;
                }

            }
        }

        view.findViewById(R.id.bt_previous).setOnClickListener(this);
        view.findViewById(R.id.bt_next).setOnClickListener(this);

        if (isFirstpage){
            view.findViewById(R.id.bt_previous).setVisibility(View.GONE);
        }
    }

    void getLocation(String selectedId, String JurisdictionLevel) {

        if (Util.isConnected(getActivity())) {
            presenter.getLocationData(selectedId
                    , Util.getUserObjectFromPref().getJurisdictionTypeId()
                    , JurisdictionLevel);
        } else {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getResources().getString(R.string.msg_no_network),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, getActivity());
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        onFailureListener(requestID, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
            CustomSpinnerObject customTaluka = new CustomSpinnerObject();
            customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
            customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
            districtList.add(customTaluka);
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etCountry:
                if (countryList.size() > 0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select Country", countryList, false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    getLocation((!TextUtils.isEmpty(selectedCountryId))
                                    ? selectedCountryId : Util.getUserObjectFromPref().getUserLocation().getCityIds().get(0).getId(),
                            Constants.JurisdictionLevelName.COUNTRY_LEVEL);
                }
                break;
            case R.id.etState:
                if (stateList.size() > 0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select State", stateList, false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    getLocation((!TextUtils.isEmpty(selectedStateId))
                                    ? selectedStateId : Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                            Constants.JurisdictionLevelName.STATE_LEVEL);
                }
                break;
            case R.id.etDistrict:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select District", districtList, false);
                    csdDisttrict.show();
                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    getLocation((!TextUtils.isEmpty(selectedStateId))
                                    ? selectedStateId : Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }
                break;
            case R.id.etCity:

                if (cityList.size() > 0) {
                    CustomSpinnerDialogClass csdCity = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select City", cityList, false);
                    csdCity.show();
                    csdCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    getLocation((!TextUtils.isEmpty(selectedDistrictId))
                                    ? selectedDistrictId : Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId(),
                            Constants.JurisdictionLevelName.CITY_LEVEL);
                }
                break;
            case R.id.etTaluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select Taluka", talukaList, false);
                    csdTaluka.show();
                    csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    getLocation((!TextUtils.isEmpty(selectedDistrictId))
                                    ? selectedDistrictId : Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;
            case R.id.etCluster:
                ArrayList<JurisdictionLocation> clusterData = (ArrayList<JurisdictionLocation>) DatabaseManager.getDBInstance(Platform.getInstance()).getAccessibleLocationData()
                        .getAccessibleLocationData(selectedTalukaId);

                if (clusterData != null && !clusterData.isEmpty()) {
                    clusterList.clear();
                    for (int i = 0; i < clusterData.size(); i++) {
                        JurisdictionLocation location = clusterData.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        clusterList.add(obj);
                    }
                }
                CustomSpinnerDialogClass csdCluster = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Cluster", clusterList, false);
                csdCluster.show();
                csdCluster.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etVillage:

                ArrayList<JurisdictionLocation> villageData = (ArrayList<JurisdictionLocation>) DatabaseManager.getDBInstance(Platform.getInstance()).getAccessibleLocationData()
                        .getAccessibleLocationData(selectedClusterId);

                if (villageData != null && !villageData.isEmpty()) {
                    villageList.clear();
                    for (int i = 0; i < villageData.size(); i++) {
                        JurisdictionLocation location = villageData.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        villageList.add(obj);
                    }
                }

                CustomSpinnerDialogClass csdVillage = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Village", villageList, false);
                csdVillage.show();
                csdVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etSchool:

                ArrayList<JurisdictionLocation> SchoolData = (ArrayList<JurisdictionLocation>) DatabaseManager.getDBInstance(Platform.getInstance()).getAccessibleLocationData()
                        .getAccessibleLocationData(selectedVillageId);

                if (SchoolData != null && !SchoolData.isEmpty()) {
                    schoolList.clear();
                    for (int i = 0; i < SchoolData.size(); i++) {
                        JurisdictionLocation location = SchoolData.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        schoolList.add(obj);
                    }
                }

                CustomSpinnerDialogClass csdSchool = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select School", schoolList, false);
                csdSchool.show();
                csdSchool.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.bt_previous:
                ((FormDisplayActivity) getActivity()).goPrevious();
                break;
            case R.id.bt_next:
                ((FormDisplayActivity) getActivity()).goNext(hashMap);
                break;
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {



        if (type.equals("Select Country")) {
            for (CustomSpinnerObject mState : countryList) {
                if (mState.isSelected()) {
                    selectedCountry = mState.getName();
                    selectedCountryId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etState);
            tv.setText(selectedCountry);
            hashMap.put(Constants.JurisdictionLevelName.STATE_LEVEL, selectedCountry);
            hashMap.put(Constants.JurisdictionLevelName.COUNTRY_LEVEL + "Id",selectedCountryId);
        }  else if (type.equals("Select State")) {
            for (CustomSpinnerObject mState : stateList) {
                if (mState.isSelected()) {
                    selectedState = mState.getName();
                    selectedStateId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etState);
            tv.setText(selectedState);
            hashMap.put(Constants.JurisdictionLevelName.STATE_LEVEL, selectedState);
            hashMap.put(Constants.JurisdictionLevelName.STATE_LEVEL + "Id",selectedStateId);

        }  else if (type.equals("Select City")) {
            for (CustomSpinnerObject mState : districtList) {
                if (mState.isSelected()) {
                    selectedCity = mState.getName();
                    selectedCityId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etCity);
            tv.setText(selectedState);
            hashMap.put(Constants.JurisdictionLevelName.CITY_LEVEL, selectedCity);
            hashMap.put(Constants.JurisdictionLevelName.CITY_LEVEL + "Id",selectedCityId);

        } else if (type.equals("Select District")) {
            for (CustomSpinnerObject mState : districtList) {
                if (mState.isSelected()) {
                    selectedDistrict = mState.getName();
                    selectedDistrictId = mState.get_id();
                }
            }

            TextView tv = view.findViewById(R.id.etDistrict);
            tv.setText(selectedDistrict);
            hashMap.put(Constants.JurisdictionLevelName.DISTRICT_LEVEL, selectedDistrict);
            hashMap.put(Constants.JurisdictionLevelName.DISTRICT_LEVEL + "Id",selectedDistrictId);

        } else if (type.equals("Select Taluka")) {
            for (CustomSpinnerObject mState : talukaList) {
                if (mState.isSelected()) {
                    selectedTaluka = mState.getName();
                    selectedTalukaId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etTaluka);
            tv.setText(selectedTaluka);
            hashMap.put(Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedTaluka);
            hashMap.put(Constants.JurisdictionLevelName.DISTRICT_LEVEL + "Id",selectedDistrictId);
            if (Util.isConnected(getActivity())) {
                presenter.getAllLocationData(selectedTalukaId
                        , Util.getUserObjectFromPref().getJurisdictionTypeId()
                        , Constants.JurisdictionLevelName.TALUKA_LEVEL);
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), getResources().getString(R.string.msg_no_network),
                        Snackbar.LENGTH_LONG);
            }
        } else if (type.equals("Select Cluster")) {
            for (CustomSpinnerObject mState : clusterList) {
                if (mState.isSelected()) {
                    selectedCluster = mState.getName();
                    selectedClusterId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etCluster);
            tv.setText(selectedCluster);
            hashMap.put(Constants.JurisdictionLevelName.CLUSTER_LEVEL, selectedCluster);

        } else if (type.equals("Select Village")) {
            for (CustomSpinnerObject mState : villageList) {
                if (mState.isSelected()) {
                    selectedVillage = mState.getName();
                    selectedVillageId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etVillage);
            tv.setText(selectedVillage);
            hashMap.put(Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedVillage);

        } else if (type.equals("Select School")) {
            for (CustomSpinnerObject mState : schoolList) {
                if (mState.isSelected()) {
                    selectedSchool = mState.getName();
                    selectedSchoolId = mState.get_id();
                }
            }
            TextView tv = view.findViewById(R.id.etSchool);
            tv.setText(selectedSchool);
            hashMap.put(Constants.JurisdictionLevelName.SCHOOL_LEVEL, selectedSchool);
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> data, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    stateList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        stateList.add(obj);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (data != null && !data.isEmpty()) {
                    districtList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        districtList.add(obj);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (data != null && !data.isEmpty()) {
                    talukaList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        talukaList.add(obj);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (data != null && !data.isEmpty()) {
                    clusterList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        clusterList.add(obj);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    villageList.clear();

                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject obj = new CustomSpinnerObject();
                        obj.set_id(location.getId());
                        obj.setName(location.getName());
                        obj.setSelected(false);
                        villageList.add(obj);
                    }
                }
                break;
            case "AllLocation":
                if (data != null && !data.isEmpty()) {
                    clusterList.clear();

                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        DatabaseManager.getDBInstance(Platform.getInstance()).getAccessibleLocationData()
                                .insert(location);
                    }

                }
                break;

        }
    }
}
