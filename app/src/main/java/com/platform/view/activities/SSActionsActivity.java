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
import com.platform.view.fragments.MachineDeployStructureListFragment;
import com.platform.view.fragments.MachineDetailsFragment;
import com.platform.view.fragments.MachineShiftingFormFragment;
import com.platform.view.fragments.StructureMachineListFragment;

public class SSActionsActivity extends AppCompatActivity  implements View.OnClickListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssactions);
        Bundle data = getIntent().getExtras();
        fManager = getSupportFragmentManager();
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        ivBackIcon.setOnClickListener(this);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        if (data != null && data.containsKey("SwitchToFragment")) {

            String switchToFragment = data.getString("SwitchToFragment") != null
                    ? data.getString("SwitchToFragment") : "null";

            String title = data.getString("title") != null
                    ? data.getString("title") : "";

            toolbar_title.setText(title);

            if (!TextUtils.isEmpty(switchToFragment)) {
                switch (switchToFragment) {
                    case "StructureMachineListFragment":
                        fragment = new StructureMachineListFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "MachineDeployStructureListFragment":
                        fragment = new MachineDeployStructureListFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "MachineShiftingFormFragment":
                        fragment = new MachineShiftingFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
//                    case "MachineDetailsFragment":
//                        fragment = new MachineDetailsFragment();
//                        fragment.setArguments(data);
//                        openFragment();
//                        break;
                }
            }
        }
    }

    public void openFragment() {
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.ss_frame_layout, fragment).addToBackStack(null)
                .commit();
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


    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
    }
}
