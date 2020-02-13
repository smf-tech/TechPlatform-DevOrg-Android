package com.octopusbjsindia.view.activities;

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
import com.octopusbjsindia.view.fragments.CreateOpeartorFragment;
import com.octopusbjsindia.view.fragments.MachineDeployStructureListFragment;
import com.octopusbjsindia.view.fragments.MachineDieselRecordFragment;
import com.octopusbjsindia.view.fragments.MachineNonUtilizationFragment;
import com.octopusbjsindia.view.fragments.MachineShiftingFormFragment;
import com.octopusbjsindia.view.fragments.MachineVisitValidationFragment;
import com.octopusbjsindia.view.fragments.MouUploadFragment;
import com.octopusbjsindia.view.fragments.SavedStructureListFragment;
import com.octopusbjsindia.view.fragments.SiltTransportationRecordFragment;
import com.octopusbjsindia.view.fragments.StructureMachineListFragment;

public class SSActionsActivity extends AppCompatActivity implements View.OnClickListener {
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
                        fragment = new StructureMachineListFragment();
                        fragment.setArguments(data);
                        openFragment();
                        if (data.getInt("viewType", 0) == 1) {
                            findViewById(R.id.toolbar_edit_action).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.toolbar_edit_action).setVisibility(View.GONE);
                        }
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
                    case "MachineVisitValidationFragment":
                        fragment = new MachineVisitValidationFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "MachineDieselRecordFragment":
                        fragment = new MachineDieselRecordFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "MachineNonUtilizationFragment":
                        fragment = new MachineNonUtilizationFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "SiltTransportationRecordFragment":
                        fragment = new SiltTransportationRecordFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "SavedStructureListFragment":
                        fragment = new SavedStructureListFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "MouUploadFragment":
                        fragment = new MouUploadFragment();
                        fragment.setArguments(data);
                        openFragment();
                        break;
                    case "CreateOperatorFragment":
                        fragment = new CreateOpeartorFragment();
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
                Intent intent = new Intent(this, SSActionsActivity.class);
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
