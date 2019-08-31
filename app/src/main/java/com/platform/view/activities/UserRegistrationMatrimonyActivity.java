package com.platform.view.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMasterRequestModel;
import com.platform.presenter.UserRegistrationMatrimonyActivityPresenter;
import com.platform.view.adapters.SmartFragmentStatePagerAdapter;
import com.platform.view.fragments.UserRegistrationMatrimonyAboutmeFragment;
import com.platform.view.fragments.UserRegistrationMatrimonyFamilyFragment;
import com.platform.view.fragments.UserRegistrationMatrimonyFragmentOne;
import com.platform.view.fragments.UserRegistrationMatrimonyResidenceFragment;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationMatrimonyActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MatrimonyViewPagerAdapter matrimonyViewPagerAdapter;
    public List<MatrimonyMasterRequestModel.DataList.Master_data> MasterDataArrayList = new ArrayList<>();
    private UserRegistrationMatrimonyActivityPresenter userRegistrationMatrimonyActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_matrimony);
        if (savedInstanceState == null) {
           /* getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserRegistrationMatrimonyFragmentOne.newInstance())
                    .commitNow();*/
        }
        viewPager = findViewById(R.id.approval_cat_view_pager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        userRegistrationMatrimonyActivityPresenter = new UserRegistrationMatrimonyActivityPresenter(this);
        userRegistrationMatrimonyActivityPresenter.getAllPendingRequests();
    }


    private void setupViewPager(ViewPager viewPager) {
        matrimonyViewPagerAdapter = new MatrimonyViewPagerAdapter(getSupportFragmentManager());

        UserRegistrationMatrimonyFragmentOne userRegistrationMatrimonyFragmentOne = new UserRegistrationMatrimonyFragmentOne();
        Bundle b = new Bundle();
        b.putBoolean("SHOW_ALL", false);
        userRegistrationMatrimonyFragmentOne.setArguments(b);
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyFragmentOne);


        UserRegistrationMatrimonyFamilyFragment userRegistrationMatrimonyFamilyFragment = new UserRegistrationMatrimonyFamilyFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyFamilyFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

        UserRegistrationMatrimonyResidenceFragment userRegistrationMatrimonyResidenceFragment = new UserRegistrationMatrimonyResidenceFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyResidenceFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

        UserRegistrationMatrimonyAboutmeFragment userRegistrationMatrimonyAboutmeFragment = new UserRegistrationMatrimonyAboutmeFragment();
        matrimonyViewPagerAdapter.addFragment(userRegistrationMatrimonyAboutmeFragment);
        viewPager.setAdapter(matrimonyViewPagerAdapter);

    }


    //load next fragment for registration
    public void loadNextScreen(int next) {
        switch (next) {
            case 1:
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, UserRegistrationMatrimonyFragmentOne.newInstance())
                        .commitNow();*/
                viewPager.setCurrentItem(0);
                break;
            case 2:
                viewPager.setCurrentItem(1);
                break;
            case 3:
                viewPager.setCurrentItem(2);
                break;
            case 4:
                viewPager.setCurrentItem(3);
                break;
            default:
                break;

        }


    }

    public void saveMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> data) {
        MasterDataArrayList =data;
        //MasterDataArrayList.get(0).getValues()
    }


    class MatrimonyViewPagerAdapter extends SmartFragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        MatrimonyViewPagerAdapter(FragmentManager manager) {
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
