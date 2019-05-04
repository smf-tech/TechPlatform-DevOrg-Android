package com.platform.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.events.Recurrence;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RepeatEventActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, MultiSelectSpinner.MultiSpinnerListener {

    private ImageView ivBackIcon;
    private ImageView toolbarAction;
    private Spinner spRepeat;
    private Spinner spDailyInterval;
    private Spinner spWeeklyInterval;
    private Spinner spMonthlyInterval;

    private EditText etEndDate;
    private EditText etWhichDate;

    private List<String> whichDays = new ArrayList<>();
    private List<String> whichDaysSelected = new ArrayList<>();

    private String selectedRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_event);
        initView();
    }

    private void initView() {
        setActionbar(getResources().getString(R.string.repeat));
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        toolbarAction = findViewById(R.id.toolbar_edit_action);
        toolbarAction.setVisibility(View.VISIBLE);
        toolbarAction.setImageResource(R.drawable.ic_check_white);

        spRepeat = findViewById(R.id.sp_repeat);
        ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeat.setAdapter(adapterRepeat);

        spDailyInterval = findViewById(R.id.sp_day_interval);
        ArrayAdapter<CharSequence> adapterDailyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_daily, android.R.layout.simple_spinner_item);
        adapterDailyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDailyInterval.setAdapter(adapterDailyInterval);

        spWeeklyInterval = findViewById(R.id.sp_week_interval);
        ArrayAdapter<CharSequence> adapterWeeklyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_weeks, android.R.layout.simple_spinner_item);
        adapterWeeklyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeeklyInterval.setAdapter(adapterWeeklyInterval);

        spMonthlyInterval = findViewById(R.id.sp_month_interval);
        ArrayAdapter<CharSequence> adapterMonthlyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_months, android.R.layout.simple_spinner_item);
        adapterMonthlyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonthlyInterval.setAdapter(adapterMonthlyInterval);

        MultiSelectSpinner spWeekDay = findViewById(R.id.sp_which_days);
        spWeekDay.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_WHICH_DAYS);
        whichDays.add(getString(R.string.monday));
        whichDays.add(getString(R.string.tuesday));
        whichDays.add(getString(R.string.wednesday));
        whichDays.add(getString(R.string.thursday));
        whichDays.add(getString(R.string.friday));
        whichDays.add(getString(R.string.saturday));
        whichDays.add(getString(R.string.sunday));
        spWeekDay.setItems(whichDays, getString(R.string.which_day), this);

        etEndDate = findViewById(R.id.et_end_date);
        etWhichDate = findViewById(R.id.et_which_date);

        setListener();
    }

    private void setListener() {
        ivBackIcon.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        spRepeat.setOnItemSelectedListener(this);
        etEndDate.setOnClickListener(this);
        etWhichDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.toolbar_edit_action:
                //Submit attendance
                submitRepeatValues();
                break;
            case R.id.et_end_date:
                Util.showDateDialog(this, findViewById(R.id.et_end_date));
                break;
            case R.id.et_which_date:
                showDateDialog(this, findViewById(R.id.et_which_date));
                break;

        }

    }

    private void submitRepeatValues() {

        Recurrence recurrence = new Recurrence();
        recurrence.setType(spRepeat.getSelectedItem().toString());
        String[] str = getResources().getStringArray(R.array.repeat_array);

        if (selectedRepeat.equalsIgnoreCase(str[1])) {
            //ly_day_interval
            recurrence.setInterval(spDailyInterval.getSelectedItem().toString());
            recurrence.setLastDate(etEndDate.getText().toString());
        } else if (selectedRepeat.equalsIgnoreCase(str[2])) {
            //ly_week_interval
            recurrence.setInterval(spWeeklyInterval.getSelectedItem().toString());
            recurrence.setDayOfWeek(whichDaysSelected.toString());
            recurrence.setLastDate(etEndDate.getText().toString());
        } else if (selectedRepeat.equalsIgnoreCase(str[3])) {
            //ly_month_interval
            recurrence.setInterval(spMonthlyInterval.getSelectedItem().toString());
            recurrence.setDayOfMonth(etWhichDate.getText().toString());
            recurrence.setLastDate(etEndDate.getText().toString());
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", recurrence);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    //singleSelection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] str = getResources().getStringArray(R.array.repeat_array);
        switch (parent.getId()) {
            case R.id.sp_repeat:
                this.selectedRepeat = spRepeat.getSelectedItem().toString();
                if (selectedRepeat.equalsIgnoreCase(str[1])) {
                    findViewById(R.id.tly_end_date).setVisibility(View.VISIBLE);
                    findViewById(R.id.ly_day_interval).setVisibility(View.VISIBLE);
                    findViewById(R.id.ly_week_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_month_interval).setVisibility(View.GONE);
                } else if (selectedRepeat.equalsIgnoreCase(str[2])) {
                    findViewById(R.id.tly_end_date).setVisibility(View.VISIBLE);
                    findViewById(R.id.ly_day_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_week_interval).setVisibility(View.VISIBLE);
                    findViewById(R.id.ly_month_interval).setVisibility(View.GONE);
                } else if (selectedRepeat.equalsIgnoreCase(str[3])) {
                    findViewById(R.id.tly_end_date).setVisibility(View.VISIBLE);
                    findViewById(R.id.ly_day_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_week_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_month_interval).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.tly_end_date).setVisibility(View.GONE);
                    findViewById(R.id.ly_day_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_week_interval).setVisibility(View.GONE);
                    findViewById(R.id.ly_month_interval).setVisibility(View.GONE);
                }
                break;
        }
    }

    //MultiSelect
    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        if (getResources().getString(R.string.which_day).equals(spinnerName)) {
            whichDaysSelected.clear();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    whichDaysSelected.add(whichDays.get(i));
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(),
                    "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

}
