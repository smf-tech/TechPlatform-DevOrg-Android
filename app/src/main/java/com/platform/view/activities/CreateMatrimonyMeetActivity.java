package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.Matrimony.MatrimonyMeet;
import com.platform.view.fragments.CreateMeetFirstFragment;
import com.platform.view.fragments.CreateMeetSecondFragment;
import com.platform.view.fragments.CreateMeetThirdFragment;

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
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        ivBackIcon.setOnClickListener(this);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.create_meet);

        if(getIntent().getStringExtra("SwitchToFragment")!=null){
            if(getIntent().getStringExtra("SwitchToFragment").equals("CreateMeetFirstFragment")){
                matrimonyMeet = new MatrimonyMeet();
                fManager = getSupportFragmentManager();
                fragment = new CreateMeetFirstFragment();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.replace(R.id.create_meet_frame_layout, fragment).addToBackStack(null)
                        .commit();
            }
        }
    }

    public void openFragment(String switchToFragment) {
        fManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(switchToFragment)) {
            switch (switchToFragment) {
                case "CreateMeetSecondFragment":
                    fragment = new CreateMeetSecondFragment();
                    break;

                case "CreateMeetThirdFragment":
                    fragment = new CreateMeetThirdFragment();
                    break;
            }
        }
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.create_meet_frame_layout, fragment)
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

    @Override
    public void onBackPressed() {
            try {
                fManager.popBackStackImmediate();
            } catch (IllegalStateException e) {
                Log.e("TAG", e.getMessage());
            }

            if (fManager.getBackStackEntryCount() == 0) {
                finish();
            }
    }
}
