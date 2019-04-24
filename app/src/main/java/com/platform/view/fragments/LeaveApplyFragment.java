package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.platform.R;

import java.util.Calendar;

public class LeaveApplyFragment extends Fragment implements View.OnClickListener {
    private TextView tvCLSLLeavesCount;
    private TextView tvPaidLeavesCount;
    private TextView tvUnPaidLeavesCount;
    private TextView tvCOffLeavesCount;
    private TextView tvTotalLeavesCount;
    private Button btnCategoryCL;
    private Button btnCategoryPaid;
    private Button btnCategoryCoff;

    private Button btnHalfDay;
    private Button btnFullDay;

    private TextView btnStartDate;
    private TextView btnEndDate;

    private DialogFragment dialogFragment;
    private Calendar c = Calendar.getInstance();

    public LeaveApplyFragment() {
        // Required empty public constructor
    }


    public static LeaveApplyFragment newInstance() {
        LeaveApplyFragment fragment = new LeaveApplyFragment();

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
        return inflater.inflate(R.layout.layout_apply_leaves, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCLSLLeavesCount = view.findViewById(R.id.tv_leaves_cl);
        tvPaidLeavesCount = view.findViewById(R.id.tv_leaves_paid);
        tvUnPaidLeavesCount = view.findViewById(R.id.tv_leaves_unpaid);
        tvCOffLeavesCount = view.findViewById(R.id.tv_leaves_coff);
        tvTotalLeavesCount = view.findViewById(R.id.tv_total_leaves_count);

        btnCategoryCL = view.findViewById(R.id.btn_cl);
        btnCategoryCL.setOnClickListener(this);
        btnCategoryPaid = view.findViewById(R.id.btn_paid);
        btnCategoryPaid.setOnClickListener(this);
        btnCategoryCoff = view.findViewById(R.id.btn_coff);
        btnCategoryCoff.setOnClickListener(this);

        btnHalfDay = view.findViewById(R.id.btn_half_day);
        btnHalfDay.setOnClickListener(this);
        btnFullDay = view.findViewById(R.id.btn_full_day);
        btnFullDay.setOnClickListener(this);

        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnStartDate.setOnClickListener(this);
        btnEndDate = view.findViewById(R.id.btn_end_date);
        btnEndDate.setOnClickListener(this);
        setUIData();

    }


    private void setUIData(){
        tvCLSLLeavesCount.setText("2");
        tvPaidLeavesCount.setText("4");
        tvUnPaidLeavesCount.setText("12");
        tvCOffLeavesCount.setText("2");
        tvTotalLeavesCount.setText("20");
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
            case R.id.btn_cl:
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryCoff.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                break;
            case R.id.btn_paid:
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryCoff.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                break;
            case R.id.btn_coff:
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryCoff.setBackgroundResource(R.drawable.leave_form_view_focused);
                break;
            case R.id.btn_half_day:
                btnHalfDay.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnFullDay.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                break;
            case R.id.btn_full_day:
                btnHalfDay.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnFullDay.setBackgroundResource(R.drawable.leave_form_view_focused);
                break;
            case R.id.btn_start_date:

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btnStartDate.setText(day+"-"+month+"-"+year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.btn_end_date:
                int year1 = c.get(Calendar.YEAR);
                int month1 = c.get(Calendar.MONTH);
                int day1 = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btnEndDate.setText(day+"-"+month+"-"+year);
                            }
                        }, year1, month1, day1);
                endDatePickerDialog.show();
                break;
        }
    }
}
