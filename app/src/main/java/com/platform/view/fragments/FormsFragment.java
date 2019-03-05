package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.view.activities.HomeActivity;

public class FormsFragment extends Fragment {

    private View formsFragmentView;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        formsFragmentView = inflater.inflate(R.layout.fragment_forms, container, false);
        return formsFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTabView();
    }

    private void initTabView() {
        viewPager = formsFragmentView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        TabLayout tabs = formsFragmentView.findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroy() {
        if (formsFragmentView != null) {
            formsFragmentView = null;
        }
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
                    title = getResources().getString(R.string.new_forms);
                    break;

                case 1:
                    title = getResources().getString(R.string.saved_forms);
                    break;

                case 2:
                    title = getResources().getString(R.string.submitted_forms);
                    break;
            }

            return title.toUpperCase();
        }

        @Override
        public int getItemPosition(@NonNull final Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }
}
