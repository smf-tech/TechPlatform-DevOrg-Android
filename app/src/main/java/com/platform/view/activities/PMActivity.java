package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class PMActivity extends BaseActivity implements PlatformTaskListener, View.OnClickListener,
        PMAdapter.OnProcessListItemClickListener {

    private final String TAG = PMActivity.class.getName();
    private PMActivityPresenter presenter;

    private int lastExpandedPosition = -1;
    private ArrayList<String> processCategory = new ArrayList<>();
    private HashMap<String, List<ProcessData>> childList = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);

        presenter = new PMActivityPresenter(this);
        init();
    }

    private void init() {
        setActionbar(getString(R.string.programme_management));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.pm_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Util.isConnected(this)) {
                presenter.getAllProcess();
            }
        });

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

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(str);

        ImageView backArrow = findViewById(R.id.toolbar_back_action);
        backArrow.setVisibility(View.VISIBLE);
        backArrow.setOnClickListener(this);
    }

    private void initViews(Processes process) {
        if (process != null) {
            processCategory.clear();
            childList.clear();

            for (ProcessData data : process.getData()) {
                String categoryName = data.getCategory().getName();
                if (childList.containsKey(categoryName)) {
                    List<ProcessData> processData = childList.get(categoryName);
                    if (processData != null) {
                        processData.add(data);
                    }
                    childList.put(categoryName, processData);
                } else {
                    List<ProcessData> processData = new ArrayList<>();
                    processData.add(data);
                    childList.put(categoryName, processData);
                    processCategory.add(categoryName);
                }
            }

            PMAdapter pmAdapter = new PMAdapter(this, processCategory, childList, this);
            ExpandableListView expListView = findViewById(R.id.rv_process);
            expListView.setAdapter(pmAdapter);

            expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> false);
            expListView.setOnGroupExpandListener(groupPosition -> {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            });
            expListView.setOnGroupCollapseListener(groupPosition -> {});
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
    public void onProcessListItemClickListener(ProcessData processData) {
        if (processData != null) {
            try {
                Intent intent = new Intent(this, ProcessListActivity.class);
                intent.putExtra(Constants.PM.PROCESS_ID, processData.getId());
                intent.putExtra(Constants.PM.PROCESS_NAME, processData.getName());
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
