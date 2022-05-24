package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.stories.CommentData;
import com.octopusbjsindia.presenter.CommentActivityPresenter;
import com.octopusbjsindia.view.activities.CommentActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter  extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    ArrayList<CommentData> commentList;
    Context mContext;
    CommentActivityPresenter presentr;
    private RequestOptions requestOptions;

    public CommentsAdapter(CommentActivity mContext, List<CommentData> data, CommentActivityPresenter presentr) {
        this.commentList = (ArrayList<CommentData>) data;
        this.mContext = mContext;
        this.presentr = presentr;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_comment,
                parent, false);
        return new CommentsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        if (commentList.get(position).getUserDetails()!= null
                && commentList.get(position).getUserDetails().getProfileImage() != null
                && commentList.get(position).getUserDetails().getProfileImage().size()>0
                && !commentList.get(position).getUserDetails().getProfileImage().get(0).equals("")) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(commentList.get(position).getUserDetails().getProfileImage().get(0))
                    .into(holder.ivProfilePic);
        } else {
            holder.ivProfilePic.setImageResource(R.drawable.ic_user_avatar);
        }
        if (commentList.get(position).getUserDetails() != null
                && commentList.get(position).getUserDetails().getName() != null
                && !commentList.get(position).getUserDetails().getName().equals("")) {
            holder.tvName.setText(commentList.get(position).getUserDetails().getName());
        } else {
            holder.tvName.setText("BJS User");
        }
        holder.tvTime.setText(commentList.get(position).getCreatedAt());
        holder.tvComment.setText(commentList.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfilePic;
        TextView tvName, tvComment, tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
