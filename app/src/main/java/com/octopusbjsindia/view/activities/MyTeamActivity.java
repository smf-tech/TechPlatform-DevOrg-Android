package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Matrimony.SubordinateResponse;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.presenter.MyTeamActivityPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.SubordinateListAdapter;
import com.octopusbjsindia.view.adapters.TeamAttendanceAdapter;

public class MyTeamActivity extends AppCompatActivity implements APIDataListener {

    private MyTeamActivityPresenter presenter;
    private RecyclerView rvTeamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        presenter = new MyTeamActivityPresenter(this);

        presenter.getTeamList();

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("My Team");
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.fbAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTeamActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        rvTeamList = findViewById(R.id.rv_team_list);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message,this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        SubordinateResponse responseOBJ = new Gson().fromJson(response, SubordinateResponse.class);
        if(responseOBJ.getStatus() == 200){
            SubordinateListAdapter adapter;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            adapter = new SubordinateListAdapter(this, responseOBJ.getData());
            rvTeamList.setLayoutManager(mLayoutManager);
            rvTeamList.setAdapter(adapter);
        } else {
            Util.showToast(responseOBJ.getMessage(),this);
        }
    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.lyProgressBar).setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }
}
