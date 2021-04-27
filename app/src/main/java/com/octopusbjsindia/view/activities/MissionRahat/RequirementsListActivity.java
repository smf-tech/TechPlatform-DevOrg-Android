package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.MissionRahat.RequirementsListData;
import com.octopusbjsindia.models.MissionRahat.RequirementsListResponse;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.RequirementsListActivityPresenter;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MatrimonyProfileListActivity;
import com.octopusbjsindia.view.adapters.mission_rahat.RequirementsListAdapter;
import com.octopusbjsindia.view.adapters.mission_rahat.SearchListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RequirementsListActivity extends AppCompatActivity implements APIDataListener {

    RequirementsListActivityPresenter presenter;
    RelativeLayout progressBar;
    RecyclerView rvRequestList;
    ArrayList<RequirementsListData> list = new ArrayList<>();
    RequirementsListAdapter adapter;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private String nextPageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirements_list);

        setTitle("Requirements List");

        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new RequirementsListActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getRequirementsList( BuildConfig.BASE_URL + Urls.MissionRahat.CONCENTRATOR_REQUEST_LIST);
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }
        rvRequestList = findViewById(R.id.rvRequestList);
        adapter = new RequirementsListAdapter(list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvRequestList.setLayoutManager(layoutManager);
        rvRequestList.setAdapter(adapter);

        rvRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
//                            getData();
                            if (nextPageUrl != null && !TextUtils.isEmpty(nextPageUrl)) {
                                presenter.getRequirementsList( nextPageUrl);
                            }
                        }
                    }
                }

//                if (dy < -5 && (this).isFilterApplied() &&
//                        btnClearFilters.getVisibility() != View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.VISIBLE);
//                } else if (dy > 5 && (!((MatrimonyProfileListActivity) getActivity()).isFilterApplied()) &&
//                        btnClearFilters.getVisibility() == View.VISIBLE) {
//                    btnClearFilters.setVisibility(View.GONE);
//                }
            }
        });

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

        try {
            if (response != null) {
                RequirementsListResponse data = new Gson().fromJson(response, RequirementsListResponse.class);
                if (data.getCode() == 200) {
                    if (requestID.equalsIgnoreCase("RequirementsList")) {
                        nextPageUrl = data.getNextPageUrl();
                        list.addAll(data.getData());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (data.getCode() == 1000) {
                        Util.logOutUser(this);
                    } else {
                        onFailureListener(requestID, data.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            onFailureListener(requestID, e.getMessage());
        }
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

    public void setList(List<RequirementsListData> data) {
    }
}