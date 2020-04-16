package com.octopusbjsindia.view.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.view.activities.HomeActivity;

public class FormsFragment extends Fragment {

    private View formsFragmentView;
    static ViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout progressBarLayout;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }

        AppEvents.trackAppEvent(getString(R.string.event_all_forms_screen_visit));
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

        progressBarLayout = formsFragmentView.findViewById(R.id.gen_frag_progress_bar);
        progressBar = formsFragmentView.findViewById(R.id.pb_gen_form_fragment);
    }

    static RelativeLayout getProgressBarView() {
        return progressBarLayout;
    }

    static ProgressBar getProgressBar() {
        return progressBar;
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
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new AllFormsFragment();
            switch (position) {
//                case 0:
//                    fragment = new AllFormsFragment();
//                    break;

                case 0:
                    fragment = new PendingFormsFragment();
                    break;

                case 1:
                    fragment = new SubmittedFormsFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            try {
                switch (position) {
//                    case 0:
//                        title = getResources().getString(R.string.new_forms);
//                        break;

                    case 0:
                        title = getResources().getString(R.string.saved_forms);
                        break;

                    case 1:
                        title = getResources().getString(R.string.submitted_forms);
                        break;
                }
            } catch (Resources.NotFoundException | IllegalStateException e) {
                Log.e("TAG", e.getMessage());
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
            Log.i("TAG", "instantiateItem");
            return super.instantiateItem(container, position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            Log.i("TAG", "notifyDataSetChanged");
        }
    }
}
