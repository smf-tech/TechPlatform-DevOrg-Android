package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.pm.ProcessData;
import com.platform.models.pm.Processes;
import com.platform.presenter.PMActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.PMAdapter;

import java.util.List;

public class PMActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener,
        PMAdapter.OnProcessListItemClickListener {

    private final String TAG = PMActivity.class.getName();
    private PMActivityPresenter presenter;
    private List<ProcessData> processAllList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);

        presenter = new PMActivityPresenter(this);
        init();
    }

    private void init() {
        setActionbar(getString(R.string.programme_management));

        if (Util.isConnected(this)) {
            presenter.getAllProcess();
        } else {
            Log.i(TAG, "Handle offline condition");
        }
    }

    private void setActionbar(String title) {
        String str = title;
        if (str.contains("\n")) {
            str = str.replace("\n", " ");
        }

        findViewById(R.id.toolbar_more_action).setVisibility(View.GONE);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(str);

        ImageView backArrow = findViewById(R.id.toolbar_back_action);
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(this);
    }

    private void initViews(Processes process) {
        if (process != null) {
            processAllList = process.getData();

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            RecyclerView processList = findViewById(R.id.pm_forms_category_list);
            processList.setLayoutManager(mLayoutManager);

            PMAdapter pmAdapter = new PMAdapter(processAllList, this);
            processList.setAdapter(pmAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        initViews((Processes) data);
    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void onProcessListItemClickListener(int position) {
        ProcessData processData = processAllList.get(position);
        if (processData != null) {
            try {
                Intent intent = new Intent(this, ProcessListActivity.class);
                intent.putExtra(Constants.PM.PROCESS_ID, processData.getId());
                intent.putExtra(Constants.PM.PROCESS_NAME, processData.getName());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
