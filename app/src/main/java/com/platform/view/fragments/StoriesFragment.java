package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.activities.FeedCreationActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.MachineWorkingDataListActivity;

@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class StoriesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        if (getActivity() != null && getArguments() != null) {
//            String title = (String) getArguments().getSerializable("TITLE");
//            ((HomeActivity) getActivity()).setActionBarTitle(getString(R.string.tab_stories));
//            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

//            if ((boolean)getArguments().getSerializable("SHOW_BACK")) {
//                ((HomeActivity) getActivity()).showBackArrow();
//            }
//        }

        Intent startMain1 = new Intent(getActivity(), MachineWorkingDataListActivity.class);
        startMain1.putExtra("meetid","5d6f90c25dda765c2f0b5dd4");
        startActivity(startMain1);

        return inflater.inflate(R.layout.fragment_stories, container, false);
    }
}
