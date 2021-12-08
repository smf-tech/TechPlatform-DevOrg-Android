package com.octopusbjsindia.view.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.sel_content.SELAssignmentData;
import com.octopusbjsindia.models.sel_content.SELVideoContent;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SELAssignmentAdapter;
import com.octopusbjsindia.view.adapters.SELTrainingAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SELTrainingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvReadingContent, rvFormAssignment;
    private SELVideoContent trainingObject;
    private SELTrainingAdapter selTrainingAdapter;
    private SELAssignmentAdapter selAssignmentAdapter;
    private RequestOptions requestOptions;
    private int downloadPosition = -1;
    private DownloadManager downloadmanager;
    private long downloadID;
    private String filename;
    private ArrayList<SELAssignmentData> assignmentList= new ArrayList<>();
    private List<Long> downloadIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_training_video);
        trainingObject = (SELVideoContent) getIntent().getSerializableExtra("TrainingObject");
        initView();
    }

    private void initView() {
        this.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(trainingObject.getTitle());
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);

        //TextView tvTrainingTitle = findViewById(R.id.tv_title);
        ImageView ivThumbnail = findViewById(R.id.iv_thumbnail);
        rvReadingContent = findViewById(R.id.rv_reading_content);
        rvFormAssignment = findViewById(R.id.rv_form_assignment);

        //tvTrainingTitle.setText(trainingObject.getTitle());
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_no_image);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
        if (trainingObject.getThumbnailUrl() != null) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(trainingObject.getThumbnailUrl())
                    .into(ivThumbnail);
        }
        ivThumbnail.setOnClickListener(this);

        if(trainingObject.getReadingDataList()!= null && trainingObject.getReadingDataList().size()>0) {
            selTrainingAdapter = new SELTrainingAdapter(this, trainingObject.getReadingDataList());
            rvReadingContent.setLayoutManager(new LinearLayoutManager(this));
            rvReadingContent.setAdapter(selTrainingAdapter);
        } else {
            findViewById(R.id.tv_reading_label).setVisibility(View.GONE);
            rvReadingContent.setVisibility(View.GONE);
        }
        if(trainingObject.getAssignmentList()!= null && trainingObject.getAssignmentList().size()>0) {
            selAssignmentAdapter = new SELAssignmentAdapter(this,
                    trainingObject.getAssignmentList(), trainingObject.isVideoSeen());
            rvFormAssignment.setLayoutManager(new LinearLayoutManager(this));
            rvFormAssignment.setAdapter(selAssignmentAdapter);
        } else {
            findViewById(R.id.tv_assignment_label).setVisibility(View.GONE);
            rvFormAssignment.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_thumbnail) {
            if (trainingObject.getVideoUrl() != null &&
                    !TextUtils.isEmpty(trainingObject.getVideoUrl())) {
                if (Util.isConnected(this)) {
                    Intent intent = new Intent(this, SELTrainingVideoActivity.class);
                    intent.putExtra("videoId", trainingObject.getId());
                    intent.putExtra("videoUrl", trainingObject.getVideoUrl());
                    startActivity(intent);
                } else {
                    Util.showToast(this, getString(R.string.msg_no_network));
                }
            } else {
                Util.showToast(this, "Something went wrong. Please try again later.");
            }
        } else if (v.getId() == R.id.toolbar_back_action) {
            finish();
        }
    }

    public void showDownloadPopup(String downloadUrl, int position) {
        downloadPosition = position;

        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(getString(R.string.app_name_ss));
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText("Are you sure you want to download this file?");
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Cancel");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText("OK");
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            if (Util.isConnected(this)) {
                beginDownload(downloadUrl, position);
            } else {
                Util.showToast(getString(R.string.msg_no_network), this);
            }
            //downloadPosition = -1;
            // Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
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
    public void displayForm(String formId){
        Intent intent = new Intent(this, FormDisplayActivity.class);
        intent.putExtra(Constants.PM.FORM_ID, formId);
        startActivityForResult(intent,1001);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if(resultCode == Activity.RESULT_OK){
                String id=data.getStringExtra("id");
                for(int i=0;i<assignmentList.size();i++){
                    if(id.equalsIgnoreCase(assignmentList.get(i).getFormId())){
                        assignmentList.get(i).setFormSubmitted(true);
                        selAssignmentAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}