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
import android.widget.TextView;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;

public class MatrimonyMeetFragment extends Fragment {
    private View matrimonyMeetFragmentView;
    private TextView text_title,text_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        matrimonyMeetFragmentView = inflater.inflate(R.layout.fragment_matrimony_meet, container, false);
        return matrimonyMeetFragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_title = matrimonyMeetFragmentView.findViewById(R.id.text_title);
        text_date =  matrimonyMeetFragmentView.findViewById(R.id.text_date);
        Bundle arguments = getArguments();
        if (arguments != null) {
            MatrimonyMeet meetData = (MatrimonyMeet) arguments.getSerializable(Constants.Home.MATRIMONY);
            text_title.setText(meetData.getMeetTitle());
            text_date.setText(meetData.getMeetDateTime());
        }
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
    }
}
