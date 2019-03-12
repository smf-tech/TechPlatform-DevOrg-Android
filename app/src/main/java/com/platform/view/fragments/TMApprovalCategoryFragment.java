package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.platform.R;
import com.platform.view.adapters.SmartFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

@SuppressWarnings("WeakerAccess")
public class TMApprovalCategoryFragment extends Fragment {

    private TabLayout tabLayout;
    private View apCategoryFragment;

    private final int[] tabIcons = {
            R.drawable.selector_pending_tab,
            R.drawable.selector_approved_tab,
            R.drawable.selector_rejected_tab
    };

    private String[] tabNames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabNames = new String[]{
                getString(R.string.cat_pending),
                getString(R.string.cat_approved),
                getString(R.string.cat_rejected)};
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        apCategoryFragment = inflater.inflate(R.layout.fragment_approvals_cat, container, false);
        return apCategoryFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    private void initViews() {
        ViewPager viewPager = apCategoryFragment.findViewById(R.id.approval_cat_view_pager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = apCategoryFragment.findViewById(R.id.approval_cat_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ApprovalsViewPagerAdapter adapter = new ApprovalsViewPagerAdapter(getChildFragmentManager());

        TMUserPendingFragment fragment = new TMUserPendingFragment();
        Bundle b = new Bundle();
        b.putBoolean("SHOW_ALL", false);
        fragment.setArguments(b);
        adapter.addFragment(fragment);

        adapter.addFragment(new TMUserApprovedFragment());
        adapter.addFragment(new TMUserRejectedFragment());
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabNames.length; i++) {
            TextView tabOne = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_approval_tab, tabLayout, false);
            tabOne.setText(tabNames[i]);
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabOne);
            }
        }
    }

    class ApprovalsViewPagerAdapter extends SmartFragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        ApprovalsViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
