package com.octopusbjsindia.view.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SELTrainingAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SELTrainingActivity extends AppCompatActivity implements View.OnClickListener {
    //    private String videoId;
//    private boolean isVideoCompleted = false;
    private RecyclerView rvReadingContent, rvFormAssignment;
    private SELVideoContent trainingObject;
    private SELTrainingAdapter selTrainingAdapter;
    private RequestOptions requestOptions;
    private int downloadPosition = -1;
    private DownloadManager downloadmanager;
    private long downloadID;
    private String filename;
    private List<Long> downloadIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_training_video);
        trainingObject = (SELVideoContent) getIntent().getSerializableExtra("TrainingObject");
        //String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        initView();
    }

    private void initView() {
        TextView tvTrainingTitle = findViewById(R.id.tv_title);
        ImageView ivThumbnail = findViewById(R.id.iv_thumbnail);
        rvReadingContent = findViewById(R.id.rv_reading_content);
        rvFormAssignment = findViewById(R.id.rv_form_assignment);

        tvTrainingTitle.setText(trainingObject.getTitle());
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
        if (trainingObject.getThumbnailUrl() != null) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(trainingObject.getThumbnailUrl())
                    .into(ivThumbnail);
        }
        ivThumbnail.setOnClickListener(this);

        selTrainingAdapter = new SELTrainingAdapter(this, trainingObject.getReadingDataList(), 1);
        rvReadingContent.setLayoutManager(new LinearLayoutManager(this));
        rvReadingContent.setAdapter(selTrainingAdapter);

//        selTrainingAdapter = new SELTrainingAdapter(this, trainingObject.getAssignmentList());
//        rvFormAssignment.setLayoutManager(new LinearLayoutManager(this));
//        rvFormAssignment.setAdapter(selTrainingAdapter);
    }

    @Override
    public void onClick(View v) {
        if (trainingObject.getVideoUrl() != null &&
                !TextUtils.isEmpty(trainingObject.getVideoUrl())) {
            Intent intent = new Intent(this, SELTrainingVideoActivity.class);
            intent.putExtra("videoId", trainingObject.getId());
            intent.putExtra("videoUrl", trainingObject.getVideoUrl());
            startActivity(intent);
        } else {
            Util.showToast(this, "Something went wrong. Please try again later.");
        }
    }

    public void setDownloadPosition(int downloadPosition) {
        this.downloadPosition = downloadPosition;
    }

    public void showDownloadPopup(String downloadUrl, int position) {
        //ArrayList<DownloadLanguageSelection> list = new ArrayList<>();
        downloadPosition = position;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.app_name_ss));
        alertDialog.setMessage(getString(R.string.msg_rejection_reason));
        alertDialog.setIcon(R.mipmap.app_logo);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(android.R.string.yes, null);
        alertDialog.setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                if (Util.isConnected(this)) {
                    beginDownload(downloadUrl, position);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), this);
                }
                //downloadPosition = -1;
                dialog.dismiss();
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialogInterface.dismiss());
        });
        dialog.show();
    }

    public void beginDownload(String url, int position) {
        downloadmanager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        File file = new File(uri.getPath());
        filename = file.getName();

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Octopus");
        request.setDescription("Downloading");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadID = downloadmanager.enqueue(request);
        downloadIdList.add(downloadID);

        Cursor cursor = downloadmanager.query(new DownloadManager.Query().setFilterById(downloadID));

        if (cursor != null && cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            cursor.close();

            if (status == DownloadManager.STATUS_FAILED) {
                // do something when failed
                trainingObject.getReadingDataList().get(position).setDownloadStarted(false);
            } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                // do something pending or paused
                trainingObject.getReadingDataList().get(position).setDownloadStarted(true);
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // do something when successful
            } else if (status == DownloadManager.STATUS_RUNNING) {
                // do something when running
                trainingObject.getReadingDataList().get(position).setDownloadStarted(true);
            }
        }
        selTrainingAdapter.notifyDataSetChanged();
    }

    // broadcast receiver for download a file
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent data = intent;
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Util.showToast("Download completed.", this);
                if (downloadPosition != -1) {
                    trainingObject.getReadingDataList().get(downloadPosition).setDownloadStarted(false);
                }
                selTrainingAdapter.notifyDataSetChanged();
            }
        }
    };
}