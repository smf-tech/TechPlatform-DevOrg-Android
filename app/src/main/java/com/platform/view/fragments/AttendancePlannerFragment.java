package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.platform.R;

public class AttendancePlannerFragment extends Fragment {

    public AttendancePlannerFragment() {
        // Required empty public constructor
    }

    public static AttendancePlannerFragment newInstance() {
        return new AttendancePlannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance_planner, container, false);
    }
}
