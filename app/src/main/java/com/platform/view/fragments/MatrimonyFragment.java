package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.models.Matrimony.MatrimonyMeetsList;
import com.platform.models.Matrimony.MatrimonyUserDetails;
import com.platform.models.Matrimony.MeetAnalytics;
import com.platform.models.Matrimony.MeetAnalyticsData;
import com.platform.presenter.CreateMeetFirstFragmentPresenter;
import com.platform.presenter.MatrimonyFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.MeetAnalyticsAdapter;
import com.platform.view.adapters.MeetContactsListAdapter;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MatrimonyFragment extends Fragment implements APIDataListener, ViewPager.OnPageChangeListener {
    private View matrimonyFragmentView;
    private FloatingActionButton btnCreateMeet;
    private List<MatrimonyMeet> matrimonyMeetList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private TextView tvMeetTitle,tvMeetDate,tvMeetTime,tvMeetCity,tvMeetVenue,tvRegAmt;
    private RecyclerView rvMeetContacts,rvMeetAnalytics;
    private MeetContactsListAdapter meetContactsListAdapter;
    private MeetAnalyticsAdapter meetAnalyticsAdapter;
    private ArrayList<MatrimonyUserDetails> contactsList= new ArrayList<>();
    private ArrayList<MeetAnalytics> meetAnalyticsData = new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyFragmentPresenter matrimonyFragmentPresenter;

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
        progressBarLayout = matrimonyFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = matrimonyFragmentView.findViewById(R.id.pb_profile_act);
        tvMeetTitle = matrimonyFragmentView.findViewById(R.id.tv_meet_title);
        tvMeetDate = matrimonyFragmentView.findViewById(R.id.tv_meet_date);
        tvMeetTime = matrimonyFragmentView.findViewById(R.id.tv_meet_time);
        tvMeetCity = matrimonyFragmentView.findViewById(R.id.tv_meet_city);
        tvMeetVenue = matrimonyFragmentView.findViewById(R.id.tv_meet_venue);
        tvRegAmt = matrimonyFragmentView.findViewById(R.id.tv_reg_amt);
        rvMeetContacts = matrimonyFragmentView.findViewById(R.id.rv_meet_organizer);
        rvMeetAnalytics = matrimonyFragmentView.findViewById(R.id.rv_meet_analytics);

        meetContactsListAdapter = new MeetContactsListAdapter(contactsList);
        rvMeetContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetContacts.setAdapter(meetContactsListAdapter);

        meetAnalyticsAdapter = new MeetAnalyticsAdapter(this.getActivity(), meetAnalyticsData);
        rvMeetAnalytics.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMeetAnalytics.setAdapter(meetAnalyticsAdapter);
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
            matrimonyFragmentPresenter = new MatrimonyFragmentPresenter(this);
            matrimonyFragmentPresenter.getMatrimonyMeets();
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
//        MatrimonyMeet m1 = new MatrimonyMeet();
//        m1.setTitle("First Meet");
//        m1.setMeetStartTime("1 August 2019");
//        MatrimonyMeet m2 = new MatrimonyMeet();
//        m2.setTitle("Second Meet");
//        m2.setMeetStartTime("10 August 2019");
//        MatrimonyMeet m3 = new MatrimonyMeet();
//        m3.setTitle("Third Meet");
//        m3.setMeetStartTime("10 Sept 2019");
//        matrimonyMeetList.clear();
//        matrimonyMeetList.add(m1);
//        matrimonyMeetList.add(m2);
//        matrimonyMeetList.add(m3);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        matrimonyMeetList.get(0);
    }

    @Override
    public void onPageSelected(int position) {
        matrimonyMeetList.get(1);
        setCurrentMeetData(position);
    }

    private void setCurrentMeetData(int position) {
        //tv.setText(matrimonyMeetList.get(position).getTitle());
        for(MatrimonyUserDetails matrimonyUserDetails: matrimonyMeetList.get(position).getMeetOrganizers()){
            contactsList.add(matrimonyUserDetails);
        }
        meetAnalyticsData = matrimonyMeetList.get(position).getAnalytics();

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        matrimonyMeetList.get(2);
    }

    public void setMatrimonyMeets(MatrimonyMeetsList data) {
        for(MatrimonyMeet m: data.getMeets()){
            matrimonyMeetList.add(m);
        }
    }
}
