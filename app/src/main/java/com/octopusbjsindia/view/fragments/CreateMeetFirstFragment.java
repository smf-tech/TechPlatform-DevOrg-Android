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
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.CreateMeetFirstFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateMatrimonyMeetActivity;
import com.octopusbjsindia.view.activities.UserRegistrationMatrimonyActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
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
        RadioGroup.OnCheckedChangeListener, APIDataListener, CustomSpinnerListener {

    private TextView tvMeetType, tvMeetCountry, tvMeetState, tvMeetCity, tvCriteria, tvMinAge, tvMaxAge;
    private Button btnFirstPartMeet;
    private ImageView ivBanner;
    private ArrayList<CustomSpinnerObject> meetTypesList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetCountryList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> meetCityList = new ArrayList<>();
    private CreateMeetFirstFragmentPresenter matrimonyMeetFirstFragmentPresenter;
    private String selectedMeetType, selectedCountry, selectedState, selectedCity,
            selectedCountryId, selectedStateId, selectedCityId;
    private EditText edtMeetName, edtMeetVenue, etMeetWebLink, edtMeetDate, edtMeetStartTime, edtMeetEndTime, edtMeetRegStartDate,
            edtMeetRegEndDate, edtRegAmt, etEducation, etMaritalStatus, etPaymentInfo ,etNote;
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

    ArrayList<CustomSpinnerObject> maritalStatusList = new ArrayList<>();
    ArrayList<CustomSpinnerObject> qualificationDegreeList = new ArrayList<>();

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
        edtRegAmt = view.findViewById(R.id.edt_reg_amt);
        rgPaidFree = view.findViewById(R.id.rg_paid_free);
        rgPaidFree.setOnCheckedChangeListener(this);
        rbPaid = view.findViewById(R.id.rb_paid);
        rbFree = view.findViewById(R.id.rb_free);
        rgOnlinePayment = view.findViewById(R.id.rg_online_payment);
        rgOnlinePayment.setOnCheckedChangeListener(this);
        rbOnlineYes = view.findViewById(R.id.rb_online_yes);
        rbOnlineNo = view.findViewById(R.id.rb_online_no);
        ivBanner = view.findViewById(R.id.ivBanner);
        etEducation = view.findViewById(R.id.etEducation);
        etMaritalStatus = view.findViewById(R.id.etMaritalStatus);
        tvCriteria = view.findViewById(R.id.tvCriteria);
        lyCriteria = view.findViewById(R.id.lyCriteria);
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
        tvMinAge.setText(String.valueOf(rangeView.getStart()+18));
        tvMaxAge.setText(String.valueOf(rangeView.getEnd()+18));

        rangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                start = start + 18 ;
                tvMinAge.setText(String.valueOf(start));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                end = end + 18 ;
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
        view.findViewById(R.id.ivBanner).setOnClickListener(this);
        etEducation.setOnClickListener(this);
        etMaritalStatus.setOnClickListener(this);
        tvCriteria.setOnClickListener(this);
    }

    public void setMatrimonyMeetTypes(List<MeetType> responseMeetTypesList) {
        //meetTypes.clear();
        for (MeetType m : responseMeetTypesList) {
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
                Constants.JurisdictionLevelName.COUNTRY_LEVEL);

    }

    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetCountryList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        meetCountryList.add(meetCountry);
                    }
                }

                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetStateList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetState = new CustomSpinnerObject();
                        meetState.set_id(location.getId());
                        meetState.setName(location.getName());
                        meetState.setSelected(false);
                        meetStateList.add(meetState);
                    }
                }

                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    meetCityList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetCity = new CustomSpinnerObject();
                        meetCity.set_id(location.getId());
                        meetCity.setName(location.getName());
                        meetCity.setSelected(false);
                        meetCityList.add(meetCity);
                    }
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
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this, "Select Meet Type", meetTypesList,
                        false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_country:
                CustomSpinnerDialogClass cddCountry = new CustomSpinnerDialogClass(getActivity(), this, "Select Country", meetCountryList,
                        false);
                cddCountry.show();
                cddCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_meet_state:
                if (selectedCountryId != null && selectedCountryId.length() > 0) {
                    CustomSpinnerDialogClass cddState = new CustomSpinnerDialogClass(getActivity(), this, "Select State", meetStateList,
                            false);
                    cddState.show();
                    cddState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select Country.",
                            Snackbar.LENGTH_LONG);
                }
                break;
            case R.id.tv_meet_city:
                if (selectedStateId != null && selectedStateId.length() > 0) {
                    CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(getActivity(), this, "Select City", meetCityList,
                            false);
                    cddCity.show();
                    cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
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
            case R.id.ivBanner:
                onAddImageClick();
                break;
            case R.id.etEducation:
                CustomSpinnerDialogClass qualificationDegree = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select education", qualificationDegreeList, true);
                qualificationDegree.show();
                qualificationDegree.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etMaritalStatus:
                CustomSpinnerDialogClass maritalStatus = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select marital status", maritalStatusList, true);
                maritalStatus.show();
                maritalStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        if (!Util.validateStartEndTime(edtMeetStartTime.getText().toString(), edtMeetEndTime.getText().toString())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.meet_timings_validation),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        if (isPaidFreeRGChecked == 2) {
            if (TextUtils.isEmpty(edtRegAmt.getText().toString().trim()) || isOnlinePaymentRGChecked == 0) {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                        Snackbar.LENGTH_LONG);
                return false;
            }
        }
