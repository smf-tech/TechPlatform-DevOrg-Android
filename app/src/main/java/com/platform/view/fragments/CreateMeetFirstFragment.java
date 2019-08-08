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
import android.widget.Spinner;

import com.platform.R;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

public class CreateMeetFirstFragment extends Fragment implements View.OnClickListener {

    private Spinner meetTypeSpinner,stateSpinner,citySpinner;
    private Button btnFirstPartMeet;
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
        init(view);
        setMeetData();
    }

    private void init(View view) {
        meetTypeSpinner = view.findViewById(R.id.meet_types_spinner);
        stateSpinner = view.findViewById(R.id.meet_state_spinner);
        citySpinner = view.findViewById(R.id.meet_city_spinner);
        btnFirstPartMeet = view.findViewById(R.id.btn_first_part_meet);
        btnFirstPartMeet.setOnClickListener(this);
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("10/8/2019");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_first_part_meet:
//                Intent createMatrimonyIntent = new Intent(getActivity(), CreateMatrimonyMeetActivity.class);
//                createMatrimonyIntent.putExtra("SwitchToFragment", "CreateMeetSecondFragment");
//                startActivity(createMatrimonyIntent);
                ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetSecondFragment");
                break;
        }
    }
}
