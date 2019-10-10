package com.platform.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.models.Matrimony.MatrimonyMeetsList;
import com.platform.models.Matrimony.MatrimonyUserDetails;
import com.platform.models.Matrimony.MeetAnalytics;
import com.platform.presenter.MatrimonyFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.MatrimonyProfileListActivity;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.platform.view.adapters.MeetAnalyticsAdapter;
import com.platform.view.adapters.MeetContactsListAdapter;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Constants.FORM_DATE_FORMAT;

public class MatrimonyFragment extends Fragment implements  View.OnClickListener , APIDataListener, ViewPager.OnPageChangeListener{
    private View matrimonyFragmentView;
    private String mobileNumberEntered="";
    private FloatingActionButton btnCreateMeet;
    private List<MatrimonyMeet> matrimonyMeetList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private TextView tvMeetTitle,tvMeetDate,tvMeetTime,tvMeetCity,tvMeetVenue,tvRegAmt,tvRegPeriod,tvBadgesInfo,btnViewProfiles, btnRegisterProfile;
    private RecyclerView rvMeetContacts,rvMeetAnalytics;
    private MeetContactsListAdapter meetContactsListAdapter;
    private ArrayList<MatrimonyUserDetails> contactsList= new ArrayList<>();
    private ArrayList<MeetAnalytics> meetAnalyticsData = new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyFragmentPresenter matrimonyFragmentPresenter;
    private ViewPager meetViewPager;
    private Button btnPublishMeet;
    private int currentPosition;
    private static MatrimonyFragment instance = null;
    private RelativeLayout rlNoMeet, rl_meetLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
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

