package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DefaultFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean)getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard_default, container, false);
    }
}
