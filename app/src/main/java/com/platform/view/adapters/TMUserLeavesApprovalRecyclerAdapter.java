package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.platform.Platform;
import com.platform.R;
import com.platform.models.tm.TMUserFormsApprovalRequest;
import com.platform.models.tm.TMUserLeaveApplications;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;

import java.util.List;

public class TMUserLeavesApprovalRecyclerAdapter extends RecyclerView.Adapter<TMUserLeavesApprovalRecyclerAdapter.EmployeeViewHolder> {


    private List<TMUserLeaveApplications> dataList;
        private Context mContext;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

        public TMUserLeavesApprovalRecyclerAdapter(Context context, List<TMUserLeaveApplications> dataList, final OnRequestItemClicked clickListener,final OnApproveRejectClicked approveRejectClickedListner) {
            mContext = context;
            this.dataList = dataList;
            this.clickListener =clickListener;
            this.buttonClickListner = approveRejectClickedListner;
            preferenceHelper = new PreferenceHelper(Platform.getInstance());
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_tm_leavespage_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.tv_leave_category.setText(dataList.get(position).getLeave_type());
            holder.tv_leave_type.setText(String.valueOf(dataList.get(position).getFull_half_day()));

            holder.tv_startdate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getStartdate()), Constants.DAY_MONTH_YEAR));
            holder.tv_enddate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getEnddate()), Constants.DAY_MONTH_YEAR));
            holder.tv_leave_reason.setText(String.valueOf(dataList.get(position).getReason()));


            String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
            if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)){
                holder.btn_reject.setVisibility(View.VISIBLE);
                holder.btn_approve.setVisibility(View.VISIBLE);
            }else {
                holder.btn_reject.setVisibility(View.GONE);
                holder.btn_approve.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView tv_leave_category, tv_leave_type,tv_startdate,tv_enddate,tv_leave_reason;

            Button btn_approve,btn_reject;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                tv_leave_category = (TextView) itemView.findViewById(R.id.tv_leave_category);
                tv_leave_type = (TextView) itemView.findViewById(R.id.tv_leave_type);
                tv_startdate = (TextView) itemView.findViewById(R.id.tv_startdate);
                tv_enddate = (TextView) itemView.findViewById(R.id.tv_enddate);
                tv_leave_reason = (TextView) itemView.findViewById(R.id.tv_leave_reason);

                btn_approve = itemView.findViewById(R.id.btn_approve);
                btn_reject  = itemView.findViewById(R.id.btn_reject);
                itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
                btn_approve.setOnClickListener(new View.OnClickListener() {
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
                });
            }
        }
    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
    public interface OnApproveRejectClicked{
            void onApproveClicked(int pos);
            void onRejectClicked(int pos);
    }
}
