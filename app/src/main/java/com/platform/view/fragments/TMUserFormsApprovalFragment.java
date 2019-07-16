package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.tm.LandingPageRequest;
import com.platform.models.tm.TMApprovalRequestModel;
import com.platform.models.tm.TMUserFormsApprovalRequest;
import com.platform.presenter.TMUserFormsApprovalFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.adapters.TMPendingApprovalPageRecyclerAdapter;
import com.platform.view.adapters.TMUserFormsApprovalRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserFormsApprovalFragment extends Fragment implements TMUserFormsApprovalRecyclerAdapter.OnRequestItemClicked, TMUserFormsApprovalRecyclerAdapter.OnApproveRejectClicked {

    private View approvalsFragmentView;
    private Toolbar toolbar;
    private RecyclerView rvPendingRequests;
    private String strTitle,filterTypeRequest;
    TMUserFormsApprovalFragmentPresenter tmUserFormsApprovalFragmentPresenter;
    TMUserFormsApprovalRecyclerAdapter tmUserFormsApprovalRecyclerAdapter;
    JSONObject requetsObject;
    private List<TMUserFormsApprovalRequest> tmUserFormsApplicationsList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (!(bundle == null))
        {
            strTitle =  bundle.getString("filter_type","");
            filterTypeRequest = bundle.getString("filter_type_request", "");
            Util.logger(getActivity().getLocalClassName().toString(),filterTypeRequest);
            try {
                requetsObject = new JSONObject(filterTypeRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.event_approvals_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        approvalsFragmentView = inflater.inflate(R.layout.fragment_user_forms_approval, container, false);
        toolbar = approvalsFragmentView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.toolbar_title);
            title.setText(strTitle);

        }
        rvPendingRequests = approvalsFragmentView.findViewById(R.id.rv_pendingapprovalpageview);
        //rvPendingRequests.setGroupIndicator(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvPendingRequests.setLayoutManager(layoutManager);
        return approvalsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tmUserFormsApprovalFragmentPresenter = new TMUserFormsApprovalFragmentPresenter(this);
        tmUserFormsApprovalFragmentPresenter.getAllPendingRequests(requetsObject);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showFetchedUserProfileForApproval(List<TMUserFormsApprovalRequest> data) {
        tmUserFormsApplicationsList =data;
        tmUserFormsApprovalRecyclerAdapter = new TMUserFormsApprovalRecyclerAdapter(getActivity(), data,
                this,this);
        rvPendingRequests.setAdapter(tmUserFormsApprovalRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {
        Util.showToast("open form details here",getActivity());
    }

    public void updateRequestStatus(String response, int position) {
        Util.showToast("form is approved or rejected",getActivity());
    }

    @Override
    public void onApproveClicked(int pos) {
        Util.showToast("Form approve",getActivity());
        TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            tmApprovalRequestModel.setType("form");
            tmApprovalRequestModel.setApprove_type("approved");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type("");
            tmApprovalRequestModel.setStartdate("");
            tmApprovalRequestModel.setEnddate("");
            tmApprovalRequestModel.setId(""+tmUserFormsApplicationsList.get(pos).getForm_detail().get(0).get_id().get$oid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmUserFormsApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel),pos);
    }

    @Override
    public void onRejectClicked(int pos) {
        Util.showToast("Form Reject",getActivity());
        TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            tmApprovalRequestModel.setType("form");
            tmApprovalRequestModel.setApprove_type("rejected");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type("");
            tmApprovalRequestModel.setStartdate("");
            tmApprovalRequestModel.setEnddate("");
            tmApprovalRequestModel.setId(""+tmUserFormsApplicationsList.get(pos).getForm_detail().get(0).get_id().get$oid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmUserFormsApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel),pos);
    }

    public void onReceiveReason(String s, int pos) {
    }
}
