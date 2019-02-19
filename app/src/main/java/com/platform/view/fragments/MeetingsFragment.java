package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.utility.Util;

public class MeetingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MeetingsFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard_meetings, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_view_all_approvals:
                Util.launchFragment(new MeetingsFragment(), getContext(), "meetingFragment");

               /* try {
                    FragmentTransaction fragmentTransaction = ((HomeActivity) Objects
                            .requireNonNull(getContext()))
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.home_page_container, new MeetingsFragment(), "meetingFragment");
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }*/

                break;
        }
    }
}
