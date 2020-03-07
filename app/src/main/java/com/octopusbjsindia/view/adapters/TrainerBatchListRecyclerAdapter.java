package com.octopusbjsindia.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.smartgirl.TrainerBachList;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateTrainerWorkshop;

import java.util.List;

public class TrainerBatchListRecyclerAdapter extends RecyclerView.Adapter<TrainerBatchListRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<TrainerBachList> dataList;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

    public TrainerBatchListRecyclerAdapter(Context context, List<TrainerBachList> dataList, final OnRequestItemClicked clickListener, final OnApproveRejectClicked approveRejectClickedListner) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;
        this.buttonClickListner = approveRejectClickedListner;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_trainer_batch_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        holder.tv_state_value.setText(dataList.get(position).getState_id());
        holder.tv_district_value.setText(dataList.get(position).getDistrict_id());

        holder.tv_program_value.setText("Smart Girl");
        holder.tv_category_value.setText(dataList.get(position).getBatch_category_id());

        //holder.tv_startdate_value.setText(String.valueOf(dataList.get(position).getBatchschedule().getStartDate()));
        //holder.tv_enddate_value.setText(String.valueOf(dataList.get(position).getBatchschedule().getStartDate()));

        /*holder.tv_startdate.setText(Util.getLongDateInString(dataList.get(position).getBatchschedule().getStartDate(), Constants.DAY_MONTH_YEAR));
        holder.tv_enddate.setText(Util.getLongDateInString(dataList.get(position).getBatchschedule().getEndDate(), Constants.DAY_MONTH_YEAR));*/


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_state_value, tv_district_value, tv_program_value, tv_category_value, tv_city_value,
                tv_attendence_value, tv_startdate_value, tv_enddate_value, tv_additional_trainer_title;
        ImageView btnPopupMenu;
        PopupMenu popup;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_state_value = itemView.findViewById(R.id.tv_state_value);
            tv_district_value = itemView.findViewById(R.id.tv_district_value);
            tv_program_value = itemView.findViewById(R.id.tv_program_value);
            tv_category_value = itemView.findViewById(R.id.tv_category_value);
            tv_city_value = itemView.findViewById(R.id.tv_city_value);
            tv_attendence_value = itemView.findViewById(R.id.tv_attendence_value);
            tv_startdate_value = itemView.findViewById(R.id.tv_startdate_value);
            tv_enddate_value = itemView.findViewById(R.id.tv_enddate_value);
            tv_additional_trainer_title = itemView.findViewById(R.id.tv_additional_trainer_title);
            //btn_approve = itemView.findViewById(R.id.btn_approve);
            //btn_reject = itemView.findViewById(R.id.btn_reject);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            /*btn_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListner.onApproveClicked(getAdapterPosition());
                }
            });
            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListner.onRejectClicked(getAdapterPosition());
                }
            });*/

            btnPopupMenu = itemView.findViewById(R.id.btn_popmenu);
            btnPopupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup = new PopupMenu((mContext), v);
                    popup.inflate(R.menu.machine_forms_menu);
                    popup.getMenu().findItem(R.id.action_machine_worklog).setVisible(true);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (Util.isConnected(mContext)) {
                                switch (item.getItemId()) {
                                    case R.id.action_machine_worklog:
                                        if (Util.isConnected(mContext)) {
                                            Intent intent = new Intent(mContext, CreateTrainerWorkshop.class);
                                            intent.putExtra(Constants.Login.ACTION_EDIT, Constants.Login.ACTION_EDIT);
                                            ((Activity) mContext).startActivityForResult(intent, Constants.Home.NEVIGET_TO);
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                }
                            } else {
                                Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                            }
                            return false;
                        }
                    });
                }
            });
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }

    public interface OnApproveRejectClicked {
        void onApproveClicked(int pos);

        void onRejectClicked(int pos);
    }
}
