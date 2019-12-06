package com.octopus.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopus.R;
import com.octopus.listeners.APIDataListener;
import com.octopus.models.events.CommonResponse;
import com.octopus.presenter.MatrimonyBookletActivityPresenter;
import com.octopus.utility.Util;

public class MatrimonyBookletActivity extends Activity implements View.OnClickListener, APIDataListener {
    private static final String TAG = MatrimonyBookletActivity.class.getName();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyBookletActivityPresenter matrimonyBookletActivityPresenter;
    private ImageView backButton, ivBooklet1, ivBooklet2, ivBooklet3;
    String meetId, bookletId;
    private Button btnCreateBooklet;
    private RelativeLayout rlTemp1, rlTemp2, rlTemp3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_booklet_layout);
        setActionbar("Create Booklet");
        meetId = getIntent().getStringExtra("meetId");
        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        backButton = findViewById(R.id.toolbar_back_action);
        backButton.setOnClickListener(this);
        ivBooklet1 = findViewById(R.id.iv_booklet1);
        ivBooklet1.setOnClickListener(this);
        ivBooklet2 = findViewById(R.id.iv_booklet2);
        ivBooklet2.setOnClickListener(this);
        ivBooklet3 = findViewById(R.id.iv_booklet3);
        ivBooklet3.setOnClickListener(this);
        btnCreateBooklet = findViewById(R.id.btn_create_booklet);
        btnCreateBooklet.setOnClickListener(this);
        rlTemp1 = findViewById(R.id.rl_temp1);
        rlTemp2 = findViewById(R.id.rl_temp2);
        rlTemp3 = findViewById(R.id.rl_temp3);
        matrimonyBookletActivityPresenter = new MatrimonyBookletActivityPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_booklet1:
                bookletId = "1";
                rlTemp1.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp2.setBackgroundResource(0);
                rlTemp3.setBackgroundResource(0);
                break;
            case R.id.iv_booklet2:
                bookletId = "2";
                rlTemp2.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp1.setBackgroundResource(0);
                rlTemp3.setBackgroundResource(0);
                break;
            case R.id.iv_booklet3:
                bookletId = "3";
                rlTemp3.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp1.setBackgroundResource(0);
                rlTemp2.setBackgroundResource(0);
                break;
            case R.id.btn_create_booklet:
                if(meetId!=null && bookletId!=null) {
                    matrimonyBookletActivityPresenter.downloadBooklet(meetId, bookletId);
                }
                break;
            case R.id.toolbar_back_action:
                onBackPressed();
                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onBackPressed() {
        try {
            hideProgressBar();
            setResult(RESULT_CANCELED);
            finish();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {;
        Util.snackBarToShowMsg(this.getWindow().getDecorView()
                        .findViewById(android.R.id.content), getString(R.string.msg_failure),
                Snackbar.LENGTH_LONG);
        Util.showToast(getString(R.string.msg_failure),this);
        onBackPressed();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        Util.showToast(getString(R.string.msg_failure),this);
            onBackPressed();
        }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseOBJ.getMessage(),
                Snackbar.LENGTH_LONG);
        Util.showToast(responseOBJ.getMessage(),this);
        onBackPressed();

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
