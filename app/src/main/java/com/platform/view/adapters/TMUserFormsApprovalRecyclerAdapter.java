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
import com.platform.models.tm.PendingApprovalsRequest;
import com.platform.models.tm.TMUserFormsApprovalRequest;
import com.platform.utility.PreferenceHelper;

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

            TextView txtTitle, txtValue;
            Button btn_approve,btn_reject;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
                txtValue = (TextView) itemView.findViewById(R.id.tv_value);
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
