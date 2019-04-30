package com.platform.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.platform.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
    private Calendar c = Calendar.getInstance();

    public LeaveApplyFragment() {
        // Required empty public constructor
    }

    public static LeaveApplyFragment newInstance() {
        return new LeaveApplyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        Button btnApplyLeaves = view.findViewById(R.id.btn_apply_leave);
        btnApplyLeaves.setOnClickListener(this);

        setUIData();
    }

    private void setUIData() {
        tvCLSLLeavesCount.setText(String.valueOf(2));
        tvPaidLeavesCount.setText(String.valueOf(4));
        tvUnPaidLeavesCount.setText(String.valueOf(12));
        tvCOffLeavesCount.setText(String.valueOf(2));
        tvTotalLeavesCount.setText(String.valueOf(20));
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
                // String leaveTypeSelected = btnCategoryCL.getText().toString();
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryCoff.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                break;

            case R.id.btn_paid:
                // leaveTypeSelected = btnCategoryPaid.getText().toString();
                btnCategoryCL.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                btnCategoryPaid.setBackgroundResource(R.drawable.leave_form_view_focused);
                btnCategoryCoff.setBackgroundResource(R.drawable.leave_form_view_nonfocused);
                break;

            case R.id.btn_coff:
                // leaveTypeSelected = btnCategoryCoff.getText().toString();
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

            case R.id.btn_apply_leave:
                break;

            case R.id.btn_start_date:
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        (datePicker, year12, month12, day12) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year12, month12, day12);
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            btnStartDate.setText(format.format(calendar.getTime()));

                        }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.btn_end_date:
                int year1 = c.get(Calendar.YEAR);
                int month1 = c.get(Calendar.MONTH);
                int day1 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog endDatePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        (datePicker, year2, month2, day2) -> {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year2, month2, day2);
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            btnEndDate.setText(format.format(calendar.getTime()));

                        }, year1, month1, day1);

                endDatePickerDialog.show();
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private String getCurrentDateInSpecificFormat(String currentCalDate, boolean isStart) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date d = null;
        try {
            d = sdf.parse(currentCalDate);
            String dayNumberSuffix = getDayNumberSuffix(d.getDate());
            final String NEW_FORMAT = isStart ? " d'" + dayNumberSuffix + "' MMMM " : " d'" + dayNumberSuffix + "' MMMM yyyy";

            sdf.applyPattern(NEW_FORMAT);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf.format(d);
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }

        switch (day % 10) {
            case 1:
                return "st";

            case 2:
                return "nd";

            case 3:
                return "rd";

            default:
                return "th";
        }
    }

    private void showAlertDialog(String dialogTitle, String message, String btn1String, String btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
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
                // Close dialog
            });
        }

        dialog.setCancelable(false);
        dialog.show();      // if decline button is clicked, close the custom dialog
    }
}
