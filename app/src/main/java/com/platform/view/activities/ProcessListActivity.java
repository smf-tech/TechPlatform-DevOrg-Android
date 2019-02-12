package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.presenter.ProcessListActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;

public class ProcessListActivity extends BaseActivity implements PlatformTaskListener,
        View.OnClickListener {

    private final String TAG = ProcessListActivity.class.getName();
    private String processId, processName;
    private ProcessListActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);

        presenter = new ProcessListActivityPresenter(this);

        if (getIntent().getExtras() != null) {
            processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);
            processName = getIntent().getExtras().getString(Constants.PM.PROCESS_NAME);
        }

        setActionbar(processName);

        FloatingActionButton faButton = findViewById(R.id.fab_add_process);
        faButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getFormData();
    }

    private void setActionbar(String Title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(Title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }

    private void getFormData() {
        if (Util.isConnected(this)) {
            getAllProcess();
        }
    }

    private void getAllProcess() {
        presenter.getProcessList();
    }

    private void onAddClick() {
        try {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra(Constants.PM.PROCESS_ID, processId);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;

            case R.id.fab_add_process:
                onAddClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void showProgressBar() {
        Log.i(TAG, "Show progress bar...");
    }

    @Override
    public void hideProgressBar() {
        Log.i(TAG, "Hide progress bar...");
    }

    @Override
    public <T> void showNextScreen(T data) {
        Log.i(TAG, "Success : " + data);
    }

    @Override
    public void showErrorMessage(String result) {
        Log.e(TAG, "Error Message " + result);
    }
}
