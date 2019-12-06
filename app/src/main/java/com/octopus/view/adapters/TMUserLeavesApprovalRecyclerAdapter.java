package com.octopus.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octopus.Platform;
import com.octopus.R;
import com.octopus.models.tm.TMUserLeaveApplications;
import com.octopus.utility.Constants;
import com.octopus.utility.PreferenceHelper;
import com.octopus.utility.Util;

import java.util.List;

public class TMUserLeavesApprovalRecyclerAdapter extends RecyclerView.Adapter<TMUserLeavesApprovalRecyclerAdapter.EmployeeViewHolder> {

    Context mContext;
    private List<TMUserLeaveApplications> dataList;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

    public TMUserLeavesApprovalRecyclerAdapter(Context context, List<TMUserLeaveApplications> dataList, final OnRequestItemClicked clickListener, final OnApproveRejectClicked approveRejectClickedListner) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;
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
        holder.tv_leave_type.setText("("+dataList.get(position).getFull_half_day()+")");

        holder.tv_startdate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getStartdate()), Constants.DAY_MONTH_YEAR));
        holder.tv_enddate.setText(Util.getLongDateInString(Long.parseLong(dataList.get(position).getEnddate()), Constants.DAY_MONTH_YEAR));
        holder.tv_leave_reason.setText(String.valueOf(dataList.get(position).getReason()));
        holder.tv_leave_status.setText(dataList.get(position).getStatus().getStatus());

        if (!TextUtils.isEmpty(dataList.get(position).getReason())){
            holder.tv_leave_reason.setText("Reason:- "+String.valueOf(dataList.get(position).getReason()));
        }
        if (!TextUtils.isEmpty(dataList.get(position).getStatus().getRejection_reason())) {
            holder.tv_leave_reason.setText("Rejected Reason:- "+String.valueOf(dataList.get(position).getStatus().getRejection_reason()));
        }
        String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
        if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)) {
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_approve.setVisibility(View.VISIBLE);
        } else {
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_approve.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_leave_category, tv_leave_type, tv_startdate, tv_enddate, tv_leave_reason,tv_leave_status;

        Button btn_approve, btn_reject;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_leave_category = itemView.findViewById(R.id.tv_leave_category);
            tv_leave_type = itemView.findViewById(R.id.tv_leave_type);
            tv_leave_status = itemView.findViewById(R.id.tv_leave_status);
            tv_startdate = itemView.findViewById(R.id.tv_startdate);
            tv_enddate = itemView.findViewById(R.id.tv_enddate);
            tv_leave_reason = itemView.findViewById(R.id.tv_leave_reason);

            btn_approve = itemView.findViewById(R.id.btn_approve);
            btn_reject = itemView.findViewById(R.id.btn_reject);
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

    public interface OnApproveRejectClicked {
        void onApproveClicked(int pos);

        void onRejectClicked(int pos);
    }
}
