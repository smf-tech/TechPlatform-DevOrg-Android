package com.octopus.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.octopus.R;
import com.octopus.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectBottomSheet extends BottomSheetDialog implements
        android.view.View.OnClickListener {
    private MultiSpinnerListener listener;
    public Activity activity;
    private boolean[] selectedValues;
    private ArrayList<String> dataList;
    private String spinnerText;
    public TextView toolbarTitle;
    public ImageView img_close;
    public String bottomSheetTitle;
    public RecyclerView rv_filterchoice;
    public Dialog d;
    public Button yes, no;
    public TextView tv_startdate,tv_enddate;
    public ArrayList<String> filterChoiceList = new ArrayList<>();
    FilterChoicedapter adapter;
    public MultiSelectBottomSheet(Activity a, String formTitle, List<String> items,MultiSpinnerListener listener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        bottomSheetTitle = formTitle;
        filterChoiceList = (ArrayList<String>) items;
        this.listener =listener;
        // all de-selectedValues by default
        selectedValues = new boolean[items.size()];
        for (int i = 0; i < selectedValues.length; i++) {
            selectedValues[i] = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_multiselect_dialog_layout, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate= findViewById(R.id.tv_enddate);
        rv_filterchoice = findViewById(R.id.rv_filterchoice);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        img_close =findViewById(R.id.toolbar_edit_action);
        toolbarTitle =findViewById(R.id.toolbar_title);
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
        adapter = new FilterChoicedapter(activity, filterChoiceList);
        rv_filterchoice.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                setSelectedFilledText();
                listener.onValuesSelected(selectedValues, bottomSheetTitle,spinnerText);
                dismiss();
                break;
            case R.id.toolbar_edit_action:
                dismiss();
                break;
            case R.id.tv_startdate:
                //selectStartDate(tv_startdate);
                break;
            case R.id.tv_enddate:
              //  selectStartDate(tv_enddate);
                break;
            default:
                break;
        }

    }

    public void setSelectedValues(boolean[] selectedValues){
        this.selectedValues = selectedValues;
    }

    public interface MultiSpinnerListener {
        void onValuesSelected(boolean[] selected, String spinnerName,String selectedValues);
    }

    public class FilterChoicedapter extends RecyclerView.Adapter<FilterChoicedapter.EmployeeViewHolder> {


        private Context mContext;
        //private OnRequestItemClicked clickListener;

        public FilterChoicedapter(Context context, ArrayList<String> filterChoiceList){//},final OnRequestItemClicked clickListener) {
            mContext = context;
            dataList = filterChoiceList;
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
            holder.txtTitle.setText(dataList.get(position));
            holder.cb_select_filter.setChecked(selectedValues[position]);
            // holder.txtValue.setText(dataList.get(position));

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class EmployeeViewHolder extends RecyclerView.ViewHolder {

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
                        //dataList.get(getAdapterPosition()).setSelected(isChecked);
                        selectedValues[getAdapterPosition()] = isChecked;
                    }
                });
                //txtValue = (TextView) itemView.findViewById(R.id.tv_value);
                // itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            }
        }
    }


    public void setSelectedFilledText() {
        StringBuilder spinnerSelectedText = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            if (selectedValues[i]) {
                spinnerSelectedText.append(dataList.get(i));
                spinnerSelectedText.append(",");
            }
        }


        if (spinnerSelectedText.length() != 0) {
            spinnerText = spinnerSelectedText.toString();
            if (spinnerText.length() > 2) {
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            }
        } else {
            spinnerText = bottomSheetTitle;
        }
    }
}