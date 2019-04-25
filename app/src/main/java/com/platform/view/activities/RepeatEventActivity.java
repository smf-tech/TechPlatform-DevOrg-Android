package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

public class RepeatEventActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,MultiSelectSpinner.MultiSpinnerListener {

    private ImageView ivBackIcon;
    private ImageView toolbarAction;

    private RelativeLayout lyDayInterval;
    private RelativeLayout lyWeekInterval;
    private RelativeLayout lyMonthInterval;
    private RelativeLayout lyWhichDays;
    private TextInputLayout tlyWhichDate;
    private TextInputLayout tlyEndDate;

    private Spinner spRepeat;
    private Spinner spDailyInterval;
    private Spinner spWeeklyInterval;
    private Spinner spMonthlyInterval;
    private MultiSelectSpinner spDistrict;

    private EditText etEndDate;

    List<String> whichDays = new ArrayList<>();
    List<String> whichDaysSelected = new ArrayList<>();

    String selectedRepeat;

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

        lyDayInterval = findViewById(R.id.ly_day_interval);
        lyWeekInterval = findViewById(R.id.ly_week_interval);
        lyMonthInterval = findViewById(R.id.ly_month_interval);
        lyWhichDays = findViewById(R.id.ly_which_days);
        tlyWhichDate = findViewById(R.id.tly_which_date);
        tlyEndDate = findViewById(R.id.tly_end_date);

        spRepeat = findViewById(R.id.sp_repeat);
        ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter.createFromResource(this,
                R.array.repeat_array,android.R.layout.simple_spinner_item);
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeat.setAdapter(adapterRepeat);

        spDailyInterval = findViewById(R.id.sp_day_interval);
        ArrayAdapter<CharSequence> adapterDailyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_weeks,android.R.layout.simple_spinner_item);
        adapterDailyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDailyInterval.setAdapter(adapterDailyInterval);

        spWeeklyInterval = findViewById(R.id.sp_week_interval);
        ArrayAdapter<CharSequence> adapterWeeklyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_weeks,android.R.layout.simple_spinner_item);
        adapterWeeklyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeeklyInterval.setAdapter(adapterWeeklyInterval);

        spMonthlyInterval = findViewById(R.id.sp_month_interval);
        ArrayAdapter<CharSequence> adapterMonthlyInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_months,android.R.layout.simple_spinner_item);
        adapterMonthlyInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonthlyInterval.setAdapter(adapterMonthlyInterval);

        spDistrict = findViewById(R.id.sp_which_days);
        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_WHICH_DAYS);
        whichDays.add("Sunday");
        whichDays.add("Monday");
        whichDays.add("Tuesday");
        whichDays.add("Wednesday");
        whichDays.add("Thursday");
        whichDays.add("Friday");
        whichDays.add("Saturday");
        spDistrict.setItems(whichDays, "Monday", this);

        etEndDate = findViewById(R.id.et_end_date);

        setListener();
    }

    private void setListener() {
        ivBackIcon.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        toolbarAction.setOnClickListener(this);
        spRepeat.setOnItemSelectedListener(this);
        etEndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.toolbar_edit_action:
                //Submit attendance
                break;
            case R.id.et_end_date:
                Util.showDateDialog(this, findViewById(R.id.et_end_date));
                break;
        }

    }

    private void setActionbar(String string) {
    }

    //singleSelection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_repeat:
                this.selectedRepeat = spRepeat.getSelectedItem().toString();
                if(selectedRepeat.equalsIgnoreCase("Daily")){
                    tlyEndDate.setVisibility(View.VISIBLE);
                    lyDayInterval.setVisibility(View.VISIBLE);
                    lyWeekInterval.setVisibility(View.GONE);
                    lyMonthInterval.setVisibility(View.GONE);
                    lyWhichDays.setVisibility(View.GONE);
                    tlyWhichDate.setVisibility(View.GONE);
                } else  if(selectedRepeat.equalsIgnoreCase("Weekly")){
                    tlyEndDate.setVisibility(View.VISIBLE);
                    lyDayInterval.setVisibility(View.GONE);
                    lyWeekInterval.setVisibility(View.VISIBLE);
                    lyMonthInterval.setVisibility(View.GONE);
                    lyWhichDays.setVisibility(View.VISIBLE);
                    tlyWhichDate.setVisibility(View.GONE);
                } else  if(selectedRepeat.equalsIgnoreCase("Monthly")){
                    tlyEndDate.setVisibility(View.VISIBLE);
                    lyDayInterval.setVisibility(View.GONE);
                    lyWeekInterval.setVisibility(View.GONE);
                    lyMonthInterval.setVisibility(View.VISIBLE);
                    lyWhichDays.setVisibility(View.GONE);
                    tlyWhichDate.setVisibility(View.VISIBLE);
                } else {
                    tlyEndDate.setVisibility(View.GONE);
                    lyDayInterval.setVisibility(View.GONE);
                    lyWeekInterval.setVisibility(View.GONE);
                    lyMonthInterval.setVisibility(View.GONE);
                    lyWhichDays.setVisibility(View.GONE);
                    tlyWhichDate.setVisibility(View.GONE);
                }
                break;
        }
    }

    //MultiSelect
    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        switch (spinnerName) {
            case Constants.MultiSelectSpinnerType.SPINNER_WHICH_DAYS:
                whichDaysSelected.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        whichDaysSelected.add(whichDays.get(i));
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
