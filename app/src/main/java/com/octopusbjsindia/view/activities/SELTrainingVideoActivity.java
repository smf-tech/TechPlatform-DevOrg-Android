package com.octopusbjsindia.view.activities;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.octopusbjsindia.R;

public class SELTrainingVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private ProgressDialog pDialog;
    private int stopPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_training_video);

        videoView = findViewById(R.id.video_view);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Video Stream");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

        try {
            MediaController vidControl = new MediaController(this, false);
            vidControl.setAnchorView(videoView);
            vidControl.hide();
            videoView.setMediaController(vidControl);
            Uri vidUri = Uri.parse(vidAddress);
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

            }
        });
        //videoView.start();
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
        //videoView.seekTo(stopPosition);
        videoView.start();
    }
}