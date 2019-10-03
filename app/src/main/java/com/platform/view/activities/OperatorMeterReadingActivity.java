package com.platform.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.platform.Platform;
import com.platform.R;
import com.platform.services.ForegroundService;
import com.platform.utility.Util;

public class OperatorMeterReadingActivity extends BaseActivity {
    private int state_start = 101;
    private int state_stop = 100;
    private int state_pause = 102;
    private int currentState =0;
    Button btnStartService, btnStopService, buttonPauseService;
    EditText et_emeter_read,et_smeter_read;
    TextView tv_text;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private void updateStatusAndProceed(int currentState)
    {
        if (currentState==state_start){
            //editor.putInt("State", state_pause);
            startService();
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText("Pause");
            btnStartService.setVisibility(View.GONE);

        }else if (currentState==state_pause){
            //editor.putInt("State", state_start);
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText("Resume");
            stopService();
        }else if (currentState==state_stop){
            //editor.putInt("State", state_start);
            buttonPauseService.setVisibility(View.GONE);
            btnStartService.setVisibility(View.VISIBLE);
            stopService();
        }
    }
    private void updateButtonsonRestart(int currentState)
    {
        if (currentState==state_pause){

            buttonPauseService.setVisibility(View.VISIBLE);
            btnStartService.setVisibility(View.GONE);
            buttonPauseService.setText("Resume");
        }else if (currentState==state_stop){

            buttonPauseService.setVisibility(View.GONE);
            btnStartService.setVisibility(View.VISIBLE);
            stopService();
        }else if (currentState==state_start){

            buttonPauseService.setVisibility(View.VISIBLE);
            btnStartService.setVisibility(View.GONE);
            startService();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_meter_reading);
        currentState =state_start;
        preferences = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE);
        editor = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE).edit();

        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        buttonPauseService = findViewById(R.id.buttonPauseService);
        tv_text = findViewById(R.id.tv_text);
        et_emeter_read = findViewById(R.id.et_emeter_read);
        et_smeter_read = findViewById(R.id.et_smeter_read);

        updateButtonsonRestart(preferences.getInt("State", 0));
        buttonPauseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (preferences.getInt("State", 0)==state_start){
                    editor.putInt("State", state_pause);
                    editor.apply();
                    updateStatusAndProceed(state_pause);
                }else if (preferences.getInt("State", 0)==state_pause){
                    editor.putInt("State", state_start);
                    editor.apply();
                    updateStatusAndProceed(state_start);
                }

                //int systemTime = preferences.getInt("systemTime", 0);
                //int systemClockTime = preferences.getInt("systemClockTime", 0);

            }
        });

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_smeter_read.getText()))
                {
                    Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                    et_smeter_read.requestFocus();
                }else {
                    editor.clear();
                    editor.commit();

                    editor.putInt("State", state_start);
                    editor.apply();
                    updateStatusAndProceed(state_start);
                }
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_emeter_read.getText()))
                {
                    Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                    et_emeter_read.requestFocus();
                }else {
                    stopService();
                    editor.putInt("State", state_stop);
                    editor.apply();
                    updateStatusAndProceed(state_stop);


                }
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String timestr = intent.getStringExtra("STR_TIME");
                        //double longitude = intent.getDoubleExtra(LocationBroadcastService.EXTRA_LONGITUDE, 0);
                        tv_text.setText("TIME: " + timestr);
                        tv_text.setText(timestr);
                    }
                }, new IntentFilter(ForegroundService.ACTION_LOCATION_BROADCAST)
        );
    }



    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);

        /*editor.putInt("State", state_start);
        editor.apply();*/
        //buttonPauseService.setVisibility(View.VISIBLE);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("method--","---OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("method--","---OnPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("method--2","---OnDestroy");
    }

}