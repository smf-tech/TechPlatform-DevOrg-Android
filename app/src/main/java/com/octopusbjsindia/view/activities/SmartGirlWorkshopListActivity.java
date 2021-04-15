package com.octopusbjsindia.view.activities;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.smartgirl.TrainerBachList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachListResponseModel;
import com.octopusbjsindia.presenter.SmartGirlWorkshopListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.WorkshopBatchListRecyclerAdapter;
import com.octopusbjsindia.view.customs.SmartGCustomFilterDialog;
import com.octopusbjsindia.view.fragments.smartgirlfragment.MemberListFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PostFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PreFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PreTestTrainingFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.RegisterTrainerFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.WorkshopPostFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.WorkshopPreFeedbackFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SmartGirlWorkshopListActivity extends AppCompatActivity implements WorkshopBatchListRecyclerAdapter.OnRequestItemClicked, WorkshopBatchListRecyclerAdapter.OnApproveRejectClicked,
        CustomSpinnerListener, View.OnClickListener {
    //--Constant

    //------
    private FloatingActionButton fb_email_data;
    private String paramjsonString;
    private String filterDataString;
    private boolean loading = true;
    private String nextPageUrl = "",requestEmailId= "";
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    public EditText tv_startdate, tv_enddate;
    public SmartGirlWorkshopListPresenter presenter;
    public RecyclerView rv_trainerbactchlistview;
    public WorkshopBatchListRecyclerAdapter trainerBatchListRecyclerAdapter;
    private List<WorkshopBachList> dataList = new ArrayList<>();
    PopupMenu popup;
    private ImageView toolbar_back_action, toolbar_edit_action,toolbar_action;
    private TextView tvTitle;
    //----declaration
    private Fragment fragment;
    private FragmentManager fManager;
    private RelativeLayout progressBar;
    public RelativeLayout ly_no_data;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> categoryList = new ArrayList<>();
    private String selectedDistrictId, selectedDistrict, selectedStateId, selectedState;
    private WorkshopBachListResponseModel trainerBachListResponseModel;
    private Context mContext;
    private String StringListType,dashboardresponse;
    String categoryId = ""; //""5e33f2ea8607c02ac0ba434d";
    private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
            userTalukas = "", userTalukaIds = "";
    //private String selectedStateId = "", selectedDistrictId = "", selectedTalukaId = "";

    @Override
    protected void onResume() {
        super.onResume();
        //presenter.getBatchList();
      //  useDefaultRequest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_batchlist);
        mContext = SmartGirlWorkshopListActivity.this;
        fManager = getSupportFragmentManager();
        tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("Workshop List");
        toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        toolbar_edit_action.setVisibility(View.GONE);
        toolbar_edit_action.setImageResource(R.drawable.ic_plus);

        toolbar_action = findViewById(R.id.toolbar_action);
        toolbar_action.setVisibility(View.VISIBLE);
        toolbar_action.setImageResource(R.drawable.ic_filter_white);
        toolbar_action.setVisibility(View.VISIBLE);
        fb_email_data  = findViewById(R.id.fb_email_data);
        fb_email_data.setOnClickListener(this);
        presenter = new SmartGirlWorkshopListPresenter(this);
        //setMasterData();
        setUserLocation();
        //---
        progressBar = findViewById(R.id.ly_progress_bar);
        ly_no_data = findViewById(R.id.ly_no_data);
        rv_trainerbactchlistview = findViewById(R.id.rv_trainerbactchlistview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rv_trainerbactchlistview.setLayoutManager(layoutManager);

        trainerBatchListRecyclerAdapter = new WorkshopBatchListRecyclerAdapter(this, dataList,
                this, this);
        rv_trainerbactchlistview.setAdapter(trainerBatchListRecyclerAdapter);

        rv_trainerbactchlistview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    callWorkshopListApi(useDefaultRequest(), nextPageUrl);
                                }else {
                                    callWorkshopListApi(paramjsonString, nextPageUrl);
                                }
                                //presenter.getBatchList("paramjson",nextPageUrl);
                            }
                        }
                    }
                }
                Log.e("TAG", "Scroll Listner called");
            }
        });
        //-------
        StringListType  = getIntent().getExtras().getString("viewType");
        StringListType  = getIntent().getExtras().getString("viewType");
        dashboardresponse = getIntent().getExtras().getString("dashboardresponse");
        //presenter.getBatchList();
        useDefaultRequest();
        callWorkshopListApi(useDefaultRequest(),"");

        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.toolbar_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callfilterDialog();
            }
        });

        findViewById(R.id.toolbar_edit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu((SmartGirlWorkshopListActivity.this), view);
                popup.inflate(R.menu.sg_batchlist_menu);


                RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
                RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
                if(roleAccessList != null) {
                    List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
                    for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                        switch(roleAccessObject.getActionCode()){

                            case Constants.SmartGirlModule.ACCESS_CODE_CREATE_BATCH:

                                break;
                            case Constants.SmartGirlModule.ACCESS_CODE_CREATE_WORKSHOP:
                                popup.getMenu().findItem(R.id.action_add_new_workshop).setVisible(true);

                                break;
                        }
                    }
                }


                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (Util.isConnected(SmartGirlWorkshopListActivity.this)) {
                            switch (item.getItemId()) {
                                case R.id.action_add_new_workshop:
                                    if (Util.isConnected(mContext)) {
                                        Intent intent = new Intent(mContext, CreateWorkshopSmartgirlActivity.class);
                                        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
                                    } else {
                                        Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                    }
                                    break;
                                case R.id.action_add_new_batch:
                                    if (Util.isConnected(mContext)) {
                                        /*Intent intent = new Intent(mContext, CreateTrainerWorkshop.class);
                                        startActivityForResult(intent, Constants.Home.NEVIGET_TO);*/


                                    } else {
                                        Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                    }
                                    break;
                            }
                        } else {
                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                        }
                        return false;
                    }
                });
            }
        });
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if(roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode()==Constants.SmartGirlModule.ACCESS_CODE_CREATE_WORKSHOP){
                    toolbar_edit_action.setVisibility(View.VISIBLE);
                }
            }
        }
    }


