package com.octopusbjsindia.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.MatrimonyMeet;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

import java.util.List;

public class MeetListBottomSheet extends BottomSheetDialog implements View.OnClickListener {

    private final List<MatrimonyMeet> meetChoiceList;
    private List<MatrimonyMeet> dataList;
    private MeetListBottomSheet.MultiSpinnerListener listener;
    public Activity activity;

    private String spinnerText = "";
    public TextView toolbarTitle;
    public ImageView img_close;
    public String bottomSheetTitle;
    public RecyclerView rv_filterchoice;
    public Dialog d;
    public Button yes, no;


    MeetListBottomSheet.FilterChoicedapter adapter;
    private int lastSelectedPosition = -1;
    private int selectedPosition;

    public MeetListBottomSheet(Activity a, String formTitle, List<MatrimonyMeet> list, MeetListBottomSheet.MultiSpinnerListener listener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        bottomSheetTitle = formTitle;
        meetChoiceList = list;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_multiselect_dialog_layout, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        rv_filterchoice = findViewById(R.id.rv_filterchoice);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        img_close = findViewById(R.id.toolbar_edit_action);
        toolbarTitle = findViewById(R.id.toolbar_title);
        // toolbarTitle.setText(bottomSheetTitle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        rv_filterchoice.setLayoutManager(layoutManager);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        img_close.setOnClickListener(this);

        adapter = new MeetListBottomSheet.FilterChoicedapter(activity, meetChoiceList);
        rv_filterchoice.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                //setSelectedFilledText();
                //if (!TextUtils.isEmpty(spinnerText))
                if (lastSelectedPosition!=-1)
                {
                    listener.onValuesSelected(selectedPosition, bottomSheetTitle, spinnerText);
                }else {
                    Util.showToast(activity,"Please select meet first.");
                }
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

    public interface MultiSpinnerListener {
        void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues);
    }

    public class FilterChoicedapter extends RecyclerView.Adapter<FilterChoicedapter.EmployeeViewHolder> {


        private Context mContext;
        //private OnRequestItemClicked clickListener;

        public FilterChoicedapter(Context context, List<MatrimonyMeet> filterChoiceList) {//},final OnRequestItemClicked clickListener) {
            mContext = context;
            dataList = filterChoiceList;
            //this.clickListener =clickListener;
        }

        @Override
        public MeetListBottomSheet.FilterChoicedapter.EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_meet_bottomsheet_item, parent, false);
            return new MeetListBottomSheet.FilterChoicedapter.EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MeetListBottomSheet.FilterChoicedapter.EmployeeViewHolder holder, int position) {
            if (dataList.get(position).getMeetImageUrl() != null && !dataList.get(position).getMeetImageUrl().equals("")) {
                Glide.with(mContext)
                        .load(dataList.get(position).getMeetImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivMeetPic);
            }
            holder.tvTitle.setText(dataList.get(position).getTitle());
            holder.tvAddress.setText(dataList.get(position).getLocation().getCity() + ", " + dataList.get(position).getLocation().getState());
            holder.tvDate.setText(Util.getDateFromTimestamp(dataList.get(position).getSchedule().getDateTime(), Constants.DAY_MONTH_YEAR) + " @ " +
                    Util.getFormattedDatee(dataList.get(position).getSchedule().getMeetStartTime(), "hh:mm aaa", "HH:mm"));

            if (position == lastSelectedPosition) {
                holder.ly_show_selection.setVisibility(View.VISIBLE);
            } else {
                holder.ly_show_selection.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class EmployeeViewHolder extends RecyclerView.ViewHolder {
            private CardView cvMain;
            private ImageView ivMeetPic;
            private RelativeLayout ly_show_selection;
            private TextView tvTitle, tvAddress, tvDate, tvRegistered, tvStatus;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                this.setIsRecyclable(false);
                ivMeetPic = itemView.findViewById(R.id.iv_meet_pic);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvAddress = itemView.findViewById(R.id.tv_address);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvRegistered = itemView.findViewById(R.id.tv_registered);
                tvStatus = itemView.findViewById(R.id.tv_stetus);
                ly_show_selection = itemView.findViewById(R.id.ly_show_selection);
/*                cb_select_filter = itemView.findViewById(R.id.cb_select_filter);

                cb_select_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //dataList.get(getAdapterPosition()).setSelected(isChecked);
                        for (int i = 0; i < selectedValues.length; i++) {
                            selectedValues[i] = false;
                        }
                        selectedValues[getAdapterPosition()] = isChecked;
                        if (isChecked==true) {
                            selectedPosition = getAdapterPosition();
                            lastSelectedPosition = getAdapterPosition();
                        }


                    }
                });
                cb_select_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //lastSelectedPosition = getAdapterPosition();
                        //notifyDataSetChanged();
                        notifyDataSetChanged();
                        *//*Toast.makeText(mContext,
                                "selected offer is ",
                                Toast.LENGTH_LONG).show();*//*
                    }
                });*/
                //txtValue = (TextView) itemView.findViewById(R.id.tv_value);
                //itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //listener.onValuesSelected(selectedPosition, bottomSheetTitle, spinnerText);
                        selectedPosition = getAdapterPosition();
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

}

