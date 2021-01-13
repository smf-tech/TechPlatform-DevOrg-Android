package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.octopusbjsindia.R;

public class YouTubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    final String API_KEY = "AIzaSyDc6rySe4b9YOnOWISrcsCZzakSJTFKxeo";
    private static final int RECOVERY_REQUEST = 1;
    String TAG = "VideoActivity";
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_video);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeview);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        String videoLink = getIntent().getStringExtra("videoId");
        Log.e(TAG, "videoId" + videoLink);
        String videoId = videoLink.substring(videoLink.lastIndexOf("=") + 1);
        youTubePlayer.loadVideo(videoId.trim());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Toast.makeText(this, "Error Intializing Youtube Player", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }

}