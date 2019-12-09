package com.octopusbjsindia.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octopusbjsindia.R;
import com.octopusbjsindia.view.activities.CreateFeedActivity;

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

        /*Intent startMain1 = new Intent(getActivity(), MachineWorkingDataListActivity.class);
        startMain1.putExtra("machineId","5de229c1ca632728f60f19aa");
        startMain1.putExtra("machineName","5de229c1ca632728f60f19aa");

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
                Intent intent = new Intent(getActivity(), CreateFeedActivity.class);
                startActivity(intent);
            }
        });
    }
}
