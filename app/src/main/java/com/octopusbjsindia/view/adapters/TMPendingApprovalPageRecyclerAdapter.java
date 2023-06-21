package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.tm.PendingApprovalsRequest;

import java.util.List;

public class TMPendingApprovalPageRecyclerAdapter extends RecyclerView.Adapter<TMPendingApprovalPageRecyclerAdapter.EmployeeViewHolder> {

        private List<PendingApprovalsRequest> dataList;
        private Context mContext;
        private RequestOptions requestOptions;
    private OnRequestItemClicked clickListener;

        public TMPendingApprovalPageRecyclerAdapter(Context context, List<PendingApprovalsRequest> dataList, final OnRequestItemClicked clickListener) {
            mContext = context;
            this.dataList = dataList;
            this.clickListener =clickListener;
             requestOptions = new RequestOptions().placeholder(R.mipmap.ic_launcher);
            requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_tm_filtered_userspage_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getName());
            holder.txtValue.setText(String.valueOf(dataList.get(position).getRole_id()));

            if (!TextUtils.isEmpty(dataList.get(position).getProfile_pic())) {
                Glide.with(mContext)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(dataList.get(position).getProfile_pic())
                        .into(holder.user_profile_pic);


            } else {
                holder.user_profile_pic.setImageResource(R.drawable.ic_user_avatar);
            }

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle, txtValue;
            ImageView user_profile_pic;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtValue = itemView.findViewById(R.id.tv_value);
                user_profile_pic = itemView.findViewById(R.id.user_profile_pic);
                itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
            }
        }
    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
