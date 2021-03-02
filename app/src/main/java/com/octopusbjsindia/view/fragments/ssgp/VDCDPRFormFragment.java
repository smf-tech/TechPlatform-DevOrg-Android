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
import com.octopusbjsindia.presenter.ssgp.VDCDPRFormFragmentPresenter;
import com.octopusbjsindia.presenter.ssgp.VDFFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class VDCDPRFormFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomSpinnerListener {

    private View vdcdprFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private VDCDPRFormFragmentPresenter presenter;
    private EditText etState,etDistrict, etTaluka, etVillage,et_remark,
            et_date,et_machine_code,et_machine_status,et_start_meter_reading,et_end_meter_reading,et_reason,et_struct_code,et_structure_type;
    private ImageView iv_start_meter,iv_end_meter,iv_structure_photo;
    private String UrlStartMeterPhoto,UrlEndMeterPhoto,UrlStructurePhoto;
    private Button btn_submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vdcdprFormFragmentView = inflater.inflate(R.layout.fragment_v_d_c_dpr_form, container, false);
        return vdcdprFormFragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        ((GPActionsActivity) getActivity()).setTitle("VDC DPR Form");
        progressBarLayout = vdcdprFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdcdprFormFragmentView.findViewById(R.id.pb_profile_act);
        presenter = new VDCDPRFormFragmentPresenter(this);
        etState = vdcdprFormFragmentView.findViewById(R.id.et_state);
        etDistrict = vdcdprFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdcdprFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdcdprFormFragmentView.findViewById(R.id.et_village);
        et_date  = vdcdprFormFragmentView.findViewById(R.id.et_date);
        et_machine_code = vdcdprFormFragmentView.findViewById(R.id.et_machine_code);
        et_machine_status = vdcdprFormFragmentView.findViewById(R.id.et_machine_status);
        et_start_meter_reading = vdcdprFormFragmentView.findViewById(R.id.et_start_meter_reading);
        et_end_meter_reading = vdcdprFormFragmentView.findViewById(R.id.et_end_meter_reading);
        et_reason = vdcdprFormFragmentView.findViewById(R.id.et_reason);
        et_struct_code = vdcdprFormFragmentView.findViewById(R.id.et_struct_code);
        et_structure_type = vdcdprFormFragmentView.findViewById(R.id.et_structure_type);


        et_start_meter_reading  = vdcdprFormFragmentView.findViewById(R.id.et_start_meter_reading);
        et_end_meter_reading  = vdcdprFormFragmentView.findViewById(R.id.et_end_meter_reading);
        iv_start_meter = vdcdprFormFragmentView.findViewById(R.id.iv_start_meter);
        iv_end_meter = vdcdprFormFragmentView.findViewById(R.id.iv_end_meter);
        iv_structure_photo = vdcdprFormFragmentView.findViewById(R.id.iv_structure_photo);
        btn_submit = vdcdprFormFragmentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);
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
        } else if (et_machine_code.getText().toString().trim().length() == 0) {
            msg =getString(R.string.msg_select_struct_code);
        } else if (et_machine_status.getText().toString().trim().length() == 0) {
            msg = getString(R.string.select_stuct_type);
        } else if (et_struct_code.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (UrlStartMeterPhoto.trim().length() == 0) {
            msg = "Please upload start meter reading photo";
        } else if (UrlEndMeterPhoto.trim().length() == 0) {
            msg = "Please upload end meter reading photo";
        } else if (UrlStructurePhoto.trim().length() == 0) {
            msg = "Please upload structure photo";
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
