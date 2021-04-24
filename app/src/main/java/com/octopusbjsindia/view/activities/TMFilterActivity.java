package com.octopusbjsindia.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.tm.FilterlistDataResponse;
import com.octopusbjsindia.models.tm.SubFilterset;
import com.octopusbjsindia.presenter.TMFilterActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class TMFilterActivity extends AppCompatActivity implements View.OnClickListener, CustomSpinnerListener{

    private int mYear, mMonth, mDay, mHour, mMinute;
    public Activity activity;
    public TextView toolbarTitle;
    public ImageView img_close;
    private RelativeLayout progressBar;
    public RecyclerView rv_filterchoice;
    public Dialog d;
    public Button yes, no;
    public EditText tv_startdate,tv_enddate,etName,etState;
    public ArrayList<String> filterChoiceList = new ArrayList<>();
    FilterChoicedapter adapter;
    private ArrayList<SubFilterset> subFiltersets = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
//    private Spinner spin;
    TMFilterActivityPresenter presenter;
    private ArrayList<FilterlistDataResponse> filterlistDataResponses;

    private String[] mainFilterType;
    private String filterTypeReceived = "",selectedStateId = "", selectedState = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_m_filter);

        progressBar = findViewById(R.id.ly_progress_bar);

        filterlistDataResponses = (ArrayList<FilterlistDataResponse>)getIntent().getSerializableExtra("filterlistDataResponses");
        filterTypeReceived = getIntent().getStringExtra("filter_type");
        selectedState = getIntent().getStringExtra("state");
        selectedStateId = getIntent().getStringExtra("stateId");
        subFiltersets =(ArrayList<SubFilterset>) getIntent().getSerializableExtra("subFiltersets");
        presenter = new TMFilterActivityPresenter(this);

        presenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);

        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate= findViewById(R.id.tv_enddate);
        etName= findViewById(R.id.etName);
        etState= findViewById(R.id.etState);
        rv_filterchoice = findViewById(R.id.rv_filterchoice);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        img_close =findViewById(R.id.toolbar_edit_action);
        toolbarTitle =findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Select Filters");
//        spin = findViewById(R.id.spinner1);
        etName.setText(getIntent().getStringExtra("name"));
        etState.setText(selectedState);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        img_close.setOnClickListener(this);
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setText(Util.getCurrentDatePreviousMonth());
        tv_enddate.setText(Util.getCurrentDate());
        etState.setOnClickListener(this);
//        spin.setOnItemSelectedListener(this);
        adapter = new FilterChoicedapter(TMFilterActivity.this, subFiltersets);
        rv_filterchoice.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result",getSelectedSpinnerItem(spin.getSelectedItemPosition()));
                returnIntent.putExtra("startdate",tv_startdate.getText().toString());
                returnIntent.putExtra("enddate",tv_enddate.getText().toString());
                returnIntent.putExtra("subFulterset",subFiltersets);
                returnIntent.putExtra("name",etName.getText().toString().trim());
                returnIntent.putExtra("stateId",selectedStateId);
                returnIntent.putExtra("state",etState.getText().toString().trim());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

                // new implementation as per discussion with kishor

//
//                jsonObjectFilterRequest = presenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"approved");
//                tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);
//
//                jsonObjectFilterRequest = presenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"rejected");
//                tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);
//
//                dismiss();
                break;
            case R.id.toolbar_edit_action:
                finish();
                break;
            case R.id.tv_startdate:
                //selectStartDate(tv_startdate);
                selectStartDate(tv_startdate, 1);
                break;
            case R.id.tv_enddate:
                //selectStartDate(tv_enddate);
                selectStartDate(tv_enddate, 2);
                break;
            case R.id.etState:
                CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                        "Select State", stateList, false);
                csdState.show();
                csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        for (CustomSpinnerObject obj : stateList) {
            if (obj.isSelected()) {
                etState.setText(obj.getName());
                selectedStateId = obj.get_id();
                break;
            }
        }
    }

    public String getSelectedSpinnerItem(int position)
    {
        String selectedfiltertype = mainFilterType[position];
        return selectedfiltertype;
    }
    private void selectStartDate(TextView textview, int flagDateStartEnd) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                        // textview.setText(selectedDateString);
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        //check for Date-->
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR);//new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        Date startDate = null;
                        Date endDate = null;

                        if (flagDateStartEnd == 1) {
                            try {
                                startDate = formatter.parse(selectedDateString);
                                endDate = formatter.parse(tv_enddate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (startDate.getTime() > endDate.getTime()) {
                                Toast.makeText(TMFilterActivity.this, "Start date should be less than end date.", Toast.LENGTH_LONG).show();
                            } else {
                                textview.setText(selectedDateString);
                            }
                        } else {
                            try {
                                startDate = formatter.parse(tv_startdate.getText().toString());
                                endDate = formatter.parse(selectedDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (startDate.getTime() > endDate.getTime()) {
                                Toast.makeText(TMFilterActivity.this, "End date should be greater than start date.", Toast.LENGTH_LONG).show();
                            } else {
                                textview.setText(selectedDateString);
                            }
                        }

                        //-----
                    }
                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> data, String stateLevel) {
        if (data != null && !data.isEmpty()) {
            stateList.clear();
            //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

            for (int i = 0; i < data.size(); i++) {
                //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                JurisdictionLocationV3 location = data.get(i);
                CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                meetCountry.set_id(location.getId());
                meetCountry.setName(location.getName());
                meetCountry.setSelected(false);
                stateList.add(meetCountry);
                //}
            }
        }
    }

    public class FilterChoicedapter extends RecyclerView.Adapter<FilterChoicedapter.EmployeeViewHolder> {

        private ArrayList<SubFilterset> dataList;
        private Context mContext;
        //private OnRequestItemClicked clickListener;

        FilterChoicedapter(Context context, ArrayList<SubFilterset> dataList){//},final OnRequestItemClicked clickListener) {
            mContext = context;
            this.dataList = dataList;
            //this.clickListener =clickListener;
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.approval_row_filterdialog_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getName().getDefault());
            holder.cb_select_filter.setChecked(dataList.get(position).isSelected());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle, txtValue;
            CheckBox cb_select_filter;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                this.setIsRecyclable(false);
                txtTitle = itemView.findViewById(R.id.tv_filters);
                cb_select_filter = itemView.findViewById(R.id.cb_select_filter);

                cb_select_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataList.get(getAdapterPosition()).setSelected(isChecked);
                    }
                });
                //txtValue = (TextView) itemView.findViewById(R.id.tv_value);
                // itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            }
        }

    }


}