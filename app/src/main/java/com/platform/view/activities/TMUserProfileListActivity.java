package com.platform.view.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.platform.R;
import com.platform.utility.Util;
import com.platform.view.fragments.TMUserAttendanceApprovalFragment;
import com.platform.view.fragments.TMUserFormsApprovalFragment;
import com.platform.view.fragments.TMUserLeavesApprovalFragment;
import com.platform.view.fragments.TMUserProfileApprovalFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class TMUserProfileListActivity extends BaseActivity {
    private FragmentManager fManager;
    private Fragment fragment;
    private String strRequestObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmuserprofilelist_layout);
        //receive intent data
        Bundle data = getIntent().getExtras();
        fManager = getSupportFragmentManager();
        if (data != null && data.containsKey("filter_type")) {
            String switchToFragment = data.getString("filter_type") != null
                    ? data.getString("filter_type") : "null";
            Util.showToast(switchToFragment, this);
            //get filter request object to get the details
            try {
                JSONObject requestObject = new JSONObject(data.getString("filter_type_request"));
                strRequestObject =data.getString("filter_type_request");
                Util.logger(getLocalClassName().toString(),strRequestObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            selectApprovalTypeFragment(switchToFragment);

        }

    }

    private void selectApprovalTypeFragment(String switchToFragment) {
        if (!TextUtils.isEmpty(switchToFragment)) {
            Bundle bundle = new Bundle();
            switch (switchToFragment) {
                case "userapproval":
                    fragment = new TMUserProfileApprovalFragment();
                    bundle.putString("filter_type", "User Approval");
                    bundle.putString("filter_type_request",strRequestObject);
                    fragment.setArguments(bundle);
                    openFragment();
                    break;

                case "forms":
                    fragment = new TMUserFormsApprovalFragment();
                    bundle.putString("filter_type", "Forms Approval");
                    bundle.putString("filter_type_request",strRequestObject);
                    fragment.setArguments(bundle);
                    openFragment();
                    break;

                case "attendance":
                    fragment = new TMUserAttendanceApprovalFragment();
                    bundle.putString("filter_type", "Attendance Approval");
                    bundle.putString("filter_type_request",strRequestObject);
                    fragment.setArguments(bundle);
                    openFragment();
                    break;

                case "leave":
                    fragment = new TMUserLeavesApprovalFragment();
                    bundle.putString("filter_type", "Leaves Approval");
                    bundle.putString("filter_type_request",strRequestObject);
                    fragment.setArguments(bundle);
                    openFragment();
                    break;
            }
        }
    }

    private void openFragment() {
        Log.d("testLOg", "@@@@@---" + fragment.getTag());
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.container_fragment, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
