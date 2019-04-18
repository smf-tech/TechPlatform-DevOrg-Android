package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.platform.utility.Util;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();

    }

    private void initView() {

        ivBackIcon = findViewById(R.id.iv_back_icon);
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
        ivBackIcon.setOnClickListener(this);
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
            case R.id.iv_back_icon:
                finish();
                break;
            case R.id.et_start_date:
                showDateDialog(CreateEventActivity.this, findViewById(R.id.et_start_date));
                break;
            case R.id.et_start_time:
                showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_start_time));
                break;
            case R.id.et_end_time:
                showTimeDialog(CreateEventActivity.this, findViewById(R.id.et_end_time));
                break;
            case R.id.et_add_members:
                Intent intentAddMemberFilerActivity = new Intent(this, AddMemberFilerActivity.class);
                this.startActivity(intentAddMemberFilerActivity);
                break;
            case R.id.bt_repeat:

                break;
            case R.id.bt_event_submit:

                break;
        }
    }

    private void showTimeDialog(Context context, final EditText editText) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }
}
