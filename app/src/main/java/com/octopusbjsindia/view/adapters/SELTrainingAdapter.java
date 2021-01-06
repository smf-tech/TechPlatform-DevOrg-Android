package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.models.sel_content.SELReadingData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.activities.SELTrainingActivity;

import java.io.File;
import java.util.List;

public class SELTrainingAdapter extends RecyclerView.Adapter<SELTrainingAdapter.ViewHolder> {

    private List<SELReadingData> SELReadingDataList;
    private List<SELAssignmentData> SELAssignmentData;
    private SELTrainingActivity mContext;
    private int type = 0;

    public SELTrainingAdapter(SELTrainingActivity context, final List<SELReadingData> SELReadingDataList, int type) {
        mContext = context;
        this.SELReadingDataList = SELReadingDataList;
        this.type = type;
    }

//    public SELTrainingAdapter(SELTrainingActivity context, final List<SELAssignmentData> SELAssignmentData) {
//        mContext = context;
//        this.SELAssignmentData = SELAssignmentData;
//    }

    @NonNull
    @Override
    public SELTrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_training_row,
                parent, false);
        return new SELTrainingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SELTrainingAdapter.ViewHolder holder, int position) {
        //if(type == 1) {
        holder.tvTitle.setText(SELReadingDataList.get(position).getContentName());
//        } else {
//            holder.tvTitle.setText(SELAssignmentData.get(position).getFormName());
//        }
        boolean isFileDownloaded = false;
        Uri uri = Uri.parse(SELReadingDataList.get(position).getContentUrl());
        File file = new File(uri.getPath());
        String fileName = file.getName();
        if (isFileAvailable(fileName)) {
            isFileDownloaded = true;
            //contentData.setDownloadedFileName(fileName);
        }

        if (isFileDownloaded && !SELReadingDataList.get(position).isDownloadStarted()) {
            holder.imgView.setVisibility(View.VISIBLE);
            holder.imgDownload.setVisibility(View.GONE);
            holder.pbDownloading.setVisibility(View.GONE);
        } else {
            if (SELReadingDataList.get(position).isDownloadStarted()) {
                holder.pbDownloading.setVisibility(View.VISIBLE);
                holder.imgDownload.setVisibility(View.GONE);
                holder.imgView.setVisibility(View.GONE);
            } else {
                holder.imgDownload.setVisibility(View.VISIBLE);
                holder.imgView.setVisibility(View.GONE);
                holder.pbDownloading.setVisibility(View.GONE);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgDownload, imgView;
        ProgressBar pbDownloading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            imgDownload = itemView.findViewById(R.id.img_download);
            imgView = itemView.findViewById(R.id.img_view);
            pbDownloading = itemView.findViewById(R.id.pbDownloading);

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_title) {
                        if (SELAssignmentData.get(getAdapterPosition()).getFormId() != null &&
                                !TextUtils.isEmpty(SELAssignmentData.get(getAdapterPosition()).getFormId())) {
                            Intent intent = new Intent(mContext, FormDisplayActivity.class);
                            intent.putExtra(Constants.PM.FORM_ID, SELAssignmentData.get(getAdapterPosition()).getFormId());
                            mContext.startActivity(intent);
                        } else {
                            Util.showToast(mContext, "Something went wrong. Please try again later.");
                        }
                    }
//                    Intent intent = new Intent(mContext, SELTrainingActivity.class);
//                    intent.putExtra("TrainingObject", selContentList.get(getAdapterPosition()));
//                    mContext.startActivity(intent);
                }
            });

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Permissions.isWriteExternalStoragePermission(mContext, mContext)) {
                        //mContext.setDownloadPosition(-1);
                        mContext.showDownloadPopup(SELReadingDataList.get(getAdapterPosition()).getContentUrl(), getAdapterPosition());
                    }
                }
            });
        }
    }

    private boolean isFileAvailable(String fileName) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + Environment.DIRECTORY_DOWNLOADS;
        File myFile = new File(storagePath + "/" + fileName);
        if (myFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return SELReadingDataList.size();
        } else {
            return SELAssignmentData.size();
        }
    }
}
