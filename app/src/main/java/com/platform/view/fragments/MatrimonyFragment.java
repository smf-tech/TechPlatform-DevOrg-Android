package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MatrimonyFragment extends Fragment implements PlatformTaskListener {
    private View matrimonyFragmentView;

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
        init();
    }

    private void init() {
        ViewPager meetViewPager = matrimonyFragmentView.findViewById(R.id.meet_view_pager);
        // Disable clip to padding
        meetViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        meetViewPager.setPadding(100, 50, 100, 50);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        meetViewPager.setPageMargin(30);
        setupViewPager(meetViewPager);
    }

    private void setupViewPager(ViewPager meetViewPager) {

        List<MatrimonyMeet> matrimonyMeetList = new ArrayList<>();
        MatrimonyMeet m1 = new MatrimonyMeet();
        m1.setMeetTitle("First Meet");
        m1.setMeetDateTime("1 August 2019");
        MatrimonyMeet m2 = new MatrimonyMeet();
        m2.setMeetTitle("Second Meet");
        m2.setMeetDateTime("10 August 2019");
        MatrimonyMeet m3 = new MatrimonyMeet();
        m3.setMeetTitle("Third Meet");
        m3.setMeetDateTime("20 August 2019");
        matrimonyMeetList.add(m1);
        matrimonyMeetList.add(m2);
        matrimonyMeetList.add(m3);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for(int i = 0; i<matrimonyMeetList.size(); i++){
            MatrimonyMeetFragment matrimonyMeetFragment = new MatrimonyMeetFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Home.MATRIMONY, matrimonyMeetList.get(i));
            matrimonyMeetFragment.setArguments(bundle);
            adapter.addFragment(matrimonyMeetFragment, getString(R.string.matrimony));
        }
        meetViewPager.setAdapter(adapter);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }
}
