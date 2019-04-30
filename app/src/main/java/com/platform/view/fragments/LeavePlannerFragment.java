package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.view.activities.GeneralActionsActivity;

public class LeavePlannerFragment extends Fragment implements View.OnClickListener {

    private TextView tvCLSLLeavesCount;
    private TextView tvPaidLeavesCount;
    private TextView tvUnPaidLeavesCount;
    private TextView tvCOffLeavesCount;
    private TextView tvTotalLeavesCount;
    private TextView tvNoLeavesBalance;
    private RelativeLayout rlLeavesCount;

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
        tvUnPaidLeavesCount = view.findViewById(R.id.tv_leaves_unpaid);
        tvCOffLeavesCount = view.findViewById(R.id.tv_leaves_com_off);
        tvTotalLeavesCount = view.findViewById(R.id.tv_total_leaves_count);
        tvNoLeavesBalance = view.findViewById(R.id.tv_no_leaves_balance);
        rlLeavesCount = view.findViewById(R.id.rl_leaves_count);

        TextView tvCheckLeaveDetailsLink = view.findViewById(R.id.tv_link_check_leaves);
        tvCheckLeaveDetailsLink.setOnClickListener(this);

        FloatingActionButton imgClickAddLeaves = view.findViewById(R.id.fab_add_leaves);
        imgClickAddLeaves.setOnClickListener(this);

        setUIData(new JsonObject());
    }

    private void setUIData(JsonObject leavesData) {
        if (leavesData != null) {
            rlLeavesCount.setVisibility(View.VISIBLE);
            tvCLSLLeavesCount.setText(String.valueOf(2));
            tvPaidLeavesCount.setText(String.valueOf(6));
            tvUnPaidLeavesCount.setText(String.valueOf(1));
            tvCOffLeavesCount.setText(String.valueOf(2));
            tvTotalLeavesCount.setText(String.valueOf(11));
        } else {
            rlLeavesCount.setVisibility(View.GONE);
            tvNoLeavesBalance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
        switch (v.getId()) {
            case R.id.tv_link_check_leaves:
                intent.putExtra("title", "Leave");
                intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                startActivity(intent);
                break;

            case R.id.fab_add_leaves:
                intent.putExtra("title", "Apply For Leave");
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(intent);
                break;
        }
    }
}
