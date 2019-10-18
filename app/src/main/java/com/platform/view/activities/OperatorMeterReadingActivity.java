package com.platform.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.util.StringUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.Operator.OperatorRequestResponseModel;
import com.platform.services.ForegroundService;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Date;
import java.util.List;

public class OperatorMeterReadingActivity extends BaseActivity {

    ImageView img_start_meter,img_end_meter;
    //------
    String machine_id ="231131321";
    String status ="";
    String workTime="";
    String lat="";
    String lon="";
    String image="";
    String meter_reading="";
    int hours = 0;
    //int totalHours =0;
    int totalHours ;
    int currentHours;
 //---
 String start_meter_reading="";
 String stop_meter_reading="";
//---
    private int state_start = 101;
    private int state_stop = 100;
    private int state_pause = 102;
    private int currentState =0;
    Button btnStartService, btnStopService, buttonPauseService;
    EditText et_emeter_read,et_smeter_read;
    TextView tv_text;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private RequestOptions requestOptions;

    private void updateStatusAndProceed(int currentState)
    {
        if (currentState==state_start){
            //editor.putInt("State", state_pause);
            startService();
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText("Pause");
            btnStartService.setVisibility(View.GONE);
            //-----------
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--","---"+workTime);
            saveOperatorStateData(machine_id,workTime,"state_start",lat,lon,meter_reading,hours,totalHours,image);
        }else if (currentState==state_pause){
            //editor.putInt("State", state_start);
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText("Resume");
            stopService();
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--","---"+workTime);
            saveOperatorStateData(machine_id,workTime,"state_pause",lat,lon,meter_reading,hours,totalHours,image);
        }else if (currentState==state_stop){
            //editor.putInt("State", state_start);
            buttonPauseService.setVisibility(View.GONE);
            btnStartService.setVisibility(View.VISIBLE);
            et_smeter_read.setText("");
            stopService();
            workTime =  String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--","---"+workTime);
            saveOperatorStateData(machine_id,workTime,"state_stop",lat,lon,et_emeter_read.getText().toString(),hours,totalHours,image);
            et_emeter_read.setText("");
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
        requestOptions = new RequestOptions().placeholder(R.mipmap.app_logo);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());

        machine_id = "bjs3232334";

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
        img_start_meter = findViewById(R.id.img_start_meter);
        img_end_meter = findViewById(R.id.img_end_meter);


        if (preferences.getInt("State", 0)==0){
            currentState =state_stop;
            //updateButtonsonRestart(currentState);
            editor.putInt("State", state_stop);
            editor.apply();
            updateButtonsonRestart(preferences.getInt("State", 0));
        }else {
            updateButtonsonRestart(preferences.getInt("State", 0));
        }
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
                if (Permissions.isCameraPermissionGranted(OperatorMeterReadingActivity.this, this)) {

                    if (TextUtils.isEmpty(et_smeter_read.getText())) {
                        Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                        et_smeter_read.requestFocus();
                    } else if (TextUtils.isEmpty(image)) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(OperatorMeterReadingActivity.this);
                    } else {
                        editor.clear();
                        editor.commit();

                        editor.putInt("State", state_start);
                        editor.apply();
                        updateStatusAndProceed(state_start);
                    }
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
                    image ="";
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
                         totalHours = intent.getIntExtra("TOTAL_HOURS",0);
                         currentHours = intent.getIntExtra("CURRENT_HOURS",0);
                        hours =currentHours;
                        if (!TextUtils.isEmpty(timestr)) {
                           /* Log.e("current Time", timestr);
                            Log.e("Total_hours", "" + totalHours);
                            Log.e("current_hours", "" + currentHours);*/
                        }

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

    public void saveOperatorStateData(String machine_id,String workTime,String status,String lat,String lon,String meter_reading,int hours,int totalHours,String image){
        OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id("MachineId");
        operatorRequestResponseModel.setWorkTime(workTime);
        operatorRequestResponseModel.setStatus(status);
        operatorRequestResponseModel.setLat(lat);
        operatorRequestResponseModel.setLong(lon);
        operatorRequestResponseModel.setMeter_reading(meter_reading);
        operatorRequestResponseModel.setHours(hours);
        operatorRequestResponseModel.setTotalHours(totalHours);
        operatorRequestResponseModel.setImage(image);
        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().insert(operatorRequestResponseModel);
        List<OperatorRequestResponseModel> list =  DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();
        for (int i = 0; i < list.size(); i++) {
            //Log.e("method--2","---"+list.get(i).getStatus());
            if (false){
                //DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().deleteSinglSynccedOperatorRecord(list.get(i).get_id());
                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(Uri.parse(list.get(0).getImage()))
                        .into(img_start_meter);
            }
            Log.e("method--2","---"+list.get(i).getStatus());
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(image)
                    .into(img_start_meter);
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load("")//new File(resultUri.getPath()
                        .into(img_start_meter);
                image = resultUri.toString();
                File file = new File(resultUri.getPath());
               // image = file.getAbsolutePath();

                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(image)
                        .into(img_start_meter);
            }

        }
    }
}