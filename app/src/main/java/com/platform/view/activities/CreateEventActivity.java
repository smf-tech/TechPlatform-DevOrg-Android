package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.Event;
import com.platform.models.events.Member;
import com.platform.presenter.CreateEventActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.AddMembersListAdapter;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, PlatformTaskListener {

    private AddMembersListAdapter addMembersListAdapter;
    private ArrayList<Member> membersList = new ArrayList<>();
    private Event event;

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
    private Button btRepeat;
    private Button btEventSubmit;

    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();
    }

    private void initView() {
        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);

        CreateEventActivityPresenter createEventPresenter = new CreateEventActivityPresenter(this);
        createEventPresenter.getEventCategory();

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

        btRepeat = findViewById(R.id.bt_repeat);
        btEventSubmit = findViewById(R.id.bt_event_submit);
        RecyclerView rvAttendeesList = findViewById(R.id.rv_attendees_list);

        btRepeat.setText("Never");

        // Task Module UI changes
        if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
            TextInputLayout tlyEndDate = findViewById(R.id.tly_end_date);
            tlyEndDate.setVisibility(View.VISIBLE);
            LinearLayout lyRepeat = findViewById(R.id.ly_repeat);
            lyRepeat.setVisibility(View.GONE);
            TextInputLayout tlyAddress = findViewById(R.id.tly_address);
            tlyAddress.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        addMembersListAdapter = new AddMembersListAdapter(CreateEventActivity.this, membersList, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendeesList.setLayoutManager(mLayoutManager);
        rvAttendeesList.setAdapter(addMembersListAdapter);

        if (event != null) {
            if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                setActionbar(getString(R.string.edit_task));
            } else {
                setActionbar(getString(R.string.edit_event));
            }

            btEventSubmit.setText(getString(R.string.btn_submit));

            setAllData();

            ImageView toolbarAction = findViewById(R.id.toolbar_edit_action);
            toolbarAction.setVisibility(View.VISIBLE);
            toolbarAction.setImageResource(R.drawable.ic_down_arrow_light_blue);
        } else {
            if (toOpen.equalsIgnoreCase(Constants.Planner.TASKS_LABEL)) {
                setActionbar(getString(R.string.create_task));
                btEventSubmit.setText(getString(R.string.create_task));
            } else {
                setActionbar(getString(R.string.create_event));
            }
        }

        setListeners();
    }

    private void setAllData() {
        ArrayAdapter myAdapter = (ArrayAdapter) spCategory.getAdapter();
        int spinnerPosition = myAdapter.getPosition(event.getCategory());
        spCategory.setSelection(spinnerPosition);
        etTitle.setText(event.getTitle());
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
                Intent intentAddMemberFilerActivity = new Intent(this, AddMembersFilterActivity.class);
                this.startActivity(intentAddMemberFilerActivity);
                break;

            case R.id.bt_repeat:
                Intent intentRepeatEventActivity = new Intent(this, RepeatEventActivity.class);
                this.startActivityForResult(intentRepeatEventActivity, 001);
                break;

            case R.id.bt_event_submit:
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
