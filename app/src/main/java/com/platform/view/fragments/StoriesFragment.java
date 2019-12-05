package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.activities.FeedCreationActivity;
import com.platform.view.activities.HomeActivity;

@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class StoriesFragment extends Fragment {

    View view;

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

        /*Intent startMain1 = new Intent(getActivity(), FeedCreationActivity.class);
        startMain1.putExtra("meetid","5d6f90c25dda765c2f0b5dd4");
        startActivity(startMain1);*/
        view = inflater.inflate(R.layout.fragment_stories, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabAddFeed = view.findViewById(R.id.fab_add_feed);
        fabAddFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), FeedCreationActivity.class);
//                startActivity(intent);
            }
        });
    }
}
