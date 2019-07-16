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
import com.platform.models.tm.TMUserAttendanceApprovalRequest;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;

import java.util.List;

public class TMUserAttendanceApprovalRecyclerAdapter extends RecyclerView.Adapter<TMUserAttendanceApprovalRecyclerAdapter.EmployeeViewHolder> {

        private List<TMUserAttendanceApprovalRequest> dataList;
        private Context mContext;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
    private PreferenceHelper preferenceHelper;

        public TMUserAttendanceApprovalRecyclerAdapter(Context context, List<TMUserAttendanceApprovalRequest> dataList, final OnRequestItemClicked clickListener,final OnApproveRejectClicked approveRejectClickedListner) {
            mContext = context;
            this.dataList = dataList;
            this.clickListener =clickListener;
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
            holder.tv_title_checkin.setText("Check in : "+ Util.getDateFromTimestamp(Long.parseLong(dataList.get(position).getCheck_in().getTime()), Constants.FORM_DATE));
            holder.tv_address_checkin.setText("Address : "+dataList.get(position).getCheck_in().getAddress());
            holder.tv_title_checkout.setText("Check out : "+dataList.get(position).getCheck_out().getTime());
            holder.tv_address_checkout.setText("Address : "+dataList.get(position).getCheck_out().getAddress());


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

            TextView tv_title_checkin,tv_title_checkout, tv_address_checkin ,tv_address_checkout;
            Button btn_approve,btn_reject;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                tv_title_checkin = (TextView) itemView.findViewById(R.id.tv_title_checkin);
                tv_title_checkout = (TextView) itemView.findViewById(R.id.tv_title_checkout);
                tv_address_checkin = (TextView) itemView.findViewById(R.id.tv_address_checkin);
                tv_address_checkout = (TextView) itemView.findViewById(R.id.tv_address_checkout);
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
