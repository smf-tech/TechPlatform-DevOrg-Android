package com.octopusbjsindia.view.adapters;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
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
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.FormDisplayActivity;
import com.octopusbjsindia.view.activities.SELTrainingActivity;

import java.io.File;
import java.util.List;

public class SELAssignmentAdapter extends RecyclerView.Adapter<SELAssignmentAdapter.ViewHolder> {

    private List<com.octopusbjsindia.models.sel_content.SELAssignmentData> SELAssignmentData;
    private SELTrainingActivity mContext;
    private boolean isVideoSeen;

    public SELAssignmentAdapter(SELTrainingActivity context,
                                final List<SELAssignmentData> SELAssignmentData, boolean isVideoSeen) {
        mContext = context;
        this.SELAssignmentData = SELAssignmentData;
        this.isVideoSeen = isVideoSeen;
    }

    @NonNull
    @Override
    public SELAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sel_assignment_row,
                parent, false);
        return new SELAssignmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SELAssignmentAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(SELAssignmentData.get(position).getFormName());
        boolean isFileDownloaded = false;
        Uri uri = Uri.parse(SELAssignmentData.get(position).getAnswersPdfUrl());
        File file = new File(uri.getPath());
        String fileName = file.getName();
        if (isFileAvailable(fileName)) {
            isFileDownloaded = true;
            SELAssignmentData.get(position).setAnswersPdfName(fileName);
        }

        if (isFileDownloaded && !SELAssignmentData.get(position).isDownloadStarted()) {
            holder.imgView.setVisibility(View.VISIBLE);
            holder.imgDownload.setVisibility(View.GONE);
            holder.pbDownloading.setVisibility(View.GONE);
            holder.imgForm.setVisibility(View.GONE);
        } else {
            if (SELAssignmentData.get(position).isDownloadStarted()) {
                holder.pbDownloading.setVisibility(View.VISIBLE);
                holder.imgDownload.setVisibility(View.GONE);
                holder.imgView.setVisibility(View.GONE);
                holder.imgForm.setVisibility(View.GONE);
            } else {
                if (SELAssignmentData.get(position).isFormSubmitted()) {
                    holder.imgDownload.setVisibility(View.VISIBLE);
                    holder.imgForm.setVisibility(View.GONE);
                } else {
                    holder.imgDownload.setVisibility(View.GONE);
                    holder.imgForm.setVisibility(View.VISIBLE);
                }
                holder.imgView.setVisibility(View.GONE);
                holder.pbDownloading.setVisibility(View.GONE);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RelativeLayout rlAssignmentRow;
        ImageView imgDownload, imgView, imgForm;
        ProgressBar pbDownloading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            rlAssignmentRow = itemView.findViewById(R.id.rl_assignment_row);
            imgDownload = itemView.findViewById(R.id.img_download);
            imgView = itemView.findViewById(R.id.img_view);
            pbDownloading = itemView.findViewById(R.id.pbDownloading);
            imgForm = itemView.findViewById(R.id.iv_form);

            imgForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVideoSeen) {
                        if (SELAssignmentData.get(getAdapterPosition()).getFormId() != null &&
                                !TextUtils.isEmpty(SELAssignmentData.get(getAdapterPosition()).getFormId())) {

                            mContext.displayForm(SELAssignmentData.get(getAdapterPosition()).getFormId());

                        } else {
                            Util.showToast(mContext, "Something went wrong. Please try again later.");
                        }
                    } else {
                        Util.showToast(mContext, "Please watch complete video and then fill the assignment.");
                    }
                }
            });

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Permissions.isWriteExternalStoragePermission(mContext, mContext)) {
                        //mContext.setDownloadPosition(-1);
                        mContext.showDownloadPopup(SELAssignmentData.get(getAdapterPosition()).getAnswersPdfUrl(),
                                getAdapterPosition());
                    }
                }
            });

            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SELAssignmentData.get(getAdapterPosition()).getAnswersPdfName() != null &&
                            SELAssignmentData.get(getAdapterPosition()).getAnswersPdfName() != "") {
                        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                + Environment.DIRECTORY_DOWNLOADS;
                        File contentFile = new File(storagePath + "/" +
                                SELAssignmentData.get(getAdapterPosition()).getAnswersPdfName());
                        openFile(contentFile);
                    } else {
                        Util.snackBarToShowMsg(mContext.getWindow().getDecorView().
                                findViewById(android.R.id.content), "Please download file and then view.", Snackbar.LENGTH_LONG);
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

    @Override
    public int getItemCount() {
        return SELAssignmentData.size();
    }
}
