package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.octopusbjsindia.R;

import java.util.List;

public class ShowImagesPageRecyclerAdapter extends RecyclerView.Adapter<ShowImagesPageRecyclerAdapter.EmployeeViewHolder> {
    private List<String> dataList;
    private RequestOptions requestOptions;
    private Context mContext;
    private OnRequestItemClicked clickListener;
    private OnRequestItemRemoval onRequestItemRemoval;

    public ShowImagesPageRecyclerAdapter(Context context, List<String> receiveddataList, final OnRequestItemClicked clickListener, final OnRequestItemRemoval requestItemRemoval) {
        mContext = context;
        this.dataList = receiveddataList;
        this.clickListener = clickListener;
        this.onRequestItemRemoval = requestItemRemoval;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_profile_closeup_placeholder).centerInside();
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_profile_images_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        /*holder.txtTitle.setText("Batch");
        holder.txtValue.setText(String.valueOf(position+1));*/
        if (position == 0) {
            holder.img_remove_image.setVisibility(View.GONE);
        }
        if (dataList.size() > position) {
            if (!TextUtils.isEmpty(dataList.get(position))) {
                if (holder.pbLayout != null && holder.pbLayout.getVisibility() == View.GONE) {
                    holder.pbLayout.setVisibility(View.VISIBLE);
                }
                Glide.with(mContext)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(dataList.get(position))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (holder.pbLayout != null && holder.pbLayout.getVisibility() == View.VISIBLE) {
                                    holder.pbLayout.setVisibility(View.GONE);
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (holder.pbLayout != null && holder.pbLayout.getVisibility() == View.VISIBLE) {
                                    holder.pbLayout.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(holder.img_user_profle);
                holder.tv_photo_type.setVisibility(View.GONE);
                if (position == 0) {
                    holder.img_remove_image.setVisibility(View.GONE);
                } else {
                    holder.img_remove_image.setVisibility(View.VISIBLE);
                }
            } else {

                holder.tv_photo_type.setVisibility(View.VISIBLE);
                if (position == 0) {
                    holder.tv_photo_type.setText("Closeup Photo");

                } else if (position == 1) {
                    holder.tv_photo_type.setText("Full Length Photo");
                } else {
                    holder.tv_photo_type.setText("Medium Length Photo");
                }
                if (position == 0) {
                    holder.img_remove_image.setVisibility(View.GONE);
                } else {
                    holder.img_remove_image.setVisibility(View.VISIBLE);
                }
            }
        } else {
            /*Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load("")
                    .into(holder.img_user_profle);*/

            holder.img_remove_image.setVisibility(View.GONE);

            holder.tv_photo_type.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.img_user_profle.setImageResource(R.drawable.ic_profile_closeup_placeholder);
            } else if (position == 1) {
                holder.img_user_profle.setImageResource(R.drawable.ic_profile_fullview_placeholder);
            } else {
                holder.img_user_profle.setImageResource(R.drawable.ic_profile_multiple_placeholder);
            }
            if (position == 0) {
                holder.tv_photo_type.setText("Closeup Photo");
            } else if (position == 1) {
                holder.tv_photo_type.setText("Full Length Photo");
            } else {
                holder.tv_photo_type.setText("Medium Length Photo");
            }
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }

    public interface OnRequestItemRemoval {
        void onItemRemoved(int pos);
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtValue, tv_photo_type;
        ImageView img_user_profle, img_remove_image, img_edit_image;
        private RelativeLayout pbLayout;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            /*txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
            txtValue = (TextView) itemView.findViewById(R.id.tv_value);*/
            pbLayout = itemView.findViewById(R.id.progress_bar);
            tv_photo_type = (TextView) itemView.findViewById(R.id.tv_photo_type);
            img_remove_image = (ImageView) itemView.findViewById(R.id.img_remove_image);
            img_user_profle = (ImageView) itemView.findViewById(R.id.img_user_profle);
            img_edit_image = (ImageView) itemView.findViewById(R.id.img_edit_image);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            img_remove_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() < dataList.size())
                        onRequestItemRemoval.onItemRemoved(getAdapterPosition());
                }
            });
            img_edit_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }

    }
}
