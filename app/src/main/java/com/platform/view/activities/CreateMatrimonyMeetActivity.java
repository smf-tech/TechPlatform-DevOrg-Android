package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.view.fragments.CreateMeetFirstFragment;

public class CreateMatrimonyMeetActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fManager;
    private Fragment fragment;
    private MatrimonyMeet matrimonyMeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_matrimony_meet);

//        TextView toolBar = findViewById(R.id.toolbar_title);
//        ImageView toolBarMenu = findViewById(R.id.toolbar_edit_action);
//        toolBar.setText(R.string.create_meet);
//
//        ImageView toolBarBack = findViewById(R.id.toolbar_back_action);
//        toolBarBack.setOnClickListener(this);

        matrimonyMeet = new MatrimonyMeet();
        fManager = getSupportFragmentManager();

        fragment = new CreateMeetFirstFragment();
//        toolBarBack.setBackgroundResource(R.drawable.ic_dialog_close_dark);
//        toolBarMenu.setVisibility(View.GONE);
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.create_meet_frame_layout, fragment).addToBackStack(null)
                .commit();
    }

    public MatrimonyMeet getMatrimonyMeet() {
        return matrimonyMeet;
    }

    public void setMatrimonyMeet(MatrimonyMeet matrimonyMeet) {
        this.matrimonyMeet = matrimonyMeet;
    }

    @Override
    public void onClick(View view) {

    }
}
