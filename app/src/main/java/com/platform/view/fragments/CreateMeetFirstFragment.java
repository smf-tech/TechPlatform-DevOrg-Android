package com.platform.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

public class CreateMeetFirstFragment extends Fragment {
    CreateMatrimonyMeetActivity createMatrimonyMeetActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_meet_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setMeetData();
    }

    private void init() {
        createMatrimonyMeetActivity = new CreateMatrimonyMeetActivity();
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("");
    }
}
