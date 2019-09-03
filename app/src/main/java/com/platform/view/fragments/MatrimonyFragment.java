package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MatrimonyFragment extends Fragment implements PlatformTaskListener, ViewPager.OnPageChangeListener {
    private View matrimonyFragmentView;
    private FloatingActionButton btnCreateMeet;
    List<MatrimonyMeet> matrimonyMeetList = new ArrayList<>();
    ViewPagerAdapter adapter;
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.event_matrimony_screen_visit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        matrimonyFragmentView = inflater.inflate(R.layout.fragment_matrimony, container, false);
        return matrimonyFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
    }

    private void init() {
        //PopulateData method called temporary
        PopulateData();
        //tv = matrimonyFragmentView.findViewById(R.id.test);
        ViewPager meetViewPager = matrimonyFragmentView.findViewById(R.id.meet_view_pager);
        // Disable clip to padding
        meetViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        meetViewPager.setPadding(80, 20, 80, 20);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        meetViewPager.setPageMargin(30);
        meetViewPager.setOnPageChangeListener(this);
        setupViewPager(meetViewPager);
        btnCreateMeet = matrimonyFragmentView.findViewById(R.id.btn_create_meet);
        btnCreateMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMatrimonyIntent = new Intent(getActivity(), CreateMatrimonyMeetActivity.class);
                createMatrimonyIntent.putExtra("SwitchToFragment", "CreateMeetFirstFragment");
                startActivity(createMatrimonyIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (Util.isConnected(getContext())) {
            //api call
        } else {

        }
    }

    private void setupViewPager(ViewPager meetViewPager) {

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < matrimonyMeetList.size(); i++) {
            MatrimonyMeetFragment matrimonyMeetFragment = new MatrimonyMeetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Home.MATRIMONY, matrimonyMeetList.get(i));
            matrimonyMeetFragment.setArguments(bundle);
            adapter.addFragment(matrimonyMeetFragment, getString(R.string.matrimony));
        }
        meetViewPager.setAdapter(adapter);
    }

    private void PopulateData(){
        MatrimonyMeet m1 = new MatrimonyMeet();
        m1.setTitle("First Meet");
        m1.setMeetStartTime("1 August 2019");
        MatrimonyMeet m2 = new MatrimonyMeet();
        m2.setTitle("Second Meet");
        m2.setMeetStartTime("10 August 2019");
        MatrimonyMeet m3 = new MatrimonyMeet();
        m3.setTitle("Third Meet");
        m3.setMeetStartTime("10 Sept 2019");
        matrimonyMeetList.clear();
        matrimonyMeetList.add(m1);
        matrimonyMeetList.add(m2);
        matrimonyMeetList.add(m3);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        PopulateData();
    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        matrimonyMeetList.get(0);
    }

    @Override
    public void onPageSelected(int position) {
        matrimonyMeetList.get(1);
        //tv.setText(matrimonyMeetList.get(position).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        matrimonyMeetList.get(2);
    }
}