//progress loaders

    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void hideFilter(){
        toolbar_action.setVisibility(View.GONE);
    }
    public void showReceivedBatchList(WorkshopBachListResponseModel trainerBachListResponseModelReceived) {
        Util.logger("received workshop -", "---");
        ly_no_data.setVisibility(View.GONE);
        //tmUserLeaveApplicationsList = data;
        trainerBachListResponseModel = trainerBachListResponseModelReceived;
        //dataList.clear();
        dataList.addAll(trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists());
        trainerBatchListRecyclerAdapter.notifyDataSetChanged();
            if (trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists()!=null&&trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().size()<1){
                ly_no_data.setVisibility(View.VISIBLE);
            }

        if (dataList!=null) {
            if (fManager.getBackStackEntryCount()>0){

            }else {
                tvTitle.setText("Workshop List" + "(" + dataList.size() + ")");
                nextPageUrl = trainerBachListResponseModel.getWorkshopBachListResponse().getNextPageUrl();
                loading =true;
            }
        }
        /*trainerBatchListRecyclerAdapter = new WorkshopBatchListRecyclerAdapter(this, trainerBachListResponseModel.getWorkshopBachLists(),
                this, this);
        rv_trainerbactchlistview.setAdapter(trainerBatchListRecyclerAdapter);*/
    }

    @Override
    public void onItemClicked(int pos) {

    }

    @Override
    public void onApproveClicked(int pos) {
    }

    @Override
    public void onRejectClicked(int pos) {
    }


    //------------
    public void addBeneficiaryToWorkshop(int adapterPosition, int type, String paramjson) {
        CloseFragment();
        rv_trainerbactchlistview.setVisibility(View.VISIBLE);
        presenter.addTrainerToBatch(paramjson);

    }

    //---------
    public void addSelfTrainerToBatch(int adapterPosition) {
        String paramjson = new Gson().toJson(getTrainerReqJson(adapterPosition));
        presenter.addSelfTrainerToBatch(paramjson);
    }

    public void cancelWorkshopRequest(int adapterPosition) {
        String paramjson = new Gson().toJson(getTrainerReqJson(adapterPosition));
        presenter.cancelWorkshopRequest(paramjson);
    }
    public void completeWorkshopRequest(int adapterPosition) {
        String paramjson = new Gson().toJson(getTrainerReqJson(adapterPosition));
        presenter.completeWorkshopRequest(paramjson);
    }

    public void EditWorkshopRequest(int adapterPosition) {
        String paramjson = new Gson().toJson(trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition));
        if (Util.isConnected(mContext)) {
            Intent intent = new Intent(mContext, CreateWorkshopSmartgirlActivity.class);
            intent.putExtra(Constants.Login.ACTION_EDIT,Constants.Login.ACTION_EDIT);
            intent.putExtra("jsonObjectString",paramjson);
            startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
        } else {
            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
        }
    }

    public JsonObject getTrainerReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("workshop_id", batchId);
        return requestObject;
    }

    //-------------- PRE TEST
    public void fillPreTestFormToBatch(int adapterPosition, int type, String paramjson) {
        rv_trainerbactchlistview.setVisibility(View.VISIBLE);
        presenter.submitPreTestFormToBatch(paramjson);
        onBackPressed();
    }

    /*public JsonObject getPreTestReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getTrainerBachListdata().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("gender", "male");
        requestObject.addProperty("age", "25");
        requestObject.addProperty("education", "BA");
        requestObject.addProperty("trainer_id", "5e4b73ec515e88607566f393");
        requestObject.addProperty("trainer_name", "Kumood S Bongale");
        requestObject.addProperty("trainer_phone", "9881499768");
        requestObject.addProperty("occupation", "Teacher");
        requestObject.addProperty("email", "abc@gmail.com");
        return requestObject;
    }*/
