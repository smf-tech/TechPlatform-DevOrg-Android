package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.stories.CommentData;
import com.octopusbjsindia.models.support.TicketData;
import com.octopusbjsindia.presenter.CommentActivityPresenter;
import com.octopusbjsindia.view.activities.CommentActivity;
import com.octopusbjsindia.view.activities.SupportActivity;
import com.octopusbjsindia.view.fragments.TicketDetailFragment;
import com.octopusbjsindia.view.fragments.TicketListFragment;

import java.util.ArrayList;
import java.util.List;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder>{
    ArrayList<TicketData> list;
    TicketListFragment mContext;
    private RequestOptions requestOptions;

    public TicketListAdapter(TicketListFragment mContext, List<TicketData> data) {
        this.list = (ArrayList<TicketData>) data;
        this.mContext = mContext;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

    }

    @NonNull
    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_ticket,
                parent, false);
        return new TicketListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketListAdapter.ViewHolder holder, int position) {
//        if (commentList.get(position).getUserDetails()!= null
//                && commentList.get(position).getUserDetails().getProfileImage() != null
//                && commentList.get(position).getUserDetails().getProfileImage().size()>0
//                && !commentList.get(position).getUserDetails().getProfileImage().get(0).equals("")) {
//            Glide.with(mContext)
//                    .applyDefaultRequestOptions(requestOptions)
//                    .load(commentList.get(position).getUserDetails().getProfileImage().get(0))
//                    .into(holder.ivProfilePic);
//        } else {
//            holder.ivProfilePic.setImageResource(R.drawable.ic_user_avatar);
//        }
//        if (list.get(position).get() != null
//                && commentList.get(position).getUserDetails().getName() != null
//                && !commentList.get(position).getUserDetails().getName().equals("")) {
//            holder.tvUserName.setText(commentList.get(position).getUserDetails().getName());
//        } else {
            holder.tvUserName.setText("BJS User");
//        }
        holder.tvTitle.setText(list.get(position).getTicketTitle());
        holder.tvTime.setText(list.get(position).getCreatedDatetime());
        holder.tvStatus.setText(list.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserPic;
        TextView tvUserName, tvTitle, tvTime,tvStatus;
        RelativeLayout lyMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            itemView.findViewById(R.id.lyMain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getActivity(), SupportActivity.class);
                    intent.putExtra("data",list.get(getAdapterPosition()));
                    intent.putExtra("toOpen","detailTicket");
                    mContext.startActivity(intent);}
            });
        }
    }
}
