package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();

    }

    private void initView() {

        String toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);

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

        btRepeat.setText("Never");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                 R.array.category_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        setListeners();

    }

    private void setListeners() {
        etStartDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        etAddMembers.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        btEventSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                break;
            case R.id.bt_repeat:

                break;
            case R.id.bt_event_submit:

                break;
        }
    }

}
