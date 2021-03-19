package com.octopusbjsindia.view.activities.ssgp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.octopusbjsindia.R;
import com.octopusbjsindia.view.fragments.StructureMachineListFragment;
import com.octopusbjsindia.view.fragments.ssgp.StructureMachineListGPFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCBDFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCCMFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCDPRFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCDPRValidationFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCSMFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDFFormFragment;

public class GPActionsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssactions);
        Bundle data = getIntent().getExtras();
        fManager = getSupportFragmentManager();
        toolbar_title = findViewById(R.id.toolbar_title);

        if (data != null && data.containsKey("SwitchToFragment")) {

            String switchToFragment = data.getString("SwitchToFragment") != null
                    ? data.getString("SwitchToFragment") : "null";

            String title = data.getString("title") != null
                    ? data.getString("title") : "";
            setTitle(title);

            if (!TextUtils.isEmpty(switchToFragment)) {
                switch (switchToFragment) {
                    case "StructureMachineListFragment":
                        fragment = new StructureMachineListGPFragment();
                        fragment.setArguments(data);
                        openFragment();
                        findViewById(R.id.toolbar_edit_action).setVisibility(View.GONE);
                        break;
                    case "VDFFormFragment":
                        fragment = new VDFFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "VDCSMFormFragment":
                        fragment = new VDCSMFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "VDCDPRFormFragment":
                        fragment = new VDCDPRFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "VDCDPRValidationFormFragment":
                        fragment = new VDCDPRValidationFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "VDCCMFormFragment":
                        fragment = new VDCCMFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "VDCBDFormFragment":
                        fragment = new VDCBDFormFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
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

    public void setTitle(String title) {
        toolbar_title.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
        ImageView toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        toolbar_edit_action.setImageResource(R.drawable.ic_saved_offline);
        toolbar_edit_action.setOnClickListener(this);
    }

    public void setActivityTitle(String title) {
        toolbar_title.setText(title);
    }

    public void hideToolbar() {
        findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    public void showToolbar() {
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.toolbar_edit_action:
                Intent intent = new Intent(this, GPActionsActivity.class);
                intent.putExtra("SwitchToFragment", "SavedStructureListFragment");
                intent.putExtra("title", "Saved Structure");
                startActivity(intent);

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
