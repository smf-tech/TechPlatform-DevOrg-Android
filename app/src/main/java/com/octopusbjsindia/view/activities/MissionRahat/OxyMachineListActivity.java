package com.octopusbjsindia.view.activities.MissionRahat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.adapter.OxyMachineListAdapter;
import com.octopusbjsindia.databinding.LayoutOxymachineListBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineList;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineListModel;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineListActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class OxyMachineListActivity extends AppCompatActivity implements
        OxyMachineListAdapter.OnRequestItemClicked, APIDataListener, CustomSpinnerListener, View.OnClickListener {
    private OxyMachineListActivityPresenter presenter;
    private LayoutOxymachineListBinding layoutOxymachineListBinding;
    private Activity activity;
    private ArrayList<OxygenMachineList> oxygenMachineLists = new ArrayList<>();
    private OxyMachineListAdapter oxyMachineListAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private String nextPageUrl = "";
    private boolean isAssignMachinesToDistrictAllowed, isAssignMachinesToTalukaAllowed;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_mou_oxymachine);
        layoutOxymachineListBinding = LayoutOxymachineListBinding.inflate(getLayoutInflater());
        View view = layoutOxymachineListBinding.getRoot();
        setContentView(view);
        activity = OxyMachineListActivity.this;
        presenter = new OxyMachineListActivityPresenter(this);
        url = BuildConfig.BASE_URL + Urls.MissionRahat.GET_ALL_OXYMACHINE_LIST;
        initView();
    }

    private void initView() {
        layoutOxymachineListBinding.toolbar.toolbarTitle.setText("Oxygen Concentrator List");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        layoutOxymachineListBinding.rvTrainerbactchlistview.setLayoutManager(layoutManager);

        oxyMachineListAdapter = new OxyMachineListAdapter(this,oxygenMachineLists, this);
        layoutOxymachineListBinding.rvTrainerbactchlistview.setAdapter(oxyMachineListAdapter);

        layoutOxymachineListBinding.rvTrainerbactchlistview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                            getData();
                            if (nextPageUrl != null && !TextUtils.isEmpty(nextPageUrl)) {
                                presenter.getOxyMachineList(nextPageUrl);
                            }
                        }
                    }
                }

//                if (dy < -5 && (this).isFilterApplied() &&
//                        btnClearFilters.getVisibility() != View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.VISIBLE);
//                } else if (dy > 5 && (!((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) &&
//                        btnClearFilters.getVisibility() == View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.GONE);
//                }
            }
        });

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_ASSIGN_MACHINES_DISTRICT)) {
                    isAssignMachinesToDistrictAllowed = true;
                    layoutOxymachineListBinding.fbMachineAssign.setVisibility(View.VISIBLE);
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.MissionRahat.ACCESS_CODE_ASSIGN_MACHINES_TALUKA)) {
                    isAssignMachinesToTalukaAllowed = true;
                    layoutOxymachineListBinding.fbMachineAssign.setVisibility(View.VISIBLE);
                    continue;
                }
            }
        }
        layoutOxymachineListBinding.toolbar.toolbarBackAction.setOnClickListener(this);
        layoutOxymachineListBinding.fbMachineAssign.setOnClickListener(this);
    }

