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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.smartgirl.TrainerBachList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachList;
import com.octopusbjsindia.models.smartgirl.WorkshopBachListResponseModel;
import com.octopusbjsindia.presenter.SmartGirlWorkshopListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.WorkshopBatchListRecyclerAdapter;
import com.octopusbjsindia.view.fragments.smartgirlfragment.MemberListFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PostFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PreFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.PreTestTrainingFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.RegisterTrainerFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.WorkshopPostFeedbackFragment;
import com.octopusbjsindia.view.fragments.smartgirlfragment.WorkshopPreFeedbackFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmartGirlWorkshopListActivity extends AppCompatActivity implements WorkshopBatchListRecyclerAdapter.OnRequestItemClicked, WorkshopBatchListRecyclerAdapter.OnApproveRejectClicked {
    //--Constant

    //------
    public EditText tv_startdate, tv_enddate;
    public SmartGirlWorkshopListPresenter presenter;
    public RecyclerView rv_trainerbactchlistview;
    public WorkshopBatchListRecyclerAdapter trainerBatchListRecyclerAdapter;
    private List<WorkshopBachList> dataList = new ArrayList<>();
    PopupMenu popup;
    private ImageView toolbar_back_action, toolbar_edit_action;
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


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getBatchList();
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
        toolbar_edit_action.setVisibility(View.INVISIBLE);
        toolbar_edit_action.setImageResource(R.drawable.ic_plus);
        presenter = new SmartGirlWorkshopListPresenter(this);
        //setMasterData();
        //---
        progressBar = findViewById(R.id.ly_progress_bar);
        ly_no_data = findViewById(R.id.ly_no_data);
        rv_trainerbactchlistview = findViewById(R.id.rv_trainerbactchlistview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rv_trainerbactchlistview.setLayoutManager(layoutManager);

        trainerBatchListRecyclerAdapter = new WorkshopBatchListRecyclerAdapter(this, dataList,
                this, this);
        rv_trainerbactchlistview.setAdapter(trainerBatchListRecyclerAdapter);
        //-------

        presenter.getBatchList();
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    public void showReceivedBatchList(WorkshopBachListResponseModel trainerBachListResponseModelReceived) {
        Util.logger("received workshop -", "---");
        ly_no_data.setVisibility(View.GONE);
        //tmUserLeaveApplicationsList = data;
        trainerBachListResponseModel = trainerBachListResponseModelReceived;
        dataList.clear();
        dataList.addAll(trainerBachListResponseModel.getWorkshopBachLists());
        trainerBatchListRecyclerAdapter.notifyDataSetChanged();
            if (trainerBachListResponseModel.getWorkshopBachLists()!=null&&trainerBachListResponseModel.getWorkshopBachLists().size()<1){
                ly_no_data.setVisibility(View.VISIBLE);
            }

        if (dataList!=null) {
            tvTitle.setText("Workshop List" + "("+dataList.size()+")");
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
        String paramjson = new Gson().toJson(trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition));
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
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(pos).get_id();
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

        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        bundle.putString("batch_id", batchId);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).getBeneficiariesList());
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
    }

    public void addOrganiserFeedbackFragment(int adapterPosition){
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.ORGANISER_FEEDBACK_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"organizerFeedBackStatus");
        mContext.startActivity(intent);
    }
    public void addParentFeedbackFragment(int adapterPosition){
        Intent intent = new Intent(mContext, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID,Constants.SmartGirlModule.PARENTS_FEEDBACK_FORM);
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"parentFeedBackStatus");
        mContext.startActivity(intent);
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
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"preFeedBackStatus");
        mContext.startActivity(intent);
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
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"postFeedBackStatus");
        mContext.startActivity(intent);
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
        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
        intent.putExtra(Constants.SmartGirlModule.BATCH_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.WORKSHOP_ID,batchId);
        intent.putExtra(Constants.SmartGirlModule.FORM_STATUS,"preTestStatus");
        mContext.startActivity(intent);
    }

    //pre test
    public void addRegisterTrainerFragment(int adapterPosition) {
        Bundle bundle = new Bundle();

        String batchId = trainerBachListResponseModel.getWorkshopBachLists().get(adapterPosition).get_id();
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

            try {
                tvTitle.setText("Workshop List");
                if (dataList!=null) {
                    tvTitle.setText("Workshop List" + "("+dataList.size()+")");
                }
                fManager.popBackStackImmediate();
                rv_trainerbactchlistview.setVisibility(View.VISIBLE);

            } catch (IllegalStateException e) {
                Log.e("TAG", e.getMessage());
            }
        }


    }

    public void showToastMessage(String message) {
        Util.showToast(message, mContext);
        CloseFragment();
    }
    public void refreshData(){
        presenter.getBatchList();
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
        if (requestCode == Constants.SmartGirlModule.BATCH_WORKSHOP_RESULT && data != null) {
presenter.getBatchList();
        }
    }

    public void showNoData() {
        ly_no_data.setVisibility(View.VISIBLE);
    }
}
