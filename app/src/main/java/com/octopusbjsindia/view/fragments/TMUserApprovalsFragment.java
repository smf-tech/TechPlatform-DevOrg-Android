package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;

public class TMUserApprovalsFragment extends Fragment {

    private View approvalsFragmentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            Context context = getActivity();
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title))
                title = getString(R.string.approvals);

            ((HomeActivity) context).setActionBarTitle(title);
            ((HomeActivity) context).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        approvalsFragmentView = inflater.inflate(R.layout.fragment_approvals, container, false);
        return approvalsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = approvalsFragmentView.findViewById(R.id.approvals_view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = approvalsFragmentView.findViewById(R.id.approvals_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.updateNotificationsCount(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TMApprovalCategoryFragment(), getString(R.string.approvals));
        adapter.addFragment(new TMWhoWorkingFragment(), getString(R.string.who_working));
        viewPager.setAdapter(adapter);
    }
}
