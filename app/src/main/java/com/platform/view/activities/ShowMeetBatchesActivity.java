package com.platform.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.Matrimony.MeetBatchesResponseModel;
import com.platform.models.login.LoginInfo;
import com.platform.utility.Constants;
import com.platform.view.adapters.ShowBatchesPageRecyclerAdapter;
import com.platform.view.fragments.NewOtpFragment;

public class ShowMeetBatchesActivity extends BaseActivity implements View.OnClickListener,ShowBatchesPageRecyclerAdapter.OnRequestItemClicked {

    private final String TAG = ShowMeetBatchesActivity.class.getName();
    private LoginInfo loginInfo;
    private RecyclerView rvBaches;
    private TextView txtNoData;
    private ShowBatchesPageRecyclerAdapter mAdapter;
    private MeetBatchesResponseModel meetBatchesResponseModel;
    private String meetBatchesResponseString;

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

        Gson gson = new Gson();

        meetBatchesResponseModel = gson.fromJson(meetBatchesResponseString, MeetBatchesResponseModel.class);

        txtNoData = findViewById(R.id.txt_no_data);
        txtNoData.setText("No Batches available yet.");
        rvBaches = findViewById(R.id.rvLandingPageView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        rvBaches.setLayoutManager(layoutManager);

        mAdapter = new ShowBatchesPageRecyclerAdapter(this, meetBatchesResponseModel.getGroup(),this::onItemClicked);
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
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClicked(int pos) {

    }
}
