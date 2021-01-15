package com.octopusbjsindia.view.customs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.Jurisdiction;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.smartgirl.SgDashboardResponseModel;
import com.octopusbjsindia.models.smartgirl.SmartGirlCategoryResponseModel;
import com.octopusbjsindia.presenter.SmartGCustomFilterPresenter;
import com.octopusbjsindia.presenter.SmartGirlDashboardsListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.MutiselectDialogAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class SmartGCustomFilterDialog  extends BottomSheetDialogFragment implements View.OnClickListener,CustomSpinnerListener{
        private int mYear, mMonth, mDay, mHour, mMinute;
        private BottomSheetBehavior mBehavior;
        private CustomSpinnerListener mListener;
        public TextView toolbarTitle;
        public ImageView img_close;
        private Button btnApply;
        public EditText tv_startdate,tv_enddate;
        public String bottomSheetTitle;
        private SmartGCustomFilterPresenter smartGCustomFilterPresenter;
        SmartGirlCategoryResponseModel smartGirlCategoryResponseModel;
        private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
        private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
        private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
        private ArrayList<CustomSpinnerObject> categoryList = new ArrayList<>();
        private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
                userTalukas = "", userTalukaIds = "";
        private String selectedStateId = "", selectedDistrictId = "", selectedTalukaId = "",selectedCategoryId = "";
        private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter,tv_select_category;
        private boolean isDateNeeded = false;
        private LinearLayout ly_date_selection_linear;
        private String dashboardresponse;
        SgDashboardResponseModel dashboardResponseModel;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                isDateNeeded = getArguments().getBoolean("isDatefilter",false);

                dashboardresponse = getArguments().getString("dashboardresponse","");

                dashboardResponseModel = PlatformGson.getPlatformGsonInstance().fromJson(dashboardresponse, SgDashboardResponseModel.class);
        }
        @Override
        public void onAttach(Context context) {
                super.onAttach(context);
                if (context instanceof CustomSpinnerListener) {
                        mListener = (CustomSpinnerListener) context;
                } else {
                        throw new RuntimeException(context.toString()
                                + " must implement ItemClickListener");
                }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

                View view = View.inflate(getContext(), R.layout.custom_filter_dialog_smartgirl, null);
                LinearLayout linearLayout = view.findViewById(R.id.linear_dynamic_filterheight);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                params.height = getScreenHeight();
                linearLayout.setLayoutParams(params);
                dialog.setContentView(view);
                mBehavior = BottomSheetBehavior.from((View) view.getParent());
                btnApply = view.findViewById(R.id.btn_apply);
                btnApply.setOnClickListener(this);
                img_close =view.findViewById(R.id.toolbar_edit_action);
                img_close.setOnClickListener(this);
                toolbarTitle =view.findViewById(R.id.toolbar_title);
                toolbarTitle.setText("Apply Filter");


                tvStateFilter = view.findViewById(R.id.tv_state_filter);
                tvDistrictFilter = view.findViewById(R.id.tv_district_filter);
                tvTalukaFilter = view.findViewById(R.id.tv_taluka_filter);
                tv_select_category = view.findViewById(R.id.tv_select_category);
                ly_date_selection_linear = view.findViewById(R.id.ly_date_selection_linear);
                tvStateFilter.setOnClickListener(this);
                tvDistrictFilter.setOnClickListener(this);
                tvTalukaFilter.setOnClickListener(this);
                tv_select_category.setOnClickListener(this);

                tv_startdate = view.findViewById(R.id.tv_startdate);
                tv_enddate= view.findViewById(R.id.tv_enddate);

                tv_startdate.setOnClickListener(this);
                tv_enddate.setOnClickListener(this);
                tv_startdate.setText(Util.getCurrentDate());
                tv_enddate.setText(Util.getCurrentDate());

                smartGCustomFilterPresenter = new SmartGCustomFilterPresenter(this);
                if (isDateNeeded){
                        ly_date_selection_linear.setVisibility(View.VISIBLE);
                }
                // jurisdiction
                smartGCustomFilterPresenter.getBatchCategory();


                hideJurisdictionLevel();
                if (dashboardResponseModel != null) {
                        List<Jurisdiction> jurisdictions = dashboardResponseModel.getSgDashboardResponseModellist().get(3).getJurisdictions().getProject().getJurisdictions();
                        if (jurisdictions != null && jurisdictions.size() > 0) {
                                //hideJurisdictionLevel();
                                for (Jurisdiction j : jurisdictions) {
                                        setJurisdictionLevel(j.getLevelName());
                                }
                                if (Util.isConnected(getActivity())) {
//                        profilePresenter.getProfileLocationData("", selectedRole.getProject().
//                                        getJurisdictionTypeId(), jurisdictions.get(0).getLevelName(), selectedOrg.getId(),
//                                selectedProjects.get(0).getId(), selectedRole.getId());
                                        /*smartGCustomFilterPresenter.getProfileLocationDataV3("", "",
                                                "", "", "", dashboardResponseModel.getSgDashboardResponseModellist().get(0).getJurisdictions().getProject().
                                                        getJurisdictionTypeId(), jurisdictions.get(0).getLevelName(), dashboardResponseModel.getSgDashboardResponseModellist().get(0).getJurisdictions().getOrgId(),
                                                selectedProjects.get(0).getId(), selectedRole.getId());*/

                                        /*smartGCustomFilterPresenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                                        ? selectedDistrictId : userDistrictIds,
                                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                                Constants.JurisdictionLevelName.TALUKA_LEVEL);*/
                                } else {
                                        Util.showToast(getResources().getString(R.string.no_data_available), this);
                                }
                        }
                }

                //---

                //setUserLocation();

                return dialog;
        }


        @Override
        public void onStart() {
                super.onStart();
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        public static int getScreenHeight() {
                return Resources.getSystem().getDisplayMetrics().heightPixels;
        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.tv_startdate:
                                selectStartDate(tv_startdate);
                                break;
                        case R.id.tv_enddate:
                                selectStartDate(tv_enddate);
                                break;
                        case R.id.btn_apply:
                                String requestMap = getRequestMap();
                                mListener.onCustomSpinnerSelection(requestMap);
                                dismiss();
                                break;
                        case R.id.tv_select_category:

                                /*CustomSpinnerDialogClass csdCategory = new CustomSpinnerDialogClass(this, this,
                                        "Select Category", categoryList, false);
                                csdCategory.show();
                                csdCategory.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);*/
                        {
                                CustomSpinnerDialogClass csdCategory = new CustomSpinnerDialogClass(getActivity(),this,
                                        "Select Category", categoryList, false);
                                csdCategory.show();
                                csdCategory.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                        }
                                break;

                        case R.id.toolbar_edit_action:
                                dismiss();
                                break;
                        case R.id.tv_state_filter:
                                /*CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
                                        "Select State",
                                        machineStateList,
                                        true);
                                cdd.show();
                                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);*/

                                smartGCustomFilterPresenter.getLocationData("",
                                        Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                        Constants.JurisdictionLevelName.STATE_LEVEL);

                                break;
                        case R.id.tv_taluka_filter:
                                if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                                        //isTalukaApiFirstCall = false;
                                        smartGCustomFilterPresenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                                        ? selectedDistrictId : userDistrictIds,
                                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                                Constants.JurisdictionLevelName.TALUKA_LEVEL);

                                } else {
                                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                                        .findViewById(android.R.id.content), "Please select District first.",
                                                Snackbar.LENGTH_LONG);
                                }

                                break;
                        case R.id.tv_district_filter:
                                if (tvStateFilter.getText() != null && tvStateFilter.getText().toString().length() > 0) {
                                        smartGCustomFilterPresenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
                                                        ? selectedStateId : userStateIds, Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                                } else {
                                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                                        .findViewById(android.R.id.content), "Your State is not available in your profile." +
                                                        "Please update your profile.",
                                                Snackbar.LENGTH_LONG);
                                }

                                break;

                }
        }

        private void selectStartDate(TextView textview) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth);
                                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                                        textview.setText(selectedDateString);
                                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
        }

        private String getRequestMap() {
                HashMap<String,String> map=new HashMap<>();
                map.put("state_id", selectedStateId);
                map.put("category_id", selectedCategoryId);
                //if(!TextUtils.isEmpty(userDistrictIds))
                {
                        map.put("district_id", selectedDistrictId);
                }
                //if(!TextUtils.isEmpty(selectedTalukaId))
                {
                        map.put("taluka_id", selectedTalukaId);
                }
                if(!TextUtils.isEmpty("startdate")){
                        map.put("startdate", "");
                }
                if(!TextUtils.isEmpty("enddate")){
                        map.put("enddate", "");
                }
                /*if(!TextUtils.isEmpty(taluka_id)){
                        map.put("category_id", categoryId);
                }
                if(!TextUtils.isEmpty(taluka_id)){
                        map.put("type", type);
                }*/
                Gson gson = new GsonBuilder().create();
                String paramjson = gson.toJson(map);
                return paramjson;
        }

        @Override
        public void onCustomSpinnerSelection(String type) {
                //Util.showToast(getActivity(),type);
                if (type.equals("Select State")) {
                        ArrayList<String> filterStateIds = new ArrayList<>();
                        String selectedState = "";
                        selectedStateId = "";
                        for (CustomSpinnerObject mState : machineStateList) {
                                if (mState.isSelected()) {
                                        if (selectedState.equals("")) {
                                                selectedState = mState.getName();
                                        } else {
                                                selectedState = selectedState + "," + mState.getName();
                                        }
                                        if (selectedStateId.length() > 0) {
                                                selectedStateId = selectedStateId + "," + mState.get_id();
                                        } else {
                                                selectedStateId = mState.get_id();
                                        }
                                        filterStateIds.add(mState.get_id());
                                }
                        }
                        tvDistrictFilter.setText("");
                        selectedDistrictId = "";
                        tvTalukaFilter.setText("");
                        selectedTalukaId = "";
                        if (!TextUtils.isEmpty(selectedStateId)) {
                                tvStateFilter.setText(selectedState);

                                //btnFilterClear.setVisibility(View.VISIBLE);
                        }
                } else if (type.equals("Select District")) {
                        ArrayList<String> filterDistrictIds = new ArrayList<>();
                        String selectedDistrict = "";
                        selectedDistrictId = "";
                        for (CustomSpinnerObject mDistrict : machineDistrictList) {
                                if (mDistrict.isSelected()) {
                                        if (selectedDistrict.equals("")) {
                                                selectedDistrict = mDistrict.getName();
                                        } else {
                                                selectedDistrict = selectedDistrict + "," + mDistrict.getName();
                                        }
                                        if (selectedDistrictId.length() > 0) {
                                                selectedDistrictId = selectedDistrictId + "," + mDistrict.get_id();
                                        } else {
                                                selectedDistrictId = mDistrict.get_id();
                                        }
                                        filterDistrictIds.add(mDistrict.get_id());
                                }
                        }
                        tvTalukaFilter.setText("");
                        selectedTalukaId = "";
                        if (!TextUtils.isEmpty(selectedDistrictId)) {
                                tvDistrictFilter.setText(selectedDistrict);

                                //btnFilterClear.setVisibility(View.VISIBLE);
                        }
                } else if (type.equals("Select Taluka")) {
                        ArrayList<String> filterTalukaIds = new ArrayList<>();
                        String selectedTaluka = "";
                        for (CustomSpinnerObject mTaluka : machineTalukaList) {
                                if (mTaluka.isSelected()) {
                                        if (selectedTaluka.equals("")) {
                                                selectedTaluka = mTaluka.getName();
                                        } else {
                                                selectedTaluka = selectedTaluka + "," + mTaluka.getName();
                                        }
                                        if (selectedTalukaId.length() > 0) {
                                                selectedTalukaId = selectedTalukaId + "," + mTaluka.get_id();
                                        } else {
                                                selectedTalukaId = mTaluka.get_id();
                                        }
                                        filterTalukaIds.add(mTaluka.get_id());
                                }
                        }
                        if (!TextUtils.isEmpty(selectedTalukaId)) {
                                tvTalukaFilter.setText(selectedTaluka);

                                //btnFilterClear.setVisibility(View.VISIBLE);
                        }
                } else if (type.equals("Select Category")) {
                        ArrayList<String> filterTalukaIds = new ArrayList<>();
                        String selectedTaluka = "";
                        for (CustomSpinnerObject mTaluka : categoryList) {
                                if (mTaluka.isSelected()) {
                                        if (selectedTaluka.equals("")) {
                                                selectedTaluka = mTaluka.getName();
                                        } else {
                                                selectedTaluka = selectedTaluka + "," + mTaluka.getName();
                                        }
                                        if (selectedCategoryId.length() > 0) {
                                                selectedCategoryId = selectedCategoryId + "," + mTaluka.get_id();
                                        } else {
                                                selectedCategoryId = mTaluka.get_id();
                                        }
                                        filterTalukaIds.add(mTaluka.get_id());
                                }
                        }
                        if (!TextUtils.isEmpty(selectedCategoryId)) {
                                tv_select_category.setText(selectedTaluka);

                                //btnFilterClear.setVisibility(View.VISIBLE);
                        }
                }
        }


        private void setUserLocation() {
                if (Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 1) {
                        tvStateFilter.setOnClickListener(this);
                        machineStateList.clear();
                        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                                CustomSpinnerObject customState = new CustomSpinnerObject();
                                customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
                                customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
                                machineStateList.add(customState);
                        }
                }
                if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                        Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
                        userStates = "";
                        userStateIds = "";
                        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                                if (i == 0) {
                                        userStates = j.getName();
                                        userStateIds = j.getId();
                                } else {
                                        userStates = userStates + "," + j.getName();
                                        userStateIds = userStateIds + "," + j.getId();
                                }
                        }
                        tvStateFilter.setText(userStates);

                } else {
                        tvStateFilter.setText("");
                }

                if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                        Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
                        userDistricts = "";
                        userDistrictIds = "";
                        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                                if (i == 0) {
                                        userDistricts = j.getName();
                                        userDistrictIds = j.getId();
                                } else {
                                        userDistricts = userDistricts + "," + j.getName();
                                        userDistrictIds = userDistrictIds + "," + j.getId();
                                }
                        }
                        tvDistrictFilter.setText(userDistricts);
                } else {
                        tvDistrictFilter.setText("");
                }

                if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                        Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
                        userTalukas = "";
                        userTalukaIds = "";
                        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                                if (i == 0) {
                                        userTalukas = j.getName();
                                        userTalukaIds = j.getId();
                                } else {
                                        userTalukas = userTalukas + "," + j.getName();
                                        userTalukaIds = userTalukaIds + "," + j.getId();
                                }
                        }
                        tvTalukaFilter.setText(userTalukas);
                } else {
                        tvTalukaFilter.setText("");
                }
        }

        public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
                switch (levelName) {
                        case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                                        machineTalukaList.clear();
                                        for (int i = 0; i < jurisdictionLevels.size(); i++) {
                                                JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                                                CustomSpinnerObject talukaList = new CustomSpinnerObject();
                                                talukaList.set_id(location.getId());
                                                talukaList.setName(location.getName());
                                                talukaList.setSelected(false);
                                                machineTalukaList.add(talukaList);
                                        }
                                }
                                //if (!isTalukaApiFirstCall)
                                {
                                        CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(),
                                                this, "Select Taluka", machineTalukaList,
                                                true);
                                        cddTaluka.show();
                                        cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT);
                                }

                                break;
                        case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                                        machineDistrictList.clear();

                                        for (int i = 0; i < jurisdictionLevels.size(); i++) {
                                                JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                                                CustomSpinnerObject districtList = new CustomSpinnerObject();
                                                districtList.set_id(location.getId());
                                                districtList.setName(location.getName());
                                                districtList.setSelected(false);
                                                machineDistrictList.add(districtList);
                                        }
                                }
                                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(getActivity(), this,
                                        "Select District", machineDistrictList,
                                        true);
                                cddDistrict.show();
                                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);

                                break;
                        case Constants.JurisdictionLevelName.STATE_LEVEL:
                                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                                        machineStateList.clear();

                                        for (int i = 0; i < jurisdictionLevels.size(); i++) {
                                                JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                                                CustomSpinnerObject districtList = new CustomSpinnerObject();
                                                districtList.set_id(location.getId());
                                                districtList.setName(location.getName());
                                                districtList.setSelected(false);
                                                machineStateList.add(districtList);
                                        }
                                }
                                CustomSpinnerDialogClass cddState = new CustomSpinnerDialogClass(getActivity(), this,
                                        "Select State", machineStateList,
                                        true);
                                cddState.show();
                                cddState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);

                                break;
                        default:
                                break;
                }
        }
        private void setJurisdictionLevel(String level) {
                switch (level) {
                        /*case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                                etUserCountry.setVisibility(View.VISIBLE);
                                selectedCountries.clear();
                                break;*/

                        case Constants.JurisdictionLevelName.STATE_LEVEL:
                                tvStateFilter.setVisibility(View.VISIBLE);
                                //selectedStates.clear();
                                break;

                        case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                                tvDistrictFilter.setVisibility(View.VISIBLE);
                                //selectedDistricts.clear();
                                break;

                        /*case Constants.JurisdictionLevelName.CITY_LEVEL:
                                tvTalukaFilter.setVisibility(View.VISIBLE);
                                selectedCities.clear();
                                break;*/

                        case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                                tvTalukaFilter.setVisibility(View.VISIBLE);
                          //      selectedTalukas.clear();
                                break;

                        case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                                tvTalukaFilter.setVisibility(View.VISIBLE);
                                //selectedVillages.clear();
                                break;

                        /*case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                                etUserCluster.setVisibility(View.VISIBLE);
                                selectedClusters.clear();
                                break;

                        case Constants.JurisdictionLevelName.GRAM_PANCHAYAT:
                                etGrampanchayt.setVisibility(View.VISIBLE);
                                selectedPanchayat.clear();
                                break;

                        case Constants.JurisdictionLevelName.SCHOOL_LEVEL:
                                etUserSchool.setVisibility(View.VISIBLE);
                                selectedSchools.clear();
                                break;
                        case Constants.JurisdictionLevelName.LEARNING_CENTER:
                                etUserCenter.setVisibility(View.VISIBLE);
                                selectedCenter.clear();
                                break;*/
                }
        }

        private void hideJurisdictionLevel() {
                tvStateFilter.setVisibility(View.GONE);
                tvDistrictFilter.setVisibility(View.GONE);
                tvTalukaFilter.setVisibility(View.GONE);
        }

        public void showReceivedCategories(SmartGirlCategoryResponseModel jurisdictionLevelResponse) {
                smartGirlCategoryResponseModel = jurisdictionLevelResponse;
                for (int i = 0; i < jurisdictionLevelResponse.getData().size(); i++) {
                        // categoryList.add(jurisdictionLevelResponse.getData().get(i).getName().getDefault());
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(jurisdictionLevelResponse.getData().get(i).get_id());
                        meetCountry.setName(jurisdictionLevelResponse.getData().get(i).getName().getDefault());
                        meetCountry.setSelected(false);
                        categoryList.add(meetCountry);
                }
        }
}

