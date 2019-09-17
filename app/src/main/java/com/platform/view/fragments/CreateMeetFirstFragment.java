package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.Matrimony.MeetLocation;
import com.platform.models.Matrimony.MeetSchedule;
import com.platform.models.Matrimony.MeetType;
import com.platform.models.Matrimony.RegistrationSchedule;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.CreateMeetFirstFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateMeetFirstFragment extends Fragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, APIDataListener, CustomSpinnerListener {

    private TextView tvMeetType ,tvMeetCountry, tvMeetState, tvMeetCity;
    private Button btnFirstPartMeet;
    private ArrayList<CustomSpinnerObject> meetTypesList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetCountryList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetCityList = new ArrayList<>();
    private CreateMeetFirstFragmentPresenter matrimonyMeetFirstFragmentPresenter;
    private String selectedMeetType, selectedCountry, selectedState, selectedCity,
            selectedCountryId, selectedStateId, selectedCityId;
    private EditText edtMeetName, edtMeetVenue,edtMeetDate, edtMeetStartTime, edtMeetEndTime,edtMeetRegStartDate,
            edtMeetRegEndDate, edtRegAmt;
    private RadioGroup rgPaidFree, rgOnlinePayment;
    private RadioButton rbPaid, rbFree, rbOnlineYes, rbOnlineNo;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private boolean isRegPaid, isOnlinePaymentAllowed;
    private int isPaidFreeRGChecked = 0, isOnlinePaymentRGChecked = 0;
    UserInfo userInfo;
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
        tvMeetType = view.findViewById(R.id.tv_meet_types);
        tvMeetType.setOnClickListener(this);
        tvMeetCountry = view.findViewById(R.id.tv_meet_country);
        tvMeetCountry.setOnClickListener(this);
        tvMeetState = view.findViewById(R.id.tv_meet_state);
        tvMeetState.setOnClickListener(this);
        tvMeetCity = view.findViewById(R.id.tv_meet_city);
        tvMeetCity.setOnClickListener(this);
        btnFirstPartMeet = view.findViewById(R.id.btn_first_part_meet);
        btnFirstPartMeet.setOnClickListener(this);
        edtMeetName = view.findViewById(R.id.edit_meet_name);
        edtMeetVenue = view.findViewById(R.id.edit_meet_venue);
        edtMeetDate = view.findViewById(R.id.edt_meet_date);
        edtMeetDate.setOnClickListener(this);
        edtMeetStartTime = view.findViewById(R.id.edt_start_time);
        edtMeetStartTime.setOnClickListener(this);
        edtMeetEndTime = view.findViewById(R.id.edt_end_time);
        edtMeetEndTime.setOnClickListener(this);
        edtMeetRegStartDate = view.findViewById(R.id.edt_meet_reg_date);
        edtMeetRegStartDate.setOnClickListener(this);
        edtMeetRegEndDate = view.findViewById(R.id.edt_meet_reg_end_date);
        edtMeetRegEndDate.setOnClickListener(this);
        edtRegAmt = view.findViewById(R.id.edt_reg_amt);
        rgPaidFree = view.findViewById(R.id.rg_paid_free);
        rgPaidFree.setOnCheckedChangeListener(this);
        rbPaid = view.findViewById(R.id.rb_paid);
        rbFree = view.findViewById(R.id.rb_free);
        rgOnlinePayment = view.findViewById(R.id.rg_online_payment);
        rgOnlinePayment.setOnCheckedChangeListener(this);
        rbOnlineYes = view.findViewById(R.id.rb_online_yes);
        rbOnlineNo = view.findViewById(R.id.rb_online_no);
        userInfo = Util.getUserObjectFromPref();
        if(((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getVenue() != null &&
                ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getVenue().length()>0){
            if(selectedMeetType!= null && selectedMeetType.length()>0) {
                tvMeetType.setText(selectedMeetType);
            }
            if(selectedCountry!= null && selectedCountry.length()>0) {
                tvMeetCountry.setText(selectedCountry);
            }
            if(selectedState!= null && selectedState.length()>0) {
                tvMeetState.setText(selectedState);
            }
            if(selectedCity!= null && selectedCity.length()>0) {
                tvMeetCity.setText(selectedCity);
            }
        } else {
            matrimonyMeetFirstFragmentPresenter = new CreateMeetFirstFragmentPresenter(this);
            matrimonyMeetFirstFragmentPresenter.getMeetTypes();
        }
    }

    private void setMeetData() {
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setTitle(edtMeetName.getText().toString());
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetType(selectedMeetType);

        MeetLocation location = new MeetLocation();
        location.setCountry(selectedCountryId);
        location.setState(selectedStateId);
        location.setCity(selectedCityId);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setLocation(location);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setVenue(edtMeetVenue.getText().toString().trim());
        MeetSchedule meetSchedule = new MeetSchedule();
        meetSchedule.setDateTime(Util.dateTimeToTimeStamp(edtMeetDate.getText().toString(), "00:00"));
        meetSchedule.setMeetStartTime(edtMeetStartTime.getText().toString());
        meetSchedule.setMeetEndTime(edtMeetEndTime.getText().toString());
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setSchedule(meetSchedule);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIsRegPaid(isRegPaid);
        RegistrationSchedule registrationSchedule = new RegistrationSchedule();
        registrationSchedule.setRegStartDateTime
                (Util.dateTimeToTimeStamp(edtMeetRegStartDate.getText().toString(), "00:00"));
        registrationSchedule.setRegEndDateTime
                (Util.dateTimeToTimeStamp(edtMeetRegEndDate.getText().toString(), "00:00"));
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setRegistrationSchedule(registrationSchedule);
        if(edtRegAmt.getText().toString().trim()!= null && edtRegAmt.getText().toString().trim().length()>0) {
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setRegAmount(Integer.parseInt(edtRegAmt.getText().toString().trim()));
        }
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIsOnlinePaymentAllowed(isOnlinePaymentAllowed);
    }

    public void setMatrimonyMeetTypes(List<MeetType> responseMeetTypesList){
        //meetTypes.clear();
        for(MeetType m : responseMeetTypesList){
            CustomSpinnerObject cMeetType = new CustomSpinnerObject();
            cMeetType.setName(m.getType());
            cMeetType.set_id(m.getId());
            cMeetType.setSelected(false);
             meetTypesList.add(cMeetType);
            //meetTypes.add(m.getType());
        }
        UserInfo userInfo = Util.getUserObjectFromPref();
        matrimonyMeetFirstFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                "5d68c6645dda765a632b4ac3",
                Constants.JurisdictionLevelName.COUNTRY_LEVEL);
    }

    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetCountryList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getCountryId());
                        meetCountry.setName(location.getCountry().getName());
                        meetCountry.setSelected(false);
                        meetCountryList.add(meetCountry);
                    }
                }

                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetStateList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                            CustomSpinnerObject meetState = new CustomSpinnerObject();
                            meetState.set_id(location.getStateId());
                            meetState.setName(location.getState().getName());
                            meetState.setSelected(false);
                            meetStateList.add(meetState);
                    }
                }

                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetCityList.clear();
                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                            if (selectedState.equalsIgnoreCase(location.getState().getName())) {
                                CustomSpinnerObject meetCity = new CustomSpinnerObject();
                                meetCity.set_id(location.getCityId());
                                meetCity.setName(location.getCity().getName());
                                meetCity.setSelected(false);
                                meetCityList.add(meetCity);
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_first_part_meet:
                if(isAllDataValid()) {
                    setMeetData();
                    ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetSecondFragment");
                }
                break;
            case R.id.tv_meet_types:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Select Meet Type", meetTypesList,
                        false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_country:
                CustomSpinnerDialogClass cddCountry = new CustomSpinnerDialogClass(getActivity(), this, "Select Country", meetCountryList,
                        false);
                cddCountry.show();
                cddCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_state:
                CustomSpinnerDialogClass cddState = new CustomSpinnerDialogClass(getActivity(), this, "Select State", meetStateList,
                        false);
                cddState.show();
                cddState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_city:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(getActivity(), this, "Select City", meetCityList,
                        false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.edt_meet_date:
                Util.showDateDialogMin(getActivity(), edtMeetDate);
                break;
            case R.id.edt_start_time:
                Util.showTimeDialog(getActivity(), edtMeetStartTime);
                break;
            case R.id.edt_end_time:
                Util.showTimeDialog(getActivity(), edtMeetEndTime);
                break;
            case R.id.edt_meet_reg_date:
                if(edtMeetDate.getText().toString()!=null && edtMeetDate.getText().toString().length()>0) {
                    Util.showDateDialogEnableBetweenMinMax(getActivity(), edtMeetRegStartDate, new SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(new Date()), edtMeetDate.getText().toString());
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select Meet date.",
                            Snackbar.LENGTH_LONG);
                }
                //showDateDialogEnableBeforeDefinedDate
                break;
            case R.id.edt_meet_reg_end_date:
                if(edtMeetDate.getText().toString()!=null && edtMeetDate.getText().toString().length()>0) {
                    if(edtMeetRegStartDate.getText().toString()!=null && edtMeetRegStartDate.getText().toString().length()>0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtMeetRegEndDate, edtMeetRegStartDate.getText().toString(),
                                edtMeetDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Please select Meet Registration start date.",
                                Snackbar.LENGTH_LONG);
                    }
                }  else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select Meet date.",
                            Snackbar.LENGTH_LONG);
                }
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

    public boolean isAllDataValid(){
        if (TextUtils.isEmpty(edtMeetName.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetVenue.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetDate.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetStartTime.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetEndTime.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetRegStartDate.getText().toString().trim())
                || TextUtils.isEmpty(edtMeetRegEndDate.getText().toString().trim())
                || selectedMeetType == null || selectedStateId == null || selectedCityId == null || selectedCountryId == null
                || isPaidFreeRGChecked == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if(isPaidFreeRGChecked == 2){
            if(TextUtils.isEmpty(edtRegAmt.getText().toString().trim()) || isOnlinePaymentRGChecked == 0){
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                        Snackbar.LENGTH_LONG);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type){
            case "Select Meet Type":
                for(CustomSpinnerObject mType: meetTypesList){
                    if(mType.isSelected()){
                        selectedMeetType = mType.getName();
                        break;
                    }
                }
                tvMeetType.setText(selectedMeetType);
                break;
            case "Select Country":
                for(CustomSpinnerObject mCountry: meetCountryList){
                    if(mCountry.isSelected()){
                        selectedCountry = mCountry.getName();
                        selectedCountryId = mCountry.get_id();
                        break;
                    }
                }
                tvMeetCountry.setText(selectedCountry);
                if(selectedCountry!="" && selectedCountry!="Country") {
                    matrimonyMeetFirstFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                            "5d68c6645dda765a632b4ac3",
                            Constants.JurisdictionLevelName.STATE_LEVEL);
                }
                break;
            case "Select State":
                for(CustomSpinnerObject mState: meetStateList){
                    if(mState.isSelected()){
                        selectedState = mState.getName();
                        selectedStateId = mState.get_id();
                        break;
                    }
                }
                tvMeetState.setText(selectedState);
                if(selectedState!="" && selectedState!="State") {
                    matrimonyMeetFirstFragmentPresenter.getJurisdictionLevelData(userInfo.getOrgId(),
                            "5d68c6645dda765a632b4ac3",
                            Constants.JurisdictionLevelName.CITY_LEVEL);
                }
                break;
            case "Select City":
                for(CustomSpinnerObject mCity: meetCityList){
                    if(mCity.isSelected()){
                        selectedCity = mCity.getName();
                        selectedCityId = mCity.get_id();
                        break;
                    }
                }
                tvMeetCity.setText(selectedCity);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_free:
                isRegPaid = false;
                isPaidFreeRGChecked = 1;
                edtRegAmt.setVisibility(View.GONE);
                rgOnlinePayment.setVisibility(View.GONE);
                edtRegAmt.setText("");
                isOnlinePaymentAllowed = false;
                break;
            case R.id.rb_paid:
                isRegPaid = true;
                isPaidFreeRGChecked = 2;
                edtRegAmt.setVisibility(View.VISIBLE);
                rgOnlinePayment.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_online_no:
                isOnlinePaymentAllowed = false;
                isOnlinePaymentRGChecked = 2;
                break;
            case R.id.rb_online_yes:
                isOnlinePaymentAllowed = true;
                isOnlinePaymentRGChecked = 1;
                break;
        }
    }
}
