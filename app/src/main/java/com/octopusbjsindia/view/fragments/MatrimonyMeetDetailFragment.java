package com.octopusbjsindia.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserDetails;
import com.octopusbjsindia.models.Matrimony.MeetAnalytics;
import com.octopusbjsindia.models.Matrimony.MeetBatchesResponseModel;
import com.octopusbjsindia.presenter.MatrimonyMeetDetailFragmentPresenter;
import com.octopusbjsindia.presenter.MatrimonyMeetFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.activities.MatrimonyBookletActivity;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.activities.ShowMeetBatchesActivity;
import com.octopusbjsindia.view.activities.UserRegistrationMatrimonyActivity;
import com.octopusbjsindia.view.adapters.MeetAnalyticsAdapter;
import com.octopusbjsindia.view.adapters.MeetContactsListAdapter;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class MatrimonyMeetDetailFragment extends Fragment implements View.OnClickListener, APIDataListener,
        ViewPager.OnPageChangeListener,
        PopupMenu.OnMenuItemClickListener {
    private View view;
    private String mobileNumberEntered="";
    private MatrimonyMeet meetData;
    private ViewPagerAdapter adapter;
    private TextView tvMeetTitle,tvMeetDate,tvMeetTime,tvMeetCity,tvMeetVenue,tvRegAmt,tvRegPeriod,tvBadgesInfo,btnViewProfiles, btnRegisterProfile;
    private RecyclerView rvMeetContacts,rvMeetAnalytics;
    private MeetContactsListAdapter meetContactsListAdapter;
    private ArrayList<MatrimonyUserDetails> contactsList= new ArrayList<>();
    private ArrayList<MeetAnalytics> meetAnalyticsData = new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyMeetDetailFragmentPresenter presenter;
    private ViewPager meetViewPager;
    private Button btnPublishMeet;
    private int currentPosition;
    private static MatrimonyMeetDetailFragment instance = null;
    private RelativeLayout rlNoMeet, rl_meetLayout;
    private final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    PopupMenu popup;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        AppEvents.trackAppEvent(getString(R.string.event_matrimony_screen_visit));
    }

    public static MatrimonyMeetDetailFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matrimony_meet_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            meetData = (MatrimonyMeet) this.getArguments().getSerializable("MeetData");
        }
        init();
    }

    private void init() {
        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        tvMeetTitle = view.findViewById(R.id.tv_meet_title);
        tvMeetDate = view.findViewById(R.id.tv_meet_date);
        tvMeetTime = view.findViewById(R.id.tv_meet_time);
        tvMeetCity = view.findViewById(R.id.tv_meet_city);
        tvMeetVenue = view.findViewById(R.id.tv_meet_venue);
        tvRegAmt = view.findViewById(R.id.tv_reg_amt);
        tvRegPeriod = view.findViewById(R.id.tv_reg_period);
        tvBadgesInfo = view.findViewById(R.id.tv_badges_info);
        rvMeetContacts = view.findViewById(R.id.rv_meet_organizer);
        rvMeetAnalytics = view.findViewById(R.id.rv_meet_analytics);
        btnPublishMeet = view.findViewById(R.id.btn_publish_saved_meet);
        btnPublishMeet.setOnClickListener(this);

        btnViewProfiles = view.findViewById(R.id.btn_view_profiles);
        btnRegisterProfile = view.findViewById(R.id.btn_register_profile);
        btnViewProfiles.setOnClickListener(this);
        btnRegisterProfile.setOnClickListener(this);

        rlNoMeet = view.findViewById(R.id.rl_no_meet);
        rl_meetLayout = view.findViewById(R.id.rl_meetLayout);
        meetViewPager = view.findViewById(R.id.meet_view_pager);
        // Disable clip to padding
        meetViewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        meetViewPager.setPadding(30, 10, 30, 10);
//        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        meetViewPager.setPageMargin(10);
        meetViewPager.setOnPageChangeListener(this);
        //setupViewPager(meetViewPager);

        meetContactsListAdapter = new MeetContactsListAdapter(contactsList, getActivity());
        RecyclerView.LayoutManager mContactsLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        rvMeetContacts.setLayoutManager(mContactsLayoutManager);
        rvMeetContacts.setAdapter(meetContactsListAdapter);

        TextView ivIconPublished = view.findViewById(R.id.iv_icon_published);
        if (meetData.getIs_published()) {
            ivIconPublished.setText("Published");//(getResources().getDrawable(R.drawable.ic_icon_publish));
        } else {
            ivIconPublished.setText(" Saved");//ivIconPublished.setImageDrawable(getResources().getDrawable(R.drawable.ic_meet_saved_label));
        }
        if (meetData.getArchive()) {
            ivIconPublished.setText("Archived");
        }
        if (meetData.getMeetImageUrl() != null && !meetData.getMeetImageUrl().isEmpty()) {
            ImageView ivMeetImage = view.findViewById(R.id.iv_meet_image);
            Glide.with(this)
                    .load(meetData.getMeetImageUrl())
                    .into(ivMeetImage);
        } else {
            ImageView ivMeetImage = view.findViewById(R.id.iv_meet_image);
            Glide.with(this)
                    .load(R.drawable.matrimony_meet_bg)
                    .into(ivMeetImage);
        }
        ImageView btnPopupMenu = view.findViewById(R.id.btn_popmenu);
        btnPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu((getActivity()), v);
                popup.setOnMenuItemClickListener(MatrimonyMeetDetailFragment.this);
                popup.inflate(R.menu.matrimony_meet_menu);
                if (meetData.getIs_published()) {
                    popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                }

                if (meetData.getRegistrationSchedule().getRegEndDateTime() <= Util.getCurrentTimeStamp()) {
                    popup.getMenu().findItem(R.id.action_gen_booklet).setVisible(false);
                } else {
                    popup.getMenu().findItem(R.id.action_gen_booklet).setVisible(false);
                }

                if (meetData.getArchive()) {
                    popup.getMenu().findItem(R.id.action_archive).setVisible(false);
                }
                popup.show();
            }
        });
        setCurrentMeetData();
