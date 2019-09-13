package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.presenter.MatrimonyBookletFragmentPresenter;

public class MatrimonyBookletFragment extends Fragment implements View.OnClickListener{
    private View MatrimonyBookletFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyBookletFragmentPresenter matrimonyMeetFragmentPresenter;
    private ImageView ivBooklet1, ivBooklet2, ivBooklet3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MatrimonyBookletFragmentView = inflater.inflate(R.layout.fragment_matrimony_booklet, container, false);
        return MatrimonyBookletFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBooklet1 = view.findViewById(R.id.tv_meet_types);
        ivBooklet1.setOnClickListener(this);
        ivBooklet2 = view.findViewById(R.id.tv_meet_country);
        ivBooklet2.setOnClickListener(this);
        ivBooklet3 = view.findViewById(R.id.tv_meet_state);
        ivBooklet3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_booklet1:
                break;
            case R.id.iv_booklet2:
                break;
            case R.id.iv_booklet3:
                break;
        }
    }
}
