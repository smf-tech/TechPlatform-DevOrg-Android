package com.platform.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.tm.TMApprovalRequestModel;
import com.platform.models.tm.TMUserAttendanceApprovalRequest;
import com.platform.presenter.TMUserAttendenceApprovalFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.adapters.TMUserAttendanceApprovalRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TMUserAttendanceApprovalFragment extends Fragment implements TMUserAttendanceApprovalRecyclerAdapter.OnRequestItemClicked, TMUserAttendanceApprovalRecyclerAdapter.OnApproveRejectClicked {

    JSONObject requetsObject;
    private View approvalsFragmentView;
    private Toolbar toolbar;
    private RecyclerView rvAttendenceList;
    private TMUserAttendenceApprovalFragmentPresenter tmUserAttendenceApprovalFragmentPresenter;
    private TMUserAttendanceApprovalRecyclerAdapter tmUserAttendanceApprovalRecyclerAdapter;
    private String strTitle, filterTypeRequest;
    private List<TMUserAttendanceApprovalRequest> tmUserAttendanceApplicationsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (!(bundle == null)) {
            strTitle = bundle.getString("filter_type", "");
            filterTypeRequest = bundle.getString("filter_type_request", "");

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

        approvalsFragmentView = inflater.inflate(R.layout.fragment_user_attendance_approval, container, false);
        toolbar = approvalsFragmentView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView title = toolbar.findViewById(R.id.toolbar_title);
            title.setText(strTitle);
        }

        ImageView img_back = toolbar.findViewById(R.id.toolbar_back_action);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        rvAttendenceList = approvalsFragmentView.findViewById(R.id.rv_pendingapprovalpageview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvAttendenceList.setLayoutManager(layoutManager);

        return approvalsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tmUserAttendenceApprovalFragmentPresenter = new TMUserAttendenceApprovalFragmentPresenter(this);
        tmUserAttendenceApprovalFragmentPresenter.getAllPendingRequests(requetsObject);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showFetchedUserProfileForApproval(List<TMUserAttendanceApprovalRequest> data) {
        tmUserAttendanceApplicationsList = data;
        tmUserAttendanceApprovalRecyclerAdapter = new TMUserAttendanceApprovalRecyclerAdapter(getActivity(), data,
                this, this);
        rvAttendenceList.setAdapter(tmUserAttendanceApprovalRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {
        //Util.showToast("Attendence item click", getActivity());
    }

    public void updateRequestStatus(String response, int position) {
        tmUserAttendanceApplicationsList.remove(position);
        tmUserAttendanceApprovalRecyclerAdapter.notifyItemRemoved(position);

        Util.showSuccessFailureToast(response, getActivity(), getActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content));
    }

    @Override
    public void onApproveClicked(int pos) {

        TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
        try {
            tmApprovalRequestModel.setType("attendance");
            tmApprovalRequestModel.setApprove_type("approved");
            tmApprovalRequestModel.setReason("");
            tmApprovalRequestModel.setLeave_type("");
            tmApprovalRequestModel.setStartdate("");
            tmApprovalRequestModel.setEnddate("");
            tmApprovalRequestModel.setId(tmUserAttendanceApplicationsList.get(pos).get_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String jsonInString = gson.toJson(tmApprovalRequestModel);
        tmUserAttendenceApprovalFragmentPresenter.approveRejectRequest(jsonInString, pos);
    }

    @Override
    public void onRejectClicked(int pos) {

        String strReason = Util.showReasonDialog(getActivity(), pos, this);
    }

    public void onReceiveReason(String s, int pos) {
        rejectApprovalRequest(s, pos);
    }

    private void rejectApprovalRequest(String strReason, int pos) {
        if (!TextUtils.isEmpty(strReason)) {
            TMApprovalRequestModel tmApprovalRequestModel = new TMApprovalRequestModel();
            try {
                tmApprovalRequestModel.setType("attendance");
                tmApprovalRequestModel.setApprove_type("rejected");
                tmApprovalRequestModel.setReason(strReason);
                tmApprovalRequestModel.setLeave_type("");
                tmApprovalRequestModel.setStartdate("");
                tmApprovalRequestModel.setEnddate("");
                tmApprovalRequestModel.setId(tmUserAttendanceApplicationsList.get(pos).get_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String jsonInString = gson.toJson(tmApprovalRequestModel);
            Util.logger("reject request--------", Util.modelToJson(tmApprovalRequestModel));
            tmUserAttendenceApprovalFragmentPresenter.approveRejectRequest(jsonInString, pos);
        } else {
            Util.showSuccessFailureToast("Please enter reason to reject.",getActivity(),getActivity().getWindow().getDecorView()
                    .findViewById(android.R.id.content));
        }
    }
}
