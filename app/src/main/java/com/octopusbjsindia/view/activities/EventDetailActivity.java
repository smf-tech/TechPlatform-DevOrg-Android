package com.octopusbjsindia.view.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.events.AddForm;
import com.octopusbjsindia.models.events.EventTask;
import com.octopusbjsindia.models.events.GetAttendanceCodeResponse;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.models.events.SetAttendanceCodeRequest;
import com.octopusbjsindia.presenter.EventDetailPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.TaskFormsListAdapter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener {

    private EventTask eventTask;
    private ImageView backButton;
    private ImageView editButton;
    private ImageView toolbarAction;
    private RecyclerView rvFormsList;
    private LinearLayout lyMembarlistCode;
    private FrameLayout lyGreyedOut;
    private Button btGetCode, btSetCode;
    private Button btParticipants;
    private Button btCompleteTask;
    private String toOpen;
    private Snackbar snackbar;
    private TextView tvMemberCount;
    private ImageView ivEventPic;

    EventDetailPresenter presenter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private boolean flagEdit;
    private boolean flagUpdateMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initView();
    }

    private void initView() {
        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        eventTask = (EventTask) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);
        flagEdit = false;
        flagUpdateMember = false;

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        presenter = new EventDetailPresenter(this);

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        toolbarAction = findViewById(R.id.toolbar_action);
        toolbarAction.setImageResource(R.drawable.ic_delete);
        lyGreyedOut = findViewById(R.id.ly_greyed_out);
        lyMembarlistCode = findViewById(R.id.ly_membarlist_code);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvOwner = findViewById(R.id.tv_owner_name);
        TextView tvStartDate = findViewById(R.id.tv_start_date);
        TextView tvEndDate = findViewById(R.id.tv_end_date);
        TextView tvAddress = findViewById(R.id.tv_address);
        ivEventPic = findViewById(R.id.event_pic);
        tvMemberCount = findViewById(R.id.tv_member_count);

        btGetCode = findViewById(R.id.bt_get_code);
        btSetCode = findViewById(R.id.bt_set_code);
        btParticipants = findViewById(R.id.bt_participants);
        btCompleteTask = findViewById(R.id.bt_complete_task);

        if(eventTask.getAddress().contains("https://")){
            tvAddress.setOnClickListener(this);
            tvAddress.setTextColor(getResources().getColor(R.color.color_db_tab_select));
        }
        tvTitle.setText(eventTask.getTitle());
        tvDescription.setText(eventTask.getDescription());
        tvAddress.setText(eventTask.getAddress());
        tvOwner.setText(eventTask.getOwnername());
        tvStartDate.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getStartdatetime(), Constants.FORM_DATE_FORMAT));
        tvEndDate.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getEnddatetime(), Constants.FORM_DATE_FORMAT));

        String strMemberDitels = "Added members " + eventTask.getParticipantsCount();
        if (toOpen.equalsIgnoreCase(Constants.Planner.EVENTS_LABEL)) {
            strMemberDitels = strMemberDitels + " | Attended by " + eventTask.getAttendedCompleted() + " members";
        }
        tvMemberCount.setText(strMemberDitels);

        if (eventTask.getThumbnailImage().equals("")) {
            ivEventPic.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(eventTask.getThumbnailImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivEventPic);
        }

        View vTaskStatusIndicator = findViewById(R.id.task_status_indicator);
        TextView tvFormListLabel = findViewById(R.id.tv_form_list_label);
        vTaskStatusIndicator.setVisibility(View.GONE);

        if (eventTask.getRequiredForms().size() > 0) {
            findViewById(R.id.ly_task_forms).setVisibility(View.VISIBLE);

            tvFormListLabel.setVisibility(View.VISIBLE);
            tvFormListLabel.setText(String.format(Locale.getDefault(), "%d %s",
                    eventTask.getRequiredForms().size(), getString(R.string.task_form_list_screen_msg)));
            rvFormsList = findViewById(R.id.rv_forms_list);
            setFormListAdapter(eventTask.getRequiredForms());
        }

        viewConfig();
        setListeners();
    }

    // configuring the ui as per the user ant date
    private void viewConfig() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.FORM_DATE, Locale.getDefault());
        DateFormat sdateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        Date starDate = null;
        Date endDate = null;
        Date currentDate = Calendar.getInstance().getTime();
        long startTimeStamp = eventTask.getSchedule().getStartdatetime();
        long endTimeStamp = eventTask.getSchedule().getEnddatetime();
        try {
            int length = (int) (Math.log10(startTimeStamp) + 1);
            if (length == 10) {
                startTimeStamp = startTimeStamp * 1000;
            }
            starDate = new Timestamp(startTimeStamp);
            length = (int) (Math.log10(endTimeStamp) + 1);
            if (length == 10) {
                endTimeStamp = endTimeStamp * 1000;
            }
            endDate = new Timestamp(endTimeStamp);

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        if (endDate.getYear() >= currentDate.getYear()) {
            if (endDate.getMonth() >= currentDate.getMonth()) {
                if (endDate.getDate() >= currentDate.getDate()) {
                    flagEdit = true;
                }
            }
        }

        if (eventTask.getOwnerid().equals(Util.getUserObjectFromPref().getId())) {
            //owner
//            if (flagEdit) {
            if(starDate.getTime() > currentDate.getTime()) {
                editButton.setVisibility(View.VISIBLE);
                toolbarAction.setVisibility(View.VISIBLE);
            } else {
                editButton.setVisibility(View.GONE);
                toolbarAction.setVisibility(View.GONE);
            }
            if (flagEdit) {
                flagUpdateMember = true;
            }
        } else {
            //viewer
            editButton.setVisibility(View.GONE);
            toolbarAction.setVisibility(View.GONE);
        }

        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
            setActionbar(getString(R.string.task_detail));
            ivEventPic.setVisibility(View.GONE);
            btCompleteTask.setVisibility(View.VISIBLE);
            if (eventTask.getEventStatus().equalsIgnoreCase("completed")){
                btCompleteTask.setVisibility(View.GONE);
            }

        } else {
            setActionbar(getString(R.string.event_detail));
            //handling attendance button
            if (eventTask.getOwnerid().equals(Util.getUserObjectFromPref().getId())) {
                lyMembarlistCode.setVisibility(View.VISIBLE);
                if (eventTask.isMarkAttendanceRequired()) {
                    if (flagEdit) {
                        if(starDate.getTime() < currentDate.getTime()) {
                            btGetCode.setVisibility(View.VISIBLE);
                        }
                    } else {
                        btGetCode.setVisibility(View.GONE);
                    }
                } else {
                    btGetCode.setVisibility(View.GONE);
                }
            } else {
                if (eventTask.isMarkAttendanceRequired()) {
//                    btSetCode.setVisibility(View.VISIBLE);
                    if (flagEdit) {
                        if(starDate.getTime() < currentDate.getTime()) {
                            btSetCode.setVisibility(View.VISIBLE);
                        }
                    } else {
                        btSetCode.setVisibility(View.GONE);
                    }
                } else {
                    btSetCode.setVisibility(View.GONE);
                }
            }
        }

    }

    private void setFormListAdapter(ArrayList<AddForm> taskFormsList) {
        TaskFormsListAdapter taskFormsListAdapter = new TaskFormsListAdapter(
                EventDetailActivity.this, taskFormsList,
                Locale.getDefault().getLanguage());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvFormsList.setLayoutManager(mLayoutManager);
        rvFormsList.setAdapter(taskFormsListAdapter);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        btGetCode.setOnClickListener(this);
        btSetCode.setOnClickListener(this);
        btParticipants.setOnClickListener(this);
        btCompleteTask.setOnClickListener(this);
        lyGreyedOut.setOnClickListener(this);
        ivEventPic.setOnClickListener(this);
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address:
                try {
                    Intent viewIntent = new Intent("android.intent.action.VIEW",
                                    Uri.parse(eventTask.getAddress()));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.toolbar_back_action:
                onBackPressed();
                break;
            case R.id.toolbar_edit_action:
                Intent intentCreateEvent = new Intent(this, CreateEventTaskActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, toOpen);
                intentCreateEvent.putExtra(Constants.Planner.EVENT_DETAIL, eventTask);
                this.startActivity(intentCreateEvent);
                finish();
                break;
            case R.id.toolbar_action:
                // delete()
                showDeleteAlert();
                break;
            case R.id.bt_participants:
                //see showMemberList()
                presenter.memberList(eventTask.getId());
                break;

            case R.id.bt_get_code:
                presenter.getAttendanceCode(eventTask.getId());
                break;

            case R.id.bt_set_code:
                setAttendanceCode();
                break;

            case R.id.bt_complete_task:
                presenter.setTaskMarkComplete(eventTask.getId());
                break;

            case R.id.ly_greyed_out:
                lyGreyedOut.setVisibility(View.GONE);
                snackbar.dismiss();
                break;
            case R.id.event_pic:
                showEnlargeImage(eventTask.getThumbnailImage());
                break;
        }
    }

    public void getAttendanceCode(GetAttendanceCodeResponse response) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_getattendancecode, null);

        TextView editText = dialogView.findViewById(R.id.tv_code);
        editText.setText(String.valueOf(response.getAttencdenceCode()));

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attendance Code")
                .setView(dialogView)
                .setMessage("Attendance code to mark attendance is:")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();

    }

    public void setAttendanceCode() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setattendance_code, null);

        EditText editText = dialogView.findViewById(R.id.et_code);

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attendance Code")
                .setView(dialogView)
                .setMessage("Please enter attendance code:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().equals("")) {
                            editText.setError("Please Enter code");
                        } else {
                            SetAttendanceCodeRequest request = new SetAttendanceCodeRequest();
                            request.setAttendanceCode(editText.getText().toString());
                            request.setEventId(eventTask.getId());
                            request.setUserId(Util.getUserObjectFromPref().getId());
                            presenter.setAttendanceCode(request);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        alertDialog = builder.create();
        alertDialog.show();

    }

    public void showDeleteAlert() {
        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete alert")
                .setMessage(getString(R.string.sure_to_delete))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        presenter.delete(eventTask.getId());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showMemberList(ArrayList<Participant> memberList) {

        Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
        intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);
        intentAddMembersListActivity.putExtra(Constants.Planner.TO_OPEN, toOpen);
        intentAddMembersListActivity.putExtra(Constants.Planner.IS_DELETE_VISIBLE, flagUpdateMember);
        intentAddMembersListActivity.putExtra(Constants.Planner.EVENT_TASK_ID, eventTask.getId());
        intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST, memberList);
        this.startActivityForResult(intentAddMembersListActivity,Constants.Planner.MEMBER_LIST);
//        this.startActivity(intentAddMembersListActivity);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Planner.MEMBER_LIST && data != null) {
            int memberCount = data.getIntExtra(Constants.Planner.MEMBER_LIST_COUNT,0);
            String strMemberDitels = "Added members " + memberCount;
            if (toOpen.equalsIgnoreCase(Constants.Planner.EVENTS_LABEL)) {
                strMemberDitels = strMemberDitels + " | Attended by " + eventTask.getAttendedCompleted() + " members";
            }
            tvMemberCount.setText(strMemberDitels);
        }
    }


    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showEnlargeImage(String thumbnailImage) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog_enlarge_image, null);
        final ImageView close_dialog = view.findViewById(R.id.iv_close);
//        TouchImageView img_post = view.findViewById(R.id.img_post);
        Glide.with(this)
                .load(thumbnailImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) view.findViewById(R.id.iv_image));

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(view.getContext());
        alertDialog.setView(view);
        android.app.AlertDialog alertD = alertDialog.create();

        if (alertD.getWindow() != null) {
            alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        close_dialog.setOnClickListener(v -> alertD.dismiss());
        alertD.show();

    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        runOnUiThread(() -> Util.showToast(data.toString(), this));
        finish();
    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
    }
}
