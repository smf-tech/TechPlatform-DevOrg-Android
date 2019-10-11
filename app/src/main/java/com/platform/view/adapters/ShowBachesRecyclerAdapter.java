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
import com.platform.models.Matrimony.Male;
import com.platform.models.tm.LandingPageRequest;

import java.util.List;

public class ShowBachesRecyclerAdapter extends RecyclerView.Adapter<ShowBachesRecyclerAdapter.EmployeeViewHolder> {

    private List<List<Male>> dataList;
    private Context mContext;
    private OnRequestItemClicked clickListener;
    private RequestOptions requestOptions;

    public ShowBachesRecyclerAdapter(Context context,List<List<Male>> male, final OnRequestItemClicked clickListener) {
        mContext = context;
        this.dataList = male;
        this.clickListener = clickListener;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_malefemale_baches_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        if (!TextUtils.isEmpty(dataList.get(0).get(position).getName())) {
            holder.txtTitle.setText(dataList.get(0).get(position).getName());
        }else {
            holder.txtTitle.setText("UserName missing");
        }
        if (!TextUtils.isEmpty(dataList.get(0).get(position).getBadge())) {
            holder.txtValue.setText(dataList.get(0).get(position).getBadge());
        }else {
            holder.txtValue.setText("Badge missing");
        }
        if (!TextUtils.isEmpty(dataList.get(0).get(position).getProfile_image().get(0))){
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(dataList.get(0).get(position).getProfile_image().get(0))
                    .into(holder.user_profile_pic);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.get(0).size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtValue;
        ImageView user_profile_pic;


        EmployeeViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
            txtValue = (TextView) itemView.findViewById(R.id.tv_value);
            user_profile_pic = (ImageView) itemView.findViewById(R.id.user_profile_pic);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
        }
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
