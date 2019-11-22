package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.platform.R;
import com.platform.view.activities.FeedCreationActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.OperatorMeterReadingActivity;

import java.util.Objects;

@SuppressWarnings({"EmptyMethod", "WeakerAccess"})
public class ConnectFragment extends Fragment {
    private View plannerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        if (getActivity() != null && getArguments() != null) {
//            String title = (String) getArguments().getSerializable("TITLE");
//        ((HomeActivity) getActivity()).setActionBarTitle(getString(R.string.tab_connect));
//        ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

//            if ((boolean)getArguments().getSerializable("SHOW_BACK")) {
//                ((HomeActivity) getActivity()).showBackArrow();
//            }
//        }


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_connect, container, false);
        plannerView = inflater.inflate(R.layout.fragment_dashboard_planner, container, false);
        Intent startMain1 = new Intent(getActivity(), FeedCreationActivity.class);
        startMain1.putExtra("meetid","5d6f90c25dda765c2f0b5dd4");
        startActivity(startMain1);


        /*try {

            Fragment fragment = new FeedCreateFragment();
            FragmentTransaction fragmentTransaction = ((HomeActivity) Objects
                    .requireNonNull(getActivity()))
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_page_container, fragment, "Create Feed");
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (true)
                fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }*/

        return plannerView;
    }

}
