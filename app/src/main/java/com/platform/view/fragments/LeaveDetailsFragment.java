package com.platform.view.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.LeaveDataListener;
import com.platform.models.leaves.HolidayData;
import com.platform.models.leaves.LeaveBalanceResponse;
import com.platform.models.leaves.LeaveData;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.leaves.MonthlyLeaveDataAPIResponse;
import com.platform.models.leaves.MonthlyLeaveHolidayData;
import com.platform.presenter.LeavesPresenter;
import com.platform.utility.EventDecorator;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AppliedLeavesAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.platform.presenter.LeavesPresenter.DELETE_LEAVE;
import static com.platform.presenter.LeavesPresenter.GET_LEAVE_BALANCE;
import static com.platform.presenter.LeavesPresenter.GET_USER_LEAVE_DETAILS;
import static com.platform.utility.Constants.FORM_DATE;
import static com.platform.utility.Util.getDateFromTimestamp;

public class LeaveDetailsFragment extends Fragment implements View.OnClickListener,
        AppliedLeavesAdapter.LeaveAdapterListener, OnDateSelectedListener, OnMonthChangedListener, LeaveDataListener {

    private RecyclerView leavesList;
    private final ArrayList<LeaveData> leavesListData = new ArrayList<>();
    private final ArrayList<HolidayData> holidaysListData = new ArrayList<>();
    private AppliedLeavesAdapter leavesAdapter;
    private MaterialCalendarView calendarView;
    ImageView ivCalendarMode;
    private boolean isMonth = true;
    private String userLeaveDetailsResponse;
    private List<LeaveDetail> leaveBalance = new ArrayList<>();
    private LeavesPresenter presenter;
    String deleteLeaveId;

    int selectedMonth;
    SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat mmFormat = new SimpleDateFormat("MM", Locale.ENGLISH);

    public LeaveDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_applied_leaves_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView toolBarMenu = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar_edit_action);
        toolBarMenu.setImageResource(R.drawable.ic_holiday_list);
        leavesList = view.findViewById(R.id.rv_applied_leaves_list);
        ivCalendarMode = view.findViewById(R.id.tv_calendar_mode);
        ivCalendarMode.setOnClickListener(this);
        calendarView = view.findViewById(R.id.calendarView);
        Button btnAddLeaves = view.findViewById(R.id.btn_add_leaves);
        btnAddLeaves.setOnClickListener(this);
        Button btnRequestCompoff = view.findViewById(R.id.btn_compoff_request);
        btnRequestCompoff.setOnClickListener(this);
        toolBarMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
            intent.putExtra("title", getString(R.string.holiday_list));
            intent.putExtra("switch_fragments", "HolidayListFragment");
            startActivity(intent);
        });

        presenter = new LeavesPresenter(this);

        if(Util.isConnected(getContext())){
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                if(bundle.getSerializable("leaveBalance")!=null) {
                    leaveBalance.clear();
                    leaveBalance.addAll((ArrayList<LeaveDetail>) bundle.getSerializable("leaveBalance"));
                }
            }

            if(leaveBalance.size()==0)
                presenter.getLeavesBalance();

            setListData();
            setUIData();
        }else {
            Util.showToast(getString(R.string.msg_no_network), this);
        }
    }
    private void setListData() {
        leavesAdapter = new AppliedLeavesAdapter(leavesListData, this);
        leavesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        leavesList.setAdapter(leavesAdapter);
    }


    private void setUIData() {
        calendarView.setOnMonthChangedListener(this);
        isMonth = !isMonth;
        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.clearData();
            presenter = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Date d = new Date();
        presenter.getUsersAllLeavesDetails(yyyyFormat.format(d.getTime()), mmFormat.format(d.getTime()));
        selectedMonth=Integer.parseInt(mmFormat.format(d.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_leaves:
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", getString(R.string.apply_leave));
                intent.putExtra("isEdit", false);
                intent.putExtra("apply_type", "Leave");
                intent.putExtra("leaveBalance", (Serializable) leaveBalance);
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(intent);
                break;

            case R.id.btn_compoff_request:
                Intent compoffIntent = new Intent(getActivity(), GeneralActionsActivity.class);
                compoffIntent.putExtra("title", getString(R.string.compoff_request));
                compoffIntent.putExtra("isEdit", false);
                compoffIntent.putExtra("apply_type", "Comp-Off");
                compoffIntent.putExtra("leaveBalance", (Serializable) leaveBalance);
                compoffIntent.putExtra("switch_fragments", "LeaveApplyFragment");
                startActivity(compoffIntent);
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

    public void displayLeavesOfMonth(List<LeaveData> data) {
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        for(LeaveData obj:data) {
            try {
                cal.setTime(formatter.parse(getDateFromTimestamp(obj.getStartdate(), FORM_DATE)));
                dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));
    }

    public void displayHolidaysOfMonth(List<HolidayData> data) {
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        for(HolidayData obj:data) {
            try {
                cal.setTime(formatter.parse(getDateFromTimestamp(obj.getHolidayDate(), FORM_DATE)));
                dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.bg_circle_red)));
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay calendarDay) {
        if (selectedMonth != Integer.parseInt(mmFormat.format(calendarDay.getDate()))) {
            presenter.getUsersAllLeavesDetails(yyyyFormat.format(calendarDay.getDate()), mmFormat.format(calendarDay.getDate()));
            selectedMonth=Integer.parseInt(mmFormat.format(calendarDay.getDate()));
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteLeaves(String leaveId) {
        deleteLeaveId = leaveId;
        showDeleteAlert();
    }

    @Override
    public void editLeaves(LeaveData leaveData) {
        Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
        intent.putExtra("title", getString(R.string.leave_details));
        intent.putExtra("isEdit", true);
        intent.putExtra("apply_type", "Leave");
        intent.putExtra("userLeaveDetails", (Serializable) leaveData);
        intent.putExtra("leaveBalance", (Serializable) leaveBalance);
        intent.putExtra("switch_fragments", "LeaveApplyFragment");

        startActivity(intent);
    }

    public void showDeleteAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete alert")
                .setMessage(getString(R.string.sure_to_delete))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteUserLeave(deleteLeaveId);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (GET_USER_LEAVE_DETAILS.equals(requestID)) {
            userLeaveDetailsResponse = response;
            MonthlyLeaveDataAPIResponse monthlyLeaveDataAPIResponse = PlatformGson.getPlatformGsonInstance().fromJson(userLeaveDetailsResponse, MonthlyLeaveDataAPIResponse.class);
            if (monthlyLeaveDataAPIResponse != null) {
                leavesListData.clear();
                MonthlyLeaveHolidayData monthlyLeaveHolidayData = monthlyLeaveDataAPIResponse.getData();
                List<LeaveData> monthlyLeaveData = monthlyLeaveHolidayData.getLeaveData();
                List<HolidayData> monthlyHolidayData = monthlyLeaveHolidayData.getHolidayData();
                if(monthlyLeaveData.size()==0){
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), getString(R.string.no_leave_applied_msg),
                            Snackbar.LENGTH_LONG);
                }
                leavesListData.addAll(monthlyLeaveData);
                leavesAdapter.notifyDataSetChanged();
                // To show highlighted calendar dates
                holidaysListData.addAll(monthlyHolidayData);
                displayLeavesOfMonth(leavesListData);
                displayHolidaysOfMonth(holidaysListData);
            }
        }else if(requestID.equals(DELETE_LEAVE)){
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.leave_deleted_msg),
                    Snackbar.LENGTH_LONG);
            int deletePosition = -1;
            for (int i = 0; i<leavesListData.size(); i++) {
                if(leavesListData.get(i).getId().equals(deleteLeaveId)){
                    deletePosition = i;
                    break;
                }
            }
            if(deletePosition>-1){
                leavesListData.remove(deletePosition);
            }
            leavesAdapter.notifyDataSetChanged();
        } else if(requestID.equals(GET_LEAVE_BALANCE)) {
            LeaveBalanceResponse leaveBalanceResponse = PlatformGson.getPlatformGsonInstance().fromJson(response, LeaveBalanceResponse.class);
            if(leaveBalanceResponse.getStatus()==200){
                leaveBalance.clear();
                leaveBalance.addAll(leaveBalanceResponse.getData());
            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), leaveBalanceResponse.getMessage(),
                        Snackbar.LENGTH_LONG);
            }

        }
    }

    @Override
    public void showProgressBar() {
        Util.showSimpleProgressDialog(getActivity(), null, getString(R.string.please_wait), false);
    }

    @Override
    public void hideProgressBar() {
        Util.removeSimpleProgressDialog();
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