//        getMeetsCall();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        ScrollView svMatrimonyFragment = view.findViewById(R.id.sv_matrimony_fragment);
        svMatrimonyFragment.setVisibility(View.GONE);
//        showResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        ScrollView svMatrimonyFragment = view.findViewById(R.id.sv_matrimony_fragment);
        svMatrimonyFragment.setVisibility(View.GONE);
//        showResponse(getResources().getString(R.string.msg_something_went_wrong));
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        activity.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        activity.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (activity != null) {
            activity.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
//        currentPosition = position;
//        setCurrentMeetData(position);
    }

    private void setCurrentMeetData() {
        contactsList.clear();
        meetAnalyticsData.clear();
        if (meetData.getIs_published()) {
            btnPublishMeet.setVisibility(View.GONE);
        }else{
            btnPublishMeet.setVisibility(View.VISIBLE);
        }
        tvMeetTitle.setText(meetData.getTitle());
        tvMeetDate.setText(Util.getDateFromTimestamp(meetData.getSchedule().getDateTime(), DAY_MONTH_YEAR));
        tvMeetTime.setText(Util.getAmPmTimeStringFromTimeString(meetData.getSchedule().getMeetStartTime()) + " - " +
                Util.getAmPmTimeStringFromTimeString(meetData.getSchedule().getMeetEndTime()));
        tvMeetCity.setText(meetData.getLocation().getCity());
        tvMeetVenue.setText(meetData.getVenue());
        tvRegAmt.setText(String.valueOf(meetData.getRegAmount()));
        tvRegPeriod.setText(Util.getDateFromTimestamp(meetData.getRegistrationSchedule().getRegStartDateTime(),
                DAY_MONTH_YEAR)+" - "+
                Util.getDateFromTimestamp(meetData.getRegistrationSchedule().getRegEndDateTime(), DAY_MONTH_YEAR));

        for (MatrimonyUserDetails matrimonyUserDetails : meetData.getMeetOrganizers()) {
            contactsList.add(matrimonyUserDetails);
        }
        meetAnalyticsData.addAll(meetData.getAnalytics());
        MeetAnalyticsAdapter meetAnalyticsAdapter = new MeetAnalyticsAdapter(this.activity, meetAnalyticsData);
        RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(activity,
                RecyclerView.VERTICAL, false);
        rvMeetAnalytics.setLayoutManager(mLayoutManagerLeave);
        rvMeetAnalytics.setAdapter(meetAnalyticsAdapter);
//        adapter.notifyDataSetChanged();
        meetContactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_publish_saved_meet:
                presenter.publishSavedMeet(meetData.getId());
                break;
            case R.id.btn_register_profile:
                showReasonDialog(activity,0);
                break;
            case R.id.btn_view_profiles:
                Intent startMain = new Intent(activity, MatrimonyProfileListActivity.class);
                startMain.putExtra("toOpean","MeetUserList");
                startMain.putExtra("meetid", meetData.getId());
                startActivity(startMain);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive:
                showDialog("Archive Meet", "Are you sure you want to archive this meet?", "YES", "NO");
                break;
            case R.id.action_delete:
                showDialog("Delete Meet", "Are you sure you want to delete this meet?", "YES", "NO");
                break;
            case R.id.action_allocate_badge:
                presenter.meetAllocateBadges(meetData.getId(), "allocateBadges");
                break;
            case R.id.action_finalise_badge:
                presenter.meetAllocateBadges(meetData.getId(), "finalizeBadges");
                break;
            case R.id.action_gen_booklet:
                Intent bookletIntent = new Intent(getActivity(), MatrimonyBookletActivity.class);
                bookletIntent.putExtra("meetId", meetData.getId());
                getActivity().startActivity(bookletIntent);
                break;
            case R.id.action_show_baches:
                presenter.showMeetBaches(meetData.getId(), "showbaches");
                break;
        }
        return false;
    }


    public void showResponse(String responseStatus, int status) {
        Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(status == 200){
            meetData.setIs_published(true);
            adapter.notifyDataSetChanged();
            btnPublishMeet.setVisibility(View.GONE);
        }
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if (requestId.equals(MatrimonyMeetFragmentPresenter.MEET_FINALIZE_BADGES)) {
            if (status == 200) {
                popup.getMenu().findItem(R.id.action_finalise_badge).setVisible(false);
                meetData.setBadgeFanlize(true);
                updateBadgeStatus(true);
            }
        }
        if (requestId.equals(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_ARCHIVE)) {
            if (status == 200) {
                popup.getMenu().findItem(R.id.action_archive).setVisible(false);
                meetData.setArchive(true);
            }
        }
        if (requestId.equals(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_DELETE)) {
            if (status == 200) {
                //matrimonyMeetList.get(currentPosition).setIs_published(true);
//                updateMeetList();
                getActivity().finish();
            }
        }
        if (requestId.equals(MatrimonyMeetFragmentPresenter.MEET_ALLOCATE_BADGES)) {
            if (status == 200) {
                //matrimonyMeetList.get(currentPosition).setIs_published(true);
                updateBadgeStatus(false);
            }
        }
    }

    public void showBachesResponse(String response) {
        Util.logger("Batches response-", response);

        Gson gson = new Gson();

        MeetBatchesResponseModel meetBatchesResponseModel = gson.fromJson(response, MeetBatchesResponseModel.class);
        if (meetBatchesResponseModel.getStatus().equalsIgnoreCase("200")) {
            Intent intent = new Intent(getActivity(), ShowMeetBatchesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("batches_resposne", response);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Util.showToast("No Batches available yet", getActivity());
        }

    }

    public void showResponseVerifyUser(String responseStatus, int status) {

        if(status == 200){
            Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                            .findViewById(android.R.id.content), responseStatus,
                    Snackbar.LENGTH_LONG);
        }else{
            if (meetData.getIs_published()) {
                if (meetData.getRegistrationSchedule().getRegEndDateTime() >= Util.getCurrentTimeStamp()
                        && meetData.getRegistrationSchedule().getRegStartDateTime() <= Util.getCurrentTimeStamp())
                {
                    //Util.logger("currentTime","-> Current Time greater");
                    Intent startMain1 = new Intent(activity, UserRegistrationMatrimonyActivity.class);
                    startMain1.putExtra("meetid", meetData.getId());
                    startMain1.putExtra("mobileNumber",mobileNumberEntered);

                    startActivity(startMain1);
                } else {
                    Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Registrations for this meet are not open.",
                            Snackbar.LENGTH_LONG);
                }
            }else {
                Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                .findViewById(android.R.id.content), "This meet is not published yet.",
                        Snackbar.LENGTH_LONG);
            }
        }
    }

    public void updateBadgeStatus(boolean badgeAllocatedandFinalisedflag){

            if(badgeAllocatedandFinalisedflag){
                tvBadgesInfo.setText(R.string.meet_badges_allocated_finalized);
            } else {
                tvBadgesInfo.setText(R.string.meet_badges_allocated_not_finalized);
            }
    }


    public void onReceiveReason(String strReason, int pos) {
        //callRejectAPI(strReason,pos);
        presenter.VerifyUserProfile(strReason, "", meetData.getId());
        mobileNumberEntered = strReason;
    }

    public void logOutUser() {
        // remove user related shared pref data

        Util.saveLoginObjectInPref("");

        try {
            Intent startMain = new Intent(activity, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
                String strReason  = edt_reason.getText().toString();

                if (strReason.trim().length() != 10 ) {
                    String msg = "Please enter the valid mobile number";//getResources().getString(R.string.msg_enter_name);
                    //et_primary_mobile.requestFocus();
                    Util.showToast(msg,activity);
                }else {

                    //-----------------------
                    if (TextUtils.isEmpty(strReason)) {
                        Util.logger("Empty Reason", "Reason can not be blank");
                        Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Reason can not be blank",
                                Snackbar.LENGTH_LONG);
                    } else {
                        onReceiveReason(strReason, pos);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();

        return "";
    }

    private void showDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
                if (dialogTitle.equals("Archive Meet")) {
                    presenter.meetArchiveDelete(meetData.getId(), "Archive");
                } else if (dialogTitle.equals("Delete Meet")) {
                    presenter.meetArchiveDelete(meetData.getId(), "Deleted");
                }
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}
