package com.octopusbjsindia.view.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserFilterData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MatrimonyUsersFilterActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.bendik.simplerangeview.SimpleRangeView;

public class MatrimonyUsersFilterFragment extends Fragment implements APIDataListener, View.OnClickListener, CustomSpinnerListener {

    private View view;
    private ImageView toolbar_back_action, ivClearFilter;
    private TextView toolbar_title;
    private RelativeLayout pbLayout;
    private Button btnApply;
    private TextView txtMinAge, txtMaxAge;
    private MatrimonyUsersFilterActivityPresenter presenter;
    public List<MatrimonyMasterRequestModel.DataList.Master_data> masterDataArrayList = new ArrayList<>();
    private EditText etMeetStatus, etVerificationStatus, etState, etGender, etSect, etQualification, etMaritalStatus, etPaidOrFree;
    private String selectedMeetStatus, selectedVerificationStatus, selectedState, selectedQualification, selectedGender, selectedSect,
            selectedMaritalStatus, selectedPaidOrFree;
    private SimpleRangeView rangeView;
    private MatrimonyUserFilterData matrimonyUserFilterData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matrimony_users_filter, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        presenter = new MatrimonyUsersFilterActivityPresenter(this);
        pbLayout = view.findViewById(R.id.progress_bar);
        txtMinAge = view.findViewById(R.id.txt_min_age);
        txtMaxAge = view.findViewById(R.id.txt_max_age);

