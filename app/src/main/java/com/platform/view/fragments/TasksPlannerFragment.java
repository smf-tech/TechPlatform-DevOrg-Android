package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.platform.R;

public class TasksPlannerFragment extends Fragment {

    public TasksPlannerFragment() {
        // Required empty public constructor
    }

    public static TasksPlannerFragment newInstance() {
        return  new TasksPlannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks_planner, container, false);
    }
}
