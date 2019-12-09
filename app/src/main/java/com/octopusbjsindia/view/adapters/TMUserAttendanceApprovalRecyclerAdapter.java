package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.tm.TMUserAttendanceApprovalRequest;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;

import java.util.List;

public class TMUserAttendanceApprovalRecyclerAdapter extends RecyclerView.Adapter<TMUserAttendanceApprovalRecyclerAdapter.EmployeeViewHolder> {

    private List<TMUserAttendanceApprovalRequest> dataList;
    private Context mContext;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

    public TMUserAttendanceApprovalRecyclerAdapter(Context context, List<TMUserAttendanceApprovalRequest> dataList, final OnRequestItemClicked clickListener, final OnApproveRejectClicked approveRejectClickedListner) {
        mContext = context;
        this.dataList = dataList;
        this.clickListener = clickListener;
        this.buttonClickListner = approveRejectClickedListner;
        preferenceHelper = new PreferenceHelper(Platform.getInstance());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_tm_attendancepage_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        //if ()
        holder.tv_title_checkin.setText("" + Util.getLongDateInString(Long.parseLong(dataList.get(position).getCheck_in().getTime()), Constants.FORM_DATE_FORMAT));
        holder.tv_address_checkin.setText("" + dataList.get(position).getCheck_in().getAddress());
        if (dataList.get(position).getCheck_out() != null) {
            if (!TextUtils.isEmpty(dataList.get(position).getCheck_out().getTime())) {
                holder.tv_title_checkout.setText("" + Util.getLongDateInString(Long.parseLong(dataList.get(position).getCheck_out().getTime()), Constants.FORM_DATE_FORMAT));
            }
            if (!TextUtils.isEmpty(dataList.get(position).getCheck_out().getAddress())) {
                holder.tv_address_checkout.setText("" + dataList.get(position).getCheck_out().getAddress());
            }
        }
        String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);
        if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)) {
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_approve.setVisibility(View.VISIBLE);
        } else {
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_approve.setVisibility(View.GONE);
        }
        if (dataList.get(position).getReason()!=null){
            if (TextUtils.isEmpty(dataList.get(position).getReason())){
              holder.tv_reason.setVisibility(View.GONE);
              holder.tv_reason_value.setVisibility(View.GONE);
                //holder.tv_leave_reason.setText(String.valueOf(dataList.get(position).getReason()));
            }
        }
            if (dataList.get(position).getStatus()!=null){
                if (dataList.get(position).getStatus().getRejection_reason()!=null && !TextUtils.isEmpty(dataList.get(position).getStatus().getRejection_reason())) {
                    holder.tv_reason.setVisibility(View.GONE);
                    holder.tv_reason_value.setVisibility(View.VISIBLE);
                    holder.tv_reason_value.setText("Rejected Reason:- " + String.valueOf(dataList.get(position).getStatus().getRejection_reason()));
                }
            }



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title_checkin, tv_title_checkout, tv_address_checkin, tv_address_checkout,
        tv_reason,tv_reason_value;
        Button btn_approve, btn_reject;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            tv_title_checkin = itemView.findViewById(R.id.tv_title_checkin_value);
            tv_title_checkout = itemView.findViewById(R.id.tv_title_checkout_value);
            tv_address_checkin = itemView.findViewById(R.id.tv_address_checkin_value);
            tv_address_checkout = itemView.findViewById(R.id.tv_address_checkout_value);
            btn_approve = itemView.findViewById(R.id.btn_approve);
            btn_reject = itemView.findViewById(R.id.btn_reject);
            tv_reason = itemView.findViewById(R.id.tv_reason);
            tv_reason_value  = itemView.findViewById(R.id.tv_reason_value);


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
