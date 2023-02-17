package com.octopusbjsindia.view.adapters;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.stories.FeedData;
import com.octopusbjsindia.presenter.StoriesFragmentPresenter;
import com.octopusbjsindia.view.activities.CommentActivity;
import com.octopusbjsindia.view.activities.WebViewActivity;
import com.octopusbjsindia.view.fragments.StoriesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    final String FEED_DATA = "FeedData";

    ArrayList<FeedData> feedList;
    StoriesFragment mContext;
    StoriesFragmentPresenter presentr;
    boolean isDeleteFeed;

    public FeedsAdapter(StoriesFragment context, List<FeedData> feedList, StoriesFragmentPresenter presentr, boolean isDeleteFeed) {
        this.feedList = (ArrayList<FeedData>) feedList;
        this.mContext = context;
        this.presentr = presentr;
        this.isDeleteFeed = isDeleteFeed;
    }

    @NonNull
    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_feed,
                parent, false);
        return new FeedsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsAdapter.ViewHolder holder, int position) {

        if (feedList.get(position).getUserProfileImage() != null
                && !feedList.get(position).getUserProfileImage().equals("")) {
            Glide.with(mContext)
                    .load(feedList.get(position).getUserProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivUserProfilePic);
        }
        if (feedList.get(position).getMediaUrl() != null && feedList.get(position).getMediaUrl().size() > 0) {

            //holder.ivFeedPic.layout(0, 0, 0, 0);
            Glide.with(mContext)
                    .load(feedList.get(position).getMediaUrl().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFeedPic);
            holder.ivFeedPic.setVisibility(View.VISIBLE);
        } else {
            holder.ivFeedPic.setVisibility(View.GONE);
        }
        holder.tvUserName.setText(feedList.get(position).getUserName());
        holder.tvTitle.setText(feedList.get(position).getTitle());
        holder.tvDescription.setText(feedList.get(position).getDescription());
        holder.tvCommentCount.setText(String.valueOf(feedList.get(position).getCommentCount()));
        holder.tvShareCount.setText(String.valueOf(feedList.get(position).getShareCount()));
        holder.tvLikeCount.setText(String.valueOf(feedList.get(position).getLikeCount()));

        holder.tvTime.setText(feedList.get(position).getCreatedDateTime());

        if (feedList.get(position).getExternalUrl()!=null && !TextUtils.isEmpty(feedList.get(position).getExternalUrl())){
            holder.tvExternalUrl.setVisibility(View.VISIBLE);
            holder.tvExternalUrl.setText(feedList.get(position).getExternalUrl());
        }else holder.tvExternalUrl.setVisibility(View.GONE);

        holder.tvDescription.post(() -> {
            if (holder.tvDescription.getLineCount() >4) {
                holder.seeMore.setVisibility(View.VISIBLE);
                holder.tvDescription.setMaxLines(4);
            } else {
                holder.seeMore.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfilePic, ivFeedPic, ivDelete;
        TextView tvUserName, tvTime, tvTitle, tvDescription, tvCommentCount, tvShareCount, tvLikeCount,
                tvExternalUrl, seeMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfilePic = itemView.findViewById(R.id.iv_user_profile_pic);
            ivFeedPic = itemView.findViewById(R.id.iv_feed_pic);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            tvShareCount = itemView.findViewById(R.id.tv_share_count);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            seeMore = itemView.findViewById(R.id.see_more);
            tvExternalUrl = itemView.findViewById(R.id.tv_external_url);

            if (isDeleteFeed) {
                ivDelete.setVisibility(View.VISIBLE);
            }
            else {
                ivDelete.setVisibility(View.GONE);
            }

            seeMore.setOnClickListener(v -> {
                if (seeMore.getText().toString().equalsIgnoreCase("Read more")) {
                    tvDescription.setMaxLines(Integer.MAX_VALUE);//your TextView
                    seeMore.setText("Read less");
                } else {
                    tvDescription.setMaxLines(4);//your TextView
                    seeMore.setText("Read more");
                }
            });

            ivDelete.setOnClickListener(view -> showDialog(mContext.getActivity(), "Alert", "Are you sure, want to Delete Feed?",
                    "Yes", "No", getAdapterPosition(), 1));

            ivFeedPic.setOnClickListener(view -> enlargePhoto(feedList.get(getAdapterPosition()).getMediaUrl().get(0)));

            tvExternalUrl.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(feedList.get(getAdapterPosition()).getExternalUrl())) {
                    Intent intent = new Intent(mContext.getActivity(), WebViewActivity.class);
                    intent.putExtra("URL", feedList.get(getAdapterPosition()).getExternalUrl());
                    mContext.startActivity(intent);
                }
            });

            tvCommentCount.setOnClickListener(view -> {
                mContext.setPosition(getAdapterPosition());
                Intent intent = new Intent(mContext.getActivity(), CommentActivity.class);
                intent.putExtra(FEED_DATA, feedList.get(getAdapterPosition()).getFeedId());
                mContext.startActivity(intent);
           });

/*            lyShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.setPosition(getAdapterPosition());
                    if(Util.isConnected(mContext.getActivity())){
                        //presentr.setShare(feedList.get(getAdapterPosition()).getId());
                    } else {
                        Util.showToast(mContext.getResources().getString(R.string.msg_no_network),mContext.getActivity());
                    }
                    String message = "Title: " + feedList.get(getAdapterPosition()).getTitle() + "\n" +
                            "Description: " + feedList.get(getAdapterPosition()).getDescription() + "\n" +
                            "Media Url: " + feedList.get(getAdapterPosition()).getMediaRul();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    mContext.startActivity(Intent.createChooser(share, "Share Information"));
               }
            });*/

        }
    }

    private void enlargePhoto(String photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(mContext.getActivity(), android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater factory = LayoutInflater.from(mContext.getActivity());
        final View enlargePhotoView = factory.inflate(
                R.layout.enlarge_photo_layout, null);
        FrameLayout closeOption = (FrameLayout) enlargePhotoView.findViewById(R.id.close_layout);
        closeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        PhotoView photoView = (PhotoView) enlargePhotoView.findViewById(R.id.img_feed_photo);
        ImageView closeImageView = (ImageView) enlargePhotoView.findViewById(R.id.img_close_dialog);
        closeImageView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(photoUrl)) {
            try {
                Glide.with(mContext.getActivity())
                        .load(photoUrl)
                        /*.placeholder(R.drawable.image_placeholder)*/
                        .into(photoView);
            } catch (Exception e) {
                e.printStackTrace();
                photoView.setImageResource(R.drawable.ic_img);
            }
        } else {
            photoView.setImageResource(R.drawable.ic_img);
        }

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(enlargePhotoView);
        dialog.show();

    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String, int adapterPosition, int flag) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                mContext.setPosition(adapterPosition);
                presentr.deleteFeed(feedList.get(adapterPosition).getFeedId());
                //Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


}