//-------------

    public void submitFeedbsckToWorkshop(int adapterPosition, int type, String paramjson) {
        //type is feedback type
        if (type == 1) {
            presenter.submitFeedbsckToWorkshop(paramjson);
            CloseFragment();
        } else {
            presenter.submitFeedbsckToWorkshop(paramjson);
            CloseFragment();
        }
    }



    public void addMemberListFragment(int adapterPosition) {
        Bundle bundle = new Bundle();

        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).getBeneficiariesList());
        bundle.putString("memberList", jsonInString);
        bundle.putString("listType", Constants.SmartGirlModule.BENEFICIARY_lIST);

        fragment = new MemberListFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.add(R.id.feedback_form_container, fragment, "formFragment");
            fragmentTransaction.addToBackStack("memberlist");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            rv_trainerbactchlistview.setVisibility(View.GONE);
            tvTitle.setText("Beneficiary List");
        } catch (Exception e) {
            Log.e("addFragment", "Exception :: FormActivity : addFragment");
        }
        fb_email_data.setVisibility(View.GONE);
    }


    public void addWorkshopSupportDocFragment(int adapterPosition){
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.WORKSHOP_SUPPORT_DOC_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"organizerFeedBackStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
        //mContext.startActivity(intent);
    }

    public void addOrganiserFeedbackFragment(int adapterPosition){
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.ORGANISER_FEEDBACK_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"organizerFeedBackStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
    }
    public void addParentFeedbackFragment(int adapterPosition){
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.PARENTS_FEEDBACK_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"parentFeedBackStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
    }
    //Add Pre Test form fragment
    public void addPreFeedbackFragment(int adapterPosition) {
        /*Bundle bundle = new Bundle();
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        fragment = new WorkshopPreFeedbackFragment();
        fragment.setArguments(bundle);
        try {
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.add(R.id.feedback_form_container, fragment, "formFragment");
            fragmentTransaction.addToBackStack("pretest");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            rv_trainerbactchlistview.setVisibility(View.GONE);
            tvTitle.setText("Pre Feedback");
        } catch (Exception e) {
            Log.e("addFragment", "Exception :: FormActivity : addFragment");
        }*/
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.PRE_FEEDBACK_WORKSHOP_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"preFeedBackStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
    }

    public void addPostFeedbackFragment(int adapterPosition) {
        /*Bundle bundle = new Bundle();

        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        fragment = new WorkshopPostFeedbackFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.add(R.id.feedback_form_container, fragment, "formFragment");
            fragmentTransaction.addToBackStack("pretest");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            rv_trainerbactchlistview.setVisibility(View.GONE);
            tvTitle.setText("Post Feedback");
        } catch (Exception e) {
            Log.e("addFragment", "Exception :: FormActivity : addFragment");
        }*/


        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.POST_FEEDBACK_WORKSHOP_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"postFeedBackStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
    }

    //pre test
    public void addTrainingPreTestFragment(int adapterPosition) {
        /*Bundle bundle = new Bundle();

        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        fragment = new PreTestTrainingFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.add(R.id.feedback_form_container, fragment, "formFragment");
            fragmentTransaction.addToBackStack("pretest");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            rv_trainerbactchlistview.setVisibility(View.GONE);
            tvTitle.setText("Pre training test");
        } catch (Exception e) {
            Log.e("addFragment", "Exception :: FormActivity : addFragment");
        }*/
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.PRE_TEST_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"preTestStatus");
        startActivityForResult(intent, Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT);
    }

    //pre test
    public void addRegisterTrainerFragment(int adapterPosition) {
        Bundle bundle = new Bundle();

        String batchId = trainerBachListResponseModel.getWorkshopBachListResponse().getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        bundle.putInt("registrationtype", 2);
        fragment = new RegisterTrainerFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.add(R.id.feedback_form_container, fragment, "formFragment");
            fragmentTransaction.addToBackStack("pretest");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            rv_trainerbactchlistview.setVisibility(View.GONE);
            tvTitle.setText("Register Beneficiary");
        } catch (Exception e) {
            Log.e("addFragment", "Exception :: FormActivity : addFragment");
        }
    }


    //close fragment if available else close activity on backpress
    @Override
    public void onBackPressed() {
        /*tvTitle.setText("Workshop List");
        if (dataList!=null) {
            tvTitle.setText("Workshop List" + "("+dataList.size()+")");
        }*/
        if (fManager.getBackStackEntryCount() == 0) {
            finish();
        }
        else {
            //showDialog(mContext, "Alert", "Do you want really want to close ?", "No", "Yes");
            if (fManager.getBackStackEntryCount() > 1) {
                fManager.popBackStackImmediate();
                toolbar_action.setVisibility(View.VISIBLE);
                if (fManager.getBackStackEntryCount() == 0) {
                    hideFilter();
                }
            } else {
                toolbar_action.setVisibility(View.VISIBLE);
                fb_email_data.setVisibility(View.VISIBLE);
                try {
                    tvTitle.setText("Workshop List");
                    if (dataList != null) {
                        tvTitle.setText("Workshop List" + "(" + dataList.size() + ")");
                    }
                    fManager.popBackStackImmediate();
                    rv_trainerbactchlistview.setVisibility(View.VISIBLE);
                    dataList.clear();
                    callWorkshopListApi(useDefaultRequest(),"");
                } catch (IllegalStateException e) {
                    Log.e("TAG", e.getMessage());
                }
            }
        }
    }

    public void showToastMessage(String message) {
        Util.showToast(message, mContext);
        CloseFragment();
    }
    public void refreshData(){
        //presenter.getBatchList();
        useDefaultRequest();
    }

    //back button confirmation
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog

                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                try {
                    tvTitle.setText("Workshop List");
                    if (dataList!=null) {
                        tvTitle.setText("Workshop List" + "("+dataList.size()+")");
                    }
                    fManager.popBackStackImmediate();
                    rv_trainerbactchlistview.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                } catch (IllegalStateException e) {
                    Log.e("TAG", e.getMessage());
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void CloseFragment(){
        try {
            showProgressBar();
            fManager.popBackStackImmediate();
            hideProgressBar();
            rv_trainerbactchlistview.setVisibility(View.VISIBLE);

        } catch (IllegalStateException e) {
            Log.e("TAG", e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  if (requestCode == Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT && data != null)
        {
        //presenter.getBatchList();
            dataList.clear();
            trainerBatchListRecyclerAdapter.notifyDataSetChanged();
            callWorkshopListApi(useDefaultRequest(),"");
        }
    }

    public void showNoData() {
        ly_no_data.setVisibility(View.VISIBLE);
    }

    // Add filter
    private String useDefaultRequest(){
        Gson gson = new GsonBuilder().create();
        HashMap<String,String> map=new HashMap<>();

        map.put("state_id", "");
        map.put("district_id", "");
        map.put("taluka_id", userTalukaIds);
        map.put("category_id", categoryId);
        if(!TextUtils.isEmpty(StringListType)){
            map.put("type", StringListType);
        }else {
            map.put("type", "Workshop");
        }
        map.put("startdate","");
        map.put("enddate","");


        String paramjson = gson.toJson(map);

        filterDataString = paramjson;
        /*final String url  = BuildConfig.BASE_URL
                + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API);
        presenter.getBatchList(paramjson,url);*/
        //presenter.getBatchList();
        return paramjson;
    }
    public void callWorkshopListApi(String paramjson,String url){

        if (TextUtils.isEmpty(url)) {
            url = BuildConfig.BASE_URL
                    + String.format(Urls.SmartGirl.GET_DAHSBOARDS_LIST_API);
        }else {}
        presenter.getBatchList(paramjson,url);
        //presenter.getBatchList(paramjson,url);
    }

    private void setUserLocation() {
        /*if (Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 1) {
            tvStateFilter.setOnClickListener(this);
            machineStateList.clear();
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                CustomSpinnerObject customState = new CustomSpinnerObject();
                customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
                customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
                machineStateList.add(customState);
            }
        }*/
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
          //  tvStateFilter.setText(userStates);

        } else {
            //tvStateFilter.setText("");
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
            //tvDistrictFilter.setText(userDistricts);
        } else {
            //tvDistrictFilter.setText("");
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
            //tvTalukaFilter.setText(userTalukas);
        } else {
            //tvTalukaFilter.setText("");
        }
    }
    private void callfilterDialog() {

        SmartGCustomFilterDialog smartGCustomFilterDialog = new SmartGCustomFilterDialog();
        Bundle args = new Bundle();
        args.putBoolean("isDatefilter",true);
        args.putBoolean("istrainerfilter",true);
        args.putString("dashboardresponse",dashboardresponse);
        smartGCustomFilterDialog.setArguments(args);
        smartGCustomFilterDialog.show(getSupportFragmentManager(), "search_dialog");
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (!TextUtils.isEmpty(type)){
            HashMap<String,String> map=new HashMap<>();
            Gson gson = new GsonBuilder().create();
            map = gson.fromJson(type,HashMap.class);


            if(!TextUtils.isEmpty(StringListType)){
                map.put("type", StringListType);
            }

            paramjsonString = gson.toJson(map);
            filterDataString = paramjsonString;
            dataList.clear();
            trainerBatchListRecyclerAdapter.notifyDataSetChanged();
            callWorkshopListApi(paramjsonString,"");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_email_data:
                Util.showEnterEmailDialog(this,1,null);
                break;

        }
    }

    public void onReceiveEmailId(String strEmailId, int feebackType) {

        if (TextUtils.isEmpty(strEmailId)){
            showToastMessage("Please enter valid email");
        }else {
            requestEmailId = strEmailId;
            presenter.sendWorkshopDataEmail(getEmailDataReqJson());
        }

    }

    public String getEmailDataReqJson() {

            HashMap<String,String> map=new HashMap<>();
            Gson gson = new GsonBuilder().create();
            map = gson.fromJson(filterDataString,HashMap.class);
            map.put("emailid",requestEmailId);

        paramjsonString = gson.toJson(map);


        /*"type": "workshop",  //"batch"
                "typeId": "5ffd85bb5c9e305733574ed3",
                "dataType": "pre", // "post"
                "emailid":"rbisen@bjsindia.org"*/

        return paramjsonString;
    }
}
