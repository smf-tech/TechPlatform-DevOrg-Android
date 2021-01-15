package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELReadingData;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.SELTrainingActivity;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class SELTrainingAdapter extends RecyclerView.Adapter<SELTrainingAdapter.ViewHolder> {

    private List<SELReadingData> SELReadingDataList;
    private SELTrainingActivity mContext;

    public SELTrainingAdapter(SELTrainingActivity context, final List<SELReadingData> SELReadingDataList) {
        mContext = context;
        this.SELReadingDataList = SELReadingDataList;
    }

    @NonNull
    @Override
    public SELTrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_training_row,
                parent, false);
        return new SELTrainingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SELTrainingAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(SELReadingDataList.get(position).getContentName());
        boolean isFileDownloaded = false;
        Uri uri = Uri.parse(SELReadingDataList.get(position).getContentUrl());
        File file = new File(uri.getPath());
        String fileName = file.getName();
        if (isFileAvailable(fileName)) {
            isFileDownloaded = true;
            SELReadingDataList.get(position).setDownloadedFileName(fileName);
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
        RelativeLayout rlTraining;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            imgDownload = itemView.findViewById(R.id.img_download);
            imgView = itemView.findViewById(R.id.img_view);
            pbDownloading = itemView.findViewById(R.id.pbDownloading);
            rlTraining = itemView.findViewById(R.id.rl_training);

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Permissions.isWriteExternalStoragePermission(mContext, mContext)) {
                        //mContext.setDownloadPosition(-1);
                        mContext.showDownloadPopup(SELReadingDataList.get(getAdapterPosition()).getContentUrl(), getAdapterPosition());
                    }
                }
            });

            rlTraining.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SELReadingDataList.get(getAdapterPosition()).getDownloadedFileName() != null &&
                            SELReadingDataList.get(getAdapterPosition()).getDownloadedFileName() != "") {
                        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                + Environment.DIRECTORY_DOWNLOADS;
                        File contentFile = new File(storagePath + "/" +
                                SELReadingDataList.get(getAdapterPosition()).getDownloadedFileName());
                        openFile(contentFile);
                    } else {
                        Util.snackBarToShowMsg(mContext.getWindow().getDecorView().
                                findViewById(android.R.id.content), "Please download file and then view.", Snackbar.LENGTH_LONG);
                    }
                }
            });
        }
    }

    private void openFile(File contentFile) {
        try {
            // Create URI
            Uri uri = FileProvider.getUriForFile(mContext,
                    mContext.getPackageName() + ".file_provider", contentFile);
            mContext.grantUriPermission
                    (mContext.getPackageName(), uri, FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setData(uri);
            intent.setType(get_mime_type(uri.toString()));
            if (contentFile.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, get_mime_type(uri.toString()));
            } else {
                //if you want you can also define the intent type for any other file
                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            Log.i("FileView", "222" + e.toString());
        }
    }

    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
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
            return SELReadingDataList.size();
    }
}