//        if(lyCriteria.getVisibility() == View.VISIBLE){
//        if (TextUtils.isEmpty(tvMinAge.getText().toString())) {
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Please add Minimum age in meet criteria",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        } else if (TextUtils.isEmpty(etMinAge.getText().toString())) {
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Please add Maximum age in meet criteria",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        }  else if (18 > Integer.parseInt(etMinAge.getText().toString())) {
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Minimum age should be 18 or greater.",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        } else if (Integer.parseInt(etMaxAge.getText().toString()) < Integer.parseInt(etMinAge.getText().toString())) {
//            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Minimum age should be minimum than maximum age",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        }
//        }
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

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else
                uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {

                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(imageFile)) {
                        //profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
                        ((CreateMatrimonyMeetActivity) getActivity()).presenter
                                .uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE,
                                        "meetBanner");

                        ivBanner.setImageURI(finalUri);
                        ((UserRegistrationMatrimonyActivity) getActivity()).showProgressBar();

                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private File getImageFile() {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.Image.IMAGE_STORAGE_DIRECTORY);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        currentPhotoPath = file.getPath();
        return file;
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

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Meet Type":
                for (CustomSpinnerObject mType : meetTypesList) {
                    if (mType.isSelected()) {
                        selectedMeetType = mType.getName();
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
                for (CustomSpinnerObject mCountry : meetCountryList) {
                    if (mCountry.isSelected()) {
                        selectedCountry = mCountry.getName();
                        selectedCountryId = mCountry.get_id();
                        break;
                    }
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
                for (CustomSpinnerObject mState : meetStateList) {
                    if (mState.isSelected()) {
                        selectedState = mState.getName();
                        selectedStateId = mState.get_id();
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
                for (CustomSpinnerObject mCity : meetCityList) {
                    if (mCity.isSelected()) {
                        selectedCity = mCity.getName();
                        selectedCityId = mCity.get_id();
                        break;
                    }
                }
                tvMeetCity.setText(selectedCity);
                break;
            case "Select education":
                selectedQualificationDegreeList.clear();
                for (CustomSpinnerObject mCity : qualificationDegreeList) {
                    if (mCity.isSelected()) {
                        selectedQualificationDegreeList.add(mCity.getName());
//                        selectedCityId = mCity.get_id();
                    }
                }
                etEducation.setText(android.text.TextUtils.join(",", selectedQualificationDegreeList));
                break;
            case "Select marital status":
                selectedMaritalStatusList.clear();
                for (CustomSpinnerObject mCity : maritalStatusList) {
                    if (mCity.isSelected()) {
                        selectedMaritalStatusList.add(mCity.getName());
//                        selectedCityId = mCity.get_id();
                    }
                }
                etMaritalStatus.setText(android.text.TextUtils.join(",", selectedMaritalStatusList));
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
                etPaymentInfo.setVisibility(View.GONE);
                edtRegAmt.setText("");
                isOnlinePaymentAllowed = false;
                break;
            case R.id.rb_paid:
                isRegPaid = true;
                isPaidFreeRGChecked = 2;
                edtRegAmt.setVisibility(View.VISIBLE);
                rgOnlinePayment.setVisibility(View.VISIBLE);
                etPaymentInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_online_no:
                isOnlinePaymentAllowed = false;
                isOnlinePaymentRGChecked = 2;
                etPaymentInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_online_yes:
                isOnlinePaymentAllowed = true;
                isOnlinePaymentRGChecked = 1;
                etPaymentInfo.setVisibility(View.GONE);
                break;
        }
    }

    public void setMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        for (MatrimonyMasterRequestModel.DataList.Master_data masterData : data) {
            switch (masterData.getKey()) {
                case "marital_status":
                    maritalStatusList.clear();
                    for (String status : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(status);
                        maritalStatusList.add(customSpinnerObject);
                    }
                    break;
                case "qualification_degree":
                    qualificationDegreeList.clear();

                    for (String status : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(status);
                        qualificationDegreeList.add(customSpinnerObject);
                    }
                    break;
            }
        }

    }
}
