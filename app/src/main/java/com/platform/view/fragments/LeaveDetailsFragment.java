package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.platform.R;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AppliedLeavesAdapter;

import java.util.ArrayList;

public class LeaveDetailsFragment extends Fragment {


    public LeaveDetailsFragment() {
        // Required empty public constructor
    }


    public static LeaveDetailsFragment newInstance(String param1, String param2) {
        LeaveDetailsFragment fragment = new LeaveDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_applied_leaves_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView toolBarMenu = getActivity().findViewById(R.id.toolbar_edit_action);

        RecyclerView leavesList = view.findViewById(R.id.rv_applied_leaves_list);
        ImageView imgAdddLeaves = view.findViewById(R.id.iv_add_leaves);
        imgAdddLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Apply For Leave");
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(intent);
            }
        });

        toolBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Holiday List");
                intent.putExtra("switch_fragments", "HolidayListFragment");
                startActivity(intent);
            }
        });

        ArrayList<String> leaves = new ArrayList<>();
        leaves.add("1");
        leaves.add("2");
        AppliedLeavesAdapter adapter = new AppliedLeavesAdapter(getActivity(),leaves);
        leavesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        leavesList.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
