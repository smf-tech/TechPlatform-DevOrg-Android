package com.platform.view.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.presenter.MatrimonyMeetFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.HomeActivity;

public class MatrimonyMeetFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, APIDataListener {
    private View matrimonyMeetFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyMeetFragmentPresenter matrimonyMeetFragmentPresenter;
    MatrimonyMeet meetData;

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

        Bundle arguments = getArguments();
        if (arguments != null) {
            meetData = (MatrimonyMeet) arguments.getSerializable(Constants.Home.MATRIMONY);
        }
        if(meetData.getIs_published()){
            ImageView ivIconPublished = view.findViewById(R.id.iv_icon_published);
            ivIconPublished.setVisibility(View.VISIBLE);
        }
        if(meetData.getMeetImageUrl() != null && !meetData.getMeetImageUrl().isEmpty()){
            ImageView ivMeetImage = view.findViewById(R.id.iv_meet_image);
            Glide.with(this)
                    .load(meetData.getMeetImageUrl())
                    .into(ivMeetImage);
        }
        ImageView btnPopupMenu = view.findViewById(R.id.btn_popmenu);
        btnPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu((getActivity()), v);
                popup.setOnMenuItemClickListener(MatrimonyMeetFragment.this);
                popup.inflate(R.menu.matrimony_meet_menu);
                if(meetData.getIs_published()){
                    popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                }
                if(meetData.getBadgeFanlize()){
                    popup.getMenu().findItem(R.id.action_allocate_badge).setVisible(false);
                    popup.getMenu().findItem(R.id.action_finalise_badge).setVisible(false);
                    popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                }
                popup.show();
            }
        });
    }

    private void init(){
        progressBarLayout = matrimonyMeetFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = matrimonyMeetFragmentView.findViewById(R.id.pb_profile_act);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (Util.isConnected(getContext())) {
            matrimonyMeetFragmentPresenter = new MatrimonyMeetFragmentPresenter(this);
        } else {

        }
    }

    public void showResponse(String responseStatus) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive:
                matrimonyMeetFragmentPresenter.meetArchiveDelete(meetData.getId(), "Archive");
                break;
            case R.id.action_delete:
                matrimonyMeetFragmentPresenter.meetArchiveDelete(meetData.getId(), "Deleted");
                break;
            case R.id.action_allocate_badge:
                matrimonyMeetFragmentPresenter.meetAllocateBadges(meetData.getId(),"allocateBadges");
                break;
            case R.id.action_finalise_badge:
                matrimonyMeetFragmentPresenter.meetAllocateBadges(meetData.getId(),"finalizeBadges");
                break;
            case R.id.action_gen_booklate:
//                ((CreateMatrimonyMeetActivity) getActivity()).openFragment("openGenerateBookletFragment");
                break;
        }
        return false;
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
}
