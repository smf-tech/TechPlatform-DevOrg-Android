package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.events.Event;
import com.platform.models.events.Participant;
import com.platform.models.events.TaskForm;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.view.activities.CreateEventActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.adapters.EventListAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TasksPlannerFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener, RadioGroup.OnCheckedChangeListener {

    private ImageView ivBackIcon;
    private ImageView ivEventsSyncIcon;
    private ImageView ivCalendarMode;

    private View tasksPlannerView;
    private TextView tvAllEventsDetail;
    private RadioGroup radioGroup;
    private FloatingActionButton btAddEvents;
    private MaterialCalendarView calendarView;

    // flag to check th calender mode
    private boolean isMonth;
    private boolean isDashboard;

    private EventListAdapter taskListAdapter;
    private ArrayList<Event> taskList;
    private ArrayList<Event> sortedTaskList;

    public TasksPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasksPlannerView = inflater.inflate(R.layout.fragment_events_planner, container, false);
        return tasksPlannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        taskList = new ArrayList<>();
        sortedTaskList = new ArrayList<>();

        ArrayList<Participant> membersList = new ArrayList<>();
        membersList.add(new Participant("1", "Sagar Mahajan", "DM", true, true));
        membersList.add(new Participant("2", "Kishor Shevkar", "TC", false, false));
        membersList.add(new Participant("3", "Jagruti Devare", "MT", true, true));
        membersList.add(new Participant("4", "Sachin Kakade", "FA", false, false));

        ArrayList<TaskForm> taskFormsList = new ArrayList<>();
        taskFormsList.add(new TaskForm("1", "Testing Form 1", Constants.Planner.PLANNED_STATUS));
        taskFormsList.add(new TaskForm("2", "Testing Form 2", Constants.Planner.COMPLETED_STATUS));
        taskFormsList.add(new TaskForm("3", "Testing Form 3", Constants.Planner.COMPLETED_STATUS));

        taskList.add(new Event("1", "meeting", "Title1", 1556794660l, "03/01/2019", "10:00 am",
                "11:00 am", "-", "test", "Wagholi,Pune.", "Sachin",
                "1234", Constants.Planner.COMPLETED_STATUS, membersList, taskFormsList));

        taskList.add(new Event("2", "visit", "Title2", 1556794660l, "03/02/2019", "11:00 am",
                "12:00 pm", "-", "test", "Hadpsar,Pune.", "Sagar",
                "1235", Constants.Planner.PLANNED_STATUS, membersList, null));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }
        setActionbar(getString(R.string.tasks));
        isMonth = false;

        ivBackIcon = tasksPlannerView.findViewById(R.id.iv_back_icon);
        ivEventsSyncIcon = tasksPlannerView.findViewById(R.id.iv_events_sync_icon);
        AppBarLayout appBarLayout = tasksPlannerView.findViewById(R.id.app_bar_layout);
        RelativeLayout lyCalender = tasksPlannerView.findViewById(R.id.ly_calender);
        ivCalendarMode = tasksPlannerView.findViewById(R.id.iv_calendar_mode);
        tvAllEventsDetail = tasksPlannerView.findViewById(R.id.tv_all_events_list);
        btAddEvents = tasksPlannerView.findViewById(R.id.bt_add_events);
        RecyclerView rvTasks = tasksPlannerView.findViewById(R.id.rv_events);
        calendarView = tasksPlannerView.findViewById(R.id.calendarView);
        radioGroup = tasksPlannerView.findViewById(R.id.radio_group_filter);
        RadioButton allTasksButton = tasksPlannerView.findViewById(R.id.rb_all_events);
        RadioButton myTasksButton = tasksPlannerView.findViewById(R.id.rb_my_events);

        allTasksButton.setText(R.string.all_tasks);
        myTasksButton.setText(R.string.my_tasks);

        tvAllEventsDetail.setText(R.string.all_tasks);

        taskListAdapter = new EventListAdapter(getActivity(), sortedTaskList, Constants.Planner.TASKS_LABEL);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTasks.setLayoutManager(mLayoutManager);
        rvTasks.setAdapter(taskListAdapter);

        sortEventsList(true);
        setCalendar();
        setListeners();

        if (isDashboard) {
            appBarLayout.setVisibility(View.GONE);
            lyCalender.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            tvAllEventsDetail.setVisibility(View.VISIBLE);
        } else {
            appBarLayout.setVisibility(View.VISIBLE);
            lyCalender.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            tvAllEventsDetail.setVisibility(View.GONE);
        }

        calendarView.setOnMonthChangedListener((widget, date) ->
                Toast.makeText(getActivity(), "Month Changed:" + date, Toast.LENGTH_SHORT).show());
    }

    private void setActionbar(String title) {
        TextView toolbar_title = tasksPlannerView.findViewById(R.id.events_toolbar_title);
        toolbar_title.setText(title);
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        ivEventsSyncIcon.setOnClickListener(this);
        btAddEvents.setOnClickListener(this);
        tvAllEventsDetail.setOnClickListener(this);
        ivCalendarMode.setOnClickListener(this);
        calendarView.setOnDateChangedListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_icon:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;

            case R.id.iv_events_sync_icon:
                break;

            case R.id.iv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;

            case R.id.bt_add_events:
                Intent intentCreateEvent = new Intent(getActivity(), CreateEventActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                this.startActivity(intentCreateEvent);
                break;

            case R.id.tv_all_events_list:
                Intent intentTaskList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentTaskList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                this.startActivity(intentTaskList);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all_events:
                sortEventsList(true);
                break;

            case R.id.rb_my_events:
                sortEventsList(false);
                break;
        }
    }

    private void sortEventsList(boolean isAllEvents) {
        sortedTaskList.clear();
        if (isAllEvents) {
            sortedTaskList.addAll(taskList);
        } else {
            String ownerID = "1234";
            for (Event event : taskList) {
                if (ownerID.equals(event.getOwnerID())) {
                    sortedTaskList.add(event);
                }
            }
        }
        taskListAdapter.notifyDataSetChanged();
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);

        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            ivCalendarMode.setRotation(180);
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
            ivCalendarMode.setRotation(0);
        }

        calendarView.setSelectedDate(instance.getTime());
        calendarView.setCurrentDate(instance.getTime());
        highlightDates();
    }

    @SuppressWarnings("deprecation")
    private void highlightDates() {
        // set the date list to highlight
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        cal.add(Calendar.DATE, 2);

        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.add(Calendar.DATE, 3);

        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }
}
