package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.models.SujalamSuphalam.MouDetails;
import com.platform.models.SujalamSuphalam.RateDetail;
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;

import java.util.ArrayList;
import java.util.List;

public class MachineMouThirdFragment extends Fragment implements View.OnClickListener {
    private View machineMouThirdFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnThirdPartMou;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouThirdFragmentView = inflater.inflate(R.layout.fragment_machine_mou_third, container, false);
        return machineMouThirdFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineMouThirdFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouThirdFragmentView.findViewById(R.id.pb_profile_act);
        btnThirdPartMou = machineMouThirdFragmentView.findViewById(R.id.btn_third_part_mou);
        btnThirdPartMou.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_third_part_mou:
                if(isAllDataValid()){
                    setMachineThirdData();
                    ((MachineMouActivity) getActivity()).openFragment("MachineMouFourthFragment");
                }
                break;
        }
    }

    private void setMachineThirdData() {
        MouDetails mouDetails = new MouDetails();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setMouDetails(mouDetails);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setDateOfSigning(745687875);
        RateDetail rateDetail = new RateDetail();
        rateDetail.setFromDate(6345672);
        rateDetail.setToDate(32642633);
        rateDetail.setValue("1500");
        List<RateDetail> rateDetailsList = new ArrayList();
        rateDetailsList.add(rateDetail);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setRateDetails(rateDetailsList);
        List mouList = new ArrayList();
        mouList.add("www.google.com");
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setMOUImages(mouList);
        //((MachineMouActivity) getActivity()).getMachineDetailData().getMouDetails().setIsMOUCancelled("NO");
    }

    public boolean isAllDataValid() {
//        if (TextUtils.isEmpty(edtMeetName.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetVenue.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetDate.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetStartTime.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetEndTime.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetRegStartDate.getText().toString().trim())
//                || TextUtils.isEmpty(edtMeetRegEndDate.getText().toString().trim())
//                || selectedMeetType == null || selectedStateId == null || selectedCityId == null || selectedCountryId == null
//                || isPaidFreeRGChecked == 0) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                Snackbar.LENGTH_LONG);
//            return false;
//        }
        return true;
    }
}
