package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.platform.R;
import com.platform.view.activities.ui.userregistrationmatrimony.UserRegistrationMatrimonyFragment;
import com.platform.view.activities.ui.userregistrationmatrimony.UserRegistrationMatrimonyFragmentTwo;

public class UserRegistrationMatrimonyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration_matrimony_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UserRegistrationMatrimonyFragment.newInstance())
                    .commitNow();
        }
    }

    public void showNextFragment(int next){
        switch (next){
            case 1:
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, UserRegistrationMatrimonyFragmentTwo.newInstance())
                        .commitNow();
                break;
            case 3:
                break;
        }

    }
}
