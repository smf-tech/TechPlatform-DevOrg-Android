package com.octopusbjsindia.view.fragments.ssgp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.octopusbjsindia.R;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;

public class VDCBDFormFragment extends Fragment {
    private View vdcbdFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private EditText etState,etDistrict, etTaluka, etVillage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vdcbdFormFragmentView = inflater.inflate(R.layout.fragment_v_d_c_bd_form, container, false);
        return vdcbdFormFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        ((GPActionsActivity) getActivity()).setTitle("VDF Form");
        progressBarLayout = vdcbdFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdcbdFormFragmentView.findViewById(R.id.pb_profile_act);
        //presenter = new VDFFormFragmentPresenter(this);
        etDistrict = vdcbdFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdcbdFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdcbdFormFragmentView.findViewById(R.id.et_village);
    }
}
