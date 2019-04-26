package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Event;
import com.platform.models.events.Member;
import com.platform.utility.Constants;
import com.platform.view.adapters.AddMembersListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private AddMembersListAdapter addMembersListAdapter;
    Event event;

    private ImageView backButton;
    private ImageView editButton;
    private TextView tvTitle;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvRepeat;
    private TextView tvAddress;
    private TextView tvAttended;
    private TextView tvNotAttended;
    private ImageView ivAttendeesList;
    private RecyclerView rvAttendeesList;
    private Button btEditAttendance;

    private boolean isMemberListVisible;
    String toOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initView();
    }

    private void initView() {
        toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        event = (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);
        if(toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)){
            setActionbar(getString(R.string.task_detail));
        }else{
            setActionbar(getString(R.string.event_detail));
        }

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        editButton.setVisibility(View.VISIBLE);

        tvTitle = findViewById(R.id.tv_title);
        tvCategory = findViewById(R.id.tv_category);
        tvDescription = findViewById(R.id.tv_description);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvRepeat = findViewById(R.id.tv_repeat);
        tvAddress = findViewById(R.id.tv_address);
        tvAttended = findViewById(R.id.tv_attended);
        tvNotAttended = findViewById(R.id.tv_not_attended);
        ivAttendeesList = findViewById(R.id.iv_attendees_list);
        rvAttendeesList = findViewById(R.id.rv_attendees_list);
        btEditAttendance = findViewById(R.id.bt_edit_attendance);

        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat weekDay = new SimpleDateFormat("EEEE");
        Date date = null;
        try {
            date = originalFormat.parse(event.getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate = weekDay.format(date);
        finalDate = finalDate +", "+ targetFormat.format(date);

        tvTitle.setText(event.getTital());
        tvCategory.setText(event.getCategory());
        tvDescription.setText(event.getDescription());
        tvDate.setText(finalDate);
        tvTime.setText(event.getStarTime()+" > "+event.getEndTime());
        tvRepeat.setText(event.getRepeat());
        tvAddress.setText(event.getAddress());
        tvAttended.setText("03 Attended");
        tvNotAttended.setText("05 Not Attended");
        setAdapter(event.getMembersList());

        isMemberListVisible=true;

        setListeners();
    }

    private void setAdapter(ArrayList<Member> membersList) {
        addMembersListAdapter = new AddMembersListAdapter(EventDetailActivity.this, membersList,false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendeesList.setLayoutManager(mLayoutManager);
        rvAttendeesList.setAdapter(addMembersListAdapter);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        ivAttendeesList.setOnClickListener(this);
        btEditAttendance.setOnClickListener(this);
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
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN,toOpen);
                intentCreateEvent.putExtra(Constants.Planner.EVENT_DETAIL,event);
                this.startActivity(intentCreateEvent);

                break;
            case R.id.iv_attendees_list:
                if(isMemberListVisible){
                    isMemberListVisible=false;
                    rvAttendeesList.setVisibility(View.VISIBLE);
                } else {
                    isMemberListVisible=true;
                    rvAttendeesList.setVisibility(View.GONE);
                }
                break;
            case R.id.bt_edit_attendance:
                Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
                intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST,false);
                intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST,event.getMembersList());
                this.startActivity(intentAddMembersListActivity);
                break;
        }
    }
}
