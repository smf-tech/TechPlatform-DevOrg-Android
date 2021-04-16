package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.smartgirl.BeneficiariesList;
import com.octopusbjsindia.models.smartgirl.TrainerList;
import com.octopusbjsindia.presenter.MemberListFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.TrainerBatchListActivity;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.BeneficiaryListRecyclerAdapter;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.MemberListRecyclerAdapter;

import java.util.List;

public class MemberListFragment extends Fragment implements View.OnClickListener, MemberListRecyclerAdapter.OnRequestItemClicked, com.octopusbjsindia.view.adapters.smartGirlAdapters.BeneficiaryListRecyclerAdapter.OnRequestItemClicked {
    public MemberListRecyclerAdapter memberListRecyclerAdapter;
    public BeneficiaryListRecyclerAdapter BeneficiaryListRecyclerAdapter;
    View view;
    String requestType ="" ,requestTypeId = "",requestDataType="",requestEmailId= "";
    String batchId, memberListStr, listType;
    RecyclerView rv_member_listview;
    private List<TrainerList> trainerLists;
    private List<TrainerList> trainerListsforprepost;
    private List<BeneficiariesList> beneficiariesLists;
    private List<BeneficiariesList> beneficiariesListsforpreorpost;
    private FloatingActionButton fb_email_data;
    private MemberListFragmentPresenter memberListFragmentPresenter;
    private MemberListFragment fragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_memberlist_layout, container, false);
        rv_member_listview = view.findViewById(R.id.rv_member_listview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rv_member_listview.setLayoutManager(layoutManager);
        fb_email_data  = view.findViewById(R.id.fb_email);
        fb_email_data.setOnClickListener(this);
        memberListFragmentPresenter = new MemberListFragmentPresenter(this);
        fragment = this;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            batchId = getArguments().getString("batch_id");
            Log.d("batch_id received", "-> " + batchId);
            requestTypeId = batchId;
            memberListStr = getArguments().getString("memberList");
            listType =  getArguments().getString("listType");
            /*bundle.putString("memberList", jsonInString);
            bundle.putString("listtype", Constants.SmartGirlModule.TRAINER_lIST);*/

        }
        if (listType.equalsIgnoreCase(Constants.SmartGirlModule.TRAINER_lIST)) {
            requestType = "batch";
            trainerLists = new Gson().fromJson(memberListStr, new TypeToken<List<TrainerList>>() {
            }.getType());

            memberListRecyclerAdapter = new MemberListRecyclerAdapter(getActivity(), trainerLists,
                    this);
            rv_member_listview.setAdapter(memberListRecyclerAdapter);
            Log.d("listSize", String.valueOf(trainerLists.size()));
        } else if (listType.equalsIgnoreCase(Constants.SmartGirlModule.BENEFICIARY_lIST)) {
            requestType = "workshop";
            beneficiariesLists = new Gson().fromJson(memberListStr, new TypeToken<List<BeneficiariesList>>() {
            }.getType());

            /*for (int i = 0; i < beneficiariesLists.size(); i++) {
                beneficiariesListsforpreorpost.add(beneficiariesLists.get(i));
            }*/

            BeneficiaryListRecyclerAdapter = new BeneficiaryListRecyclerAdapter(getActivity(), beneficiariesLists,
                    this);
            rv_member_listview.setAdapter(BeneficiaryListRecyclerAdapter);
            Log.d("listSize", String.valueOf(beneficiariesLists.size()));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_email:
                /*if (isAllInputsValid()) {
                    ((TrainerBatchListActivity) getActivity()).submitFeedbsckToBatch(0, 1, new Gson().toJson(getFeedbackReqJson(0)));
                }*/

                Util.showEnterEmailDialog(getActivity(),-1,fragment);

                break;
            case R.id.tv_startdate:

                break;
            case R.id.tv_enddate:

                break;
        }
    }

    public JsonObject getFeedbackReqJson() {
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("type", requestType);
        requestObject.addProperty("typeId", requestTypeId);
        requestObject.addProperty("dataType", requestDataType);
        requestObject.addProperty("emailid", requestEmailId);


        /*"type": "workshop",  //"batch"
                "typeId": "5ffd85bb5c9e305733574ed3",
                "dataType": "pre", // "post"
                "emailid":"rbisen@bjsindia.org"*/

        return requestObject;
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
    public void onItemClicked(int pos) {
        if (listType.equalsIgnoreCase(Constants.SmartGirlModule.TRAINER_lIST)) {
            TrainerList mtrainerList = trainerLists.get(pos);
            ((TrainerBatchListActivity) getActivity()).addTrainingMockTestFragment(pos,
                    mtrainerList.getUserId(),batchId,mtrainerList.getPhone());
            /*"trainer_id":"5e4b73ec515e88607566f393",
                    "batch_id":"5e5904d8fcba8f309f672b48",̥
                    "trainer_phone":"9881499768",*/
        } else if (listType.equalsIgnoreCase(Constants.SmartGirlModule.BENEFICIARY_lIST)) {
            //BeneficiariesList beneficiariesList = beneficiariesLists.get(pos);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Util.showToast("onResume Member list",getActivity());
        if (listType.equalsIgnoreCase(Constants.SmartGirlModule.BENEFICIARY_lIST)) {

        }else {
            ((TrainerBatchListActivity) getActivity()).changeTitle("Member List");

        }
        showEmailButton();
    }

    public void showToastMessage(String message) {
        Util.showToast(message, getActivity());
    }

    public void onReceiveEmailId(String strEmailId, int feebackType) {
        if (feebackType ==1){
            requestDataType = "pre";
        }else {
            requestDataType = "post";
        }
        if (TextUtils.isEmpty(strEmailId)){
            showToastMessage("Please enter valid email");
        }else {
            requestEmailId = strEmailId;
            memberListFragmentPresenter.sendFeedbackEmail(new Gson().toJson(getFeedbackReqJson()));
        }

    }
    public void showEmailButton(){
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if(roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();

            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode()== Constants.SmartGirlModule.ACCESS_CODE_EMAIL_FEEDBACK){
                    fb_email_data.setVisibility(View.VISIBLE);
                }else {
                    fb_email_data.setVisibility(View.GONE);
                }
            }
        }
    }

}
