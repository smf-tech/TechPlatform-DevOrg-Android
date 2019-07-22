package com.platform.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.platform.R;
import com.platform.listeners.LeaveDataListener;
import com.platform.models.leaves.HolidayData;
import com.platform.models.leaves.LeaveData;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.leaves.MonthlyLeaveDataAPIResponse;
import com.platform.models.leaves.MonthlyLeaveHolidayData;
import com.platform.presenter.LeavesPresenter;
import com.platform.utility.Constants;
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
import static com.platform.presenter.LeavesPresenter.GET_USER_LEAVE_DETAILS;
import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Constants.FORM_DATE;
import static com.platform.utility.Util.getDateFromTimestamp;

public class LeaveDetailsFragment extends Fragment implements View.OnClickListener,
        AppliedLeavesAdapter.LeaveAdapterListener, OnDateSelectedListener, OnMonthChangedListener, LeaveDataListener {

    private RecyclerView leavesList;
    private final ArrayList<LeaveData> leavesListData = new ArrayList<>();
    //private final ArrayList<LeaveData> filteredLeavesListData = new ArrayList<>();
    private final ArrayList<HolidayData> holidaysListData = new ArrayList<>();
    private AppliedLeavesAdapter leavesAdapter;
    private MaterialCalendarView calendarView;
    ImageView ivCalendarMode;
    //private TabLayout tabLayout;
//    private final int[] tabIcons = {
//            R.drawable.selector_pending_tab,
//            R.drawable.selector_approved_tab,
//            R.drawable.selector_rejected_tab
//    };

//    private String[] tabNames;
    private boolean isMonth = true;
    private String serverResponse;
    private String userLeaveDetailsResponse;
    private List<LeaveDetail> leaveBalance = new ArrayList<>();
    private LeavesPresenter presenter;
    String deleteLeaveId;

    public LeaveDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tabNames = new String[]{
//                getString(R.string.cat_pending),
//                getString(R.string.cat_approved),
//                getString(R.string.cat_rejected)};
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
    //    toolBarMenu.setBackgroundResource(R.drawable.ic_holiday_menu);
        toolBarMenu.setImageResource(R.drawable.ic_holiday_list);
        leavesList = view.findViewById(R.id.rv_applied_leaves_list);
        //tabLayout = view.findViewById(R.id.leave_cat_tabs);
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(bundle.getSerializable("leaveBalance")!=null) {
                leaveBalance.addAll((ArrayList<LeaveDetail>) bundle.getSerializable("leaveBalance"));
            }
        }
//        leavesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy < -5 && btnAddLeaves.getVisibility() != View.VISIBLE) {
//                    btnAddLeaves.setVisibility(View.VISIBLE);
//                    btnRequestCompoff.setVisibility(View.VISIBLE);
//                } else if (dy > 5 && btnAddLeaves.getVisibility() == View.VISIBLE) {
//                    btnAddLeaves.setVisibility(View.INVISIBLE);
//                    btnRequestCompoff.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
        //Date d = new Date();
//        presenter = new LeavesPresenter(this);
//        presenter.getUsersAllLeavesDetails(DateFormat.format("yyyy", d.getTime()).toString(),DateFormat.format("MM", d.getTime()).toString());
        setListData();
        //setTabData();
        setUIData();
        //filterListData(Constants.Leave.PENDING_STATUS);
    }

    /*private void setTabData() {
        //setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    filterListData(Constants.Leave.PENDING_STATUS);
                    //setTabData(1);
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    filterListData(Constants.Leave.APPROVED_STATUS);
                    //setTabData(2);
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    filterListData(Constants.Leave.REJECTED_STATUS);
                    //setTabData(3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }*/

    private void setListData() {
        leavesAdapter = new AppliedLeavesAdapter(leavesListData, this);
        leavesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        leavesList.setAdapter(leavesAdapter);
        //setTabData(1);
    }

//    private void filterListData(String filterStatus){
//        filteredLeavesListData.clear();
//        for (LeaveData leaveData: leavesListData) {
//            if(leaveData.getStatus().equalsIgnoreCase(filterStatus)){
//                filteredLeavesListData.add(leaveData);
//            }
//        }leavesAdapter.notifyDataSetChanged();
//    }

//    private void setupTabIcons() {
//        for (int i = 0; i < tabNames.length; i++) {
//            TextView tabOne = (TextView) LayoutInflater.from(getActivity())
//                    .inflate(R.layout.layout_leaves_attendance_tab, tabLayout, false);
//            tabOne.setText(tabNames[i]);
//            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
//            tabLayout.addTab(tabLayout.newTab().setCustomView(tabOne));
//        }
//    }


    private void setUIData() {
        //initCalender(view);

        calendarView.setOnMonthChangedListener(this);

        isMonth = !isMonth;
        setCalendar();
    }

//    private void setTabData(int size) {
//        ArrayList<String> leaves = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            leaves.add("1");
//        }
//        leavesListData.clear();
//        //leavesListData.addAll(leaves);
//        leavesAdapter.notifyDataSetChanged();
//
//    }

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
        presenter = new LeavesPresenter(this);
        presenter.getUsersAllLeavesDetails(DateFormat.format("yyyy", d.getTime()).toString(),DateFormat.format("MM", d.getTime()).toString());
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
        //highlightDates(dateList);
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
        Date d = new Date();
        presenter = new LeavesPresenter(this);
        presenter.getUsersAllLeavesDetails(DateFormat.format("yyyy", calendarDay.getDate()).toString(),DateFormat.format("MM", calendarDay.getDate()).toString());

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteLeaves(String leaveId) {
        deleteLeaveId = leaveId;
        showAlertDialog(getString(R.string.sure_to_delete), getString(R.string.cancel), getString(R.string.delete));
    }

    @Override
    public void editLeaves(LeaveData leaveData) {
        //userLeaveDetailsResponse = "{\"userId\": \"12345\",\"leaveTypes\": [{\"leaveType\": \"CL\",\"allocatedLeaves\": 2 }],\"fromDate\": \"2019-03-11T18:30:00.000Z\",\"toDate\": \"2019-03-16T18:30:00.000Z\",\"isHalfDay\": false,\"reason\": \"NA\",\"numberOfDays\": 3,\"status\": \"pending\" }";
        Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
        intent.putExtra("title", getString(R.string.leave_details));
        intent.putExtra("isEdit", true);
        intent.putExtra("apply_type", "Leave");
        intent.putExtra("userLeaveDetails", (Serializable) leaveData);
        intent.putExtra("leaveBalance", (Serializable) leaveBalance);
        intent.putExtra("switch_fragments", "LeaveApplyFragment");

        startActivity(intent);
    }

    private void showAlertDialog(String message, String btn1String, String btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty("")) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText("");
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);

            button1.setOnClickListener(v -> {
                presenter.deleteUserLeave(deleteLeaveId);
                dialog.dismiss();
            });
        }

        dialog.setCancelable(true);
        dialog.show();      // if decline button is clicked, close the custom dialog
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
                //filteredLeavesListData.clear();
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
                // To show filter leaves data
                //filterListData(Constants.Leave.PENDING_STATUS);
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
            if(deletePosition>0){
                leavesListData.remove(deletePosition);
//                filteredLeavesListData.clear();
//                filteredLeavesListData.addAll(leavesListData);
            }
            leavesAdapter.notifyDataSetChanged();
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
