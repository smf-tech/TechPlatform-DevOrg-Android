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

import com.platform.R;
import com.platform.view.activities.MachineMouActivity;

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
                ((MachineMouActivity) getActivity()).openFragment("MachineMouFourthFragment");
                break;
        }
    }
}
