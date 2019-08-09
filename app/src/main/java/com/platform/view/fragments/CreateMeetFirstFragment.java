package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.MatrimonyMeetPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateMeetFirstFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        APIDataListener {

    private Spinner meetTypeSpinner,stateSpinner,citySpinner;
    private Button btnFirstPartMeet;
    List<String> meetTypes = new ArrayList<>();
    List<String> meetStates = new ArrayList<>();
    List<String> meetDistricts = new ArrayList<>();
    private MatrimonyMeetPresenter matrimonyMeetPresenter;
    private String selectedMeetType, selectedState, selectedDistrict;
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
    }

    private void init(View view) {
        meetTypeSpinner = view.findViewById(R.id.spinner_meet_types);
        meetTypeSpinner.setOnItemSelectedListener(this);
        stateSpinner = view.findViewById(R.id.spinner_meet_state);
        stateSpinner.setOnItemSelectedListener(this);
        citySpinner = view.findViewById(R.id.spinner_meet_city);
        citySpinner.setOnItemSelectedListener(this);
        btnFirstPartMeet = view.findViewById(R.id.btn_first_part_meet);
        btnFirstPartMeet.setOnClickListener(this);

        meetTypes.add("Educated");
        meetTypes.add("Rural Area");
        meetTypes.add("Urban Area");
        ArrayAdapter<String> meetTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetTypes);
        meetTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetTypeSpinner.setAdapter(meetTypeAdapter);

        meetStates.add("Maharashtra");
        meetStates.add("Kerala");
        meetStates.add("Panjab");
        ArrayAdapter<String> meetStateAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetStates);
        meetStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(meetStateAdapter);

        meetDistricts.add("Pune");
        meetDistricts.add("Aurangabad");
        meetDistricts.add("Parbhani");
        ArrayAdapter<String> meetDistrictAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetDistricts);
        meetDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(meetDistrictAdapter);

//        matrimonyMeetPresenter = new MatrimonyMeetPresenter(this);
//        matrimonyMeetPresenter.getMeetTypes();
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("10/8/2019");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_first_part_meet:
                setMeetData();
                ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetSecondFragment");
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (matrimonyMeetPresenter != null) {
            matrimonyMeetPresenter.clearData();
            matrimonyMeetPresenter = null;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        UserInfo userInfo = Util.getUserObjectFromPref();
        if(requestID.equalsIgnoreCase(MatrimonyMeetPresenter.GET_MATRIMONY_MEET_TYPES)){
            meetTypes.clear();
            matrimonyMeetPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                    "5c4ab05cd503a372d0391467",
                    Constants.JurisdictionLevelName.STATE_LEVEL);
        }
        if(requestID.equalsIgnoreCase(MatrimonyMeetPresenter.GET_STATES)){
            meetStates.clear();
            matrimonyMeetPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                    "5c4ab05cd503a372d0391467",
                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
        }
        if(requestID.equalsIgnoreCase(MatrimonyMeetPresenter.GET_DISTRICTS)){
            meetDistricts.clear();
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_meet_types:
                selectedMeetType = meetTypes.get(i);
                break;
            case R.id.spinner_meet_state:
                selectedState = meetStates.get(i);
                break;
            case R.id.spinner_meet_city:
                selectedDistrict = meetDistricts.get(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
