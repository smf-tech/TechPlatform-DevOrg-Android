package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.CreateMeetFirstFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CreateMeetFirstFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        APIDataListener {

    private Spinner meetTypeSpinner,stateSpinner,citySpinner;
    private Button btnFirstPartMeet;
    List<String> meetTypes = new ArrayList<>();
    List<String> meetStates = new ArrayList<>();
    List<String> meetDistricts = new ArrayList<>();
    private CreateMeetFirstFragmentPresenter matrimonyMeetFirstFragmentPresenter;
    private String selectedMeetType, selectedState, selectedDistrict;
    private ArrayAdapter<String> meetTypeAdapter, meetStateAdapter, meetDistrictAdapter;
    private EditText edtMeetDate,edtMeetTime,edtMeetRegStartDate,edtMeetRegEndDate;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
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
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        meetTypeSpinner = view.findViewById(R.id.spinner_meet_types);
        meetTypeSpinner.setOnItemSelectedListener(this);
        stateSpinner = view.findViewById(R.id.spinner_meet_state);
        stateSpinner.setOnItemSelectedListener(this);
        citySpinner = view.findViewById(R.id.spinner_meet_city);
        citySpinner.setOnItemSelectedListener(this);
        btnFirstPartMeet = view.findViewById(R.id.btn_first_part_meet);
        btnFirstPartMeet.setOnClickListener(this);
        edtMeetDate = view.findViewById(R.id.edt_meet_date);
        edtMeetDate.setOnClickListener(this);
        edtMeetTime = view.findViewById(R.id.edt_meet_time);
        edtMeetTime.setOnClickListener(this);
        edtMeetRegStartDate = view.findViewById(R.id.edt_meet_reg_date);
        edtMeetRegStartDate.setOnClickListener(this);
        edtMeetRegEndDate = view.findViewById(R.id.edt_meet_reg_end_date);
        edtMeetRegEndDate.setOnClickListener(this);

        meetTypes.add("Meet Type");
        meetTypes.add("Educated");
        meetTypes.add("Rural Area");
        meetTypes.add("Urban Area");
        meetTypeAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetTypes);
        meetTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meetTypeSpinner.setAdapter(meetTypeAdapter);

        meetStateAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetStates);
        meetStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(meetStateAdapter);

        meetDistricts.add("District");
        meetDistrictAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, meetDistricts);
        meetDistrictAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(meetDistrictAdapter);

        matrimonyMeetFirstFragmentPresenter = new CreateMeetFirstFragmentPresenter(this);
        //matrimonyMeetFirstFragmentPresenter.getMeetTypes();
        UserInfo userInfo = Util.getUserObjectFromPref();
        matrimonyMeetFirstFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                "5c4ab05cd503a372d0391467",
                Constants.JurisdictionLevelName.STATE_LEVEL);
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetDateTime("10/8/2019");
    }

    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetStates.clear();
                    meetStates.add("State");
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        meetStates.add(location.getState().getName());
                    }
                    meetStateAdapter.notifyDataSetChanged();
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetDistricts.clear();
                    meetDistricts.add("District");
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                            if (selectedState.equalsIgnoreCase(location.getState().getName())) {
                                meetDistricts.add(location.getDistrict().getName());
                        }
                    }
                    meetDistrictAdapter.notifyDataSetChanged();
//                    matrimonyMeetPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
//                            "5c4ab05cd503a372d0391467",
//                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }
                break;

            default:
                break;
        }
    }

    public void setMatrimonyMeetTypes(){
        meetTypes.clear();
        //UserInfo userInfo = Util.getUserObjectFromPref();
//        matrimonyMeetPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
//                "5c4ab05cd503a372d0391467",
//                Constants.JurisdictionLevelName.STATE_LEVEL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_first_part_meet:
                setMeetData();
                ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetSecondFragment");
                break;
            case R.id.edt_meet_date:
                Util.showDateDialogMin(getActivity(), edtMeetDate);
                break;
            case R.id.edt_meet_time:
                Util.showTimeDialog(getActivity(), edtMeetTime);
                break;
            case R.id.edt_meet_reg_date:
                Util.showDateDialogEnableBeforeDefinedDate(getActivity(),edtMeetRegStartDate, edtMeetDate.getText().toString());
                break;
            case R.id.edt_meet_reg_end_date:
                Util.showDateDialogEnableBetweenMinMax(getActivity(), edtMeetRegEndDate, edtMeetRegStartDate.getText().toString(),
                        edtMeetDate.getText().toString());
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
        if (matrimonyMeetFirstFragmentPresenter != null) {
            matrimonyMeetFirstFragmentPresenter.clearData();
            matrimonyMeetFirstFragmentPresenter = null;
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
        if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_MATRIMONY_MEET_TYPES)){

        }
        if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_STATES)){

        }
        if(requestID.equalsIgnoreCase(CreateMeetFirstFragmentPresenter.GET_DISTRICTS)){

        }
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        UserInfo userInfo = Util.getUserObjectFromPref();
        switch (adapterView.getId()) {
            case R.id.spinner_meet_types:
                selectedMeetType = meetTypes.get(i);
                break;
            case R.id.spinner_meet_state:
                selectedState = meetStates.get(i);
                if(selectedState!="" && selectedState!="State") {
                    matrimonyMeetFirstFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                            "5c4ab05cd503a372d0391467",
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }
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
