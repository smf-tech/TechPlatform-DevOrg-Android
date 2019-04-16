package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AppliedLeavesAdapter;

import java.util.ArrayList;


public class LeavePlannerFragment extends Fragment {

    private boolean isDashboard;

    View view1;


    public LeavePlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_planner, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view1 = view.findViewById(R.id.layout_1);



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }
    }
        TextView tvLink = view.findViewById(R.id.tv_link_check_leaves);
        ImageView imgAddLeaves = view.findViewById(R.id.img_add_leaves);
        TextView tvCntCL = view.findViewById(R.id.tv_leaves_cl);
        TextView tvCntPaid = view.findViewById(R.id.tv_leaves_paid);
        TextView tvCntCoff = view.findViewById(R.id.tv_leaves_coff);
        TextView tvCntTotal = view.findViewById(R.id.tv_total_leaves_count);


        tvCntCL.setText("241");
        tvCntPaid.setText("08");
        tvCntCoff.setText("12");
        tvCntTotal.setText("20");

        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Leave");
                intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                startActivity(intent);
            }
        });

        imgAddLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Apply For Leave");
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(intent);
            }
        });


    }
}

