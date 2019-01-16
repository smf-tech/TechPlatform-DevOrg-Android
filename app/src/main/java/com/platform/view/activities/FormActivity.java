package com.platform.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.platform.R;
import com.platform.presenter.FormActivityPresenter;
import com.platform.utility.Constants;
import com.platform.view.fragments.FormFragment;

public class FormActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private FormFragment fragment;
    private FormActivityPresenter formPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gen_form);
        addFragment();
    }

    public FormActivityPresenter getFormPresenter() {
        if (formPresenter == null) {
            formPresenter = new FormActivityPresenter(this);
        }
        return formPresenter;
    }

    private void addFragment() {
        Bundle bundle = new Bundle();

        if (getIntent().getExtras() != null) {
            String data = getIntent().getExtras().getString(Constants.PM.PROCESS_DETAILS);
            bundle.putString(Constants.PM.PROCESS_DETAILS, data);
        }

        fragment = new FormFragment();
        fragment.setArguments(bundle);

        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.gen_form_container, fragment, "fragment");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: FormActivity : addFragment");
            e.printStackTrace();
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

        if (formPresenter != null) {
            formPresenter = null;
        }
        super.onDestroy();
    }
}
