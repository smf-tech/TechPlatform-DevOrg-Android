package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Event;
import com.platform.utility.Constants;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private ImageView editButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        initView();
    }

    private void initView() {
        Event event= (Event) getIntent().getSerializableExtra(Constants.Planner.EVENT_DETAIL);
        setActionbar(getString(R.string.event_detail));

        backButton = findViewById(R.id.toolbar_back_action);
        editButton = findViewById(R.id.toolbar_edit_action);
        editButton.setVisibility(View.VISIBLE);

        tvCategory = findViewById(R.id.tv_category);
        tvDescription= findViewById(R.id.tv_description);
        tvDate= findViewById(R.id.tv_date);
        tvTime= findViewById(R.id.tv_time);
        tvRepeat= findViewById(R.id.tv_repeat);
        tvAddress= findViewById(R.id.tv_address);
        tvAttended= findViewById(R.id.tv_attended);
        tvNotAttended= findViewById(R.id.tv_not_attended);
        ivAttendeesList= findViewById(R.id.iv_attendees_list);
        rvAttendeesList= findViewById(R.id.rv_attendees_list);
        btEditAttendance= findViewById(R.id.bt_edit_attendance);

        tvCategory.setText(event.getCategory());
        tvDescription.setText(event.getDescription());
        tvDate.setText(event.getStartDate());
        tvTime.setText(event.getStarTime()+" > "+event.getEndTime());
        tvRepeat.setText(event.getRepeat());
        tvAddress.setText(event.getAddress());

        setListeners();
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
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

                break;
        }
    }
}
