package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.os.Bundle;
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

public class SELTrainingActivity extends AppCompatActivity implements View.OnClickListener {
    private String videoId;
    private boolean isVideoCompleted = false;
    private RecyclerView rvReadingContent, rvFormAssignment;
    private SELVideoContent trainingObject;
    private SELTrainingAdapter selTrainingAdapter;
    private RequestOptions requestOptions;

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
}