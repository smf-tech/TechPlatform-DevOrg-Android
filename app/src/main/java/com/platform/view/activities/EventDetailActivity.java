package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.models.events.Event;
import com.platform.models.events.Participant;
import com.platform.models.events.TaskForm;
import com.platform.utility.Constants;
import com.platform.view.adapters.AddMembersListAdapter;
import com.platform.view.adapters.TaskFormsListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Event event;

    private ImageView backButton;
    private ImageView editButton;
    private ImageView ivAttendeesList;
    private RecyclerView rvAttendeesList;
    private RecyclerView rvFormsList;
    private FrameLayout lyGreyedOut;
    private Button btEditAttendance;
    private boolean isMemberListVisible;
    private String toOpen;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initView();
    }

    private void initView() {
        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        event = (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        editButton.setVisibility(View.VISIBLE);
        lyGreyedOut = findViewById(R.id.ly_greyed_out);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvOwner = findViewById(R.id.tv_owner_name);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvTime = findViewById(R.id.tv_time);
        TextView tvRepeat = findViewById(R.id.tv_repeat);
        TextView tvAddress = findViewById(R.id.tv_address);
        ivAttendeesList = findViewById(R.id.iv_attendees_list);
        btEditAttendance = findViewById(R.id.bt_edit_attendance);

        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat weekDay = new SimpleDateFormat("EEEE", Locale.getDefault());

        String finalDate = weekDay.format(event.getEventStartDateTime());
        finalDate = finalDate + ", " + targetFormat.format(event.getEventStartDateTime());

        tvTitle.setText(event.getTitle());
        tvCategory.setText(event.getEventType());
        tvDescription.setText(event.getEventDescription());
        tvOwner.setText(event.getOrganizer());
        tvDate.setText(finalDate);
//        tvTime.setText(String.format("%s > %s", event.getStarTime(), event.getEndTime()));
        tvAddress.setText(event.getAddress());
//        rvAttendeesList = findViewById(R.id.rv_attendees_list);
//        int attendedCount = 0;
//        boolean isAttendanceMarked = false;
//        for (Member m : event.getMembersList()) {
//            if (m.getMemberAttended()) {
//                attendedCount++;
//                isAttendanceMarked = true;
//            }
//        }
//
//        if (isAttendanceMarked) {
//            tvAttended.setText(String.format(Locale.getDefault(), "%d Attended", attendedCount));
//            tvNotAttended.setText(String.format(Locale.getDefault(), "%d Not Attended",
//                    event.getMembersList().size() - attendedCount));
//        } else {
//            tvAttended.setText("0 Attended");
//            tvNotAttended.setText(String.format(Locale.getDefault(), "%d Not Attended",
//                    event.getMembersList().size()));
//        }

//        setAdapter(event.getMembersList());

        isMemberListVisible = true;

        mySnackBar();
        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
            setActionbar(getString(R.string.task_detail));
            View vTaskStatusIndicator = findViewById(R.id.task_status_indicator);
            TextView tvFormlistLabel = findViewById(R.id.tv_form_list_label);
            vTaskStatusIndicator.setVisibility(View.VISIBLE);
            findViewById(R.id.ly_attended).setVisibility(View.GONE);
            findViewById(R.id.ly_category).setVisibility(View.GONE);
            TextView tvEndDate = findViewById(R.id.tv_end_date);
            tvEndDate.setVisibility(View.VISIBLE);
            TextView tvEndTime = findViewById(R.id.tv_end_time);
            tvEndTime.setVisibility(View.VISIBLE);
            tvEndTime.setText(event.getEndTime());
            tvRepeat.setVisibility(View.GONE);

            Date endDate = null;
            try {
                endDate = originalFormat.parse(event.getEndDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String finalEndDate = weekDay.format(endDate);
            finalEndDate = finalEndDate + ", " + targetFormat.format(endDate);
            tvEndDate.setText(event.getEndDate());

            tvTime.setText(String.format(event.getStarTime()));

            if (event.getStatus().equals(Constants.Planner.PLANNED_STATUS)) {
                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.red));
            } else if (event.getStatus().equals(Constants.Planner.COMPLETED_STATUS)) {
                vTaskStatusIndicator.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.green));
            }
            if(event.getFormsList().size()>0){
                findViewById(R.id.ly_task_forms).setVisibility(View.VISIBLE);
                btEditAttendance.setVisibility(View.GONE);
                tvFormlistLabel.setVisibility(View.VISIBLE);
                tvFormlistLabel.setText(event.getFormsList().size()+getString(R.string.task_formlist_screen_msg));
                rvFormsList = findViewById(R.id.rv_forms_list);
                setFormListAdapter(event.getFormsList());
            }else{
                btEditAttendance.setText(getString(R.string.mark_completed));
                tvFormlistLabel.setVisibility(View.GONE);
            }
        } else {
            setActionbar(getString(R.string.event_detail));
            tvRepeat.setText(event.getRepeat());
            tvTime.setText(String.format("%s to %s", event.getStarTime(), event.getEndTime()));
            rvAttendeesList = findViewById(R.id.rv_attendees_list);
            setAdapter(event.getMembersList());
            getAttendedCount();
        }

        setListeners();
    }

    private void setAdapter(ArrayList<Participant> membersList) {
        AddMembersListAdapter addMembersListAdapter
                = new AddMembersListAdapter(EventDetailActivity.this, membersList, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendeesList.setLayoutManager(mLayoutManager);
        rvAttendeesList.setAdapter(addMembersListAdapter);
    }

    private void setFormListAdapter(ArrayList<TaskForm> taskFormsList) {
        TaskFormsListAdapter taskFormsListAdapter = new TaskFormsListAdapter(EventDetailActivity.this, taskFormsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvFormsList.setLayoutManager(mLayoutManager);
        rvFormsList.setAdapter(taskFormsListAdapter);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        ivAttendeesList.setOnClickListener(this);
        btEditAttendance.setOnClickListener(this);
        lyGreyedOut.setOnClickListener(this);
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    private void getAttendedCount(){
        TextView tvAttended = findViewById(R.id.tv_attended);
        TextView tvNotAttended = findViewById(R.id.tv_not_attended);

        int attendedCount = 0;
        boolean isAttendanceMarked = false;
        for (Participant m : event.getMembersList()) {
            if (m.getAttended()) {
                attendedCount++;
                isAttendanceMarked = true;
            }
        }
        if (isAttendanceMarked) {
            tvAttended.setText(attendedCount + getString(R.string.attended_label));
            tvNotAttended.setText(event.getMembersList().size() - attendedCount + getString(R.string.not_attended_label));
        } else {
            tvAttended.setText("0"+getString(R.string.attended_label));
            tvNotAttended.setText(event.getMembersList().size() + getString(R.string.not_attended_label));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.toolbar_edit_action:
                if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                    Intent intentCreateEvent = new Intent(this, CreateEventActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, toOpen);
                    intentCreateEvent.putExtra(Constants.Planner.EVENT_DETAIL, event);
                    this.startActivity(intentCreateEvent);
                }else{
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                        lyGreyedOut.setVisibility(View.GONE);
                    } else {
                        snackbar.show();
                        lyGreyedOut.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.iv_attendees_list:
                if (isMemberListVisible) {
                    isMemberListVisible = false;
                    rvAttendeesList.setVisibility(View.VISIBLE);
                    ivAttendeesList.setRotation(180);
                } else {
                    isMemberListVisible = true;
                    rvAttendeesList.setVisibility(View.GONE);
                    ivAttendeesList.setRotation(0);
                }
                break;

            case R.id.bt_edit_attendance:
                Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
                intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST, false);
                intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST, event.getMembersList());
                this.startActivity(intentAddMembersListActivity);
                break;

            case R.id.tv_edit_this_event:
                callCreateEvent("Edit This Event");
                break;

            case R.id.tv_edit_all_event:
                callCreateEvent("Edit All Events");
                break;

            case R.id.ly_greyed_out:
                lyGreyedOut.setVisibility(View.GONE);
                snackbar.dismiss();
                break;
        }
    }

    private void callCreateEvent(String edit) {
        Intent intentCreateEvent = new Intent(this, CreateEventActivity.class);
        intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, toOpen);
        intentCreateEvent.putExtra(Constants.Planner.EVENT_DETAIL, event);
        this.startActivity(intentCreateEvent);
        lyGreyedOut.setVisibility(View.GONE);
        snackbar.dismiss();
    }
    private void mySnackBar() {
        CoordinatorLayout containerLayout = findViewById(R.id.ly_coordinator);
        snackbar = Snackbar.make(containerLayout, "", Snackbar.LENGTH_INDEFINITE);

        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        View snackView = getLayoutInflater().inflate(R.layout.snackbar_cust, layout, false);
        TextView tvThisEvent = snackView.findViewById(R.id.tv_edit_this_event);
        tvThisEvent.setOnClickListener(this);

        TextView tvAllEvent = snackView.findViewById(R.id.tv_edit_all_event);
        tvAllEvent.setOnClickListener(this);
        layout.setPadding(0, 0, 0, 0);

        layout.addView(snackView, 0);
    }

    public String timeStampToDate(int timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
        return formatter.format(timeStamp);
    }
}
