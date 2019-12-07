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

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.tm.TMApprovalRequestModel;
import com.octopusbjsindia.models.tm.TMUserFormsApprovalRequest;
import com.octopusbjsindia.presenter.TMUserFormsApprovalFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.TMUserFormsApprovalRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserFormsApprovalFragment extends Fragment implements TMUserFormsApprovalRecyclerAdapter.OnRequestItemClicked, TMUserFormsApprovalRecyclerAdapter.OnApproveRejectClicked {

    private View approvalsFragmentView;
    private RecyclerView rvPendingRequests;
    private String strTitle, filterTypeRequest;
    TMUserFormsApprovalFragmentPresenter tmUserFormsApprovalFragmentPresenter;
    TMUserFormsApprovalRecyclerAdapter tmUserFormsApprovalRecyclerAdapter;
    JSONObject requetsObject;
    private List<TMUserFormsApprovalRequest.Form_detail> tmUserFormsApplicationsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (!(bundle == null)) {
            strTitle = bundle.getString("filter_type", "");
            filterTypeRequest = bundle.getString("filter_type_request", "");
            Util.logger(getActivity().getLocalClassName(), filterTypeRequest);
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
        tmUserFormsApprovalFragmentPresenter = new TMUserFormsApprovalFragmentPresenter(this);
        tmUserFormsApprovalFragmentPresenter.getAllPendingRequests(requetsObject);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showFetchedUserProfileForApproval(List<TMUserFormsApprovalRequest.Form_detail> data) {
        tmUserFormsApplicationsList = data;
        tmUserFormsApprovalRecyclerAdapter = new TMUserFormsApprovalRecyclerAdapter(getActivity(), data,
                this, this);
        rvPendingRequests.setAdapter(tmUserFormsApprovalRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {
        Util.showToast("open form details here", getActivity());
    }

    public void updateRequestStatus(String response, int position) {
        //Util.showToast("form is approved or rejected",getActivity());
        tmUserFormsApplicationsList.remove(position);
        tmUserFormsApprovalRecyclerAdapter.notifyItemRemoved(position);
        Util.showSuccessFailureToast(response, getActivity(), getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content));
    }

    @Override
    public void onApproveClicked(int pos) {

        TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            tmApprovalRequestModel.setType("form");
            tmApprovalRequestModel.setApprove_type("approved");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type("");
            tmApprovalRequestModel.setStartdate("");
            tmApprovalRequestModel.setEnddate("");
            tmApprovalRequestModel.setUserId(requetsObject.getString("user_id"));
            tmApprovalRequestModel.setId("" + tmUserFormsApplicationsList.get(pos).get_id().get$oid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmUserFormsApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel), pos);
    }

    @Override
    public void onRejectClicked(int pos) {
        //Util.showToast("Form Reject",getActivity());
        String strReason = Util.showReasonDialog(getActivity(), pos, this);

  /*      TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            tmApprovalRequestModel.setType("form");
            tmApprovalRequestModel.setApprove_type("rejected");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type("");
            tmApprovalRequestModel.setStartdate("");
            tmApprovalRequestModel.setEnddate("");
            tmApprovalRequestModel.setId(""+tmUserFormsApplicationsList.get(pos).get_id().get$oid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tmUserFormsApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel),pos);*/

    }

    public void onReceiveReason(String s, int pos) {
        rejectApprovalRequest(s, pos);
    }

    public void rejectApprovalRequest(String strReason, int pos) {
        if (!TextUtils.isEmpty(strReason)) {

            TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
            try {
                tmApprovalRequestModel.setType("form");
                tmApprovalRequestModel.setApprove_type("rejected");
                tmApprovalRequestModel.setReason("");
                tmApprovalRequestModel.setLeave_type("");
                tmApprovalRequestModel.setStartdate("");
                tmApprovalRequestModel.setEnddate("");
                tmApprovalRequestModel.setId("" + tmUserFormsApplicationsList.get(pos).get_id().get$oid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            tmUserFormsApprovalFragmentPresenter.approveRejectRequest(Util.modelToJson(tmApprovalRequestModel), pos);
        } else {
            Util.showSuccessFailureToast("Please enter reason to reject.", getActivity(), getActivity().getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        }
    }
}
