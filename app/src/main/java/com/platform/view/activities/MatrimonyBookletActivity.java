package com.platform.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.presenter.MatrimonyBookletFragmentPresenter;

public class MatrimonyBookletActivity extends Activity implements View.OnClickListener{
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MatrimonyBookletFragmentPresenter matrimonyMeetFragmentPresenter;
    private ImageView ivBooklet1, ivBooklet2, ivBooklet3;
    String meetId, bookletId;
    private Button btnCreateBooklet;
    private RelativeLayout rlTemp1, rlTemp2, rlTemp3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_booklet_layout);

        setActionbar("Create Booklet");

        meetId = getIntent().getStringExtra("meetId");
        bookletId = getIntent().getStringExtra("bookletId");
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
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_booklet1:
                rlTemp1.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp2.setBackgroundResource(0);
                rlTemp3.setBackgroundResource(0);
                break;
            case R.id.iv_booklet2:
                rlTemp2.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp1.setBackgroundResource(0);
                rlTemp3.setBackgroundResource(0);
                break;
            case R.id.iv_booklet3:
                rlTemp3.setBackgroundResource(R.drawable.bg_rect_primary_border);
                rlTemp1.setBackgroundResource(0);
                rlTemp2.setBackgroundResource(0);
                break;
            case R.id.btn_create_booklet:
                break;
        }
    }
}
