package com.platform.view.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.platform.Platform;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.Calendar;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
    }

    private void initViews() {
        setActionbar(getString(R.string.registration_title));
        findViewById(R.id.et_user_birth_date).setOnClickListener(this);
        findViewById(R.id.btn_profile_submit).setOnClickListener(this);

        if (Platform.getInstance().getAppMode().equals(Constants.App.BJS_MODE)) {
            findViewById(R.id.user_geo_location_view).setVisibility(View.GONE);
            findViewById(R.id.input_user_address).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_user_birth_date:
                showDateDialog(RegistrationActivity.this, findViewById(R.id.et_user_birth_date));
                break;

            case R.id.btn_profile_submit:
                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }
}
