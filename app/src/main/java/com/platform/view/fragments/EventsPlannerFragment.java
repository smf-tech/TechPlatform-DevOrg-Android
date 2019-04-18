package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.models.events.Event;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.view.activities.CreateEventActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.adapters.EventListAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventsPlannerFragment extends Fragment implements View.OnClickListener,OnDateSelectedListener,RadioGroup.OnCheckedChangeListener {

    private ImageView ivBackIcon;
    private ImageView ivEventsSyncIcon;
    private View eventsPlannerView;
    private boolean isDashboard;
    private AppBarLayout appBarLayout;
    private RelativeLayout lyCalender;
    private ImageView ivCalendarMode;
    private TextView tvAllEventsDetail;
    private TextView tvNoEventsMsg;
    private RadioGroup radioGroup;
    private FloatingActionButton btAddEvents;
    private RecyclerView rvEvents;
    private MaterialCalendarView calendarView ;
    // flag to check th calender mode
    boolean isMonth;

    EventListAdapter eventListAdapter;
    ArrayList<Event> eventsList;
    ArrayList<Event> sortedEventsList;

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

        eventsList=new ArrayList<Event>();
        sortedEventsList=new ArrayList<Event>();

        eventsList.add(new Event("1","meeting", "Tital1", "01/01/0001","10:00 am",
                "11:00 am","-","test","wagoli,pune.","sachin",
                "1234"));

        eventsList.add(new Event("2","visit" ,"Tital2", "01/01/0001","10:00 am",
                "11:00 am", "-","test","hadpsir,pune.","sagar",
                "1235"));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }
        isMonth=false;

        ivBackIcon = eventsPlannerView.findViewById(R.id.iv_back_icon);
        ivEventsSyncIcon = eventsPlannerView.findViewById(R.id.iv_events_sync_icon);
        appBarLayout= eventsPlannerView.findViewById(R.id.app_bar_layout);
        lyCalender = eventsPlannerView.findViewById(R.id.ly_calender);
        ivCalendarMode = eventsPlannerView.findViewById(R.id.iv_calendar_mode);
        tvAllEventsDetail = eventsPlannerView.findViewById(R.id.tv_all_events_list);
        tvNoEventsMsg = eventsPlannerView.findViewById(R.id.tv_no_events_msg);
        btAddEvents = eventsPlannerView.findViewById(R.id.bt_add_events);
        rvEvents = eventsPlannerView.findViewById(R.id.rv_events);
        calendarView = eventsPlannerView.findViewById(R.id.calendarView);
        radioGroup = (RadioGroup) eventsPlannerView.findViewById(R.id.radio_group_filter);

        eventListAdapter = new EventListAdapter(getActivity(),sortedEventsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvEvents.setLayoutManager(mLayoutManager);
        rvEvents.setAdapter(eventListAdapter);

        sorteEventsList(true);
        setCalendar();
        setListeners();

        if(isDashboard) {
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

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Toast.makeText(getActivity(),"Month Changed:"+date,Toast.LENGTH_SHORT).show();
            }
        });
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
        switch (v.getId()){
            case R.id.iv_back_icon:
                getActivity().finish();
                break;
            case R.id.iv_events_sync_icon:
                break;
            case R.id.iv_calendar_mode:
                if(isMonth){
                    isMonth=false;
//                    ivCalendarMode.setImageResource(getResources().getDrawable(R.drawable.ic_right_arrow_grey));
                } else {
                    isMonth=true;
                }
                setCalendar();
                break;
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all_events:
                sorteEventsList(true);
                break;
            case R.id.rb_my_event:
                sorteEventsList(false);
                break;
        }
    }

    private void sorteEventsList(boolean isAllEnents) {
        sortedEventsList.clear();
        if(isAllEnents){
            sortedEventsList.addAll(eventsList);
        } else {
            String ownerID="1234";
            for(Event event:eventsList){
                if(ownerID.equals(event.getOwnerID())){
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
        if(isMonth){
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
        }
        calendarView.setSelectedDate(instance.getTime());
        calendarView.setCurrentDate(instance.getTime());
        highliteDates();
    }

    private void highliteDates() {
        // set the date list to highlight
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        String reg_date = formatter.format(cal.getTime());

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
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        Toast.makeText(getActivity(),"date:"+calendarDay,Toast.LENGTH_SHORT).show();
    }


}
