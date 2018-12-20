package com.platform.view.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private final String TAG = BaseActivity.class.getSimpleName();

    public BaseActivity() {
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Destroy called...");
        super.onDestroy();
    }
}
