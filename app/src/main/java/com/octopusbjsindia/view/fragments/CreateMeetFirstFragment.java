package com.octopusbjsindia.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.models.Matrimony.MeetCriteria;
import com.octopusbjsindia.models.Matrimony.MeetLocation;
import com.octopusbjsindia.models.Matrimony.MeetSchedule;
import com.octopusbjsindia.models.Matrimony.MeetType;
import com.octopusbjsindia.models.Matrimony.RegistrationSchedule;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.CreateMeetFirstFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateMatrimonyMeetActivity;
import com.octopusbjsindia.view.activities.UserRegistrationMatrimonyActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.sagar.selectiverecycleviewinbottonsheetdialog.CustomBottomSheetDialogFragment;
import com.sagar.selectiverecycleviewinbottonsheetdialog.interfaces.CustomBottomSheetDialogInterface;
import com.sagar.selectiverecycleviewinbottonsheetdialog.model.SelectionListObject;
import com.soundcloud.android.crop.Crop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.bendik.simplerangeview.SimpleRangeView;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.remoteconfig.FirebaseRemoteConfig.TAG;

public class CreateMeetFirstFragment extends Fragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, APIDataListener/*, CustomSpinnerListener*/, CustomBottomSheetDialogInterface {

    private TextView tvMeetType, tvMeetCountry, tvMeetState, tvMeetCity, tvCriteria, tvMinAge, tvMaxAge;
    private Button btnFirstPartMeet;
    //private ImageView ivBanner;
    private ArrayList<SelectionListObject> meetTypesList = new ArrayList<>();
    private ArrayList<SelectionListObject> meetCountryList = new ArrayList<>();
    private ArrayList<SelectionListObject> meetStateList = new ArrayList<>();
    private ArrayList<SelectionListObject> meetCityList = new ArrayList<>();
    private CreateMeetFirstFragmentPresenter matrimonyMeetFirstFragmentPresenter;
    private String selectedMeetType, selectedCountry, selectedState, selectedCity,
            selectedCountryId, selectedStateId, selectedCityId;
    private EditText edtMeetName, edtMeetVenue, etMeetWebLink, edtMeetDate, edtMeetStartTime, edtMeetEndTime, edtMeetRegStartDate,
            edtMeetRegEndDate, edtRegAmt, etEducation, etMaritalStatus, etPaymentInfo, etNote;
    private TextInputLayout lyRegAmt, lyPaymentInfo;
    private SimpleRangeView rangeView;
    private RadioGroup rgPaidFree, rgOnlinePayment;
    private RadioButton rbPaid, rbFree, rbOnlineYes, rbOnlineNo;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private LinearLayout lyCriteria;
    private boolean isRegPaid, isOnlinePaymentAllowed;
    private int isPaidFreeRGChecked = 0, isOnlinePaymentRGChecked = 0;
    UserInfo userInfo;
    //image upload
    private String bannerUrl;
    private Uri outputUri;
    private Uri finalUri;
    private String currentPhotoPath = "";

    ArrayList<SelectionListObject> maritalStatusList = new ArrayList<>();
    ArrayList<SelectionListObject> qualificationDegreeList = new ArrayList<>();

    ArrayList<String> selectedMaritalStatusList = new ArrayList<>();
    ArrayList<String> selectedQualificationDegreeList = new ArrayList<>();

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
        etMeetWebLink = view.findViewById(R.id.etMeetWebLink);
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
        lyRegAmt = view.findViewById(R.id.ly_reg_amt);
        edtRegAmt = view.findViewById(R.id.edt_reg_amt);
        rgPaidFree = view.findViewById(R.id.rg_paid_free);
        rgPaidFree.setOnCheckedChangeListener(this);
        rbPaid = view.findViewById(R.id.rb_paid);
        rbFree = view.findViewById(R.id.rb_free);
        rgOnlinePayment = view.findViewById(R.id.rg_online_payment);
        rgOnlinePayment.setOnCheckedChangeListener(this);
        rbOnlineYes = view.findViewById(R.id.rb_online_yes);
        rbOnlineNo = view.findViewById(R.id.rb_online_no);
        //ivBanner = view.findViewById(R.id.ivBanner);
        etEducation = view.findViewById(R.id.etEducation);
        etMaritalStatus = view.findViewById(R.id.etMaritalStatus);
        tvCriteria = view.findViewById(R.id.tvCriteria);
        lyCriteria = view.findViewById(R.id.lyCriteria);
        lyPaymentInfo = view.findViewById(R.id.ly_payment_info);
        etPaymentInfo = view.findViewById(R.id.etPaymentInfo);
        etNote = view.findViewById(R.id.etNote);

