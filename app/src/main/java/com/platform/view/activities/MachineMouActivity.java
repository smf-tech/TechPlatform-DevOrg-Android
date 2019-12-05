package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.MachineData;
import com.platform.models.SujalamSuphalam.MachineDetailData;
import com.platform.models.SujalamSuphalam.MouDetails;
import com.platform.models.SujalamSuphalam.OperatorDetails;
import com.platform.models.SujalamSuphalam.ProviderInformation;
import com.platform.presenter.MachineMouActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.MachineMouFirstFragment;
import com.platform.view.fragments.MachineMouFourthFragment;
import com.platform.view.fragments.MachineMouSecondFragment;
import com.platform.view.fragments.MachineMouThirdFragment;

import java.util.HashMap;
import java.util.List;

public class MachineMouActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private MachineDetailData machineDetailData;
    private static final String TAG = MachineMouActivity.class.getName();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private String machineId;
    private int statusCode;
    private MachineMouActivityPresenter machineMouActivityPresenter;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    public Uri chequeImageUri, operatorLicenseImageUri;

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

        machineMouActivityPresenter = new MachineMouActivityPresenter(this);
        machineDetailData = new MachineDetailData();
        if(getIntent().getStringExtra("SwitchToFragment")!=null){
            if(getIntent().getStringExtra("SwitchToFragment").equals("MachineMouFirstFragment")){
                if(getIntent().getIntExtra("statusCode", 0) ==
                        Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
                    statusCode = getIntent().getIntExtra("statusCode",0);
                    toolbar_title.setText(R.string.create_machine);
                    fManager = getSupportFragmentManager();
                    fragment = new MachineMouFirstFragment();
                    FragmentTransaction fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null).commit();
                } else {
                    if(getIntent().getIntExtra("statusCode", 0) ==
                            Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
                        toolbar_title.setText(R.string.machine_eligible_form_title);
                    } else {
                        toolbar_title.setText(R.string.machine_mou_form);
                    }
                    machineId = getIntent().getStringExtra("machineId");
                    statusCode = getIntent().getIntExtra("statusCode",0);
                    if(Util.isConnected(this)) {
                        machineMouActivityPresenter.getMachineDetails(machineId, statusCode);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
            }
        }
    }

    public void openFragment(String switchToFragment) {
        fManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(switchToFragment)) {
            switch (switchToFragment) {
                case "MachineMouSecondFragment":
                    fragment = new MachineMouSecondFragment();
                    break;
                case "MachineMouThirdFragment":
                    fragment = new MachineMouThirdFragment();
                    break;
                case "MachineMouFourthFragment":
                    fragment = new MachineMouFourthFragment();
                    break;
            }
        }
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null)
                .commit();
    }

    public MachineDetailData getMachineDetailData() {
        return machineDetailData;
    }

    public HashMap<String, Bitmap> getImageHashmap() {
        return imageHashmap;
    }

    public void setMachineDetailData(MachineDetailData machineDetail) {
        this.machineDetailData = machineDetail;
        fManager = getSupportFragmentManager();
        fragment = new MachineMouFirstFragment();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null).commit();
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

    public void openFragmentAtPosition(int position) {
        try {
            fManager.getBackStackEntryAt(position);
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
        if (machineMouActivityPresenter != null) {
            machineMouActivityPresenter.clearData();
            machineMouActivityPresenter = null;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }
}
