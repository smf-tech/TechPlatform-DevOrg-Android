package com.octopusbjsindia.view.activities.ssgp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.view.fragments.StructureMachineListFragment;
import com.octopusbjsindia.view.fragments.ssgp.StructureMachineListGPFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCBDFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCCMFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCDPRFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCDPRValidationFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDCSMFormFragment;
import com.octopusbjsindia.view.fragments.ssgp.VDFFormFragment;

import java.util.Objects;

public class GPActionsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private TextView toolbar_title;
    private String switchToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssactions);
        Bundle data = getIntent().getExtras();
        fManager = getSupportFragmentManager();
        toolbar_title = findViewById(R.id.toolbar_title);

        if (data != null && data.containsKey("SwitchToFragment")) {

             switchToFragment = data.getString("SwitchToFragment") != null
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
                onBackPressed();
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

        if(!switchToFragment.equals("StructureMachineListFragment")){
            final Dialog dialog = new Dialog(Objects.requireNonNull(this));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogs_leave_layout);

            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(getResources().getString(R.string.alert));
            title.setVisibility(View.VISIBLE);

            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText("Are you sure, want to discard");
            text.setVisibility(View.VISIBLE);

            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText("Yes");
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                finish();
                dialog.dismiss();
            });

            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText("No");
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        } else {
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
    }
}
