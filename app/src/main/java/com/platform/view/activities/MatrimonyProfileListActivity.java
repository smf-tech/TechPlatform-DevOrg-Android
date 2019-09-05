package com.platform.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.Matrimony.UserProfileList;
import com.platform.presenter.MatrimonyProfilesListActivityPresenter;
import com.platform.view.adapters.MatrimonyProfileListRecyclerAdapter;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class MatrimonyProfileListActivity extends BaseActivity implements View.OnClickListener, MatrimonyProfileListRecyclerAdapter.OnRequestItemClicked {


    private MatrimonyProfilesListActivityPresenter tmFilterListActivityPresenter;
    private MatrimonyProfileListRecyclerAdapter matrimonyProfileListRecyclerAdapter;
    private RecyclerView rv_matrimonyprofileview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofilelist_layout);
        //receive intent data
        //filterTypeReceived = getIntent().getStringExtra("filter_type");
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        //initViews();
        tmFilterListActivityPresenter.getAllFiltersRequests();
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



        /*    DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList.clear();
            this.pendingRequestList.addAll(pendingRequestList);



            *//*mAdapter = new TMPendingApprovalPageRecyclerAdapter(getActivity(), pendingRequestList,
                    pendingFragmentPresenter, this);*/
            matrimonyProfileListRecyclerAdapter = new MatrimonyProfileListRecyclerAdapter(this, pendingRequestList,
                    this);
            rv_matrimonyprofileview.setAdapter(matrimonyProfileListRecyclerAdapter);
        } else {
        /*    DashboardFragment.setApprovalCount(0);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
            rvPendingRequests.setVisibility(View.GONE);
        }

        if (getParentFragment() != null && getParentFragment() instanceof DashboardFragment) {
            ((DashboardFragment) getParentFragment()).updateBadgeCount();
        }*/
        }
    }


    @Override
    public void onItemClicked(int pos) {

    }
}
