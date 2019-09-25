package com.platform.view.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.models.Matrimony.MeetBatchesResponseModel;
import com.platform.presenter.MatrimonyMeetFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.MatrimonyBookletActivity;
import com.platform.view.activities.CreateMatrimonyMeetActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.ShowMeetBatchesActivity;

import java.util.Objects;

public class MatrimonyMeetFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, APIDataListener {
    private View matrimonyMeetFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyMeetFragmentPresenter matrimonyMeetFragmentPresenter;
    MatrimonyMeet meetData;
    PopupMenu popup;
    private MatrimonyFragment matrimonyFragment;

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
        TextView ivIconPublished = view.findViewById(R.id.iv_icon_published);
        if(meetData.getIs_published()){
            ivIconPublished.setText("Published");//(getResources().getDrawable(R.drawable.ic_icon_publish));
        } else {
            ivIconPublished.setText(" Saved");//ivIconPublished.setImageDrawable(getResources().getDrawable(R.drawable.ic_meet_saved_label));
        }
        if (meetData.getArchive()){
            ivIconPublished.setText("Archived");
        }
        if(meetData.getMeetImageUrl() != null && !meetData.getMeetImageUrl().isEmpty()){
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
                popup.setOnMenuItemClickListener(MatrimonyMeetFragment.this);
                popup.inflate(R.menu.matrimony_meet_menu);
                if(meetData.getIs_published()){
                    popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                }
                //if(meetData.getBadgeFanlize()){
                if(meetData.getBadgeFanlize() != null && meetData.getBadgeFanlize()) {
                    popup.getMenu().findItem(R.id.action_allocate_badge).setVisible(false);
                    popup.getMenu().findItem(R.id.action_finalise_badge).setVisible(false);
                    popup.getMenu().findItem(R.id.action_delete).setVisible(false);
                }
                if(meetData.getArchive()){
                    popup.getMenu().findItem(R.id.action_archive).setVisible(false);
                }
                //else if (meetData.getSchedule().getDateTime())

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

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MatrimonyMeetFragmentPresenter.MEET_FINALIZE_BADGES)){
            if(status == 200){
                popup.getMenu().findItem(R.id.action_finalise_badge).setVisible(false);
                meetData.setBadgeFanlize(true);
                MatrimonyFragment.getInstance().updateBadgeStatus(true);
            }
        }
        if(requestId.equals(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_ARCHIVE)){
            if(status == 200){
                popup.getMenu().findItem(R.id.action_archive).setVisible(false);
                meetData.setArchive(true);
            }
        }
        if(requestId.equals(MatrimonyMeetFragmentPresenter.MATRIMONY_MEET_DELETE)){
            if(status == 200){
                //matrimonyMeetList.get(currentPosition).setIs_published(true);
                MatrimonyFragment.getInstance().updateMeetList();
            }
        }
        if(requestId.equals(MatrimonyMeetFragmentPresenter.MEET_ALLOCATE_BADGES)){
            if(status == 200){
                //matrimonyMeetList.get(currentPosition).setIs_published(true);
                MatrimonyFragment.getInstance().updateBadgeStatus(false);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive:
                showDialog("Archive Meet", "Are you sure you want to archive this meet?","YES", "NO");
                break;
            case R.id.action_delete:
                showDialog("Delete Meet", "Are you sure you want to delete this meet?","YES", "NO");
                break;
            case R.id.action_allocate_badge:
                matrimonyMeetFragmentPresenter.meetAllocateBadges(meetData.getId(),"allocateBadges");
                break;
            case R.id.action_finalise_badge:
                matrimonyMeetFragmentPresenter.meetAllocateBadges(meetData.getId(),"finalizeBadges");
                break;
            case R.id.action_gen_booklet:
                Intent bookletIntent = new Intent(getActivity(), MatrimonyBookletActivity.class);
                bookletIntent.putExtra("meetId",meetData.getId());
                getActivity().startActivity(bookletIntent);
                break;
            case R.id.action_show_baches:
                matrimonyMeetFragmentPresenter.showMeetBaches(meetData.getId(),"showbaches");
                break;
        }
        return false;
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
                if(dialogTitle.equals("Archive Meet")){
                    matrimonyMeetFragmentPresenter.meetArchiveDelete(meetData.getId(), "Archive");
                } else if(dialogTitle.equals("Delete Meet")){
                    matrimonyMeetFragmentPresenter.meetArchiveDelete(meetData.getId(), "Deleted");
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

    public void showBachesResponse(String response) {
        Util.logger("Batches response-",response);

        Gson gson = new Gson();

        MeetBatchesResponseModel meetBatchesResponseModel = gson.fromJson(response, MeetBatchesResponseModel.class);
        if (meetBatchesResponseModel.getStatus().equalsIgnoreCase("200")){
        Intent intent =new Intent(getActivity(), ShowMeetBatchesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("batches_resposne",response);
        intent.putExtras(bundle);
        startActivity(intent);
        }else {
            Util.showToast("No Batches available yet",getActivity());
        }

    }
}
