package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.view.fragments.CreateMeetFirstFragment;
import com.platform.view.fragments.CreateMeetSecondFragment;

public class CreateMatrimonyMeetActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private MatrimonyMeet matrimonyMeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_matrimony_meet);
        init();
    }

    private void init(){
        String switchToFragment = getIntent().getStringExtra("SwitchToFragment");
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.create_meet);

        fManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(switchToFragment)) {

            switch (switchToFragment) {
                case "CreateMeetFirstFragment":
                    matrimonyMeet = new MatrimonyMeet();
                    fragment = new CreateMeetFirstFragment();
                    openFragment();
                    break;

                case "CreateMeetSecondFragment":
                    fragment = new CreateMeetSecondFragment();
                    openFragment();
                    break;

                case "CreateMeetThirdFragment":
                    fragment = new CreateMeetSecondFragment();
                    openFragment();
                    break;
            }
        }

//        Intent matrimonyFragmentsActionsIntent = new Intent(this, MeetFragmentsActionsActivity.class);
//        startActivity(matrimonyFragmentsActionsIntent);
        //fManager = getSupportFragmentManager();
        //fragment = new CreateMeetFirstFragment();
        // Begin transaction.
//        FragmentTransaction fTransaction = fManager.beginTransaction();
//        fTransaction.replace(R.id.create_meet_frame_layout, fragment).addToBackStack(null)
//                .commit();
    }

    private void openFragment() {
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
        switch (view.getId()){
            case R.id.toolbar_back_action:
                finish();
                break;
        }
    }
}
