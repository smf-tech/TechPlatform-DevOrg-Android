package com.platform.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.fragments.FormFragment;
import com.platform.view.fragments.FormStatusFragment;

import static com.platform.utility.Constants.Form.FORM_STATUS_ALL;
import static com.platform.utility.Constants.Form.FORM_STATUS_COMPLETED;
import static com.platform.utility.Constants.Form.FORM_STATUS_PENDING;

public class FormsActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private FormFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forms);

        initTabView();
        addFragment();
    }

    private void initTabView() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FormsActivity.ViewPagerAdapter(getSupportFragmentManager()));

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicator(null);

        for (int j = 0; j < tabs.getTabCount(); j++) {
            TabLayout.Tab tabAt = tabs.getTabAt(j);
            assert tabAt != null;
            View CustomView = LayoutInflater.from(this).inflate(R.layout.form_tab_view, null);
            tabAt.setCustomView(CustomView);
            View view = tabAt.getCustomView();
            assert view != null;
            if (j == 0) {
                view.findViewById(R.id.indicator_view)
                        .setBackgroundColor(getResources().getColor(R.color.dark_blue));
            } else {
                view.findViewById(R.id.indicator_view).setBackgroundColor(0);
            }
            ((TextView)view.findViewById(R.id.category_name)).setText(getPageTitle(j));
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
                    ((TextView)customView.findViewById(R.id.category_name)).setText(getPageTitle(i));
                    tabAt.setCustomView(customView);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public String getPageTitle(int position) {
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

    private void addFragment() {
        Bundle bundle = new Bundle();

        if (getIntent().getExtras() != null) {
            String processId = getIntent().getExtras().getString(Constants.PM.PROCESS_ID);
            bundle.putString(Constants.PM.PROCESS_ID, processId);
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
        super.onDestroy();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = FormStatusFragment.newInstance(FORM_STATUS_ALL);
            } else if (position == 1) {
                fragment = FormStatusFragment.newInstance(FORM_STATUS_PENDING);
            } else if (position == 2) {
                fragment = FormStatusFragment.newInstance(FORM_STATUS_COMPLETED);
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
