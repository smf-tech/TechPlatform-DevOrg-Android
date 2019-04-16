package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.activities.CreateEventActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;


public class EventsPlannerFragment extends Fragment implements View.OnClickListener {

    private View eventsPlannerView;
    private boolean isDashboard;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }

        AppBarLayout appBarLayout= eventsPlannerView.findViewById(R.id.app_bar_layout);
        RelativeLayout lyCalender = eventsPlannerView.findViewById(R.id.ly_calender);
        LinearLayout lyFilterTab = eventsPlannerView.findViewById(R.id.ly_filter_tab);
        TextView todayEvent = eventsPlannerView.findViewById(R.id.tv_today_events);
        TextView tvAllEventsDetail = eventsPlannerView.findViewById(R.id.tv_all_events_list);
        TextView tvNoEventsMsg = eventsPlannerView.findViewById(R.id.tv_no_events_msg);
        FloatingActionButton btAddEvents = eventsPlannerView.findViewById(R.id.bt_add_events);
        RecyclerView rvEvents = eventsPlannerView.findViewById(R.id.rv_events);
/////////////// Calender /////////////////////
        final CollapsibleCalendar collapsibleCalendar = eventsPlannerView.findViewById(R.id.calendarView);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Toast.makeText(getActivity(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemClick(View view) {
            }
            @Override
            public void onDataUpdate() {

            }
            @Override
            public void onMonthChange() {
                Toast.makeText(getActivity(), "Month changed",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onWeekChange(int i) {
                Toast.makeText(getActivity(), "Week changed",Toast.LENGTH_SHORT).show();
            }
        });

//        CalendarView collapsibleCalendarr = eventsPlannerView.findViewById(R.id.calendar_View);
//        collapsibleCalendarr.sele(cal.getTime(), true);
///////////////////////////////////
        btAddEvents.setOnClickListener(this);
        tvAllEventsDetail.setOnClickListener(this);

        if(isDashboard) {
            appBarLayout.setVisibility(View.GONE);
            lyCalender.setVisibility(View.GONE);
            lyFilterTab.setVisibility(View.GONE);
            todayEvent.setVisibility(View.VISIBLE);
            tvAllEventsDetail.setVisibility(View.VISIBLE);
//            tvAllEventsDetail.setVisibility(View.GONE);
        } else {
            appBarLayout.setVisibility(View.VISIBLE);
            lyCalender.setVisibility(View.VISIBLE);
            lyFilterTab.setVisibility(View.VISIBLE);
            todayEvent.setVisibility(View.GONE);
            tvAllEventsDetail.setVisibility(View.GONE);
//            tvAllEventsDetail.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_events:
                Intent intentCreateEvent = new Intent(getActivity(), CreateEventActivity.class);
                this.startActivity(intentCreateEvent);
                break;
            case R.id.tv_all_events_list:
                Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentEventList.putExtra(Constants.Planner.TO_OPEN,"EVENTS");
                this.startActivity(intentEventList);
                break;
        }
    }
}
