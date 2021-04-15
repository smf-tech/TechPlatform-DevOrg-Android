package com.octopusbjsindia.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.smartgirl.DashboardItemListResponseModel;
import com.octopusbjsindia.models.smartgirl.DashboardListItem;
import com.octopusbjsindia.models.smartgirl.SGTrainersList;
import com.octopusbjsindia.models.smartgirl.SgDashboardResponseModel;
import com.octopusbjsindia.models.smartgirl.WorkshopBachListResponseModel;
import com.octopusbjsindia.presenter.SmartGCustomFilterPresenter;
import com.octopusbjsindia.presenter.SmartGirlDashboardsListPresenter;
import com.octopusbjsindia.presenter.SmartGirlWorkshopListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.AllTrainersListRecyclerAdapter;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.SmartGirlDashboardListAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.customs.SmartGCustomFilterDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartGirlDashboardListActivity extends AppCompatActivity implements SmartGirlDashboardListAdapter.OnRequestItemClicked, View.OnClickListener, CustomSpinnerListener {
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private boolean isTalukaApiFirstCall;

    private String paramjsonString;
    private boolean loading = true,isCategoryfilterneeded =false;
    private String nextPageUrl = "";
    private SmartGirlDashboardListAdapter smartGirlDashboardListAdapter;
    private SmartGirlDashboardsListPresenter smartGirlDashboardsListPresenter;
    private RecyclerView rv_dashboard_listview;
    private RelativeLayout ly_no_data;
    private ImageView toolbar_back_action, toolbar_edit_action;
    private TextView tvTitle;
    List<DashboardListItem> DataList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();

    private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
            userTalukas = "", userTalukaIds = "";
    private String selectedStateId = "", selectedDistrictId = "", selectedTalukaId = "";
    String categoryId = "";
    String StringListType,dashboardresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_girl_dashboard_list);

        tvStateFilter = findViewById(R.id.tv_state_filter);
        tvDistrictFilter = findViewById(R.id.tv_district_filter);
        tvTalukaFilter = findViewById(R.id.tv_taluka_filter);

        tvStateFilter.setOnClickListener(this);
        tvDistrictFilter.setOnClickListener(this);
        tvTalukaFilter.setOnClickListener(this);

        toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        toolbar_edit_action.setVisibility(View.VISIBLE);
        toolbar_edit_action.setImageResource(R.drawable.ic_filter_white);
        toolbar_edit_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callfilterDialog();
            }
        });
        setUserLocation();
        ly_no_data = findViewById(R.id.ly_no_data);
        rv_dashboard_listview = findViewById(R.id.rv_dashboard_listview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_dashboard_listview.setLayoutManager(layoutManager);
        //
        StringListType  = getIntent().getExtras().getString("viewType");
        dashboardresponse = getIntent().getExtras().getString("dashboardresponse");

        if (StringListType.equalsIgnoreCase("trainer")){

        }else if (StringListType.equalsIgnoreCase("masterTrainer")){


        }else if (StringListType.equalsIgnoreCase("beneficiary")){
            toolbar_edit_action.setVisibility(View.GONE);
        }




        SgDashboardResponseModel dashboardResponseModel = PlatformGson.getPlatformGsonInstance().fromJson(dashboardresponse, SgDashboardResponseModel.class);
        smartGirlDashboardsListPresenter = new SmartGirlDashboardsListPresenter(this);
        /*final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API, StringListType);
        smartGirlDashboardsListPresenter.getRequestedList(getRoleAccessUrl);*/
        //set up adapter
        smartGirlDashboardListAdapter = new SmartGirlDashboardListAdapter(this, DataList,
                this::onItemClicked);
        rv_dashboard_listview.setAdapter(smartGirlDashboardListAdapter);
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(StringListType);

        rv_dashboard_listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (nextPageUrl != null && !TextUtils.isEmpty(nextPageUrl)) {
                                //smartGirlDashboardsListPresenter.getRequestedList(nextPageUrl);
                                if (TextUtils.isEmpty(paramjsonString)) {
                                    callGetDataList(useDefaultRequest(), nextPageUrl);
                                }else {
                                    callGetDataList(paramjsonString, nextPageUrl);
                                }
                            }
                        }
                    }
                }
            }
        });
        useDefaultRequest();
        callGetDataList(useDefaultRequest(),"");
    }

    private void callfilterDialog() {

        SmartGCustomFilterDialog smartGCustomFilterDialog = new SmartGCustomFilterDialog();
        Bundle args = new Bundle();
        args.putBoolean("isDatefilter",false);
        args.putBoolean("isCategoryfilter",false);
        args.putString("dashboardresponse",dashboardresponse);
        smartGCustomFilterDialog.setArguments(args);
        smartGCustomFilterDialog.show(getSupportFragmentManager(), "search_dialog");
    }


    @Override
    public void onItemClicked(int pos) {
        // todo take to webview for workshop list data if needed
    }

    public void showNoData() {
        ly_no_data.setVisibility(View.VISIBLE);
    }

    public void refreshData() {
    }
    public void showToastMessage(String message) {
    }
    public void showProgressBar() {
    }
    public void hideProgressBar() {
    }

    public void showReceivedDataList(DashboardItemListResponseModel dashboardItemListResponseModel) {
        ly_no_data.setVisibility(View.GONE);
        loading =true;
        nextPageUrl = dashboardItemListResponseModel.getDashboardItemListResponse().getNextPageUrl();
        DataList.addAll(dashboardItemListResponseModel.getDashboardItemListResponse().getDashboardListItems());
        smartGirlDashboardListAdapter.notifyDataSetChanged();
        if(DataList!=null && DataList.size()<1){
            ly_no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_state_filter) {
            /*CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(this, this,
                    "Select State",
                    machineStateList,
                    true);
            cdd.show();
            cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);*/

            /*SmartGCustomFilterDialog cdd = new SmartGCustomFilterDialog(this, this,
                    "Select State",
                    machineStateList,
                    true);
            cdd.show();
            cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);*/

        } else
        if (view.getId() == R.id.tv_taluka_filter) {
//            if (machineTalukaList.size() > 0) {
//                CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(getActivity(), this,
//                        "Select Taluka", machineTalukaList, false);
//                csdTaluka.show();
//                csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
            if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                isTalukaApiFirstCall = false;
                smartGirlDashboardsListPresenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                ? selectedDistrictId : userDistrictIds,
                        Util.getUserObjectFromPref().getJurisdictionTypeId(),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);

            } else {
                Util.snackBarToShowMsg(getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please select District first.",
                        Snackbar.LENGTH_LONG);
            }
            //}
        } else if (view.getId() == R.id.tv_district_filter) {
//            if (machineDistrictList.size() > 0) {
//                CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(getActivity(), this,
//                        "Select District", machineDistrictList, false);
//                csdDisttrict.show();
//                csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
            if (tvStateFilter.getText() != null && tvStateFilter.getText().toString().length() > 0) {
                smartGirlDashboardsListPresenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
                                ? selectedStateId : userStateIds, Util.getUserObjectFromPref().getJurisdictionTypeId(),
                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
            } else {
                Util.snackBarToShowMsg(getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Your State is not available in your profile." +
                                "Please update your profile.",
                        Snackbar.LENGTH_LONG);
            }
            //}
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
                if (!isTalukaApiFirstCall) {
                    CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(this,
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
                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(this, this,
                        "Select District", machineDistrictList,
                        true);
                cddDistrict.show();
                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            default:
                break;
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {

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
        }else if (!TextUtils.isEmpty(type)){
            HashMap<String,String> map=new HashMap<>();
            Gson gson = new GsonBuilder().create();
            map = gson.fromJson(type,HashMap.class);

          /*  if(!TextUtils.isEmpty(categoryId)){
                        map.put("category_id", categoryId);
                }*/
                if(!TextUtils.isEmpty(StringListType)){
                        map.put("type", StringListType);
                }

            //String paramjson = gson.toJson(map);
            paramjsonString = gson.toJson(map);
                DataList.clear();
            smartGirlDashboardListAdapter.notifyDataSetChanged();
            callGetDataList(paramjsonString,"");
        }
    }


private String useDefaultRequest(){
    Gson gson = new GsonBuilder().create();
    HashMap<String,String> map=new HashMap<>();
    map.put("state_id", "");
    map.put("district_id", "");
    map.put("taluka_id", "");
    map.put("category_id", "");
    if(!TextUtils.isEmpty(StringListType)){
        map.put("type", StringListType);
    }

    String paramjson = gson.toJson(map);
    return  paramjson;

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

private void callGetDataList(String paramjson,String url){
    //get filtered data list
    //(String state_id, String district_id, String taluka_id,String type,String categoryId)
    /*HashMap<String,String> map=new HashMap<>();
    Gson gson = new GsonBuilder().create();
    String paramjson = gson.toJson(map);*/

    if (TextUtils.isEmpty(url)) {
        url = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API);
    }else {}
    smartGirlDashboardsListPresenter.getRequestedDataList(paramjson, url);
}

}