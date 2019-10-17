package com.platform.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MachineDieselRecord;
import com.platform.models.SujalamSuphalam.MachineWorkingHoursRecord;
import com.platform.presenter.MachineDieselRecordFragmentPresenter;
import com.platform.presenter.MachineShiftingFormFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.adapters.MachineDieselRecordsAdapter;
import com.platform.view.adapters.MachineWorkingHoursAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MachineDieselRecordFragment extends Fragment  implements APIDataListener, View.OnClickListener,
        OnDateSelectedListener, OnMonthChangedListener {

    private View machineDieselRecordFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    String machineId, currentStructureId;
    private EditText etMachineCode, etStructureCode, etDieselQuantity;
    private Button btnAdd, btnSubmit;
    private MachineDieselRecordFragmentPresenter machineDieselRecordFragmentPresenter;
    private RecyclerView rvDieselRecords;
    private ImageView ivCalendarMode, imgDieselReceipt, imgRegisterOne, imgRegisterTwo;
    private boolean isMonth = true;
    private MaterialCalendarView calendarView;private final ArrayList<MachineDieselRecord> machineDieselRecordsList = new ArrayList<>();
    private int selectedMonth;
    private SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private SimpleDateFormat mmFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
    private MachineDieselRecordsAdapter machineDieselRecordsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDieselRecordFragmentView = inflater.inflate(R.layout.fragment_machine_diesel_record, container, false);
        return machineDieselRecordFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        init();
    }

    private void init() {
        progressBarLayout = machineDieselRecordFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDieselRecordFragmentView.findViewById(R.id.pb_profile_act);
        machineDieselRecordFragmentPresenter = new MachineDieselRecordFragmentPresenter(this);
        ivCalendarMode = machineDieselRecordFragmentView.findViewById(R.id.tv_calendar_mode);
        ivCalendarMode.setOnClickListener(this);
        calendarView = machineDieselRecordFragmentView.findViewById(R.id.calendarView);
        etStructureCode = machineDieselRecordFragmentView.findViewById(R.id.et_structure_code);
        etMachineCode = machineDieselRecordFragmentView.findViewById(R.id.et_machine_code);
        etDieselQuantity = machineDieselRecordFragmentView.findViewById(R.id.et_diesel_quantity);
        btnSubmit = machineDieselRecordFragmentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnAdd = machineDieselRecordFragmentView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        imgDieselReceipt = machineDieselRecordFragmentView.findViewById(R.id.img_diesel_receipt);
        imgDieselReceipt.setOnClickListener(this);
        imgRegisterOne = machineDieselRecordFragmentView.findViewById(R.id.img_register_one);
        imgRegisterOne.setOnClickListener(this);
        imgRegisterTwo = machineDieselRecordFragmentView.findViewById(R.id.img_register_two);
        imgRegisterTwo.setOnClickListener(this);
        machineDieselRecordsAdapter = new MachineDieselRecordsAdapter(machineDieselRecordsList, this);
        rvDieselRecords = machineDieselRecordFragmentView.findViewById(R.id.rv_diesel_record);
        rvDieselRecords.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDieselRecords.setAdapter(machineDieselRecordsAdapter);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        isMonth = !isMonth;
        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        Date d = new Date();
        selectedMonth=Integer.parseInt(mmFormat.format(d.getTime()));
        etMachineCode.setText(machineId);
        etStructureCode.setText(currentStructureId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineDieselRecordFragmentPresenter != null) {
            machineDieselRecordFragmentPresenter.clearData();
            machineDieselRecordFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
            case R.id.img_diesel_receipt:
                //onAddImageClick();
                break;
            case R.id.img_register_one:
                //onAddImageClick();
                break;
            case R.id.img_register_two:
                //onAddImageClick();
                break;
            case R.id.btn_submit:
//                uploadImage();
//                machineDieselRecordFragmentPresenter.submitDieselRecord();
                break;
            case R.id.btn_add:
                if(etDieselQuantity.getText().toString()!=null && etDieselQuantity.getText().toString().length()>0) {
                    MachineDieselRecord machineDieselRecord = new MachineDieselRecord();
                    machineDieselRecord.setDieselDate(123343223);
                    machineDieselRecord.setDieselQuantity(etDieselQuantity.getText().toString());
                    machineDieselRecord.setMachineId(machineId);
                    machineDieselRecord.setStructureId(currentStructureId);
                    machineDieselRecordsList.add(machineDieselRecord);
                    machineDieselRecordsAdapter.notifyDataSetChanged();
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Enter diesel quantity.",
                            Snackbar.LENGTH_LONG);
                }
//                uploadImage();
//                machineDieselRecordFragmentPresenter.submitDieselRecord();
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

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        date.getDate();
        date.getDate();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
