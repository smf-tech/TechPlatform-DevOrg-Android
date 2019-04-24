package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.platform.R;
import com.platform.models.events.Event;
import com.platform.models.events.Member;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private AddMembersListAdapter addMembersListAdapter;
    ArrayList<Member> membersList =new ArrayList<Member>();
    Event event;

    private ImageView ivBackIcon;
    private Spinner spCategory;
    private EditText etTitle;
    private EditText etStartDate;
    private EditText etStartTime;
    private EditText etEndTime;
    private EditText etRepeat;
    private EditText etDescription;
    private EditText etAddress;
    private EditText etAddMembers;
    private TextView tvRepeatDetail;
    private Button btRepeat;
    private Button btEventSubmit;
    private RecyclerView rvAttendeesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();
    }

    private void initView() {
        setActionbar(getString(R.string.create_event));
        String toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);
        event = (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);

        ivBackIcon = findViewById(R.id.toolbar_back_action);
        spCategory = findViewById(R.id.sp_category);
        etTitle = findViewById(R.id.et_title);
        etStartDate = findViewById(R.id.et_start_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        etRepeat = findViewById(R.id.et_repeat);
        etDescription = findViewById(R.id.et_description);
        etAddress = findViewById(R.id.et_address);
        etAddMembers = findViewById(R.id.et_add_members);
        tvRepeatDetail = findViewById(R.id.tv_repeat_detail);
        btRepeat = findViewById(R.id.bt_repeat);
        btEventSubmit = findViewById(R.id.bt_event_submit);
        rvAttendeesList = findViewById(R.id.rv_attendees_list);

        btRepeat.setText("Never");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                 R.array.category_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        addMembersListAdapter = new AddMembersListAdapter(CreateEventActivity.this, membersList,false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendeesList.setLayoutManager(mLayoutManager);
        rvAttendeesList.setAdapter(addMembersListAdapter);

        if(event!=null){
            setAllData();
        }

        setListeners();

    }

    private void setAllData() {
        ArrayAdapter myAdap = (ArrayAdapter) spCategory.getAdapter();
        int spinnerPosition = myAdap.getPosition(event.getCategory());
        spCategory.setSelection(spinnerPosition);
        etTitle.setText(event.getTital());
        etStartDate.setText(event.getStartDate());
        etStartTime.setText(event.getStarTime());
        etEndTime.setText(event.getEndTime());
        etRepeat.setText(event.getRepeat());
        etDescription.setText(event.getDescription());
        etAddress.setText(event.getAddress());
        setAdapter(event.getMembersList());
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        etAddMembers.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        btEventSubmit.setOnClickListener(this);
    }

    private void setAdapter(ArrayList<Member> members) {
        membersList.addAll(members);
        addMembersListAdapter.notifyDataSetChanged();
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_start_date:
                Util.showDateDialog(CreateEventActivity.this, findViewById(R.id.et_start_date));
                break;
            case R.id.et_start_time:
                Util.showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_start_time));
                break;
            case R.id.et_end_time:
                Util.showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_end_time));
                break;
            case R.id.et_add_members:
                Intent intentAddMemberFilerActivity = new Intent(this, AddMemberFilerActivity.class);
                this.startActivity(intentAddMemberFilerActivity);
                break;
            case R.id.bt_repeat:
                Intent intentRepeatEventActivity = new Intent(this, RepeatEventActivity.class);
                this.startActivityForResult(intentRepeatEventActivity,001);
                break;
            case R.id.bt_event_submit:

                break;
        }
    }

}