//    private void setClickListners() {
//        layoutOxymachineListBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        oxygenMachineLists.clear();
        presenter.getOxyMachineList(url);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        emptyResponse();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        emptyResponse();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        hideProgressBar();
        /*CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
        OxygenMachineListModel oxygenMachineListModel = new Gson().fromJson(response, OxygenMachineListModel.class);*/
        try {
            if (response != null) {
                OxygenMachineListModel oxygenMachineListModel = new Gson().fromJson(response, OxygenMachineListModel.class);
                if (oxygenMachineListModel.getCode() == 200) {
                    if (requestID.equalsIgnoreCase(OxyMachineListActivityPresenter.GET_MACHINE_LIST)) {
                        nextPageUrl = oxygenMachineListModel.getNextPageUrl();
                        oxygenMachineLists.addAll(oxygenMachineListModel.getOxygenMachineLists());
                        layoutOxymachineListBinding.toolbar.toolbarTitle.setText("Oxygen Concentrator List ("
                                + oxygenMachineListModel.getTotal() + ")");
                        oxyMachineListAdapter.notifyDataSetChanged();
                        loading = true;
                        emptyResponse();
                    }
                } else {
                    if (oxygenMachineListModel.getCode() == 1000) {
                        Util.logOutUser(this);
                    } else {
                        onFailureListener(requestID, oxygenMachineListModel.getMessage());
                    }
                    emptyResponse();
                }
            }
        } catch (Exception e) {
            onFailureListener(requestID, e.getMessage());
        }
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (layoutOxymachineListBinding.lyProgressBar != null && layoutOxymachineListBinding.pbProfileAct != null) {
                layoutOxymachineListBinding.lyProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (layoutOxymachineListBinding.lyProgressBar != null && layoutOxymachineListBinding.pbProfileAct != null) {
                layoutOxymachineListBinding.lyProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onCustomSpinnerSelection(String type) {

    }

    public void showSuccessResponse(String response) {
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemClicked(int pos) {
        if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_MR_HOSPITAL_INCHARGE) {
            Intent intent1 = new Intent(this, OxyMachineDailyReportActivity.class);
            Gson gson = new Gson();
            String machineDataString = gson.toJson(oxygenMachineLists.get(pos));
            intent1.putExtra("MachineDataString", machineDataString);
            intent1.putExtra("position", pos);
            startActivityForResult(intent1,Constants.MissionRahat.RECORD_UPDATE);
        }
    }

    public void emptyResponse() {
        if (oxygenMachineLists.size()<1) {
            layoutOxymachineListBinding.lyNoData.setVisibility(View.VISIBLE);
        }else {
            layoutOxymachineListBinding.lyNoData.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.MissionRahat.RECORD_UPDATE && data != null) {
            double hoursUsedCount = data.getDoubleExtra("HOURS_USED_COUNT", 0);
            int patientsBenefitedCount = data.getIntExtra("PATIENTS_BENEFITED_COUNT", 0);
            int updatePosition = data.getIntExtra("UPDATE_POSITION", 0);
            updateList(hoursUsedCount,patientsBenefitedCount,updatePosition);
        }
    }

    private void updateList(double hoursUsedCount, int patientsBenefitedCount, int updatePosition) {
        if (oxygenMachineLists.size() > updatePosition) {
            oxygenMachineLists.get(updatePosition).setBenefitedPatientCount(oxygenMachineLists.get(updatePosition).getBenefitedPatientCount() + patientsBenefitedCount);
            oxygenMachineLists.get(updatePosition).setWorkingHrsCount(oxygenMachineLists.get(updatePosition).getWorkingHrsCount() + hoursUsedCount);
            oxyMachineListAdapter.notifyItemChanged(updatePosition);
            oxyMachineListAdapter.notifyDataSetChanged();
            ;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.fb_machine_assign:
                Intent intent = new Intent(this, OxyMachinesAssignActivity.class);
                intent.putExtra("isAssignToDistrictAllowed", isAssignMachinesToDistrictAllowed);
                intent.putExtra("isAssignToTalukaAllowed", isAssignMachinesToTalukaAllowed);
                startActivity(intent);
                break;
        }
    }

    public void callToAddDailyReportorPatients(int pos,int type){
        if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_MR_HOSPITAL_INCHARGE) {
            if (type == 1) {
                Intent intent1 = new Intent(this, OxyMachineDailyReportActivity.class);
                Gson gson = new Gson();
                String machineDataString = gson.toJson(oxygenMachineLists.get(pos));
                intent1.putExtra("MachineDataString", machineDataString);
                intent1.putExtra("position", pos);
                startActivityForResult(intent1, Constants.MissionRahat.RECORD_UPDATE);
            }
            if (type == 2) {
                Intent intent1 = new Intent(this, PatientInfoActivity.class);
                Gson gson = new Gson();
                String machineDataString = gson.toJson(oxygenMachineLists.get(pos));
                intent1.putExtra("MachineDataString", machineDataString);
                intent1.putExtra("position", pos);
                startActivityForResult(intent1, Constants.MissionRahat.RECORD_UPDATE);
            }
        }
    }
}