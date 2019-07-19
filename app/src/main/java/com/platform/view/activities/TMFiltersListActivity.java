package com.platform.view.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.models.tm.FilterlistDataResponse;
import com.platform.models.tm.SubFilterset;
import com.platform.presenter.TMFilterListActivityPresenter;
import com.platform.utility.Util;
import com.platform.view.adapters.SmartFragmentStatePagerAdapter;
import com.platform.view.fragments.TMUserAprovedFragment;
import com.platform.view.fragments.TMUserPendingFragment;
import com.platform.view.fragments.TMUserRejectedFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

@SuppressWarnings("CanBeFinal")
public class TMFiltersListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private TabLayout tabLayout;
    private ImageView img_filter_image;
    ApprovalsViewPagerAdapter approvalsViewPagerAdapter;
    ViewPager viewPager;
    private TMUserAprovedFragment tmUserAprovedFragment;
    private TMUserRejectedFragment tmUserRejectedFragment;
    private TMUserPendingFragment  tmUserPendingFragment;
    private OnFilterSelected clickListener;
    private JSONObject jsonObjectFilterRequest;
    TMFilterListActivityPresenter tmFilterListActivityPresenter;
    private ArrayList<SubFilterset> subFiltersets = new ArrayList<>();
    ArrayList<FilterlistDataResponse> filterlistDataResponses;
    private final int[] tabIcons = {
            R.drawable.selector_pending_tab,
            R.drawable.selector_approved_tab,
            R.drawable.selector_rejected_tab
    };
    private String[] tabNames;
    private String[] mainFilterType;
    private ArrayAdapter<String> adapter;
    Spinner spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmfilter_layout);
        tabNames = new String[]{
                getString(R.string.cat_pending),
                getString(R.string.cat_approved),
                getString(R.string.cat_rejected)};
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
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
        img_filter_image = findViewById(R.id.img_filter_image);
        img_filter_image.setOnClickListener(this);
        spin = (Spinner) findViewById(R.id.spinner1);

         tmFilterListActivityPresenter = new TMFilterListActivityPresenter(this);
        tmFilterListActivityPresenter.getAllFiltersRequests();


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
                CustomDialogClass cdd=new CustomDialogClass(TMFiltersListActivity.this);
                cdd.show();
                //onBackPressed();
                break;
            case R.id.img_filter_image:
                showFilterDialog();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        subFiltersets.clear();
        for (int i = 0; i <filterlistDataResponses.get(position).getFilterSet().size() ; i++) {
            if(filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset()!=null){
                //subFiltersets = (ArrayList<SubFilterset>) filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset();
                subFiltersets.addAll(filterlistDataResponses.get(position).getFilterSet().get(i).getFilterset());
            }
        }
        /*CustomDialogClass cdd=new CustomDialogClass(TMFiltersListActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();*/
        //showFilterDialog();
        defaultFilterRequest();
  //      (ArrayList<SubFilterset>) filterlistDataResponses.get(position).getFilterSet().get();
