package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.Event;
import com.platform.models.events.Participant;
import com.platform.presenter.EventsPlannerFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.utility.Util;
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

public class EventsPlannerFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener, RadioGroup.OnCheckedChangeListener, PlatformTaskListener {

    private ImageView ivBackIcon;
    private ImageView ivEventsSyncIcon;
    private ImageView ivCalendarMode;
    private TextView tvAllEventsDetail;
    private View eventsPlannerView;
    private RadioGroup radioGroup;
    private FloatingActionButton btAddEvents;
    private MaterialCalendarView calendarView;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;

    private boolean isDashboard;
    private boolean isMonth;

    private EventListAdapter eventListAdapter;
    private ArrayList<Event> eventsList;
    private ArrayList<Event> sortedEventsList;

    public EventsPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventsPlannerView = inflater.inflate(R.layout.fragment_events_planner, container, false);
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
        progressBarLayout = eventsPlannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = eventsPlannerView.findViewById(R.id.pb_profile_act);


        EventsPlannerFragmentPresenter eventsPlannerPresenter = new EventsPlannerFragmentPresenter(this);
        eventsPlannerPresenter.getEvents("");


        eventsList = new ArrayList<>();
        sortedEventsList = new ArrayList<>();

        ArrayList<Participant> membersList = new ArrayList<>();
        membersList.add(new Participant("1", "Sagar Mahajan", "DM", true, true));
        membersList.add(new Participant("2", "Kishor Shevkar", "TC", false, false));
        membersList.add(new Participant("3", "Jagruti Devare", "MT", true, true));
        membersList.add(new Participant("4", "Sachin Kakade", "FA", false, false));

        eventsList.add(new Event("1", "Meeting", "Title1", 1556794660l, null, "10:00 am",
                "11:00 am", "-", "test", "Wagholi,pune.", "Sachin",
                "1234", Constants.Planner.COMPLETED_STATUS, membersList, null));

        eventsList.add(new Event("2", "Event", "Title2", 1556794660l, null, "10:00 am",
                "11:30 am", "-", "test", "Hadpsar,pune.", "Sagar",
                "1235", Constants.Planner.PLANNED_STATUS, membersList, null));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }
        isMonth = false;

        ivBackIcon = eventsPlannerView.findViewById(R.id.iv_back_icon);
        ivEventsSyncIcon = eventsPlannerView.findViewById(R.id.iv_events_sync_icon);
        AppBarLayout appBarLayout = eventsPlannerView.findViewById(R.id.app_bar_layout);
        RelativeLayout lyCalender = eventsPlannerView.findViewById(R.id.ly_calender);
        ivCalendarMode = eventsPlannerView.findViewById(R.id.iv_calendar_mode);
        tvAllEventsDetail = eventsPlannerView.findViewById(R.id.tv_all_events_list);

        btAddEvents = eventsPlannerView.findViewById(R.id.bt_add_events);
        RecyclerView rvEvents = eventsPlannerView.findViewById(R.id.rv_events);
        calendarView = eventsPlannerView.findViewById(R.id.calendarView);
        radioGroup = eventsPlannerView.findViewById(R.id.radio_group_filter);

        eventListAdapter = new EventListAdapter(getActivity(), sortedEventsList, Constants.Planner.EVENTS_LABEL);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvEvents.setLayoutManager(mLayoutManager);
        rvEvents.setAdapter(eventListAdapter);

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
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                this.startActivity(intentCreateEvent);
                break;

            case R.id.tv_all_events_list:
                Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                this.startActivity(intentEventList);
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
        sortedEventsList.clear();
        if (isAllEvents) {
            sortedEventsList.addAll(eventsList);
        } else {
            String ownerID = "1234";
            for (Event event : eventsList) {
                if (ownerID.equals(event.getOwnerID())) {
                    sortedEventsList.add(event);
                }
            }
        }

        eventListAdapter.notifyDataSetChanged();
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
            Log.e("TAG", e.getMessage());
        }

        cal.add(Calendar.DATE, 3);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {
        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
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
}
