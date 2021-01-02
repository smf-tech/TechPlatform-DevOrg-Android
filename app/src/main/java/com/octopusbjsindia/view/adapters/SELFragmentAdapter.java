package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.text.TextUtils;
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
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.activities.SELTrainingVideoActivity;
import com.octopusbjsindia.view.fragments.SELFragment;

import java.util.List;

public class SELFragmentAdapter extends RecyclerView.Adapter<SELFragmentAdapter.ViewHolder> {

    private List<SELVideoContent> selContentList;
    private SELFragment mContext;
    private RequestOptions requestOptions;

    public SELFragmentAdapter(SELFragment context, final List<SELVideoContent> selContentList) {
        mContext = context;
        this.selContentList = selContentList;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
    }

    @NonNull
    @Override
    public SELFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_video_row,
                parent, false);
        return new SELFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (selContentList.get(position).getThumbnailUrl() != null) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(selContentList.get(position).getThumbnailUrl().replaceAll("//", ""))
                    .into(holder.imgThumbnail);
        }
        holder.tvForm.setText(selContentList.get(position).getTitle());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout lyThumbnail, rlVideoForm;
        ImageView imgThumbnail;
        TextView tvForm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvForm = itemView.findViewById(R.id.tv_form);
            lyThumbnail = itemView.findViewById(R.id.ly_thumbnail);
            lyThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selContentList.get(getAdapterPosition()).getVideoUrl() != null &&
                            !TextUtils.isEmpty(selContentList.get(getAdapterPosition()).getVideoUrl())) {
                        Intent intent = new Intent(mContext.getActivity(), SELTrainingVideoActivity.class);
                        intent.putExtra("videoId", selContentList.get(getAdapterPosition()).getId());
                        intent.putExtra("videoUrl", selContentList.get(getAdapterPosition()).getVideoUrl());
                        mContext.startActivity(intent);
                    } else {
                        Util.showToast(mContext.getActivity(), "Something went wrong. Please try again later.");
                    }
                }
            });
            rlVideoForm = itemView.findViewById(R.id.rl_video_form);
            rlVideoForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selContentList.get(getAdapterPosition()).getFormId() != null &&
                            !TextUtils.isEmpty(selContentList.get(getAdapterPosition()).getFormId())) {
                        Intent intent = new Intent(mContext.getActivity(), FormDisplayActivity.class);
                        intent.putExtra(Constants.PM.FORM_ID, selContentList.get(getAdapterPosition()).getFormId());
                        mContext.startActivity(intent);
                    } else {
                        Util.showToast(mContext.getActivity(), "Something went wrong. Please try again later.");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selContentList.size();
    }
}
