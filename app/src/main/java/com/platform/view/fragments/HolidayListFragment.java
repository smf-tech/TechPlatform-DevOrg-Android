package com.platform.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.view.adapters.HolidayListAdapter;

import java.util.ArrayList;

public class HolidayListFragment extends Fragment {

    public HolidayListFragment() {
        // Required empty public constructor
    }

    public static HolidayListFragment newInstance() {
        return new HolidayListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holiday_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView leavesList = view.findViewById(R.id.rv_holiday_list);

        ArrayList<String> leaves = new ArrayList<>();
        leaves.add("1");
        leaves.add("2");
        HolidayListAdapter adapter = new HolidayListAdapter(getActivity(), leaves);
        leavesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        leavesList.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
