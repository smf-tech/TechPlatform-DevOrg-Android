package com.platform.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;

public class MatrimonyMeetFragment extends Fragment {
    private View matrimonyMeetFragmentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        matrimonyMeetFragmentView = inflater.inflate(R.layout.fragment_matrimony_meet, container, false);
        return matrimonyMeetFragmentView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            MatrimonyMeet meetData = (MatrimonyMeet) arguments.getSerializable(Constants.Home.MATRIMONY);
//            text_title.setText(meetData.getMeetTitle());
//            text_date.setText(meetData.getMeetDateTime());
        }
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
        //setUpToolbar();
    }
//    private void setUpToolbar() {
//        Toolbar toolbar = findViewById(R.id.meet_toolbar);
//        setSupportActionBar(toolbar);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        inflater.inflate(R.menu.menu_sample, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_contact_us:
//                Toast.makeText(this, "Contact us clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_terms_conditions:
//                Toast.makeText(this, "Terms and Conditions clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_logout:
//                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
//                break;
//            case android.R.id.home:
//                //click event of back arrow button to finish activity
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