        toolbar_title = view.findViewById(R.id.toolbar_title1);
        toolbar_title.setText("Apply Filter");
        toolbar_back_action = view.findViewById(R.id.iv_toobar_back);
        toolbar_back_action.setOnClickListener(this);
        ivClearFilter = view.findViewById(R.id.iv_clear_filter);
        ivClearFilter.setOnClickListener(this);
        etMeetStatus = view.findViewById(R.id.et_meet_status);
        etVerificationStatus = view.findViewById(R.id.et_verification_status);
        etState = view.findViewById(R.id.et_state);
        etGender = view.findViewById(R.id.et_gender);
        etSect = view.findViewById(R.id.et_sect);
        etQualification = view.findViewById(R.id.et_qualification);
        etMaritalStatus = view.findViewById(R.id.et_marital_status);
        etPaidOrFree = view.findViewById(R.id.et_paid_free);
        if (((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().getSection_type().
                equalsIgnoreCase(Constants.MatrimonyModule.MEET_USERS_SECTION)) {
            etMeetStatus.setVisibility(View.VISIBLE);
            etMeetStatus.setOnClickListener(this);
        }else if (((MatrimonyProfileListActivity) getActivity()).getMatrimonyUserFilterData().getSection_type().
                equalsIgnoreCase(Constants.MatrimonyModule.VERIFICATION_PENDING_SECTION)) {
            etVerificationStatus.setVisibility(View.GONE);
            etMeetStatus.setVisibility(View.VISIBLE);
            etMeetStatus.setOnClickListener(this);
        }

        etVerificationStatus.setOnClickListener(this);
        etState.setOnClickListener(this);
        etGender.setOnClickListener(this);
        etSect.setOnClickListener(this);
        etQualification.setOnClickListener(this);
        etMaritalStatus.setOnClickListener(this);
        etPaidOrFree.setOnClickListener(this);
        btnApply = view.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(this);
        matrimonyUserFilterData = new MatrimonyUserFilterData();
        rangeView = view.findViewById(R.id.fixed_rangeview);
        rangeView.setCount(43);
        rangeView.setStart(0);
        rangeView.setEnd(42);
        rangeView.setStartFixed(0);
        rangeView.setEndFixed(42);
        txtMinAge.setText(String.valueOf(rangeView.getStart() + 18));
        txtMaxAge.setText(String.valueOf(rangeView.getEnd() + 18));

        rangeView.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView rangeView, int start) {
                start = start + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge());
                txtMinAge.setText(String.valueOf(start));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView rangeView, int end) {
                end = end + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge());
                txtMaxAge.setText(String.valueOf(end));
            }
        });

        if (((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) {
            setFilterData();
        } else {
            presenter.getFilterMasterData(BuildConfig.BASE_URL + String.format
                    (Urls.Matrimony.GET_FILTER_MASTER_DATA));
        }
    }

    private void setFilterData() {
        matrimonyUserFilterData = ((MatrimonyProfileListActivity) getActivity()).
                getMatrimonyUserFilterData();
        if (matrimonyUserFilterData.getUser_meet_status() != null) {
            etMeetStatus.setText(matrimonyUserFilterData.getUser_meet_status());
        }
        if (matrimonyUserFilterData.getUser_verification_status() != null) {
            etVerificationStatus.setText(matrimonyUserFilterData.getUser_verification_status());
        }
        if (matrimonyUserFilterData.getState_names() != null) {
            etState.setText(matrimonyUserFilterData.getState_names());
        }
        if (matrimonyUserFilterData.getGender() != null) {
            etGender.setText(matrimonyUserFilterData.getGender());
        }
        if (matrimonyUserFilterData.getUser_sect() != null) {
            etSect.setText(matrimonyUserFilterData.getUser_sect());
        }
        if (matrimonyUserFilterData.getUser_paid_free() != null) {
            etPaidOrFree.setText(matrimonyUserFilterData.getUser_paid_free());
        }
        if (matrimonyUserFilterData.getQualification_degrees() != null) {
            etQualification.setText(matrimonyUserFilterData.getQualification_degrees());
        }
        if (matrimonyUserFilterData.getMarital_status() != null) {
            etMaritalStatus.setText(matrimonyUserFilterData.getMarital_status());
        }
        if (matrimonyUserFilterData.getAge_ranges() != null) {
            String min = matrimonyUserFilterData.getAge_ranges().substring(0, 2);
            String max = matrimonyUserFilterData.getAge_ranges().substring(3, 5);
            rangeView.setStart(Integer.parseInt(min) - Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge()));
            rangeView.setEnd(Integer.parseInt(max) - Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge()));
            txtMinAge.setText(min);
            txtMaxAge.setText(max);
        }
    }

    private void setRangeView() {
        rangeView.setCount((Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMaxAge()) -
                Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())) + 1);
        rangeView.setStart(0);
        rangeView.setEnd((Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMaxAge()) -
                Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
        rangeView.setStartFixed(0);
        rangeView.setEndFixed((Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMaxAge()) -
                Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
        txtMinAge.setText(String.valueOf(rangeView.getStart() + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
        txtMaxAge.setText(String.valueOf(rangeView.getEnd() + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
    }

    public void setMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        masterDataArrayList.clear();
        masterDataArrayList = data;
        for (MatrimonyMasterRequestModel.DataList.Master_data masterData : masterDataArrayList) {
            switch (masterData.getKey()) {
                case "meet_status":
                    ((MatrimonyProfileListActivity) getActivity()).getMeetStatusList().clear();
                    ArrayList<CustomSpinnerObject> tempStatusList = new ArrayList<>();
                    for (String status : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(status);
                        tempStatusList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setMeetStatusList(tempStatusList);
                    break;
                case "verification_status":
                    ((MatrimonyProfileListActivity) getActivity()).getVerificationStatusList().clear();
                    ArrayList<CustomSpinnerObject> tempVerificationStatusList = new ArrayList<>();
                    for (String status : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(status);
                        tempVerificationStatusList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setVerificationStatusList(tempVerificationStatusList);
                    break;
                case "state":
                    ((MatrimonyProfileListActivity) getActivity()).getStateList().clear();
                    ArrayList<CustomSpinnerObject> tempStateList = new ArrayList<>();
                    for (String stateName : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(stateName);
                        tempStateList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setStateList(tempStateList);
                    break;
                case "gender":
                    ((MatrimonyProfileListActivity) getActivity()).getGenderList().clear();
                    ArrayList<CustomSpinnerObject> tempGenderList = new ArrayList<>();
                    for (String gender : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(gender);
                        tempGenderList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setGenderList(tempGenderList);
                    break;
                case "sect":
                    ((MatrimonyProfileListActivity) getActivity()).getSectList().clear();
                    ArrayList<CustomSpinnerObject> tempSectList = new ArrayList<>();
                    for (String gender : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(gender);
                        tempSectList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setSectList(tempSectList);
                    break;
                case "paid_free":
                    ((MatrimonyProfileListActivity) getActivity()).getPaidOrFreeList().clear();
                    ArrayList<CustomSpinnerObject> tempPaidFreeList = new ArrayList<>();
                    for (String paidOrFree : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(paidOrFree);
                        tempPaidFreeList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setPaidOrFreeList(tempPaidFreeList);
                    break;
                case "marital_status":
                    ((MatrimonyProfileListActivity) getActivity()).getMaritalStatusList().clear();
                    ArrayList<CustomSpinnerObject> tempMaritalStatusList = new ArrayList<>();
                    for (String maritalStatus : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(maritalStatus);
                        tempMaritalStatusList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setMaritalStatusList(tempMaritalStatusList);
                    break;
                case "qualification_degree":
                    ((MatrimonyProfileListActivity) getActivity()).getQualificationList().clear();
                    ArrayList<CustomSpinnerObject> tempQualificationList = new ArrayList<>();
                    for (String qualification : masterData.getValues()) {
                        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                        customSpinnerObject.setName(qualification);
                        tempQualificationList.add(customSpinnerObject);
                    }
                    ((MatrimonyProfileListActivity) getActivity()).setQualificationList(tempQualificationList);
                    break;
                case "age":
                    ((MatrimonyProfileListActivity) getActivity()).setMinAge(masterData.getValues().get(0));
                    ((MatrimonyProfileListActivity) getActivity()).setMaxAge(masterData.getValues().get(1));
                    setRangeView();
                    break;
            }
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
        if (pbLayout != null && pbLayout.getVisibility() == View.GONE) {
            pbLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.VISIBLE) {
            pbLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_meet_status:
                CustomSpinnerDialogClass csdMeetStatus = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Status in Meet", ((MatrimonyProfileListActivity) getActivity()).getMeetStatusList(), true);
                csdMeetStatus.show();
                csdMeetStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_verification_status:
                CustomSpinnerDialogClass csdverificationStatus = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Verification Status", ((MatrimonyProfileListActivity) getActivity()).getVerificationStatusList(), true);
                csdverificationStatus.show();
                csdverificationStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_state:
                CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State", ((MatrimonyProfileListActivity) getActivity()).getStateList(), true);
                csdState.show();
                csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_qualification:
                CustomSpinnerDialogClass csdEducation = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Qualification Degree", ((MatrimonyProfileListActivity) getActivity()).getQualificationList(), true);
                csdEducation.show();
                csdEducation.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_gender:
                CustomSpinnerDialogClass csdIncome = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Gender", ((MatrimonyProfileListActivity) getActivity()).getGenderList(), true);
                csdIncome.show();
                csdIncome.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_sect:
                CustomSpinnerDialogClass csdSect = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Sect", ((MatrimonyProfileListActivity) getActivity()).getSectList(), true);
                csdSect.show();
                csdSect.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_paid_free:
                CustomSpinnerDialogClass csdPaidOrFree = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Paid/Free", ((MatrimonyProfileListActivity) getActivity()).getPaidOrFreeList(), true);
                csdPaidOrFree.show();
                csdPaidOrFree.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_marital_status:
                CustomSpinnerDialogClass csdMaritalStatus = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Marital Status", ((MatrimonyProfileListActivity) getActivity()).getMaritalStatusList(), true);
                csdMaritalStatus.show();
                csdMaritalStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_apply:
                ((MatrimonyProfileListActivity) getActivity()).clearFilterCandidtaesData();
                ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(false);
                matrimonyUserFilterData = new MatrimonyUserFilterData();
                if (etMeetStatus.getText().toString().trim().length() > 0) {
                    matrimonyUserFilterData.setUser_meet_status(etMeetStatus.getText().toString().trim());
                    ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(true);
                } else {
                    matrimonyUserFilterData.setUser_meet_status(null);
                }
                if (Integer.parseInt(txtMinAge.getText().toString()) > Integer.parseInt(((MatrimonyProfileListActivity)
                        getActivity()).getMinAge()) || Integer.parseInt(txtMaxAge.getText().toString()) <
                        Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMaxAge())) {
                    matrimonyUserFilterData.setAge_ranges(txtMinAge.
                            getText().toString() + "," + txtMaxAge.getText().toString());
                    ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(true);
                } else {
                    matrimonyUserFilterData.setAge_ranges(null);
                }
                if (matrimonyUserFilterData.getUser_verification_status() != null
                        || matrimonyUserFilterData.getUser_meet_status() != null
                        || matrimonyUserFilterData.getState_names() != null
                        || matrimonyUserFilterData.getUser_sect() != null
                        || matrimonyUserFilterData.getQualification_degrees() != null
                        || matrimonyUserFilterData.getGender() != null
                        || matrimonyUserFilterData.getMarital_status() != null
                        || matrimonyUserFilterData.getUser_paid_free() != null) {
                    ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(true);
                }
                ((MatrimonyProfileListActivity) getActivity()).setMatrimonyUserFilterData(matrimonyUserFilterData);
                ((MatrimonyProfileListActivity) getActivity()).openFragment("profile_list_fragment");
                break;
            case R.id.iv_toobar_back:
                ((MatrimonyProfileListActivity) getActivity()).handleBackPressed(this);
                break;
            case R.id.iv_clear_filter:
                ((MatrimonyProfileListActivity) getActivity()).clearFilterCandidtaesData();
                ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(false);
                matrimonyUserFilterData = new MatrimonyUserFilterData();
                etMeetStatus.setText("");
                etVerificationStatus.setText("");
                etState.setText("");
                etQualification.setText("");
                etGender.setText("");
                etSect.setText("");
                etPaidOrFree.setText("");
                etMaritalStatus.setText("");
                rangeView.setStart(0);
                rangeView.setEnd((Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMaxAge()) -
                        Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
                txtMinAge.setText(String.valueOf(rangeView.getStart() + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));
                txtMaxAge.setText(String.valueOf(rangeView.getEnd() + Integer.parseInt(((MatrimonyProfileListActivity) getActivity()).getMinAge())));

                matrimonyUserFilterData.setUser_meet_status(null);
                matrimonyUserFilterData.setUser_verification_status(null);
                matrimonyUserFilterData.setState_names(null);
                matrimonyUserFilterData.setQualification_degrees(null);
                matrimonyUserFilterData.setGender(null);
                matrimonyUserFilterData.setUser_sect(null);
                matrimonyUserFilterData.setUser_paid_free(null);
                matrimonyUserFilterData.setMarital_status(null);
                matrimonyUserFilterData.setAge_ranges(null);

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getMeetStatusList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getVerificationStatusList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getStateList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getQualificationList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getGenderList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getSectList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getPaidOrFreeList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }

                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getMaritalStatusList()) {
                    if (obj.isSelected()) {
                        obj.setSelected(false);
                    }
                }
                Util.showToast("All selected filters have cleared.", getActivity());
                ((MatrimonyProfileListActivity) getActivity()).setMatrimonyUserFilterData(matrimonyUserFilterData);
                ((MatrimonyProfileListActivity) getActivity()).setFilterApplied(false);
                break;
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Status in Meet":
                selectedMeetStatus = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getMeetStatusList()) {
                    if (obj.isSelected()) {
                        if (selectedMeetStatus != null && selectedMeetStatus != "") {
                            selectedMeetStatus = selectedMeetStatus + "," + obj.getName();
                        } else {
                            selectedMeetStatus = obj.getName();
                        }
                    }
                }
                etMeetStatus.setText(selectedMeetStatus);
                matrimonyUserFilterData.setUser_meet_status(selectedMeetStatus);
                break;
            case "Select Verification Status":
                selectedVerificationStatus = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getVerificationStatusList()) {
                    if (obj.isSelected()) {
                        if (selectedVerificationStatus != null && selectedVerificationStatus != "") {
                            selectedVerificationStatus = selectedVerificationStatus + "," + obj.getName();
                        } else {
                            selectedVerificationStatus = obj.getName();
                        }
                    }
                }
                etVerificationStatus.setText(selectedVerificationStatus);
                matrimonyUserFilterData.setUser_verification_status(selectedVerificationStatus);
                break;
            case "Select State":
                selectedState = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getStateList()) {
                    if (obj.isSelected()) {
                        if (selectedState != null && selectedState != "") {
                            selectedState = selectedState + "," + obj.getName();
                        } else {
                            selectedState = obj.getName();
                        }
                    }
                }
                etState.setText(selectedState);
                matrimonyUserFilterData.setState_names(selectedState);
                break;
            case "Select Qualification Degree":
                selectedQualification = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getQualificationList()) {
                    if (obj.isSelected()) {
                        if (selectedQualification != null && selectedQualification != "") {
                            selectedQualification = selectedQualification + "," + obj.getName();
                        } else {
                            selectedQualification = obj.getName();
                        }
                    }
                }
                etQualification.setText(selectedQualification);
                matrimonyUserFilterData.setQualification_degrees(selectedQualification);
                break;
            case "Select Gender":
                selectedGender = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getGenderList()) {
                    if (obj.isSelected()) {
                        if (selectedGender != null && selectedGender != "") {
                            selectedGender = selectedGender + "," + obj.getName();
                        } else {
                            selectedGender = obj.getName();
                        }
                    }
                }
                etGender.setText(selectedGender);
                matrimonyUserFilterData.setGender(selectedGender);
                break;
            case "Select Sect":
                selectedSect = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getSectList()) {
                    if (obj.isSelected()) {
                        if (selectedSect != null && selectedSect != "") {
                            selectedSect = selectedSect + "," + obj.getName();
                        } else {
                            selectedSect = obj.getName();
                        }
                    }
                }
                etSect.setText(selectedSect);
                matrimonyUserFilterData.setUser_sect(selectedSect);
                break;
            case "Select Paid/Free":
                selectedPaidOrFree = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getPaidOrFreeList()) {
                    if (obj.isSelected()) {
                        if (selectedPaidOrFree != null && selectedPaidOrFree != "") {
                            selectedPaidOrFree = selectedPaidOrFree + "," + obj.getName();
                        } else {
                            selectedPaidOrFree = obj.getName();
                        }
                    }
                }
                etPaidOrFree.setText(selectedPaidOrFree);
                matrimonyUserFilterData.setUser_paid_free(selectedPaidOrFree);
                break;
            case "Select Marital Status":
                selectedMaritalStatus = null;
                for (CustomSpinnerObject obj : ((MatrimonyProfileListActivity) getActivity()).getMaritalStatusList()) {
                    if (obj.isSelected()) {
                        if (selectedMaritalStatus != null && selectedMaritalStatus != "") {
                            selectedMaritalStatus = selectedMaritalStatus + "," + obj.getName();
                        } else {
                            selectedMaritalStatus = obj.getName();
                        }
                    }
                }
                etMaritalStatus.setText(selectedMaritalStatus);
                matrimonyUserFilterData.setMarital_status(selectedMaritalStatus);
                break;
        }
    }
}
