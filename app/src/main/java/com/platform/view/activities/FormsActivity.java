package com.platform.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.platform.R;
import com.platform.view.fragments.AllFormsFragment;
import com.platform.view.fragments.CompletedFormsFragment;
import com.platform.view.fragments.PendingFormsFragment;

@SuppressWarnings("EmptyMethod")
public class FormsActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forms);

        initToolbar();
        initTabView();
    }

    @SuppressWarnings("deprecation")
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.forms);
        setSupportActionBar(toolbar);
        toolbar.setTitleMarginStart(50);// Use 300 for center allocation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.blue_theme_color));
        toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    @SuppressWarnings("deprecation")
    private void initTabView() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FormsActivity.ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = AllFormsFragment.newInstance();
                    break;

                case 1:
                    fragment = PendingFormsFragment.newInstance();
                    break;

                case 2:
                    fragment = CompletedFormsFragment.newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "All";
                    break;

                case 1:
                    title = "Pending";
                    break;

                case 2:
                    title = "Completed";
                    break;
            }

            return title.toUpperCase();
        }
    }
}
