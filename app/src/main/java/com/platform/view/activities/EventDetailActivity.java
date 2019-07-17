package com.platform.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.AddForm;
import com.platform.models.events.EventTask;
import com.platform.models.events.GetAttendanceCodeResponse;
import com.platform.models.events.Participant;
import com.platform.models.events.SetAttendanceCodeRequest;
import com.platform.presenter.EventDetailPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.TaskFormsListAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class EventDetailActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener {

    private EventTask eventTask;

    private ImageView backButton;
    private ImageView editButton;
    private RecyclerView rvFormsList;
    private LinearLayout lyMembarlistCode;
    private FrameLayout lyGreyedOut;
    private Button btGetCode, btSetCode;
    private Button btParticipants;
    private Button btCompleteTask;
    private String toOpen;
    private Snackbar snackbar;

    EventDetailPresenter presenter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initView();
    }

    private void initView() {
        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        eventTask = (EventTask) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        presenter = new EventDetailPresenter(this);

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        lyGreyedOut = findViewById(R.id.ly_greyed_out);
        lyMembarlistCode = findViewById(R.id.ly_membarlist_code);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvOwner = findViewById(R.id.tv_owner_name);
        TextView tvStartDate = findViewById(R.id.tv_start_date);
        TextView tvEndDate = findViewById(R.id.tv_end_date);
//        TextView tvStartTime = findViewById(R.id.tv_start_time);
//        TextView tvEndTime = findViewById(R.id.tv_end_time);
        TextView tvAddress = findViewById(R.id.tv_address);
        ImageView ivEventPic = findViewById(R.id.event_pic);
        TextView tvMemberCount = findViewById(R.id.tv_member_count);

        btGetCode = findViewById(R.id.bt_get_code);
        btSetCode = findViewById(R.id.bt_set_code);
        btParticipants = findViewById(R.id.bt_participants);
        btCompleteTask = findViewById(R.id.bt_complete_task);

        tvTitle.setText(eventTask.getTitle());
        tvDescription.setText(eventTask.getDescription());
//        tvStartTime.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getStartdatetime(),Constants.FORM_DATE_FORMAT));
//        tvEndTime.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getEnddatetime(),Constants.FORM_DATE_FORMAT));
        tvAddress.setText(eventTask.getAddress());
        tvOwner.setText(eventTask.getOwnername());
        tvStartDate.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getStartdatetime(),Constants.FORM_DATE_FORMAT));
        tvEndDate.setText(Util.getDateFromTimestamp(eventTask.getSchedule().getEnddatetime(),Constants.FORM_DATE_FORMAT));
        tvMemberCount.setText(eventTask.getParticipantsCount()+" member added");
//        eventTask.getAttendedCompleted();

        if(eventTask.getThumbnailImage().equals("")){
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

//            if (eventTask.getStatus().equalsIgnoreCase(Constants.Planner.PLANNED_STATUS)) {
//                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.red));
//            } else if (eventTask.getStatus().equalsIgnoreCase(Constants.Planner.COMPLETED_STATUS)) {
//                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.green));
//            }

        if (eventTask.getOwnerid().equals(Util.getUserObjectFromPref().getId())) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
        }

        if (eventTask.getRequiredForms().size() > 0) {
            findViewById(R.id.ly_task_forms).setVisibility(View.VISIBLE);

            tvFormListLabel.setVisibility(View.VISIBLE);
            tvFormListLabel.setText(String.format(Locale.getDefault(), "%d%s",
                    eventTask.getRequiredForms().size(), getString(R.string.task_form_list_screen_msg)));
            rvFormsList = findViewById(R.id.rv_forms_list);
            setFormListAdapter(eventTask.getRequiredForms());
        }
        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
            setActionbar(getString(R.string.task_detail));
            ivEventPic.setVisibility(View.GONE);
            tvMemberCount.setVisibility(View.GONE);
            btCompleteTask.setVisibility(View.VISIBLE);
        } else {
            setActionbar(getString(R.string.event_detail));
            //handling attendance button
            if (eventTask.getOwnerid().equals(Util.getUserObjectFromPref().getId())) {
                lyMembarlistCode.setVisibility(View.VISIBLE);
            } else {
                btSetCode.setVisibility(View.VISIBLE);
            }
        }

        setListeners();
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
        btGetCode.setOnClickListener(this);
        btSetCode.setOnClickListener(this);
        btParticipants.setOnClickListener(this);
        btCompleteTask.setOnClickListener(this);
        lyGreyedOut.setOnClickListener(this);
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    public void getAttendanceCode(GetAttendanceCodeResponse response) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_getattendancecode, null);
        dialogBuilder.setView(dialogView);

        TextView editText = (TextView) dialogView.findViewById(R.id.tv_code);
        editText.setText(String.valueOf(response.getAttencdenceCode()));
//        editText.setText("123456");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Attendance Code");
        alertDialog.setMessage("Attendance code to mark attendance is:");
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void setAttendanceCode() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setattendance_code, null);
        dialogBuilder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.et_code);
//        editText.setText("123456");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Attendance Code");
        alertDialog.setMessage("Please enter attendance code:");
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().equals("")) {
                    editText.setError("Please Enter code");
                } else {
                    SetAttendanceCodeRequest request = new SetAttendanceCodeRequest();
                    request.setAttendanceCode(editText.getText().toString());
                    request.setEventId(eventTask.getId());
                    request.setUserId(Util.getUserObjectFromPref().getId());
                    alertDialog.dismiss();
                    presenter.setAttendanceCode(request);
                }

            }
        });
        alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showMemberList(ArrayList<Participant> memberList) {
        Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
        intentAddMembersListActivity.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);
        intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST, memberList);
        this.startActivity(intentAddMembersListActivity);
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

    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
    }
}
