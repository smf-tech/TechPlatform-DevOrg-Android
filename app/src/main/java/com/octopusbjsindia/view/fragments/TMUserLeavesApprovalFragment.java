package com.octopusbjsindia.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.tm.TMApprovalRequestModel;
import com.octopusbjsindia.models.tm.TMUserLeaveApplications;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.TMUserLeavesApprovalFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.TMUserLeavesApprovalRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserLeavesApprovalFragment extends Fragment implements TMUserLeavesApprovalRecyclerAdapter.OnRequestItemClicked,
        TMUserLeavesApprovalRecyclerAdapter.OnApproveRejectClicked {

    private JSONObject requetsObject;
    TMUserLeavesApprovalRecyclerAdapter.OnApproveRejectClicked onApproveRejectClicked;
    private RecyclerView rvPendingRequests;
    private TMUserLeavesApprovalFragmentPresenter tmUserLeavesApprovalFragmentPresenter;
    private TMUserLeavesApprovalRecyclerAdapter tmUserLeavesApprovalRecyclerAdapter;
    private String strTitle, filterTypeRequest;
    private List<TMUserLeaveApplications> tmUserLeaveApplicationsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (!(bundle == null)) {
            strTitle = bundle.getString("filter_type", "");
            filterTypeRequest = bundle.getString("filter_type_request", "");
            try {
                requetsObject = new JSONObject(filterTypeRequest);
                UserInfo userInfo = Util.getUserObjectFromPref();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View approvalsFragmentView = inflater.inflate(R.layout.fragment_user_leaves_approval, container, false);
            TextView title = approvalsFragmentView.findViewById(R.id.toolbar_title);
            title.setText(strTitle);
        ImageView img_back = approvalsFragmentView.findViewById(R.id.toolbar_back_action);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        rvPendingRequests = approvalsFragmentView.findViewById(R.id.rv_pendingapprovalpageview);
        //rvPendingRequests.setGroupIndicator(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvPendingRequests.setLayoutManager(layoutManager);

        return approvalsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tmUserLeavesApprovalFragmentPresenter = new TMUserLeavesApprovalFragmentPresenter(this);
        tmUserLeavesApprovalFragmentPresenter.getAllPendingRequests(requetsObject);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showFetchedUserProfileForApproval(List<TMUserLeaveApplications> data) {
        Util.logger("Leaves -", "---");
        tmUserLeaveApplicationsList = data;
        tmUserLeavesApprovalRecyclerAdapter = new TMUserLeavesApprovalRecyclerAdapter(getActivity(), data,
                this, this);
        rvPendingRequests.setAdapter(tmUserLeavesApprovalRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {
        //Util.showToast("Leaves Approval item clicked", getActivity());
    }

    @Override
    public void onApproveClicked(int pos) {
        TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            //tmApprovalRequestModel.setType("leave");requetsObject.get("type");
            tmApprovalRequestModel.setType(requetsObject.getString("type"));
            tmApprovalRequestModel.setApprove_type("approved");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type(tmUserLeaveApplicationsList.get(pos).getLeave_type());
            tmApprovalRequestModel.setStartdate(tmUserLeaveApplicationsList.get(pos).getStartdate());
            tmApprovalRequestModel.setEnddate(tmUserLeaveApplicationsList.get(pos).getEnddate());
            tmApprovalRequestModel.setId(tmUserLeaveApplicationsList.get(pos).get_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmUserLeavesApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel), pos);
    }

    @Override
    public void onRejectClicked(int pos) {

        String strReason = Util.showReasonDialog(getActivity(), pos, this);
    }

    public void updateRequestStatus(String response, int position) {
        tmUserLeaveApplicationsList.remove(position);
        tmUserLeavesApprovalRecyclerAdapter.notifyItemRemoved(position);
        Util.showSuccessFailureToast(response, getActivity(), getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content));
        if (tmUserLeaveApplicationsList.size()<1) {
            getActivity().finish();
        }
    }

    private void rejectApprovalRequest(String strReason, int pos) {
        if (!TextUtils.isEmpty(strReason)) {
            TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
            try {
                //tmApprovalRequestModel.setType("leave");
                tmApprovalRequestModel.setType(requetsObject.getString("type"));
                tmApprovalRequestModel.setApprove_type("rejected");
                tmApprovalRequestModel.setReason(strReason);
                tmApprovalRequestModel.setLeave_type(tmUserLeaveApplicationsList.get(pos).getLeave_type());
                tmApprovalRequestModel.setStartdate(tmUserLeaveApplicationsList.get(pos).getStartdate());
                tmApprovalRequestModel.setEnddate(tmUserLeaveApplicationsList.get(pos).getEnddate());
                tmApprovalRequestModel.setId(tmUserLeaveApplicationsList.get(pos).get_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String jsonInString = gson.toJson(tmApprovalRequestModel);
            tmUserLeavesApprovalFragmentPresenter.approveRejectRequest(jsonInString, pos);
        } else {
            Util.showSuccessFailureToast("Please enter reason to reject.",getActivity(),getActivity().getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        }
    }

    public void onReceiveReason(String strReason, int pos) {
        rejectApprovalRequest(strReason, pos);
    }

}
