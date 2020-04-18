package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.smartgirl.BeneficiariesList;
import com.octopusbjsindia.models.smartgirl.SGTrainersList;
import com.octopusbjsindia.models.smartgirl.SGTrainersListResponseModel;
import com.octopusbjsindia.models.smartgirl.TrainerList;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.AllTrainersListRecyclerAdapter;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.BeneficiaryListRecyclerAdapter;
import com.octopusbjsindia.view.adapters.smartGirlAdapters.MemberListRecyclerAdapter;

import java.util.List;

public class AllTrainerListFragment extends Fragment implements View.OnClickListener, MemberListRecyclerAdapter.OnRequestItemClicked, com.octopusbjsindia.view.adapters.smartGirlAdapters.BeneficiaryListRecyclerAdapter.OnRequestItemClicked, AllTrainersListRecyclerAdapter.OnRequestItemClicked {

    public AllTrainersListRecyclerAdapter memberListRecyclerAdapter;
    public BeneficiaryListRecyclerAdapter BeneficiaryListRecyclerAdapter;
    View view;
    String batchId, memberListStr, listType;
    RecyclerView rv_member_listview;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            batchId = getArguments().getString("batch_id");
            Log.d("batch_id received", "-> " + batchId);
            memberListStr = batchId = getArguments().getString("memberList");
            listType = batchId = getArguments().getString("listType");
            /*bundle.putString("memberList", jsonInString);
            bundle.putString("listtype", Constants.SmartGirlModule.TRAINER_lIST);*/

        }
        if (listType.equalsIgnoreCase(Constants.SmartGirlModule.TRAINER_lIST)) {

            /*List<TrainerList> trainerLists = new Gson().fromJson(memberListStr, new TypeToken<List<TrainerList>>() {
            }.getType());*/
            SGTrainersListResponseModel sgTrainersListResponseModel
                    = new Gson().fromJson(memberListStr, SGTrainersListResponseModel.class);
            List<SGTrainersList> trainerLists =  sgTrainersListResponseModel.getSgTrainersListList();

            memberListRecyclerAdapter = new AllTrainersListRecyclerAdapter(getActivity(), trainerLists,
                    this);
            rv_member_listview.setAdapter(memberListRecyclerAdapter);
            Log.d("listSize", String.valueOf(trainerLists.size()));
        } else if (listType.equalsIgnoreCase(Constants.SmartGirlModule.BENEFICIARY_lIST)) {

            List<BeneficiariesList> trainerLists = new Gson().fromJson(memberListStr, new TypeToken<List<BeneficiariesList>>() {
            }.getType());
            BeneficiaryListRecyclerAdapter = new BeneficiaryListRecyclerAdapter(getActivity(), trainerLists,
                    this);
            rv_member_listview.setAdapter(memberListRecyclerAdapter);
            Log.d("listSize", String.valueOf(trainerLists.size()));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                /*if (isAllInputsValid()) {
                    ((TrainerBatchListActivity) getActivity()).submitFeedbsckToBatch(0, 1, new Gson().toJson(getFeedbackReqJson(0)));
                }*/
                break;
            case R.id.tv_startdate:

                break;
            case R.id.tv_enddate:

                break;
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
    public void onItemClicked(int pos) {

    }
}
