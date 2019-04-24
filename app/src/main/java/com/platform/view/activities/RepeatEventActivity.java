package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

public class RepeatEventActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,MultiSelectSpinner.MultiSpinnerListener {

    private ImageView ivBackIcon;
    private ImageView toolbarAction;

    private Spinner spRepeat;
    private Spinner spInterval;
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
        setActionbar(getResources().getString(R.string.task_add_members));
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        toolbarAction = findViewById(R.id.toolbar_edit_action);
        toolbarAction.setVisibility(View.VISIBLE);
        toolbarAction.setImageResource(R.drawable.ic_down_arrow_light_blue);

        spRepeat = findViewById(R.id.sp_repeat);
        ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter.createFromResource(this,
                R.array.repeat_array,android.R.layout.simple_spinner_item);
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRepeat.setAdapter(adapterRepeat);

        spInterval = findViewById(R.id.sp_interval);
        ArrayAdapter<CharSequence> adapterInterval = ArrayAdapter.createFromResource(this,
                R.array.repeat_weeks,android.R.layout.simple_spinner_item);
        adapterInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInterval.setAdapter(adapterInterval);

        spDistrict = findViewById(R.id.sp_which_days);
        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_WHICH_DAYS);
        whichDays.add("Sunday");
        whichDays.add("Monday");
        whichDays.add("Tuesday");
        whichDays.add("Wednesday");
        whichDays.add("Thursday");
        whichDays.add("Friday");
        whichDays.add("Saturday");
        spDistrict.setItems(whichDays, getString(R.string.district), this);

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

    //singleSelection
   @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_repeat:
                this.selectedRepeat = spRepeat.getSelectedItem().toString();
                int i=1;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
