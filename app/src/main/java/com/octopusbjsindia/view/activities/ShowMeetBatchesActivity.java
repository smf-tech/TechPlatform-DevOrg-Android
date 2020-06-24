package com.octopusbjsindia.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.MeetBatchesResponseModel;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ShowBatchesPageRecyclerAdapter;
import com.octopusbjsindia.view.fragments.ShowBachesDialogFragment;

public class ShowMeetBatchesActivity extends BaseActivity implements View.OnClickListener, ShowBatchesPageRecyclerAdapter.OnRequestItemClicked {
    private final String TAG = ShowMeetBatchesActivity.class.getName();
    private FragmentManager fManager;
    private Fragment fragment;
    private LoginInfo loginInfo;
    private RecyclerView rvBaches;
    private TextView txtNoData;
    private ShowBatchesPageRecyclerAdapter mAdapter;
    private MeetBatchesResponseModel meetBatchesResponseModel;
    private String meetBatchesResponseString;
    private ImageView toolbar_back_action;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_batches_layout);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            meetBatchesResponseString = bundle.getString("batches_resposne");
        }
        fManager = getSupportFragmentManager();
        Gson gson = new Gson();

        meetBatchesResponseModel = gson.fromJson(meetBatchesResponseString, MeetBatchesResponseModel.class);

        txtNoData = findViewById(R.id.txt_no_data);
        txtNoData.setText("No Batches available yet.");
        txtNoData.setVisibility(View.GONE);
        rvBaches = findViewById(R.id.rvLandingPageView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        rvBaches.setLayoutManager(layoutManager);

        mAdapter = new ShowBatchesPageRecyclerAdapter(this, meetBatchesResponseModel.getData().getGroup(), this::onItemClicked);
        rvBaches.setAdapter(mAdapter);

       /* try {
//            OtpFragment otpFragment = new OtpFragment();
            NewOtpFragment otpFragment = NewOtpFragment.newInstance(loginInfo);
            Bundle data = new Bundle();
            data.putParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA, loginInfo);
            otpFragment.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.otp_form_container, otpFragment).commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: OtpActivity : initView");
        }*/

        toolbar_back_action = findViewById(R.id.toolbar_back_action);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Meet Batches");
        toolbar_back_action.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                if (fManager.getBackStackEntryCount() > 0) {
                    fManager.popBackStack();
                    toolbar_title.setText("Meet Batches");
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemClicked(int pos) {
        Bundle bundle = new Bundle();
        meetBatchesResponseModel.getData().getGroup().get(pos);
        if (meetBatchesResponseModel.getData().getGroup().get(pos).getMale().get(0).size()>0 ||
                meetBatchesResponseModel.getData().getGroup().get(pos).getFemale().get(0).size()>0) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(meetBatchesResponseModel.getData().getGroup().get(pos));
            Log.e(TAG, "Exception :: OtpActivity : initView");
            fragment = new ShowBachesDialogFragment();
            bundle.putString("filter_type", "User Approval");
            bundle.putString("bachesObjectString", jsonString);
            fragment.setArguments(bundle);
            openFragment();

            toolbar_title.setText("Batch " + (pos + 1));
        }else {
            Util.showToast("No profiles under this batch yet.",this);
        }

/*        FragmentManager fm = getSupportFragmentManager();
//fragment class name : DFragment
        ShowBachesDialogFragment dFragment = new ShowBachesDialogFragment();
        // Show DialogFragment
        dFragment.setArguments(bundle);
        dFragment.show(fm, "Dialog Fragment");*/
        //fragment.show(fManager, "Dialog Fragment");

    }

    private void openFragment() {
        Log.d("testLOg", "@@@@@---" + fragment.getTag());
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.addToBackStack("showbatch");
        fTransaction.replace(R.id.home_page_container, fragment)
                .commit();
    }
}
