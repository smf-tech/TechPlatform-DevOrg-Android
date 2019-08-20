package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.tm.LandingPageRequest;

import java.util.ArrayList;
import java.util.List;

public class TMLandingPageRecyclerAdapter extends RecyclerView.Adapter<TMLandingPageRecyclerAdapter.EmployeeViewHolder> {

        private List<LandingPageRequest> dataList;
        private Context mContext;
    private OnRequestItemClicked clickListener;

        public TMLandingPageRecyclerAdapter(Context context, List<LandingPageRequest> dataList,final OnRequestItemClicked clickListener) {
            mContext = context;
            this.dataList = dataList;
            this.clickListener =clickListener;
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_tm_landingpage_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getApprovalType());
            holder.txtValue.setText(String.valueOf(dataList.get(position).getPendingCount()));

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle, txtValue;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtValue = itemView.findViewById(R.id.tv_value);
                itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            }
        }
    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
