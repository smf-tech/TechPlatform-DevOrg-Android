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
import com.platform.models.SujalamSuphalam.MachineMouData;
import com.platform.view.fragments.MachineMouFirstFragment;

public class MachineMouActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private MachineMouData machineMouData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_mou);
        init();
    }

    private void init(){
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        ivBackIcon.setOnClickListener(this);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.machine_mou_form);

        if(getIntent().getStringExtra("SwitchToFragment")!=null){
            if(getIntent().getStringExtra("SwitchToFragment").equals("MachineMouFirstFragment")){
                machineMouData = new MachineMouData();
                fManager = getSupportFragmentManager();
                fragment = new MachineMouFirstFragment();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null)
                        .commit();
            }
        }
    }

    public void openFragment(String switchToFragment) {
        fManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(switchToFragment)) {
            switch (switchToFragment) {
                case "MachineMouSecondFragment":
                    //fragment = new MachineMouSecondFragment();
                    break;
                case "MachineMouThirdFragment":
                    //fragment = new MachineMouThirdFragment();
                    break;
            }
        }
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null)
                .commit();
    }

    public MachineMouData getMachineMouData() {
        return machineMouData;
    }

    public void setMachineMouData(MachineMouData machineMouData) {
        this.machineMouData = machineMouData;
    }

    @Override
    public void onClick(View view) {
        finish();
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


    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
    }
}