        tvMinAge = view.findViewById(R.id.tvMinAge);
        tvMaxAge = view.findViewById(R.id.tvMaxAge);
        rangeView = view.findViewById(R.id.fixed_rangeview);
        rangeView.setCount(48);
        rangeView.setStart(0);
        rangeView.setEnd(48);
        rangeView.setStartFixed(0);
        rangeView.setEndFixed(48);
        tvMinAge.setText(String.valueOf(rangeView.getStart() + 18));
        tvMaxAge.setText(String.valueOf(rangeView.getEnd() + 18));

        rangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                start = start + 18;
                tvMinAge.setText(String.valueOf(start));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                end = end + 18;
                tvMaxAge.setText(String.valueOf(end));
            }
        });

        userInfo = Util.getUserObjectFromPref();
        if (((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getVenue() != null &&
                ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().getVenue().length() > 0) {
            if (selectedMeetType != null && selectedMeetType.length() > 0) {
                tvMeetType.setText(selectedMeetType);
            }
            if (selectedCountry != null && selectedCountry.length() > 0) {
                tvMeetCountry.setText(selectedCountry);
            }
            if (selectedState != null && selectedState.length() > 0) {
                tvMeetState.setText(selectedState);
            }
            if (selectedCity != null && selectedCity.length() > 0) {
                tvMeetCity.setText(selectedCity);
            }
        } else {
            matrimonyMeetFirstFragmentPresenter = new CreateMeetFirstFragmentPresenter(this);
            matrimonyMeetFirstFragmentPresenter.getMeetTypes();
            matrimonyMeetFirstFragmentPresenter.getFilterMasterData();
        }
        // view.findViewById(R.id.ivBanner).setOnClickListener(this);
        etEducation.setOnClickListener(this);
        etMaritalStatus.setOnClickListener(this);
        tvCriteria.setOnClickListener(this);
    }

    public void setMatrimonyMeetTypes(List<MeetType> responseMeetTypesList) {
        //meetTypes.clear();
       /* for (MeetType m : responseMeetTypesList) {
            CustomSpinnerObject cMeetType = new CustomSpinnerObject();
            cMeetType.setName(m.getType());
            cMeetType.set_id(m.getId());
            cMeetType.setSelected(false);
            meetTypesList.add(cMeetType);
            //meetTypes.add(m.getType());
        }
        UserInfo userInfo = Util.getUserObjectFromPref();
        matrimonyMeetFirstFragmentPresenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),  //5d98330bee061834d23798b2
                Constants.JurisdictionLevelName.COUNTRY_LEVEL);*/

        meetTypesList.clear();
        ArrayList<SelectionListObject> tempMeetTypeList = new ArrayList<>();
        for (MeetType meet : responseMeetTypesList) {
            tempMeetTypeList.add(new SelectionListObject(meet.getId(),
                    meet.getType(), false, false));
        }
        meetTypesList.addAll(tempMeetTypeList);

        UserInfo userInfo = Util.getUserObjectFromPref();
        matrimonyMeetFirstFragmentPresenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),  //5d98330bee061834d23798b2
                Constants.JurisdictionLevelName.COUNTRY_LEVEL);
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                   /* meetCountryList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        meetCountryList.add(meetCountry);
                    }*/

                    meetCountryList.clear();
                    ArrayList<SelectionListObject> tempCountryList = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        tempCountryList.add(new SelectionListObject(location.getId(),
                                location.getName(), false, false));
                    }
                    meetCountryList.addAll(tempCountryList);

                }

                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetStateList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                   /* for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetState = new CustomSpinnerObject();
                        meetState.set_id(location.getId());
                        meetState.setName(location.getName());
                        meetState.setSelected(false);
                        meetStateList.add(meetState);
                    }*/

                    ArrayList<SelectionListObject> tempStateList = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        tempStateList.add(new SelectionListObject(location.getId(),
                                location.getName(), false, false));
                    }
                    meetStateList.addAll(tempStateList);


                }

                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetCityList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                   /* for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetCity = new CustomSpinnerObject();
                        meetCity.set_id(location.getId());
                        meetCity.setName(location.getName());
                        meetCity.setSelected(false);
                        meetCityList.add(meetCity);
                    }*/

                    ArrayList<SelectionListObject> tempCityList = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        tempCityList.add(new SelectionListObject(location.getId(),
                                location.getName(), false, false));
                    }
                    meetCityList.addAll(tempCityList);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_first_part_meet:
                if (isAllDataValid()) {
                    setMeetData();
                    ((CreateMatrimonyMeetActivity) getActivity()).openFragment("CreateMeetSecondFragment");
                }
                break;
            case R.id.tv_meet_types:
                CustomBottomSheetDialogFragment customBottomsheet = null;
                showSelectiveBottomSheet(customBottomsheet, "Select Meet Type", meetTypesList, false);

                break;
            case R.id.tv_meet_country:
                CustomBottomSheetDialogFragment customBottomsheet2 = null;
                showSelectiveBottomSheet(customBottomsheet2, "Select Country", meetCountryList, false);
                break;
            case R.id.tv_meet_state:
                if (selectedCountryId != null && selectedCountryId.length() > 0) {

                    CustomBottomSheetDialogFragment customBottomsheet3 = null;
                    showSelectiveBottomSheet(customBottomsheet3, "Select State", meetStateList, false);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select Country.",
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.tv_meet_city:
                if (selectedStateId != null && selectedStateId.length() > 0) {
                    CustomBottomSheetDialogFragment customBottomsheet4 = null;
                    showSelectiveBottomSheet(customBottomsheet4, "Select City", meetCityList, false);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select State.",
                            Snackbar.LENGTH_LONG);
                }
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
                if (edtMeetDate.getText().toString() != null && edtMeetDate.getText().toString().length() > 0) {
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
                if (edtMeetDate.getText().toString() != null && edtMeetDate.getText().toString().length() > 0) {
                    if (edtMeetRegStartDate.getText().toString() != null && edtMeetRegStartDate.getText().toString().length() > 0) {
                        Util.showDateDialogEnableBetweenMinMax(getActivity(), edtMeetRegEndDate, edtMeetRegStartDate.getText().toString(),
                                edtMeetDate.getText().toString());
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Please select Meet Registration start date.",
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select Meet date.",
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.etEducation:
                CustomBottomSheetDialogFragment customBottomsheet5 = null;
                showSelectiveBottomSheet(customBottomsheet5, "Select education", qualificationDegreeList, true);
                break;
            case R.id.etMaritalStatus:
                CustomBottomSheetDialogFragment customBottomsheet6 = null;
                showSelectiveBottomSheet(customBottomsheet6, "Select marital status", maritalStatusList, true);
                break;
            case R.id.tvCriteria:
                if (lyCriteria.getVisibility() == View.VISIBLE) {
                    lyCriteria.setVisibility(View.GONE);
                    tvCriteria.setText("Add Criteria");
                } else {
                    lyCriteria.setVisibility(View.VISIBLE);
                    tvCriteria.setText("Remove Criteria");
                }
                break;
        }
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(edtMeetName.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please enter meet name.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (selectedMeetType == null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet type.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (selectedCountryId == null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select country.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (selectedStateId == null || TextUtils.isEmpty(selectedStateId)) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select state.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (selectedCityId == null || TextUtils.isEmpty(selectedCityId)) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select city.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetVenue.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please enter meet venue.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetDate.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet date.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetStartTime.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet start time",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetEndTime.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet end time.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (!Util.validateStartEndTime(edtMeetStartTime.getText().toString(), edtMeetEndTime.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.meet_timings_validation),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetRegStartDate.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet registration start date.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (TextUtils.isEmpty(edtMeetRegEndDate.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet registration end date.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (isPaidFreeRGChecked == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please select meet paid or free.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (isPaidFreeRGChecked == 2) {
            if (TextUtils.isEmpty(edtRegAmt.getText().toString().trim())) {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please enter meet registration amount.",
                        Snackbar.LENGTH_LONG);
                return false;
            }
            if (isOnlinePaymentRGChecked == 0) {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please select payment through app option.",
                        Snackbar.LENGTH_LONG);
                return false;
            }
        }
/*        if(lyCriteria.getVisibility() == View.VISIBLE){
        if (TextUtils.isEmpty(tvMinAge.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please add Minimum age in meet criteria",
                    Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etMinAge.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please add Maximum age in meet criteria",
                    Snackbar.LENGTH_LONG);
            return false;
        }  else if (18 > Integer.parseInt(etMinAge.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Minimum age should be 18 or greater.",
                    Snackbar.LENGTH_LONG);
            return false;
        } else if (Integer.parseInt(etMaxAge.getText().toString()) < Integer.parseInt(etMinAge.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Minimum age should be minimum than maximum age",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        }*/
        return true;
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
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetWebLink(etMeetWebLink.getText().toString().trim());
        MeetSchedule meetSchedule = new MeetSchedule();
        meetSchedule.setDateTime(Util.dateTimeToTimeStamp(edtMeetDate.getText().toString(), edtMeetStartTime.getText().toString()));
        meetSchedule.setMeetStartTime(edtMeetStartTime.getText().toString());
        meetSchedule.setMeetEndTime(edtMeetEndTime.getText().toString());
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setSchedule(meetSchedule);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIsRegPaid(isRegPaid);
        RegistrationSchedule registrationSchedule = new RegistrationSchedule();
        registrationSchedule.setRegStartDateTime
                (Util.dateTimeToTimeStamp(edtMeetRegStartDate.getText().toString(), "00:00"));
        registrationSchedule.setRegEndDateTime
                (Util.dateTimeToTimeStamp(edtMeetRegEndDate.getText().toString(), "23:59"));
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setRegistrationSchedule(registrationSchedule);
        if (edtRegAmt.getText().toString().trim() != null && edtRegAmt.getText().toString().trim().length() > 0) {
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setRegAmount(Integer.parseInt(edtRegAmt.getText().toString().trim()));
        } else {
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setRegAmount(0);
        }
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setIsOnlinePaymentAllowed(isOnlinePaymentAllowed);
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setPaymentInfo(etPaymentInfo.getText().toString());
        ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setNote(etNote.getText().toString());
        if (lyCriteria.getVisibility() == View.VISIBLE) {
            MeetCriteria meetCriteria = new MeetCriteria();
            meetCriteria.setMinAge(Integer.parseInt(tvMinAge.getText().toString()));
            meetCriteria.setMaxAge(Integer.parseInt(tvMaxAge.getText().toString()));
            meetCriteria.setQualificationCriteria(selectedQualificationDegreeList);
            meetCriteria.setMaritalCriteria(selectedMaritalStatusList);
            ((CreateMatrimonyMeetActivity) getActivity()).getMatrimonyMeet().setMeetCriteria(meetCriteria);
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
        //your background thread is still running. By the time that thread reaches the getActivity().runOnUiThread()
        // code,the activity no longer exists. So check if the activity still exists.
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private void showSelectiveBottomSheet(CustomBottomSheetDialogFragment bottomSheet, String Title, ArrayList<SelectionListObject> List, Boolean isMultiSelect) {
        bottomSheet = new CustomBottomSheetDialogFragment(
                this, Title, List, isMultiSelect);
        bottomSheet.show(requireActivity().getSupportFragmentManager(),
                CustomBottomSheetDialogFragment.TAG);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_free:
                isRegPaid = false;
                isPaidFreeRGChecked = 1;
                lyRegAmt.setVisibility(View.GONE);
                rgOnlinePayment.setVisibility(View.GONE);
                lyPaymentInfo.setVisibility(View.GONE);
                edtRegAmt.setText("");
                isOnlinePaymentAllowed = false;
                break;
            case R.id.rb_paid:
                isRegPaid = true;
                isPaidFreeRGChecked = 2;
                lyRegAmt.setVisibility(View.VISIBLE);
                rgOnlinePayment.setVisibility(View.VISIBLE);
                lyPaymentInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_online_no:
                isOnlinePaymentAllowed = false;
                isOnlinePaymentRGChecked = 2;
                lyPaymentInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_online_yes:
                isOnlinePaymentAllowed = true;
                isOnlinePaymentRGChecked = 1;
                lyPaymentInfo.setVisibility(View.GONE);
                break;
        }
    }

    public void setMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        for (MatrimonyMasterRequestModel.DataList.Master_data masterData : data) {
            switch (masterData.getKey()) {
                case "marital_status":
                    maritalStatusList.clear();
                    ArrayList<SelectionListObject> tempList = new ArrayList<>();
                    for (String maritalStatus : masterData.getValues()) {
                        tempList.add(new SelectionListObject(
                                String.valueOf(masterData.getValues().indexOf(maritalStatus + 1)),
                                maritalStatus, false, false));
                    }
                    maritalStatusList.addAll(tempList);

                    break;
                case "qualification_degree":

                    qualificationDegreeList.clear();
                    ArrayList<SelectionListObject> tempQualificationList = new ArrayList<>();
                    for (String qualification : masterData.getValues()) {
                        tempQualificationList.add(new SelectionListObject(
                                String.valueOf(masterData.getValues().indexOf(qualification + 1)),
                                qualification, false, false));
                    }
                    qualificationDegreeList.addAll(tempQualificationList);

                    break;
            }
        }

    }

    @Override
    public void onCustomBottomSheetSelection(@NonNull String type) {
        switch (type) {
            case "Select Meet Type":
                selectedMeetType = null;
                for (SelectionListObject obj : meetTypesList) {
                    if (obj.isSelected()) {
                        selectedMeetType = obj.getValue();
                        break;
                    }
                }
                tvMeetType.setText(selectedMeetType);

                break;
            case "Select Country":
                tvMeetState.setText("");
                selectedState = "";
                selectedStateId = "";
                tvMeetCity.setText("");
                selectedCity = "";
                selectedCityId = "";
                selectedCountry= null;
                selectedCountryId = null;
                for (SelectionListObject obj : meetCountryList) {
                    selectedCountryId = obj.getId();
                    selectedCountry = obj.getValue();
                    break;
                }
                tvMeetCountry.setText(selectedCountry);
                if (selectedCountry != "" && selectedCountry != "Country") {
                    matrimonyMeetFirstFragmentPresenter.getLocationData(selectedCountryId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.STATE_LEVEL);
                }

                break;
            case "Select State":

                tvMeetCity.setText("");
                selectedCity = "";
                selectedCityId = "";
                selectedState = null;
                selectedStateId = null;
                for (SelectionListObject obj : meetStateList) {
                    if (obj.isSelected()) {
                        selectedStateId = obj.getId();
                        selectedState = obj.getValue();
                        break;
                    }
                }
                tvMeetState.setText(selectedState);

                if (selectedState != "" && selectedState != "State") {
                    matrimonyMeetFirstFragmentPresenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.CITY_LEVEL);
                }

                break;
            case "Select City":
                selectedCity = null;
                selectedCityId = null;
                for (SelectionListObject obj : meetCityList) {
                    if (obj.isSelected()) {
                        selectedCityId = obj.getId();
                        selectedCity = obj.getValue();
                        break;
                    }
                }
                tvMeetCity.setText(selectedCity);
                break;
            case "Select education":
                selectedQualificationDegreeList.clear();
                for (SelectionListObject obj : qualificationDegreeList) {
                    if (obj.isSelected()) {
                        selectedQualificationDegreeList.add(obj.getValue());
                    }
                }
                etEducation.setText(android.text.TextUtils.join(",", selectedQualificationDegreeList));

                break;
            case "Select marital status":
                selectedMaritalStatusList.clear();
                for (SelectionListObject obj : maritalStatusList) {
                    if (obj.isSelected()) {
                        selectedMaritalStatusList.add(obj.getValue());
                    }
                }
                etMaritalStatus.setText(android.text.TextUtils.join(",", selectedMaritalStatusList));

                break;
        }
    }
}
