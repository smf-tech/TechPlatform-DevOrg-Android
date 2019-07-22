package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.LeaveDataListener;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.leaves.LeaveType;
import com.platform.presenter.LeavesPresenter;
import com.platform.utility.Constants;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.GeneralActionsActivity;

import java.util.List;

public class LeavePlannerFragment extends Fragment implements View.OnClickListener, LeaveDataListener {

    private TextView tvCLSLLeavesCount;
    private TextView tvPaidLeavesCount;
    private TextView tvCOffLeavesCount;
    private TextView tvTotalLeavesCount;
    private TextView tvNoLeavesBalance;
    private RelativeLayout rlLeavesCount;
    private LeavesPresenter presenter;
    private String serverResponse;

    public LeavePlannerFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        tvCLSLLeavesCount = view.findViewById(R.id.tv_leaves_cl);
        tvPaidLeavesCount = view.findViewById(R.id.tv_leaves_paid);
        tvCOffLeavesCount = view.findViewById(R.id.tv_leaves_com_off);
        tvTotalLeavesCount = view.findViewById(R.id.tv_total_leaves_count);
        tvNoLeavesBalance = view.findViewById(R.id.tv_no_leaves_balance);
        rlLeavesCount = view.findViewById(R.id.rl_leaves_count);

        TextView tvCheckLeaveDetailsLink = view.findViewById(R.id.tv_link_check_leaves);
        tvCheckLeaveDetailsLink.setOnClickListener(this);

        FloatingActionButton imgClickAddLeaves = view.findViewById(R.id.fab_add_leaves);
        imgClickAddLeaves.setOnClickListener(this);
        presenter = new LeavesPresenter(this);
        presenter.getLeavesData();
        onSuccessListener(LeavesPresenter.GET_LEAVE_DETAILS, "");
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
        switch (v.getId()) {
            case R.id.tv_link_check_leaves:
                intent.putExtra("title", getString(R.string.leave));
                intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                startActivity(intent);
                break;

            case R.id.fab_add_leaves:
                intent.putExtra("title", getString(R.string.apply_leave));
                intent.putExtra("isEdit", false);
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                intent.putExtra("leaveDetail", serverResponse);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), error.getMessage(),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (LeavesPresenter.GET_LEAVE_DETAILS.equals(requestID)) {
            if (response != null) {
                serverResponse = response;
                serverResponse = "{\"year\": 2019,\"leaveTypes\": [{\"leaveType\": \"CL\",\"allocatedLeaves\": 2},{\"leaveType\": \"Paid\",\"allocatedLeaves\": 4}],\"balanceLeaves\": 10}";
                LeaveDetail leaveDetail = PlatformGson.getPlatformGsonInstance().fromJson(serverResponse, LeaveDetail.class);
                if (leaveDetail != null) {
                    rlLeavesCount.setVisibility(View.VISIBLE);
                    if (leaveDetail.getBalanceLeaves() != null) {
                        int totalBalanceLeaves = leaveDetail.getBalanceLeaves();
                        tvTotalLeavesCount.setText(String.valueOf(totalBalanceLeaves));
                    }
                    List<LeaveType> leaveTypes = leaveDetail.getLeaveTypes();
                    if (leaveTypes != null) {
                        for (LeaveType type : leaveTypes) {
                            if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_CL)) {
                                tvCLSLLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            } else if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_PAID)) {
                                tvPaidLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            } else if (type.getLeaveType().equalsIgnoreCase(Constants.Planner.LEAVE_TYPE_COMP_OFF)) {
                                tvCOffLeavesCount.setText(TextUtils.isEmpty(String.valueOf(type.getAllocatedLeaves()))
                                        ? "0" : String.valueOf(type.getAllocatedLeaves()));
                            }
                        }

                    }
                } else {

                    rlLeavesCount.setVisibility(View.GONE);
                    tvNoLeavesBalance.setVisibility(View.VISIBLE);
                }
            } else {

                rlLeavesCount.setVisibility(View.GONE);
                tvNoLeavesBalance.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.clearData();
        presenter = null;
    }

    @Override
    public void showProgressBar() {
        Util.showSimpleProgressDialog(getActivity(), null, getString(R.string.please_wait), false);

    }

    @Override
    public void hideProgressBar() {
        Util.removeSimpleProgressDialog();
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
