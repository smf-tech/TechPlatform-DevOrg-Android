package com.platform.view.adapters;

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
import com.platform.R;
import com.platform.models.Matrimony.UserProfileList;
import com.platform.models.tm.PendingApprovalsRequest;

import java.util.List;

public class MatrimonyProfileListRecyclerAdapter extends RecyclerView.Adapter<MatrimonyProfileListRecyclerAdapter.EmployeeViewHolder> {

        private List<UserProfileList> dataList;
        private Context mContext;
        private RequestOptions requestOptions;
    private OnRequestItemClicked clickListener;

        public MatrimonyProfileListRecyclerAdapter(Context context, List<UserProfileList> dataListreceived, final OnRequestItemClicked clickListener) {
            mContext = context;
            this.dataList = dataListreceived;
            this.clickListener =clickListener;
             requestOptions = new RequestOptions().placeholder(R.mipmap.app_logo);
            requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        }

        @Override
        public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_matrimony_profilelist_item, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EmployeeViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getMatrimonial_profile().getPersonal_details().getFirst_name());
            holder.txtValue.setText(String.valueOf(dataList.get(position).getMatrimonial_profile().getPersonal_details().getAge()));

            if (!TextUtils.isEmpty(dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image())) {
                Glide.with(mContext)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(dataList.get(position).getMatrimonial_profile().getOther_marital_information().getProfile_image())
                        .into(holder.user_profile_pic);


            }

        }

        @Override
        public int getItemCount() {
            return this.dataList.size();
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