//        subFiltersets = (ArrayList<SubFilterset>) filterlistDataResponses.get(position).getFilterSet().get(1).getFilterset();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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


    /*private void setActionbar(String title) {
        if (title.contains("\n")) {
            title = title.replace("\n", " ");
        }

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }
*/

    public void showPendingApprovalRequests(List<FilterlistDataResponse> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            filterlistDataResponses = (ArrayList<FilterlistDataResponse>) pendingRequestList;
           // subFiltersets = (ArrayList<SubFilterset>) filterlistDataResponses.get(1).getFilterSet().get(1).getFilterset();
            mainFilterType =new String[pendingRequestList.size()];
            for (int i = 0; i <pendingRequestList.size() ; i++) {
                mainFilterType[i]  =pendingRequestList.get(i).getType();

            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainFilterType);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        /*    DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList.clear();
            this.pendingRequestList.addAll(pendingRequestList);



            *//*mAdapter = new TMPendingApprovalPageRecyclerAdapter(getActivity(), pendingRequestList,
                    pendingFragmentPresenter, this);*//*
            mAdapter = new TMPendingApprovalPageRecyclerAdapter(getActivity(), pendingRequestList,
                    this);
            rvPendingRequests.setAdapter(mAdapter);*/
        } else {
        /*    DashboardFragment.setApprovalCount(0);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
            rvPendingRequests.setVisibility(View.GONE);
        }

        if (getParentFragment() != null && getParentFragment() instanceof DashboardFragment) {
            ((DashboardFragment) getParentFragment()).updateBadgeCount();
        }*/
        }
    }

    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {
        private int mYear, mMonth, mDay, mHour, mMinute;
        public Activity activity;
        public RecyclerView rv_filterchoice;
        public Dialog d;
        public Button yes, no;
        public TextView tv_startdate,tv_enddate;
        public ArrayList<String> filterChoiceList = new ArrayList<>();
        FilterChoicedapter adapter;
        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.activity = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_filter);

            tv_startdate = findViewById(R.id.tv_startdate);
            tv_enddate= findViewById(R.id.tv_enddate);
            rv_filterchoice = findViewById(R.id.rv_filterchoice);
            yes = (Button) findViewById(R.id.btn_yes);
            no = (Button) findViewById(R.id.btn_no);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            rv_filterchoice.setLayoutManager(layoutManager);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
            tv_startdate.setOnClickListener(this);
            tv_enddate.setOnClickListener(this);
            tv_startdate.setText(Util.getCurrentDate());
            tv_enddate.setText(Util.getCurrentDate());
            adapter = new FilterChoicedapter(TMFiltersListActivity.this, subFiltersets);
            rv_filterchoice.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
             switch (v.getId()) {
                case R.id.btn_yes:
                    //activity.finish();
                    //jsonObjectFilterRequest = createBodyParams(tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets);
                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"pending");
                    //clickListener.onFilterButtonClicked();

                    //OLD implementation
                    /*Fragment fragment = (Fragment) approvalsViewPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                    if(fragment instanceof TMUserPendingFragment){
                        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"pending");
                        ((TMUserPendingFragment) fragment).onFilterButtonClicked(jsonObjectFilterRequest);
                    }

                    if(fragment instanceof TMUserRejectedFragment){
                        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"rejected");
                        ((TMUserRejectedFragment) fragment).onFilterButtonClicked(jsonObjectFilterRequest);
                    }

                    if(fragment instanceof TMUserAprovedFragment){
                        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"approved");
                        ((TMUserAprovedFragment) fragment).onFilterButtonClicked(jsonObjectFilterRequest);
                    }*/
                    // new implementation as per discussion with kishor
                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"pending");
                    tmUserPendingFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"approved");
                    tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                    jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets,"rejected");
                    tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

                    dismiss();
                    break;
                case R.id.btn_no:
                    dismiss();
                    break;
                 case R.id.tv_startdate:
                     selectStartDate(tv_startdate);
                     break;
                 case R.id.tv_enddate:
                     selectStartDate(tv_enddate);
                     break;
                default:
                    break;
            }
            //dismiss();
        }

        private JSONObject createBodyParams(String startDate,String endDate,ArrayList<SubFilterset> subFiltersets) {
            JSONObject requestObject = new JSONObject();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson("");
            Log.d("JsonObjRequestfilter", "SubmitRequest: " + json);

            try {
                requestObject.put("type",spin.getSelectedItem().toString());
                requestObject.put("approval_type","pending");
                requestObject.put("filterSet",getFilterObject(subFiltersets));  // new JSONArray().put(getFilterObject()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                return requestObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private JSONObject getFilterObject(ArrayList<SubFilterset> subFiltersets) {
            JSONObject requestObject =new JSONObject();

            try {

                requestObject.put("start_date","1558959956");
                requestObject.put("end_date","1558960046");
                if (subFiltersets!=null) {
                    if (subFiltersets.size()>0) {
                        requestObject.put("filterType", "category");
                        requestObject.put("id", getidObject());  // "5c6bbf3dd503a3057867cf24");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return requestObject;
        }

        private JSONArray getidObject() {
            JSONArray requestObject =new JSONArray();

            try {

                requestObject.put("5c6bbf3dd503a3057867cf24");
                requestObject.put("5c6bbf07d503a30a5e724eab");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return requestObject;
        }


        public class FilterChoicedapter extends RecyclerView.Adapter<FilterChoicedapter.EmployeeViewHolder> {

            private ArrayList<SubFilterset> dataList;
            private Context mContext;
            //private OnRequestItemClicked clickListener;

            public FilterChoicedapter(Context context, ArrayList<SubFilterset> dataList){//},final OnRequestItemClicked clickListener) {
                mContext = context;
                this.dataList = dataList;
                //this.clickListener =clickListener;
            }

            @Override
            public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.row_filterdialog_item, parent, false);
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
                    txtTitle = (TextView) itemView.findViewById(R.id.tv_filters);
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
    }
    public void setFilterClickListener(OnFilterSelected listener) {
        clickListener = listener;
    }
    public interface OnFilterSelected {
        void onFilterButtonClicked(JSONObject requestobject);
    }
private void showFilterDialog(){
    CustomDialogClass cdd=new CustomDialogClass(TMFiltersListActivity.this);
    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    cdd.show();
}
    private void defaultFilterRequest(){
        //activity.finish();
        //jsonObjectFilterRequest = createBodyParams(tv_startdate.getText().toString(),tv_enddate.getText().toString(),subFiltersets);
        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"pending");
        //clickListener.onFilterButtonClicked();

        /*Fragment fragment = (Fragment) approvalsViewPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if(fragment instanceof TMUserPendingFragment){
            jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"pending");
            ((TMUserPendingFragment) fragment).onFilterButtonClicked(jsonObjectFilterRequest);
        }
        if(fragment instanceof TMUserRejectedFragment){
            jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"approved");
            ((TMUserRejectedFragment) fragment).onFilterButtonClicked(jsonObjectFilterRequest);
        }*/


        //new implemented
        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"pending");
        tmUserPendingFragment.onFilterButtonClicked(jsonObjectFilterRequest);

        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"approved");
        tmUserAprovedFragment.onFilterButtonClicked(jsonObjectFilterRequest);

        jsonObjectFilterRequest = tmFilterListActivityPresenter.createBodyParams(spin.getSelectedItem().toString(),Util.getCurrentDate(),Util.getCurrentDate(),subFiltersets,"rejected");
        tmUserRejectedFragment.onFilterButtonClicked(jsonObjectFilterRequest);
    }
}