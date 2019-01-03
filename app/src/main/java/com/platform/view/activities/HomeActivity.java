package com.platform.view.activities;

import android.os.Bundle;
import android.view.View;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;

public class HomeActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void gotoNextScreen(String response) {

    }
}
