package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.EventTask;
import com.platform.models.events.EventParams;
import com.platform.models.events.EventsResponse;
import com.platform.presenter.EventsTaskLandingFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventTaskActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.adapters.EventTaskListAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsTaskLandingFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener, OnMonthChangedListener, RadioGroup.OnCheckedChangeListener, PlatformTaskListener {

    private ImageView ivBackIcon;
    private ImageView ivEventsSyncIcon;
    private ImageView ivCalendarMode;
    private TextView tvAllEventsDetail;
    private TextView tvToolbarTitle;
    private View eventsPlannerView;
    private RadioGroup radioGroup;
    private FloatingActionButton btAddEvents;
    private MaterialCalendarView calendarView;

    private EventTaskListAdapter eventTaskListAdapter;
    private ArrayList<EventTask> eventsList;
    private ArrayList<EventTask> sortedEventsList;

    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private EventsTaskLandingFragmentPresenter presenter;

    private String toOpen;
    private boolean isMonth;
    private boolean isAllEvents;
    private EventParams eventParams;
    private int selectedMonth;

    public EventsTaskLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventsPlannerView = inflater.inflate(R.layout.fragment_events_task_landing, container, false);
        return eventsPlannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            toOpen = bundle.getString(Constants.Planner.TO_OPEN);
        }
        progressBarLayout = eventsPlannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = eventsPlannerView.findViewById(R.id.pb_profile_act);

        isAllEvents = true;

        Date d = new Date();
        eventParams = new EventParams();
        eventParams.setType(toOpen);
        eventParams.setDay(DateFormat.format("dd", d.getTime()).toString());
        eventParams.setMonth(DateFormat.format("MM", d.getTime()).toString());
        eventParams.setYear(DateFormat.format("yyyy", d.getTime()).toString());
        eventParams.setUserId(Util.getUserObjectFromPref().getId());
        selectedMonth = Integer.parseInt(eventParams.getMonth());

        presenter = new EventsTaskLandingFragmentPresenter(this);
        presenter.getEventsOfMonth(eventParams);


        eventsList = new ArrayList<>();
        sortedEventsList = new ArrayList<>();

        isMonth = false;

        ivBackIcon = eventsPlannerView.findViewById(R.id.iv_back_icon);
        ivEventsSyncIcon = eventsPlannerView.findViewById(R.id.iv_events_sync_icon);
        ivCalendarMode = eventsPlannerView.findViewById(R.id.iv_calendar_mode);
        tvAllEventsDetail = eventsPlannerView.findViewById(R.id.tv_all_events_list);
        tvToolbarTitle = eventsPlannerView.findViewById(R.id.tv_toolbar_title);

        btAddEvents = eventsPlannerView.findViewById(R.id.bt_add_events);
        RecyclerView rvEvents = eventsPlannerView.findViewById(R.id.rv_events);
        calendarView = eventsPlannerView.findViewById(R.id.calendarView);
        radioGroup = eventsPlannerView.findViewById(R.id.radio_group_filter);
        RadioButton rbAllEventTask = eventsPlannerView.findViewById(R.id.rb_all_events_task);
        RadioButton rbMyEventTask = eventsPlannerView.findViewById(R.id.rb_my_events_task);

        eventTaskListAdapter = new EventTaskListAdapter(getActivity(), sortedEventsList, toOpen);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvEvents.setLayoutManager(mLayoutManager);
        rvEvents.setAdapter(eventTaskListAdapter);

        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        setListeners();

        if (toOpen.equals(Constants.Planner.EVENTS_LABEL)) {
            tvToolbarTitle.setText(getResources().getString(R.string.events));
            rbAllEventTask.setText(getString(R.string.all_events));
            rbMyEventTask.setText(getString(R.string.my_events));
        } else {
            tvToolbarTitle.setText(getResources().getString(R.string.tasks));
            rbAllEventTask.setText(getString(R.string.all_tasks));
            rbMyEventTask.setText(getString(R.string.my_tasks));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getEventsOfDay(eventParams);
    }

    // On clicking the date
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {
//        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
        eventParams = new EventParams();
        eventParams.setType(toOpen);
        eventParams.setDay(DateFormat.format("dd", calendarDay.getDate()).toString());
        eventParams.setMonth(DateFormat.format("MM", calendarDay.getDate()).toString());
        eventParams.setYear(DateFormat.format("yyyy", calendarDay.getDate()).toString());
        eventParams.setUserId(Util.getUserObjectFromPref().getId());

        presenter.getEventsOfDay(eventParams);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
//        Toast.makeText(getActivity(), "Month Changed:" + calendarDay, Toast.LENGTH_SHORT).show();
        if (selectedMonth != Integer.parseInt(DateFormat.format("MM", calendarDay.getDate()).toString())) {
            eventParams = new EventParams();
            eventParams.setType(toOpen);
            eventParams.setDay(DateFormat.format("dd", calendarDay.getDate()).toString());
            eventParams.setMonth(DateFormat.format("MM", calendarDay.getDate()).toString());
            eventParams.setYear(DateFormat.format("yyyy", calendarDay.getDate()).toString());
            eventParams.setUserId(Util.getUserObjectFromPref().getId());

            presenter.getEventsOfMonth(eventParams);
        }
    }

    private void setListeners() {
        ivBackIcon.setOnClickListener(this);
        ivEventsSyncIcon.setOnClickListener(this);
        btAddEvents.setOnClickListener(this);
        tvAllEventsDetail.setOnClickListener(this);
        ivCalendarMode.setOnClickListener(this);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
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
                presenter.getEventsOfDay(eventParams);
                break;

            case R.id.iv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;

            case R.id.bt_add_events:
                Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, toOpen);
                this.startActivity(intentCreateEvent);
                break;

            case R.id.tv_all_events_list:
                Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentEventList.putExtra(Constants.Planner.TO_OPEN, toOpen);
                this.startActivity(intentEventList);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all_events_task:
                isAllEvents = true;
                sortEventsList(isAllEvents);
                break;

            case R.id.rb_my_events_task:
                isAllEvents = false;
                sortEventsList(isAllEvents);
                break;
        }
    }

    private void sortEventsList(boolean isAllEvents) {
        sortedEventsList.clear();
        if (isAllEvents) {
            sortedEventsList.addAll(eventsList);
        } else {
            for (EventTask eventTask : eventsList) {
                if (Util.getUserObjectFromPref().getId().equals(eventTask.getOwnerid())) {
                    sortedEventsList.add(eventTask);
                }
            }
        }
        eventTaskListAdapter.notifyDataSetChanged();
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();

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
//        calendarView.setSelectedDate(instance.getTime());
//        calendarView.setCurrentDate(instance.getTime());
    }

    @Override
    public void showProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBarLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void hideProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> Util.showToast(result, this));
        }
    }

    public void displayEventsListOfDay(EventsResponse data) {
        eventsList.clear();
        if (data.getStatus() == 200) {
            if (data.getData() != null && data.getData().size() > 0) {
                eventsList.addAll(data.getData());
            }
        } else {
            showErrorMessage(data.getMessage());
        }
        sortEventsList(isAllEvents);
    }

    public void displayEventsListOfMonth(List<String> data) {
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        for (String obj : data) {
            try {
                cal.setTime(formatter.parse(obj));
                dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));

    }


}
