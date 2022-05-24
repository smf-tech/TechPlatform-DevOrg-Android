package com.octopusbjsindia.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.tm.FilterlistDataResponse;
import com.octopusbjsindia.models.tm.SubFilterset;
import com.octopusbjsindia.presenter.TMFilterListActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SmartFragmentStatePagerAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.view.fragments.TMUserAprovedFragment;
import com.octopusbjsindia.view.fragments.TMUserPendingFragment;
import com.octopusbjsindia.view.fragments.TMUserRejectedFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

@SuppressWarnings("CanBeFinal")
public class TMFiltersListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CustomSpinnerListener {

    private String selectedStartDate, getSelectedEndDate;
    private TabLayout tabLayout;
    private String filterTypeReceived = "",filteNname = "", filterStateId = "", selectedState="";
    private ApprovalsViewPagerAdapter approvalsViewPagerAdapter;
    private ViewPager viewPager;
    private TMUserAprovedFragment tmUserAprovedFragment;
    private TMUserRejectedFragment tmUserRejectedFragment;
    private TMUserPendingFragment tmUserPendingFragment;
    private OnFilterSelected clickListener;
    private JSONObject jsonObjectFilterRequest;
    private TMFilterListActivityPresenter tmFilterListActivityPresenter;
    private ArrayList<SubFilterset> subFiltersets = new ArrayList<>();
    private ArrayList<FilterlistDataResponse> filterlistDataResponses;
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    EditText etState;
    private final int[] tabIcons = {
            R.drawable.selector_pending_tab,
            R.drawable.selector_approved_tab,
            R.drawable.selector_rejected_tab
    };
    private String[] tabNames;
    private String[] mainFilterType;
    private String[] mainFilterTypeDisplayname;
    private ArrayAdapter<String> adapter;
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmfilter_layout);
        tabNames = new String[]{
                getString(R.string.cat_pending),
                getString(R.string.cat_approved),
                getString(R.string.cat_rejected)};
        //receive intent data
        filterTypeReceived = getIntent().getStringExtra("filter_type");

        CustomSpinnerObject o1 = new CustomSpinnerObject();
        o1.set_id("1");
        o1.setName("maha");
        stateList.add(o1);
        CustomSpinnerObject o2 = new CustomSpinnerObject();
        o2.set_id("1");
        o2.setName("maha");
        stateList.add(o2);

        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        tmFilterListActivityPresenter.getAllFiltersRequests();
    }

    private void initViews() {
        viewPager = findViewById(R.id.approval_cat_view_pager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);


        tabLayout = findViewById(R.id.approval_cat_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        TextView tv_filtertype = findViewById(R.id.tv_filtertype);
        tv_filtertype.setOnClickListener(this);
        ImageView img_filter_image = findViewById(R.id.img_filter_image);
        img_filter_image.setOnClickListener(this);
        spin = findViewById(R.id.spinner1);

        tmFilterListActivityPresenter = new TMFilterListActivityPresenter(this);

//        tmFilterListActivityPresenter.getLocationData("",
//                Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                Constants.JurisdictionLevelName.STATE_LEVEL);

        spin.setOnItemSelectedListener(this);


    }

    private void setupViewPager(ViewPager viewPager) {
        approvalsViewPagerAdapter = new ApprovalsViewPagerAdapter(getSupportFragmentManager());

        tmUserPendingFragment = new TMUserPendingFragment();
        Bundle b = new Bundle();
        b.putBoolean("SHOW_ALL", false);
        tmUserPendingFragment.setArguments(b);
        approvalsViewPagerAdapter.addFragment(tmUserPendingFragment);

        //adapter.addFragment(new TMUserApprovedFragment());
        tmUserAprovedFragment = new TMUserAprovedFragment();
        approvalsViewPagerAdapter.addFragment(tmUserAprovedFragment);
        viewPager.setAdapter(approvalsViewPagerAdapter);
        tmUserRejectedFragment = new TMUserRejectedFragment();
        approvalsViewPagerAdapter.addFragment(tmUserRejectedFragment);
        viewPager.setAdapter(approvalsViewPagerAdapter);

    }

    private void setupTabIcons() {
        for (int i = 0; i < tabNames.length; i++) {
            TextView tabOne = (TextView) LayoutInflater.from(TMFiltersListActivity.this)
                    .inflate(R.layout.layout_approval_tab, tabLayout, false);
            tabOne.setText(tabNames[i]);
            //  tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabOne);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filtertype:
                CustomDialogClass cdd = new CustomDialogClass(TMFiltersListActivity.this, "Select date range");
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                //onBackPressed();
                break;
            case R.id.img_filter_image:
//                showFilterDialog();
                Intent intent = new Intent(this, TMFilterActivity.class);
                intent.putExtra("filterlistDataResponses",filterlistDataResponses);
                intent.putExtra("filter_type",filterTypeReceived);
                intent.putExtra("subFiltersets",subFiltersets);
                intent.putExtra("name",filteNname);
                intent.putExtra("stateId",subFiltersets);
                intent.putExtra("state",selectedState);

                startActivityForResult(intent, 1001);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        subFiltersets.clear();
        for (int i = 0; i < filterlistDataResponses.get(position).getFilterSet().size(); i++) {
            if (filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset() != null) {
                //subFiltersets = (ArrayList<SubFilterset>) filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset();
                subFiltersets.addAll(filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset());
            }
        }
        filterTypeReceived = getSelectedSpinnerItem(spin.getSelectedItemPosition());

        defaultFilterRequest();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        for (CustomSpinnerObject obj : stateList) {
            if (obj.isSelected()) {
                etState.setText(obj.getName());
//                selectedState = obj.getName();
//                selectedStateId = obj.get_id();
//                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if(resultCode == Activity.RESULT_OK){

                String startdate=data.getStringExtra("startdate");
                String enddate=data.getStringExtra("enddate");
                filteNname=data.getStringExtra("name");
                filterStateId =data.getStringExtra("stateId");
                selectedState =data.getStringExtra("state");
                ArrayList<SubFilterset> subFulterset=(ArrayList<SubFilterset>)data.getSerializableExtra("subFulterset");

                jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                        getSelectedSpinnerItem(spin.getSelectedItemPosition()),
                        startdate,
                        enddate,
                        subFulterset,
                        filteNname,
                        filterStateId,
                        "pending");
                tmUserPendingFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                        getSelectedSpinnerItem(spin.getSelectedItemPosition()),
                        startdate,
                        enddate,
                        subFulterset,
                        filteNname,
                        filterStateId,
                        "approved");
                tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                        getSelectedSpinnerItem(spin.getSelectedItemPosition()),
                        startdate,
                        enddate,
                        subFulterset,
                        filteNname,
                        filterStateId,
                        "rejected");
                tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

            }
        }
    }//onActivityResult

    class ApprovalsViewPagerAdapter extends SmartFragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        ApprovalsViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    public void showPendingApprovalRequests(List<FilterlistDataResponse> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            filterlistDataResponses = (ArrayList<FilterlistDataResponse>) pendingRequestList;
            // subFiltersets = (ArrayList<SubFilterset>) filterlistDataResponses.get(1).getFilterSet().get(1).getFilterset();
            mainFilterType = new String[pendingRequestList.size()];
            mainFilterTypeDisplayname = new String[pendingRequestList.size()];
            for (int i = 0; i < pendingRequestList.size(); i++) {
                mainFilterType[i] = pendingRequestList.get(i).getType();
                mainFilterTypeDisplayname[i] = pendingRequestList.get(i).getApprovalType();
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainFilterTypeDisplayname);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            for (int i = 0; i < mainFilterType.length; i++) {
                if (filterTypeReceived.equalsIgnoreCase(mainFilterType[i])) {
                    spin.setSelection(i);
                }
            }

            adapter.notifyDataSetChanged();

        }
    }

    public class CustomDialogClass extends BottomSheetDialog implements
            android.view.View.OnClickListener {
        private int mYear, mMonth, mDay, mHour, mMinute;
        public Activity activity;
        public LinearLayout linear_dynamic_filterheight;
        public TextView toolbarTitle;
        public ImageView img_close;
        public String bottomSheetTitle;
        public RecyclerView rv_filterchoice;
        public Dialog d;
        public Button yes, no;
        public EditText tv_startdate, tv_enddate, etName;
        public ArrayList<String> filterChoiceList = new ArrayList<>();
        FilterChoicedapter adapter;

        public CustomDialogClass(Activity a, String formTitle) {
            super(a);
            // TODO Auto-generated constructor stub
            this.activity = a;
            bottomSheetTitle = formTitle;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //setContentView(R.layout.custom_dialog_filter);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.custom_dialog_filter, null);
            setContentView(bottomSheetView);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            //bottomSheetBehavior.setPeekHeight(500);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            linear_dynamic_filterheight = findViewById(R.id.linear_dynamic_filterheight);
            if (subFiltersets != null) {
                if (subFiltersets.size() > 0) {
                    float pixels = 1 * activity.getResources().getDisplayMetrics().density;

                    CoordinatorLayout.LayoutParams param = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (500 * pixels));
                    linear_dynamic_filterheight.setLayoutParams(param);
                }
            }
            tv_startdate = findViewById(R.id.tv_startdate);
            tv_enddate = findViewById(R.id.tv_enddate);
            etName = findViewById(R.id.etName);
            etState = findViewById(R.id.etState);
            rv_filterchoice = findViewById(R.id.rv_filterchoice);
            yes = findViewById(R.id.btn_yes);
            no = findViewById(R.id.btn_no);
            img_close = findViewById(R.id.toolbar_edit_action);
            toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText(bottomSheetTitle);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rv_filterchoice.setLayoutManager(layoutManager);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
            img_close.setOnClickListener(this);
            tv_startdate.setOnClickListener(this);
            tv_enddate.setOnClickListener(this);
            tv_startdate.setText(Util.getCurrentDatePreviousMonth());
            tv_enddate.setText(Util.getCurrentDate());
            etState.setOnClickListener(this);
            adapter = new FilterChoicedapter(TMFiltersListActivity.this, subFiltersets);
            rv_filterchoice.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes:
//                    //activity.finish();
//                    //jsonObjectFilterRequest = createBodyParams(tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets);
//                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()), tv_startdate.getText().toString(), tv_enddate.getText().toString(), subFiltersets, "pending");
//                    //clickListener.onFilterButtonClicked();
//
//                    // new implementation as per discussion with kishor
//                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()), tv_startdate.getText().toString(), tv_enddate.getText().toString(), subFiltersets, "pending");
//                    tmUserPendingFragment.onFilterButtonClicked(jsonObjectFilterRequest);
//
//                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()), tv_startdate.getText().toString(), tv_enddate.getText().toString(), subFiltersets, "approved");
//                    tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);
//
//                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(getSelectedSpinnerItem(spin.getSelectedItemPosition()), tv_startdate.getText().toString(), tv_enddate.getText().toString(), subFiltersets, "rejected");
//                    tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                    dismiss();
                    break;
                case R.id.toolbar_edit_action:
                    dismiss();
                    break;
                case R.id.tv_startdate:
                    //selectStartDate(tv_startdate);
                    selectStartDate(tv_startdate, 1);
                    break;
                case R.id.tv_enddate:
                    //selectStartDate(tv_enddate);
                    selectStartDate(tv_enddate, 2);
                    break;
                default:
                    break;
            }
            //dismiss();
        }


        public class FilterChoicedapter extends RecyclerView.Adapter<FilterChoicedapter.EmployeeViewHolder> {

            private ArrayList<SubFilterset> dataList;
            private Context mContext;
            //private OnRequestItemClicked clickListener;

            FilterChoicedapter(Context context, ArrayList<SubFilterset> dataList) {//},final OnRequestItemClicked clickListener) {
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
            public void onBindViewHolder(EmployeeViewHolder holder, int position) {
                holder.txtTitle.setText(dataList.get(position).getName().getDefault());
                holder.cb_select_filter.setChecked(dataList.get(position).isSelected());
                // holder.txtValue.setText(dataList.get(position));

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

        //select start date and end date for filter
        private void selectStartDate(TextView textview) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                            textview.setText(selectedDateString);
                            //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        //date validations
        private void selectStartDate(TextView textview, int flagDateStartEnd) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(TMFiltersListActivity.this,
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
                                    Toast.makeText(TMFiltersListActivity.this, "Start date should be less than end date.", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(TMFiltersListActivity.this, "End date should be greater than start date.", Toast.LENGTH_LONG).show();
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


    }

    public void setFilterClickListener(OnFilterSelected listener) {
        clickListener = listener;
    }

    public interface OnFilterSelected {
        void onFilterButtonClicked(JSONObject requestobject);
    }


    private void defaultFilterRequest() {
       //new implemented
        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                getSelectedSpinnerItem(spin.getSelectedItemPosition()), Util.getCurrentDatePreviousMonth(),
                Util.getCurrentDate(), subFiltersets,filteNname, filterStateId, "pending");
        tmUserPendingFragment.onFilterButtonClicked(jsonObjectFilterRequest);

        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                getSelectedSpinnerItem(spin.getSelectedItemPosition()), Util.getCurrentDatePreviousMonth(),
                Util.getCurrentDate(), subFiltersets, filteNname, filterStateId,"approved");
        tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(
                getSelectedSpinnerItem(spin.getSelectedItemPosition()), Util.getCurrentDatePreviousMonth(),
                Util.getCurrentDate(), subFiltersets, filteNname, filterStateId,"rejected");
        tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);
    }

    public String getSelectedSpinnerItem(int position) {
        String selectedfiltertype = mainFilterType[position];
        return selectedfiltertype;
    }
}
