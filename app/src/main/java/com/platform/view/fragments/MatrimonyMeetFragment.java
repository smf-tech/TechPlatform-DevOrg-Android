package com.platform.view.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;

public class MatrimonyMeetFragment extends Fragment implements PopupMenu.OnMenuItemClickListener  {
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
        }
        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
        ImageView btnPopupMenu = view.findViewById(R.id.btn_popmenu);
        btnPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu((getActivity()), v);
                popup.setOnMenuItemClickListener(MatrimonyMeetFragment.this);
                popup.inflate(R.menu.matrimony_meet_menu);
                popup.show();
            }
        });
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_archive:
                break;
            case R.id.action_allocate_badge:
                break;
            case R.id.action_finalise_badge:
                break;
            case R.id.action_gen_booklate:
                break;
        }
        return false;
    }
}
