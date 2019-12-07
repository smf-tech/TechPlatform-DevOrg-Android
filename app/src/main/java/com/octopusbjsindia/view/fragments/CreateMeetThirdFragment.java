package com.octopusbjsindia.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.octopusbjsindia.R;

public class CreateMeetThirdFragment extends Fragment implements View.OnClickListener  {
    private Button btnBackToDashboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_meet_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        btnBackToDashboard = view.findViewById(R.id.btn_back_to_dashboard);
        btnBackToDashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
