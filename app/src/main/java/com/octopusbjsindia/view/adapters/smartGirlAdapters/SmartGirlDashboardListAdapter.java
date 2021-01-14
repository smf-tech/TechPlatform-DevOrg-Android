package com.octopusbjsindia.view.adapters.smartGirlAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.smartgirl.DashboardListItem;
import com.octopusbjsindia.models.smartgirl.SGTrainersList;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SmartGirlWorkshopListActivity;

import java.util.List;

public class SmartGirlDashboardListAdapter extends RecyclerView.Adapter<SmartGirlDashboardListAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<DashboardListItem> dataList;
    private AllTrainersListRecyclerAdapter.OnRequestItemClicked clickListener;


    public SmartGirlDashboardListAdapter(Context context, List<DashboardListItem> dataList, final AllTrainersListRecyclerAdapter.OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;

    }

    @Override
    public SmartGirlDashboardListAdapter.EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_sg_user_list, parent, false);
        return new SmartGirlDashboardListAdapter.EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmartGirlDashboardListAdapter.EmployeeViewHolder holder, int position) {
        holder.tv_member_name.setText(dataList.get(position).getName());
        holder.tv_member_phone.setText(dataList.get(position).getPhone());
        if (dataList.get(position).getEmail()!=null) {
            holder.tv_member_email.setText(dataList.get(position).getEmail());
        }else {
            holder.tv_member_email.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_member_phone,tv_member_name,tv_member_email;
        ImageView btnPopupMenu;
        PopupMenu popup;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_member_phone = itemView.findViewById(R.id.tv_member_phone);
            tv_member_name = itemView.findViewById(R.id.tv_member_name);
            tv_member_email = itemView.findViewById(R.id.tv_member_email);

            //btn_approve = itemView.findViewById(R.id.btn_approve);
            //btn_reject = itemView.findViewById(R.id.btn_reject);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            tv_member_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent dial = new Intent();
                        dial.setAction("android.intent.action.DIAL");

                        {
                            dial.setData(Uri.parse("tel:" + tv_member_phone.getText().toString()));
                        }

                        mContext.startActivity(dial);
                    } catch (Exception e) {
                        Log.e("Calling Phone", "" + e.getMessage());
                    }
                }
            });

            /*btn_reject.setOnClickListener(new View.OnClickListener() {
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
                    popup.inflate(R.menu.sg_batchlist_menu);
                    popup.getMenu().findItem(R.id.action_add_beneficiary).setVisible(false);
                    popup.getMenu().findItem(R.id.action_register_beneficiary).setVisible(false);
                    popup.getMenu().findItem(R.id.action_pre_feedback).setVisible(false);
                    popup.getMenu().findItem(R.id.action_post_feedback).setVisible(false);

                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (Util.isConnected(mContext)) {
                                switch (item.getItemId()) {
                                    case R.id.action_add_beneficiary:
                                        if (Util.isConnected(mContext)) {
                                            //showDialog(mContext, "Alert", "Do you want register beneficiary for workshop?", "No", "Yes", false,1,getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_register_beneficiary:
                                        if (Util.isConnected(mContext)) {
                                            //showDialog(mContext, "Alert", "Do you want to register to workshop?", "No", "Yes", false,2,getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_post_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addPostFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_pre_feedback:
                                        if (Util.isConnected(mContext)) {
                                            ((SmartGirlWorkshopListActivity)mContext).addPreFeedbackFragment(getAdapterPosition());
                                        } else {
                                            Util.showToast(mContext.getString(R.string.msg_no_network), mContext);
                                        }
                                        break;
                                    case R.id.action_pretest_trainer:
                                        if (Util.isConnected(mContext)) {
                                            //((TrainerBatchListActivity)mContext).fillPreTestFormToBatch(getAdapterPosition());
                                            ((SmartGirlWorkshopListActivity)mContext).addTrainingPreTestFragment(getAdapterPosition());

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
