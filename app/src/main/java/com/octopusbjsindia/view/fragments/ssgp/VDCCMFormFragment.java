package com.octopusbjsindia.view.fragments.ssgp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.ssgp.VDCCMFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class VDCCMFormFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomSpinnerListener {

    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private VDCCMFormFragmentPresenter presenter;
    private EditText etState,etDistrict, etTaluka, etVillage;
    private EditText et_date,et_activity_type,et_activity_purpose,et_participant1,et_participant2,et_participant3,et_total_participant,et_discussion,
    et_meeting_feedback,et_remark;
    private ImageView iv_activity_photo,iv_meeting_photo,iv_attendace_sheet_photo;
    private String UrlActivityPhoto,UrlMeetingPhoto,UrlAttendacesheetPhoto;
    private Button btn_submit;
    private View vdccmFormFragmentView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vdccmFormFragmentView = inflater.inflate(R.layout.fragment_v_d_c_cm_form, container, false);
        return vdccmFormFragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        ((GPActionsActivity) getActivity()).setTitle("Community mobilization Form");
        progressBarLayout = vdccmFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdccmFormFragmentView.findViewById(R.id.pb_profile_act);
        //presenter = new VDFFormFragmentPresenter(this);
        etState = vdccmFormFragmentView.findViewById(R.id.etState);
        etDistrict = vdccmFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdccmFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdccmFormFragmentView.findViewById(R.id.et_village);
        et_date = vdccmFormFragmentView.findViewById(R.id.et_date);
        et_activity_type = vdccmFormFragmentView.findViewById(R.id.et_activity_type);
        et_activity_purpose = vdccmFormFragmentView.findViewById(R.id.et_activity_purpose);
        et_participant1 = vdccmFormFragmentView.findViewById(R.id.et_participant1);
        et_participant2  = vdccmFormFragmentView.findViewById(R.id.et_participant2);
        et_participant3  = vdccmFormFragmentView.findViewById(R.id.et_participant3);
        et_total_participant  = vdccmFormFragmentView.findViewById(R.id.et_total_participant);
        et_discussion  = vdccmFormFragmentView.findViewById(R.id.et_discussion);
        et_meeting_feedback  = vdccmFormFragmentView.findViewById(R.id.et_meeting_feedback);
        et_remark  = vdccmFormFragmentView.findViewById(R.id.et_remark);
        iv_activity_photo  = vdccmFormFragmentView.findViewById(R.id.iv_activity_photo);
        iv_meeting_photo  = vdccmFormFragmentView.findViewById(R.id.iv_meeting_photo);
        iv_attendace_sheet_photo  = vdccmFormFragmentView.findViewById(R.id.iv_attendace_sheet_photo);
        btn_submit = vdccmFormFragmentView.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);
        et_date.setOnClickListener(this);
        iv_activity_photo.setOnClickListener(this);
        iv_meeting_photo.setOnClickListener(this);
        iv_attendace_sheet_photo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (isAllInputsValid()){
                    Util.showToast(getActivity(),"data is valid call API here");
                }
                break;
        }
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (etState.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_state);
        } else if (etDistrict.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_district);
        } else if (etTaluka.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_select_taluka);
        } else if (et_activity_type.getText().toString().trim().length() == 0) {
            msg =getString(R.string.msg_select_struct_code);
        } else if (et_activity_purpose.getText().toString().trim().length() == 0) {
            msg = getString(R.string.select_stuct_type);
        } else if (et_participant1.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (UrlActivityPhoto.trim().length() == 0) {
            msg = "Please upload Activity photo";
        } else if (UrlMeetingPhoto.trim().length() == 0) {
            msg = "Please upload Meeting photo";
        } else if (UrlAttendacesheetPhoto.trim().length() == 0) {
            msg = "Please upload Attendance sheet photo";
        } else if (et_remark.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_enter_remark);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
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

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    districtList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        districtList.add(district);
                    }
                }
                CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District",
                        districtList,
                        false);
                cdd7.show();
                cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    talukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        this.talukaList.add(talukaList);
                    }
                }
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Taluka", talukaList, false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    villageList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject villageList = new CustomSpinnerObject();
                        villageList.set_id(location.getId());
                        villageList.setName(location.getName());
                        villageList.setSelected(false);
                        this.villageList.add(villageList);
                    }
                }
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Village", villageList, false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {

    }
}
