package com.octopusbjsindia.view.activities;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.presenter.SELTrainingVideoActivityPresenter;
import com.octopusbjsindia.utility.Util;

public class SELTrainingVideoActivity extends AppCompatActivity implements APIDataListener {

    private VideoView videoView;
    private ProgressDialog pDialog;
    private int stopPosition;
    private SELTrainingVideoActivityPresenter presenter;
    private SELTrainingVideoActivity mContext;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private String videoId;
    private boolean isVideoCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_training_video);
        presenter = new SELTrainingVideoActivityPresenter(this);
        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        videoView = findViewById(R.id.video_view);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Video Stream");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        String videoUrl = getIntent().getStringExtra("videoUrl");
        videoId = getIntent().getStringExtra("videoId");
        //String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

        try {
            MediaController vidControl = new MediaController(this, false);
            vidControl.setAnchorView(videoView);
            vidControl.hide();
            videoView.setMediaController(vidControl);
            Uri vidUri = Uri.parse(videoUrl);
            videoView.setVideoURI(vidUri);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        //videoView.setBackgroundResource(R.color.base_background_color);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isVideoCompleted = true;
                presenter.sendVideoStatus(videoId, "completed", videoView.getCurrentPosition());
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                pDialog.dismiss();
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        Log.e("stop_position", String.valueOf(stopPosition));
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pDialog.show();
        videoView.seekTo(stopPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isVideoCompleted) {
            presenter.sendVideoStatus(videoId, "resume", stopPosition);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.snackBarToShowMsg(getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.msg_failure),
                Snackbar.LENGTH_LONG);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.snackBarToShowMsg(getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.msg_failure),
                Snackbar.LENGTH_LONG);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Util.snackBarToShowMsg(getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.msg_failure),
                Snackbar.LENGTH_LONG);
        finish();
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }
}