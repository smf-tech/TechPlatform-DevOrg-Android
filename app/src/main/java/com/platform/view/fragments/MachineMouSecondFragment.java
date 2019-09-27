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
import com.platform.utility.Util;
import com.platform.view.activities.MachineMouActivity;

public class MachineMouSecondFragment extends Fragment implements View.OnClickListener {
    private View machineMouSecondFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    Button btnSecondPartMou;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouSecondFragmentView = inflater.inflate(R.layout.fragment_machine_mou_second, container, false);
        return machineMouSecondFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineMouSecondFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouSecondFragmentView.findViewById(R.id.pb_profile_act);
        btnSecondPartMou = machineMouSecondFragmentView.findViewById(R.id.btn_second_part_mou);
        btnSecondPartMou.setOnClickListener(this);
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
        switch (view.getId()){
            case R.id.btn_second_part_mou:
                ((MachineMouActivity) getActivity()).openFragment("MachineMouThirdFragment");
                break;
        }
    }
}
