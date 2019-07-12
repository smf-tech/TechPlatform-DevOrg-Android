package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.Event;
import com.platform.models.events.Participant;
import com.platform.models.events.TaskForm;
import com.platform.presenter.EventDetailPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.AddMembersListAdapter;
import com.platform.view.adapters.TaskFormsListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventDetailActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener {

    private Event event;

    private ImageView backButton;
    private ImageView editButton;
    private RecyclerView rvAttendeesList;
    private RecyclerView rvFormsList;
    private FrameLayout lyGreyedOut;
    private Button btAttendance;
    private Button btParticipantsList;
    private boolean isMemberListVisible;
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
        event = (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        presenter = new EventDetailPresenter(this);

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        editButton.setVisibility(View.VISIBLE);
        lyGreyedOut = findViewById(R.id.ly_greyed_out);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvOwner = findViewById(R.id.tv_owner_name);
        TextView tvStartDate = findViewById(R.id.tv_start_date);
        TextView tvEndDate = findViewById(R.id.tv_start_date);
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvAddress = findViewById(R.id.tv_address);

        btAttendance = findViewById(R.id.bt_attendance);
        btParticipantsList = findViewById(R.id.bt_participants_list);

        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat weekDay = new SimpleDateFormat("EEEE", Locale.getDefault());

        tvTitle.setText(event.getTitle());
        tvDescription.setText(event.getDescription());
        tvTime.setText(String.format("%s > %s", event.getStartdatetime(), event.getEnddatetime()));
        tvAddress.setText(event.getAddress());
        rvAttendeesList = findViewById(R.id.rv_attendees_list);

        isMemberListVisible = true;

        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
            setActionbar(getString(R.string.task_detail));
            View vTaskStatusIndicator = findViewById(R.id.task_status_indicator);
            TextView tvFormListLabel = findViewById(R.id.tv_form_list_label);
            vTaskStatusIndicator.setVisibility(View.VISIBLE);
            tvEndDate.setVisibility(View.VISIBLE);
            TextView tvEndTime = findViewById(R.id.tv_end_time);
            tvEndTime.setVisibility(View.VISIBLE);
            tvEndTime.setText(event.getEnddatetime());

            tvEndDate.setText(event.getEnddatetime());

            tvTime.setText(String.format(event.getStartdatetime()));

//            if (event.getStatus().equalsIgnoreCase(Constants.Planner.PLANNED_STATUS)) {
//                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.red));
//            } else if (event.getStatus().equalsIgnoreCase(Constants.Planner.COMPLETED_STATUS)) {
//                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.green));
//            }

//            if (event.getFormsList().size() > 0) {
//                findViewById(R.id.ly_task_forms).setVisibility(View.VISIBLE);
//                btEditAttendance.setVisibility(View.GONE);
//                tvFormListLabel.setVisibility(View.VISIBLE);
//                tvFormListLabel.setText(String.format(Locale.getDefault(), "%d%s",
//                        event.getFormsList().size(), getString(R.string.task_form_list_screen_msg)));
//                rvFormsList = findViewById(R.id.rv_forms_list);
//                setFormListAdapter(event.getFormsList());
//            } else {
//                btEditAttendance.setText(getString(R.string.mark_completed));
//                tvFormListLabel.setVisibility(View.GONE);
//            }
        }
        setListeners();
    }

    private void setFormListAdapter(ArrayList<TaskForm> taskFormsList) {
        TaskFormsListAdapter taskFormsListAdapter = new TaskFormsListAdapter(
                EventDetailActivity.this, taskFormsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvFormsList.setLayoutManager(mLayoutManager);
        rvFormsList.setAdapter(taskFormsListAdapter);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        btAttendance.setOnClickListener(this);
        btParticipantsList.setOnClickListener(this);
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
                Intent intentCreateEvent = new Intent(this, CreateEventActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, toOpen);
                intentCreateEvent.putExtra(Constants.Planner.EVENT_DETAIL, event);
                this.startActivity(intentCreateEvent);
                break;

            case R.id.bt_participants_list:
                if (toOpen.equalsIgnoreCase(Constants.Planner.EVENTS_LABEL)) {
                    Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
                    intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);
//                    intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST, event.getMembersList());
                    this.startActivity(intentAddMembersListActivity);
                } else if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
//                    Toast.makeText(this, "Status marked as Completed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_attendance:
//                if (Util.isConnected(this))
                    presenter.getAttendanceCode();
//                else
//                    Toast.makeText(this, getString(R.string.interval), Toast.LENGTH_SHORT).show();
                break;

            case R.id.ly_greyed_out:
                lyGreyedOut.setVisibility(View.GONE);
                snackbar.dismiss();
                break;
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
