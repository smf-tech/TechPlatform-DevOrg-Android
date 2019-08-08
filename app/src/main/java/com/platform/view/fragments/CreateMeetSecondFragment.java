package com.platform.view.fragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

public class CreateMeetSecondFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {

    private Button btnSecondPartMeet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_meet_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setMeetData();
    }

    private void init(View view) {

        btnSecondPartMeet = view.findViewById(R.id.btn_second_part_meet);
        btnSecondPartMeet.setOnClickListener(this);
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("12/08/2019");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_second_part_meet:
                ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetThirdFragment");
                break;
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }
}
