package com.octopusbjsindia.view.adapters;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.stories.FeedData;
import com.octopusbjsindia.presenter.StoriesFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.StructureCompletionActivity;
import com.octopusbjsindia.view.activities.StructurePripretionsActivity;
import com.octopusbjsindia.view.fragments.StoriesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    final String FEED_DATA = "FeedData";

    ArrayList<FeedData> feedList;
    StoriesFragment mContext;
    StoriesFragmentPresenter presentr;
//    private RequestOptions requestOptionsCirculer, requestOptions;

    public FeedsAdapter(StoriesFragment context, List<FeedData> feedList, StoriesFragmentPresenter presentr) {
        this.feedList = (ArrayList<FeedData>) feedList;
        this.mContext = context;
        this.presentr = presentr;
//        requestOptionsCirculer = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
//        requestOptionsCirculer = requestOptionsCirculer.apply(RequestOptions.circleCropTransform());
//        requestOptions = new RequestOptions().placeholder(R.drawable.ic_img);
//        requestOptions = requestOptions.apply(RequestOptions.());

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
//                    .applyDefaultRequestOptions(requestOptionsCirculer)
                    .load(feedList.get(position).getUserProfileImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivUserProfilePic);
        }
        if (feedList.get(position).getMediaUrl() != null && feedList.get(position).getMediaUrl().size()>0) {
            holder.ivFeedPic.setVisibility(View.VISIBLE);
            Glide.with(mContext)
//                    .applyDefaultRequestOptions(requestOptions)
                    .load(feedList.get(position).getMediaUrl().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFeedPic);
        } else {
            holder.ivFeedPic.setVisibility(View.GONE);
        }
        holder.tvUserName.setText(feedList.get(position).getUserName());
        holder.tvTitle.setText(feedList.get(position).getTitle());
        holder.tvDescription.setText(feedList.get(position).getDescription());
        holder.tvDescriptionMore.setText(feedList.get(position).getDescription());
        holder.tvCommentCount.setText(String.valueOf(feedList.get(position).getCommentCount()));
        holder.tvShareCount.setText(String.valueOf(feedList.get(position).getShareCount()));
        holder.tvLikeCount.setText(String.valueOf(feedList.get(position).getLikeCount()));
        holder.tvExternalUrl.setText(feedList.get(position).getExternalUrl());
        holder.tvTime.setText(feedList.get(position).getCreatedDateTime());

//        if (feedList.get(position).isFeedLiked()) {
//            holder.ivLike.setImageResource(R.drawable.ic_hart_like);
//        } else {
//            holder.ivLike.setImageResource(R.drawable.ic_hart_unlike);
//        }

        // done for time been tack the time stamp from backend
//        Date date = null;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            date = format.parse(feedList.get(position).getCreatedAt());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        holder.tvTime.setText(Util.getDateFromTimestamp(date.getTime(), Constants.LIST_DATE_FORMAT));

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfilePic, ivFeedPic, ivLike, ivDelete;
        TextView tvUserName, tvTime, tvTitle, tvDescription, tvCommentCount, tvShareCount, tvLikeCount,
                tvDescriptionMore, tvReadLess, tvReadMore, tvExternalUrl;
        RelativeLayout lyComment, lyShare, lyLike, lyMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfilePic = itemView.findViewById(R.id.iv_user_profile_pic);
            ivFeedPic = itemView.findViewById(R.id.iv_feed_pic);
            ivLike = itemView.findViewById(R.id.iv_like);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            tvShareCount = itemView.findViewById(R.id.tv_share_count);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvDescriptionMore = itemView.findViewById(R.id.tv_description_more);
            lyComment = itemView.findViewById(R.id.ly_comment);
            lyShare = itemView.findViewById(R.id.ly_share);
            lyLike = itemView.findViewById(R.id.ly_like);
            lyMain = itemView.findViewById(R.id.ly_main);
            tvReadLess = itemView.findViewById(R.id.tv_read_less);
            tvReadMore = itemView.findViewById(R.id.tv_read_more);
            tvExternalUrl = itemView.findViewById(R.id.tv_external_url);
            tvReadLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvDescriptionMore.setVisibility(View.GONE);
                    tvReadLess.setVisibility(View.GONE);
                    tvDescription.setVisibility(View.VISIBLE);
                    tvReadMore.setVisibility(View.VISIBLE);
                }
            });

            tvReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvDescriptionMore.setVisibility(View.VISIBLE);
                    tvReadLess.setVisibility(View.VISIBLE);
                    tvDescription.setVisibility(View.GONE);
                    tvReadMore.setVisibility(View.GONE);
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(mContext.getActivity(), "Alert", "Are you sure, want to Delete Feed?",
                            "Yes", "No", getAdapterPosition(), 1);
               }
            });

            ivFeedPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enlargePhoto(feedList.get(getAdapterPosition()).getMediaUrl().get(0));
                }
            });
            tvExternalUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String urlString = feedList.get(getAdapterPosition()).getExternalUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlString));
                    intent.setPackage("com.android.chrome");
                    try {
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Incorrect Link.. OR Chrome browser presumably not installed
                        Util.showToast("Incorrect Link..",mContext);
                    }
                }
            });
//            lyMain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mContext.setPosition(getAdapterPosition());
//                    Intent intent = new Intent(mContext.getActivity(), FeedDetailActivity.class);
//                    intent.putExtra(FEED_DATA, feedList.get(getAdapterPosition()));
//                    mContext.startActivityForResult(intent,Constants.FEED_DATA);
//                }
//            });
//            lyComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mContext.setPosition(getAdapterPosition());
//                    Intent intent = new Intent(mContext.getActivity(), CommentActivity.class);
//                    intent.putExtra(FEED_DATA, feedList.get(getAdapterPosition()));
//                    mContext.startActivityForResult(intent,Constants.COMMENT_COUNT);
//               }
//            });
//            lyShare.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mContext.setPosition(getAdapterPosition());
//                    if(Util.isConnected(mContext.getActivity())){
////                        presentr.setShare(feedList.get(getAdapterPosition()).getId());
//                    } else {
//                        Util.showToast(mContext.getResources().getString(R.string.msg_no_network),mContext.getActivity());
//                    }
//                    String message = "Title: " + feedList.get(getAdapterPosition()).getTitle() + "\n" +
//                            "Description: " + feedList.get(getAdapterPosition()).getDescription() + "\n" +
//                            "Media Url: " + feedList.get(getAdapterPosition()).getMediaRul();
//                    Intent share = new Intent(Intent.ACTION_SEND);
//                    share.setType("text/plain");
//                    share.putExtra(Intent.EXTRA_TEXT, message);
//                    mContext.startActivity(Intent.createChooser(share, "Share Information"));
//               }
//            });
//            lyLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mContext.setPosition(getAdapterPosition());
//                    if(Utils.isConnected(mContext.getActivity())){
//                        presentr.setLike(feedList.get(getAdapterPosition()).getId());
//                    } else {
//                        Utils.showToast(mContext.getActivity(),mContext.getResources().getString(R.string.msg_no_network));
//                    }
//               }
//            });
        }
    }
    private void enlargePhoto(String photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(mContext.getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
