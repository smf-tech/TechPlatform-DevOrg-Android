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
import android.view.View;
import android.widget.TextView;

import com.platform.R;
import com.platform.view.fragments.AllFormsFragment;
import com.platform.view.fragments.CompletedFormsFragment;
import com.platform.view.fragments.PendingFormsFragment;

import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;

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
        tabs.setSelectedTabIndicator(null);

        for (int j = 0; j < tabs.getTabCount(); j++) {
            TabLayout.Tab tabAt = tabs.getTabAt(j);
            assert tabAt != null;
            tabAt.setCustomView(R.layout.form_tab_view);
            View view = tabAt.getCustomView();

            assert view != null;
            if (j == 0) {
                view.findViewById(R.id.indicator_view)
                        .setBackgroundColor(getResources().getColor(R.color.dark_blue));
            } else {
                view.findViewById(R.id.indicator_view).setBackgroundColor(0);
            }

            ((TextView) view.findViewById(R.id.category_name)).setText(getPageTitle(j));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < tabs.getTabCount(); j++) {
                    TabLayout.Tab tabAt = tabs.getTabAt(j);
                    assert tabAt != null;
                    View view = tabAt.getCustomView();

                    assert view != null;
                    view.findViewById(R.id.indicator_view).setBackgroundColor(0);
                    tabAt.setCustomView(view);
                }

                TabLayout.Tab tabAt = tabs.getTabAt(i);
                View customView;
                if (tabAt != null && tabAt.getCustomView() != null) {
                    customView = tabAt.getCustomView();
                    customView.findViewById(R.id.indicator_view)
                            .setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    ((TextView) customView.findViewById(R.id.category_name)).setText(getPageTitle(i));
                    tabAt.setCustomView(customView);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private String getPageTitle(int position) {
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
