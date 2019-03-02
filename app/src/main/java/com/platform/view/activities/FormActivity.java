package com.platform.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.fragments.FormFragment;

public class FormActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private FormFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gen_form);
        addFragment();
    }

    private void addFragment() {
        Bundle bundle = new Bundle();

        if (getIntent().getExtras() != null) {
            String processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);
            String formId = getIntent().getExtras().getString(Constants.PM.FORM_ID);
            boolean isPartialForm = getIntent().getExtras().getBoolean(Constants.PM.PARTIAL_FORM);
            bundle.putString(Constants.PM.PROCESS_ID, processId);
            bundle.putString(Constants.PM.FORM_ID, formId);
            bundle.putBoolean(Constants.PM.PARTIAL_FORM, isPartialForm);

            boolean readOnly = getIntent().getExtras().getBoolean(Constants.PM.EDIT_MODE);
            if (readOnly)
                bundle.putBoolean(Constants.PM.EDIT_MODE, true);
        }

        fragment = new FormFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.gen_form_container, fragment, "formFragment");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: FormActivity : addFragment");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
