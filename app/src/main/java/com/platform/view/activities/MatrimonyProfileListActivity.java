package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.Matrimony.UserProfileList;
import com.platform.presenter.MatrimonyProfilesListActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.adapters.MatrimonyProfileListRecyclerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileListActivity extends BaseActivity implements View.OnClickListener, MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked, MatrimonyProfileListRecyclerAdapter.OnApproveRejectClicked {

    private MatrimonyProfilesListActivityPresenter tmFilterListActivityPresenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;
    private ArrayList<UserProfileList> userProfileLists;
    private String approvalType;
    private String meetIdReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofilelist_layout);
        //receive intent data
        meetIdReceived = getIntent().getStringExtra("meetid");
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        tmFilterListActivityPresenter.getAllFiltersRequests(meetIdReceived);
    }

    private void initViews() {

        rv_matrimonyprofileview = findViewById(R.id.rv_matrimonyprofileview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rv_matrimonyprofileview.setLayoutManager(layoutManager);

        tmFilterListActivityPresenter = new MatrimonyProfilesListActivityPresenter(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_filtertype:
                //onBackPressed();
                break;
            case R.id.img_filter_image:

                break;
        }
    }

    /*private void setActionbar(String title) {
        if (title.contains("\n")) {
            title = title.replace("\n", " ");
        }

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);

        ImageView img_back = findViewById(R.id.toolbar_back_action);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
    }
*/

    public void showPendingApprovalRequests(List<UserProfileList> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            userProfileLists = (ArrayList<UserProfileList>) pendingRequestList;
            matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(this, pendingRequestList,
                    this, this);
            rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);
        } else {

        }
    }

    @Override
    public void onItemClicked(int pos) {
        Util.showToast("item ->" + pos + " Open detais", this);
        UserProfileList userProfileList = userProfileLists.get(pos);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userProfileList);
        Intent startMain1 = new Intent(MatrimonyProfileListActivity.this, MatrimonyProfileDetailsActivity.class);
        startMain1.putExtra("filter_type", jsonInString);
        startActivity(startMain1);
    }

    @Override
    public void onApproveClicked(int pos) {
        Util.showToast("item ->" + pos + " Approve Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.APPROVE);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0, Constants.APPROVE);
    }

    @Override
    public void onRejectClicked(int pos) {
        Util.showToast("item ->" + pos + " Reject Clicked", this);
        UserProfileList userProfileList = userProfileLists.get(pos);

        JSONObject jsonObject = tmFilterListActivityPresenter.createBodyParams(meetIdReceived, "user", userProfileList.get_id(), Constants.REJECT);
        tmFilterListActivityPresenter.approveRejectRequest(jsonObject, 0, Constants.REJECT);
    }

    public void updateRequestStatus(String response, int position) {
        if (Constants.REJECT.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.APPROVE);
        }
        if (Constants.APPROVE.equalsIgnoreCase(approvalType)) {
            userProfileLists.get(position).setIsApproved(Constants.REJECT);
        }
    }
}
