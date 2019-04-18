package com.platform.view.fragments;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.utility.EventDecorator;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AppliedLeavesAdapter;

//import com.shrikanthravi.collapsiblecalendarview.data.Day;
//import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LeaveDetailsFragment extends Fragment implements View.OnClickListener,OnDateSelectedListener {

    private ImageView toolBarMenu;
    private RecyclerView leavesList;
    private ImageView imgAddLeaves;

    private TextView tvClickPending;
    private TextView tvClickApproved;
    private TextView tvClickRejected;
    private int tabClicked =-1;
    private MaterialCalendarView calendarView ;
    private ImageView tvCalendarMode;
    boolean isMonth = true;

    public LeaveDetailsFragment() {
        // Required empty public constructor
    }


    public static LeaveDetailsFragment newInstance(String param1, String param2) {
        LeaveDetailsFragment fragment = new LeaveDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_applied_leaves_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolBarMenu = getActivity().findViewById(R.id.toolbar_edit_action);
        toolBarMenu.setBackgroundResource(R.drawable.ic_menu_ho_support);
        leavesList = view.findViewById(R.id.rv_applied_leaves_list);
        imgAddLeaves = view.findViewById(R.id.iv_add_leaves);
        tvClickPending = view.findViewById(R.id.tv_tb_pending);
        tvClickPending.setOnClickListener(this);
        tvClickApproved = view.findViewById(R.id.tv_tb_approved);
        tvClickApproved.setOnClickListener(this);
        tvClickRejected = view.findViewById(R.id.tv_tb_rejected);
        tvClickRejected.setOnClickListener(this);
        tvCalendarMode = view.findViewById(R.id.tv_calendar_mode);
        tvCalendarMode.setOnClickListener(this);
        calendarView = view.findViewById(R.id.calendarView);

        imgAddLeaves.setOnClickListener(this);

        toolBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Holiday List");
                intent.putExtra("switch_fragments", "HolidayListFragment");
                startActivity(intent);
            }
        });
        setUIData(view);

    }

    private void setUIData(View view) {
        //initCalender(view);

        ArrayList<String> leaves = new ArrayList<>();
        leaves.add("1");
        leaves.add("2");
        AppliedLeavesAdapter adapter = new AppliedLeavesAdapter(getActivity(), leaves);
        leavesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        leavesList.setAdapter(adapter);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Toast.makeText(getActivity(),"Month Changed:"+date,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_leaves:
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", "Apply For Leave");
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(intent);
                break;
            case R.id.tv_tb_pending:

                if(tabClicked != 1) {
                    tabClicked = 1;
                    tvClickPending.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                break;
            case R.id.tv_tb_approved:
                if(tabClicked != 2) {
                    tabClicked = 2;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                break;
            case R.id.tv_tb_rejected:
                if(tabClicked != 3) {
                    tabClicked = 3;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.black_green));
                }
                break;

            case R.id.tv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
        }
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
