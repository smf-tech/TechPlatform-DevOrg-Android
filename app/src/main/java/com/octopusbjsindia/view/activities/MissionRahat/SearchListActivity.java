package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorRequirementActivityPresenter;
import com.octopusbjsindia.presenter.MissionRahat.SearchListActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.mission_rahat.SearchListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchListActivity extends AppCompatActivity implements  APIDataListener {

    RelativeLayout ly_no_data;
    private int actionType = -1;
    private SearchListActivityPresenter presenter;
    private ArrayList<SearchListData> hospitalList = new ArrayList<>();
    private SearchListAdapter adapter;
    private RelativeLayout progressBar;
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new SearchListActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getHospitals();
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }
        setTitle("Select Hospital");
        actionType = getIntent().getIntExtra("actionType", -1);
        listView = findViewById(R.id.listView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        adapter = new SearchListAdapter(hospitalList, SearchListActivity.this);
        listView.setAdapter(adapter);

    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        ImageView back = findViewById(R.id.toolbar_back_action);
        tvTitle.setText(title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onSelected(int position) {
        if (actionType == Constants.MissionRahat.HOSPITAL_SELECTION_FOR_RESULT) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", position);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (actionType == Constants.MissionRahat.HOSPITAL_SELECTION_FOR_INCHARGE) {
            // call api to connect in charge to hospital
            HashMap request = new HashMap<String, Object>();
            request.put("hospital_id", hospitalList.get(position).getId());
            presenter.assignHospitaltoIncharge(request);
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(this, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(this, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        Util.showToast(this, response);
        finish();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void setHostpitalList(String get_hospitals, List<SearchListData> data) {
        hospitalList.addAll(data);
        //hospitalList = (ArrayList<SearchListData>) data;
        if (hospitalList.size() == 0)
            findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();

            }
        });

    }
}