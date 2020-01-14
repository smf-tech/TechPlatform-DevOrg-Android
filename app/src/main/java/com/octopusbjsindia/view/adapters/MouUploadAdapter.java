package com.octopusbjsindia.view.adapters;

import android.app.Dialog;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.MouUploadData;
import com.octopusbjsindia.view.fragments.MouUploadFragment;

import java.util.List;

public class MouUploadAdapter extends RecyclerView.Adapter<MouUploadAdapter.ViewHolder> {

    private List<Uri> mouUriList;
    private MouUploadFragment fragment;

    public MouUploadAdapter(final List<Uri> mouUriList, MouUploadFragment fragment) {
        this.mouUriList = mouUriList;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public MouUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mou_upload_item,
                parent, false);
        return new MouUploadAdapter.ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMou, imgRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMou = itemView.findViewById(R.id.img_mou);
            imgRemove = itemView.findViewById(R.id.img_remove);
            imgMou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition()+1 == mouUriList.size()) {
                        fragment.onAddImageClick();
                    } else {
                        enlargePhoto(mouUriList.get(getAdapterPosition()));
                    }
                }
            });
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.removeUri(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MouUploadAdapter.ViewHolder viewHolder, int position) {
        if(mouUriList.get(position)!= null) {
            viewHolder.imgMou.setImageURI(mouUriList.get(position));
        } else {
            viewHolder.imgMou.setImageResource(R.drawable.ic_add_img);
        }
        if(position+1 == mouUriList.size()) {
            viewHolder.imgRemove.setVisibility(View.GONE);
        } else {
            viewHolder.imgRemove.setVisibility(View.VISIBLE);
        }
    }

    private void enlargePhoto(Uri photoUrl) {
        // stop the video if playing

        final Dialog dialog = new Dialog(fragment.getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(fragment.getActivity());
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
        if (photoUrl!= null) {
            try {
                Glide.with(fragment.getActivity())
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

    @Override
    public int getItemCount() {
        return mouUriList.size();
    }
}
