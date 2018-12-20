package com.platform.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.platform.R;
import com.platform.controller.GeneralFormActivityController;
import com.platform.listeners.GeneralFormActivityListener;
import com.platform.view.fragments.GeneralFormFragment;

public class GeneralFormActivity extends BaseActivity implements GeneralFormActivityListener {

    private GeneralFormFragment fragment;
    private GeneralFormActivityController formController;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formController = new GeneralFormActivityController(this);
        setContentView(R.layout.activity_gen_form);
        openNewFragment();
    }

    private void openNewFragment() {
        addFragment();
    }

    private void addFragment() {
        fragment = new GeneralFormFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.gen_form_container, fragment, "fragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        fragment = null;
        if (formController != null) {
            formController = null;
        }
        super.onDestroy();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}