    public static MatrimonyFragment getInstance() {
        return instance;
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
        progressBarLayout = matrimonyFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = matrimonyFragmentView.findViewById(R.id.pb_profile_act);
        tvMeetTitle = matrimonyFragmentView.findViewById(R.id.tv_meet_title);
        tvMeetDate = matrimonyFragmentView.findViewById(R.id.tv_meet_date);
        tvMeetTime = matrimonyFragmentView.findViewById(R.id.tv_meet_time);
        tvMeetCity = matrimonyFragmentView.findViewById(R.id.tv_meet_city);
        tvMeetVenue = matrimonyFragmentView.findViewById(R.id.tv_meet_venue);
        tvRegAmt = matrimonyFragmentView.findViewById(R.id.tv_reg_amt);
        tvRegPeriod = matrimonyFragmentView.findViewById(R.id.tv_reg_period);
        tvBadgesInfo = matrimonyFragmentView.findViewById(R.id.tv_badges_info);
        rvMeetContacts = matrimonyFragmentView.findViewById(R.id.rv_meet_organizer);
        rvMeetAnalytics = matrimonyFragmentView.findViewById(R.id.rv_meet_analytics);
        btnPublishMeet = matrimonyFragmentView.findViewById(R.id.btn_publish_saved_meet);
        btnPublishMeet.setOnClickListener(this);

        btnViewProfiles = matrimonyFragmentView.findViewById(R.id.btn_view_profiles);
        btnRegisterProfile = matrimonyFragmentView.findViewById(R.id.btn_register_profile);
        btnViewProfiles.setOnClickListener(this);
        btnRegisterProfile.setOnClickListener(this);

        rlNoMeet = matrimonyFragmentView.findViewById(R.id.rl_no_meet);
        rl_meetLayout = matrimonyFragmentView.findViewById(R.id.rl_meetLayout);
        meetViewPager = matrimonyFragmentView.findViewById(R.id.meet_view_pager);
        // Disable clip to padding
        meetViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        meetViewPager.setPadding(80, 20, 80, 20);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        meetViewPager.setPageMargin(30);
        meetViewPager.setOnPageChangeListener(this);
        //setupViewPager(meetViewPager);

        meetContactsListAdapter = new MeetContactsListAdapter(contactsList, getActivity());
        RecyclerView.LayoutManager mContactsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        rvMeetContacts.setLayoutManager(mContactsLayoutManager);
        rvMeetContacts.setAdapter(meetContactsListAdapter);

        btnCreateMeet = matrimonyFragmentView.findViewById(R.id.btn_create_meet);
        btnCreateMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMatrimonyIntent = new Intent(getActivity(), CreateMatrimonyMeetActivity.class);
                createMatrimonyIntent.putExtra("SwitchToFragment", "CreateMeetFirstFragment");
                startActivity(createMatrimonyIntent);
            }
        });
        ScrollView sv = matrimonyFragmentView.findViewById(R.id.sv_matrimony_fragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        btnCreateMeet.hide();
                    } else {
                        btnCreateMeet.show();
                    }
                }
            });
        }
        getMeetsCall();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getMeetsCall(){
        if (Util.isConnected(getContext())) {
            matrimonyFragmentPresenter = new MatrimonyFragmentPresenter(this);
            matrimonyFragmentPresenter.getMatrimonyMeets();
        } else {
            RelativeLayout rlMatrimonyFragment = matrimonyFragmentView.findViewById(R.id.rl_matrimony_fragment);
            rlMatrimonyFragment.setVisibility(View.GONE);
            Util.showToast(getString(R.string.msg_no_network), this);
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

    @Override
    public void onFailureListener(String requestID, String message) {
        ScrollView svMatrimonyFragment = matrimonyFragmentView.findViewById(R.id.sv_matrimony_fragment);
        svMatrimonyFragment.setVisibility(View.GONE);
        showResponse(String.valueOf(R.string.msg_something_went_wrong));
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        ScrollView svMatrimonyFragment = matrimonyFragmentView.findViewById(R.id.sv_matrimony_fragment);
        svMatrimonyFragment.setVisibility(View.GONE);
        showResponse(getString(R.string.msg_something_went_wrong));
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
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        setCurrentMeetData(position);
    }

    private void setCurrentMeetData(int position) {
        contactsList.clear();
        meetAnalyticsData.clear();
        if(matrimonyMeetList.get(position).getIs_published()){
            btnPublishMeet.setVisibility(View.GONE);
        }else{
            btnPublishMeet.setVisibility(View.VISIBLE);
        }
        tvMeetTitle.setText(matrimonyMeetList.get(position).getTitle());
        tvMeetDate.setText(Util.getDateFromTimestamp(matrimonyMeetList.get(position).getSchedule().getDateTime(),DAY_MONTH_YEAR));
        tvMeetTime.setText(Util.getAmPmTimeStringFromTimeString(matrimonyMeetList.get(position).getSchedule().getMeetStartTime())+" - "+
                Util.getAmPmTimeStringFromTimeString(matrimonyMeetList.get(position).getSchedule().getMeetEndTime()));
        tvMeetCity.setText(matrimonyMeetList.get(position).getLocation().getCity());
        tvMeetVenue.setText(matrimonyMeetList.get(position).getVenue());
        tvRegAmt.setText(String.valueOf(matrimonyMeetList.get(position).getRegAmount()));
        tvRegPeriod.setText(Util.getDateFromTimestamp(matrimonyMeetList.get(position).getRegistrationSchedule().getRegStartDateTime(),
                DAY_MONTH_YEAR)+" - "+
                Util.getDateFromTimestamp(matrimonyMeetList.get(position).getRegistrationSchedule().getRegEndDateTime(), DAY_MONTH_YEAR));
        if(matrimonyMeetList.get(position).getAllocate()){
            if(matrimonyMeetList.get(position).getBadgeFanlize()){
                tvBadgesInfo.setText(R.string.meet_badges_allocated_finalized);
            } else {
                tvBadgesInfo.setText(R.string.meet_badges_allocated_not_finalized);
            }
        } else {
            tvBadgesInfo.setText(R.string.meet_badges_not_allocated);
        }

        for(MatrimonyUserDetails matrimonyUserDetails: matrimonyMeetList.get(position).getMeetOrganizers()){
            contactsList.add(matrimonyUserDetails);
        }
        meetAnalyticsData.addAll(matrimonyMeetList.get(position).getAnalytics());
        MeetAnalyticsAdapter meetAnalyticsAdapter = new MeetAnalyticsAdapter(this.getActivity(), meetAnalyticsData);
        RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        rvMeetAnalytics.setLayoutManager(mLayoutManagerLeave);
        rvMeetAnalytics.setAdapter(meetAnalyticsAdapter);
        adapter.notifyDataSetChanged();
        meetContactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void setMatrimonyMeets(List<MatrimonyMeet> data) {
        matrimonyMeetList.clear();
        if(data.size()>0) {
            rl_meetLayout.setVisibility(View.VISIBLE);
            rlNoMeet.setVisibility(View.GONE);
            matrimonyMeetList.addAll(data);
            setupViewPager(meetViewPager);
            setCurrentMeetData(0);
        } else {
            rl_meetLayout.setVisibility(View.GONE);
            rlNoMeet.setVisibility(View.VISIBLE);
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No Meet available at your location.",
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_publish_saved_meet:
                matrimonyFragmentPresenter.publishSavedMeet(matrimonyMeetList.get(currentPosition).getId());
                break;
            case R.id.btn_register_profile:

                showReasonDialog(getActivity(),0);

                break;
            case R.id.btn_view_profiles:

                Intent startMain = new Intent(getActivity(), MatrimonyProfileListActivity.class);
                startMain.putExtra("meetid",matrimonyMeetList.get(currentPosition).getId());
                startMain.putExtra("mobileNumber",mobileNumberEntered);

                startActivity(startMain);
                break;
        }
    }

    public void showResponse(String responseStatus) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
    }

    public void showResponse(String responseStatus, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(status == 200){
            matrimonyMeetList.get(currentPosition).setIs_published(true);
            adapter.notifyDataSetChanged();
            btnPublishMeet.setVisibility(View.GONE);
        }
    }

    public void showResponseVerifyUser(String responseStatus, int status) {

        if(status == 200){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), responseStatus,
                    Snackbar.LENGTH_LONG);
        }else{
            if (matrimonyMeetList.get(currentPosition).getIs_published()) {
                if (matrimonyMeetList.get(currentPosition).getRegistrationSchedule().getRegEndDateTime() >= Util.getCurrentTimeStamp()
                        && matrimonyMeetList.get(currentPosition).getRegistrationSchedule().getRegStartDateTime()<= Util.getCurrentTimeStamp())
                {
                    //Util.logger("currentTime","-> Current Time greater");
                    Intent startMain1 = new Intent(getActivity(), UserRegistrationMatrimonyActivity.class);
                    startMain1.putExtra("meetid", matrimonyMeetList.get(currentPosition).getId());
                    startMain1.putExtra("mobileNumber",mobileNumberEntered);

                    startActivity(startMain1);
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Registrations for this meet are not open.",
                            Snackbar.LENGTH_LONG);
                }
            }else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "This meet is not published yet.",
                        Snackbar.LENGTH_LONG);
            }
        }
    }

    public void updateMeetList(){
        getMeetsCall();
    }

    public void updateBadgeStatus(boolean badgeAllocatedandFinalisedflag){

            if(badgeAllocatedandFinalisedflag){
                tvBadgesInfo.setText(R.string.meet_badges_allocated_finalized);
            } else {
                tvBadgesInfo.setText(R.string.meet_badges_allocated_not_finalized);
            }
    }



    public String showReasonDialog(final Activity context, int pos){
        Dialog dialog;
        Button btnSubmit,btn_cancel;
        EditText edt_reason;
        Activity activity =context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mobile_input_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /*Intent loginIntent = new Intent(context, LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(loginIntent);*/
                String strReason  = edt_reason.getText().toString();


                if (strReason.trim().length() != 10 ) {
                    String msg = "Please enter the valid mobile number";//getResources().getString(R.string.msg_enter_name);
                    //et_primary_mobile.requestFocus();
                    Util.showToast(msg,getActivity());
                }else {

                    //-----------------------
                    if (TextUtils.isEmpty(strReason)) {
                        Util.logger("Empty Reason", "Reason Can not be blank");
                        Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Reason Can not be blank",
                                Snackbar.LENGTH_LONG);
                    } else {
                    /*if (fragment instanceof TMUserLeavesApprovalFragment) {
                        ((TMUserLeavesApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserAttendanceApprovalFragment) {
                        ((TMUserAttendanceApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserProfileApprovalFragment) {
                        ((TMUserProfileApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserFormsApprovalFragment) {
                        ((TMUserFormsApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }*/
                        onReceiveReason(strReason, pos);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();


        return "";
    }

    public void onReceiveReason(String strReason, int pos) {
        //callRejectAPI(strReason,pos);
        matrimonyFragmentPresenter.VerifyUserProfile(strReason,"",matrimonyMeetList.get(currentPosition).getId());
        mobileNumberEntered = strReason;
    }

}
