package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.tm.TMUserFormsApprovalRequest;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;

import java.util.List;

public class TMUserFormsApprovalRecyclerAdapter extends RecyclerView.Adapter<TMUserFormsApprovalRecyclerAdapter.EmployeeViewHolder> {

        private List<TMUserFormsApprovalRequest.Form_detail> dataList;
        private Context mContext;
    private OnRequestItemClicked clickListener;
    private OnApproveRejectClicked buttonClickListner;
private PreferenceHelper preferenceHelper;
        public TMUserFormsApprovalRecyclerAdapter(Context context, List<TMUserFormsApprovalRequest.Form_detail> dataList, final OnRequestItemClicked clickListener, final OnApproveRejectClicked approveRejectClickedListner) {
            mContext = context;
            this.dataList = dataList;
            this.clickListener =clickListener;
            this.buttonClickListner = approveRejectClickedListner;
            preferenceHelper = new PreferenceHelper(Platform.getInstance());
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_tm_formspage_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getForm_title());
            holder.txtValue.setText(String.valueOf(dataList.get(position).getSurvey_name().getDefault()));
            holder.tv_date.setText(Util.getDateFromTimestamp(dataList.get(position).getCreatedDateTime(), Constants.FORM_DATE));
            String ispending = preferenceHelper.getString(PreferenceHelper.IS_PENDING);


            if (ispending.equalsIgnoreCase(PreferenceHelper.IS_PENDING)){
                holder.btn_reject.setVisibility(View.VISIBLE);
                holder.btn_approve.setVisibility(View.VISIBLE);
                holder.tv_leave_reason.setVisibility(View.GONE);
            }else {
                holder.btn_reject.setVisibility(View.GONE);
                holder.btn_approve.setVisibility(View.GONE);
                holder.tv_leave_reason.setVisibility(View.VISIBLE);
                if (dataList.get(position).getStatus()!=null&&dataList.get(position).getStatus().getRejection_reason()!=null) {

                    holder.tv_leave_reason.setText("Rejected Reason:- "+dataList.get(position).getStatus().getRejection_reason());
                }
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle, txtValue,tv_leave_reason,tv_date;
            Button btn_approve,btn_reject;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtValue = itemView.findViewById(R.id.tv_value);
                tv_date = itemView.findViewById(R.id.tv_date);
                tv_leave_reason = itemView.findViewById(R.id.tv_leave_reason);
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
