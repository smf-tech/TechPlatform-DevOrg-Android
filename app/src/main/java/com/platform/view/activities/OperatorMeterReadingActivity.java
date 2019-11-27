package com.platform.view.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.models.Operator.OperatorMachineData;
import com.platform.models.Operator.OperatorRequestResponseModel;
import com.platform.presenter.OperatorMeterReadingActivityPresenter;
import com.platform.receivers.ConnectivityReceiver;
import com.platform.services.ForegroundService;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.widgets.SingleSelectBottomSheet;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.platform.receivers.ConnectivityReceiver.connectivityReceiverListener;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;

public class OperatorMeterReadingActivity extends BaseActivity implements APIDataListener, ConnectivityReceiver.ConnectivityReceiverListener, SingleSelectBottomSheet.MultiSpinnerListener {
    private String strReasonId="";
    ArrayList<String> ListHaltReasons = new ArrayList<>();
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private BroadcastReceiver connectionReceiver;
    private GPSTracker gpsTracker;
    private Location location;
    LottieAnimationView gear_action_start,gear_action_stop;
    private OperatorMeterReadingActivityPresenter operatorMeterReadingActivityPresenter;
    private static final String TAG = OperatorMeterReadingActivity.class.getCanonicalName();
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    Uri photoURI;
    Uri finalUri;
    OperatorRequestResponseModel operatorRequestResponseModel;
    boolean flag = true;
    boolean isStartImage = true;
    ImageView img_start_meter, img_end_meter;
    //------
    String machine_id = "";
    String status = "";
    String workTime = "";
    String lat = "18.516726";
    String lon = "73.856255";
    String image = "";
    String imageStartReading = "";
    String imageEndReading = "";
    String meter_reading = "";
    int hours = 0;
    //int totalHours =0;
    int totalHours;
    int currentHours;
    //---
    String start_meter_reading = "";
    String stop_meter_reading = "";
    Button btnStartService, btnStopService, buttonPauseService,buttonHaltService;
    EditText et_emeter_read, et_smeter_read;
    TextView tv_text,tv_machine_code;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String imageFilePath;
    private RequestQueue rQueue;
    private String upload_URL = "http://13.235.124.3/api/machineWorkLog";
    //---
    private int state_start = 112;
    private int state_stop = 110;
    private int state_pause = 113;
    private int state_halt = 111;
    private int currentState = 0;
    private RequestOptions requestOptions;
private Toolbar toolbar;
private ImageView toolbar_edit_action;
    private void updateStatusAndProceed(int currentStateReceived) {
        Log.e("currentstate--3", "----"+currentState);
        currentState  =currentStateReceived;
        if (currentState == state_start) {
            //editor.putInt("State", state_pause);
            startService();
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText(getResources().getString(R.string.meter_pause));
            btnStartService.setVisibility(View.GONE);
            et_emeter_read.setText("");
            //-----------
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--", "---" + workTime);
            saveOperatorStateData(machine_id, workTime, "start",""+state_start, lat, lon, meter_reading, hours, totalHours, image);
            image = "";
            gear_action_start.setVisibility(View.VISIBLE);
            gear_action_stop.setVisibility(View.GONE);
            Util.showToast("Machine started Working.",this);
            currentState =state_start;
        } else if (currentState == state_pause) {
            //editor.putInt("State", state_start);
            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText(getResources().getString(R.string.meter_resume));
            stopService();
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--", "---" + workTime);
            saveOperatorStateData(machine_id, workTime, "pause",""+state_pause, lat, lon, meter_reading, hours, totalHours, image);
            image = "";
            gear_action_start.setVisibility(View.GONE);
            gear_action_stop.setVisibility(View.VISIBLE);
            Util.showToast("Machine is on break.",this);
            currentState =state_pause;
        } else if (currentState == state_stop) {
            //editor.putInt("State", 0);
            buttonPauseService.setVisibility(View.GONE);
            btnStartService.setVisibility(View.VISIBLE);
            et_smeter_read.setText("");
            stopService();
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--", "---" + workTime);
            saveOperatorStateData(machine_id, workTime, "stop",""+state_stop, lat, lon, et_emeter_read.getText().toString(), hours, totalHours, image);

            image = "";
            gear_action_start.setVisibility(View.GONE);
            gear_action_stop.setVisibility(View.VISIBLE);
            Util.showToast("Machine stopped Working.",this);
            currentState =state_stop;
        }else if (currentState == state_halt){
            stopService();
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--", "---" + workTime);
            saveOperatorStateData(machine_id, workTime, "halt",""+state_halt, lat, lon, meter_reading, hours, totalHours, image);
            gear_action_start.setVisibility(View.GONE);
            gear_action_stop.setVisibility(View.VISIBLE);
            Util.showToast("Machine facing issue and stopped working.",this);
            currentState =state_halt;
        }
        Log.e("currentstate--4", "----"+currentState);
    }

    private void updateButtonsonRestart(int currentState) {
        if (currentState == state_pause) {

            buttonPauseService.setVisibility(View.VISIBLE);
            btnStartService.setVisibility(View.GONE);
            buttonPauseService.setText(getResources().getString(R.string.meter_resume));
        } else if (currentState == state_stop) {

            buttonPauseService.setVisibility(View.GONE);
            btnStartService.setVisibility(View.VISIBLE);
            stopService();
        } else if (currentState == state_start) {

            buttonPauseService.setVisibility(View.VISIBLE);
            buttonPauseService.setText(getResources().getString(R.string.meter_pause));
            btnStartService.setVisibility(View.GONE);
            startService();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_meter_reading_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        toolbar =  findViewById(R.id.operator_toolbar);
        toolbar_edit_action =  findViewById(R.id.toolbar_edit_action);
        gear_action_start = findViewById(R.id.gear_action_start);
        gear_action_stop = findViewById(R.id.gear_action_stop);
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_meter);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());
        gpsTracker = new GPSTracker(OperatorMeterReadingActivity.this);



        GetLocationofOperator();
        initConnectivityReceiver();
        if (Permissions.isCameraPermissionGranted(this, this)) {

        }
        operatorMeterReadingActivityPresenter = new OperatorMeterReadingActivityPresenter(this);
        operatorMeterReadingActivityPresenter.getAllFiltersRequests();
        //machine_id = "bjs3232334";

        currentState = state_start;
        preferences = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE);
        editor = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE).edit();

        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        buttonPauseService = findViewById(R.id.buttonPauseService);
        buttonHaltService = findViewById(R.id.buttonHaltService);
        tv_text = findViewById(R.id.tv_text);
        tv_machine_code = findViewById(R.id.tv_machine_code);
        et_emeter_read = findViewById(R.id.et_emeter_read);
        et_smeter_read = findViewById(R.id.et_smeter_read);
        img_start_meter = findViewById(R.id.img_start_meter);
        img_end_meter = findViewById(R.id.img_end_meter);

        if (!preferences.getString("machine_id", "").equalsIgnoreCase("")){
            tv_machine_code.setText(preferences.getString("machine_code", ""));
        }
        clearDataForNewDate();
        if (preferences.getInt("State", 0) == 0) {
            currentState = state_stop;
            //updateButtonsonRestart(currentState);
            editor.putInt("State", state_stop);
            editor.apply();
            updateButtonsonRestart(preferences.getInt("State", 0));
        } else {
            updateButtonsonRestart(preferences.getInt("State", 0));
        }
        buttonPauseService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetLocationofOperator();
                if (preferences.getInt("State", 0) == state_start) {
                    editor.putInt("State", state_pause);
                    editor.apply();
                    currentState = state_pause;
                    updateStatusAndProceed(state_pause);
                } else if (preferences.getInt("State", 0) == state_pause) {
                    editor.putInt("State", state_start);
                    editor.apply();
                    currentState = state_start;
                    updateStatusAndProceed(state_start);
                }

                //int systemTime = preferences.getInt("systemTime", 0);
                //int systemClockTime = preferences.getInt("systemClockTime", 0);
                buttonPauseService.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    buttonPauseService.setEnabled(true);
                }, 1000);
            }
        });
        /*buttonHaltService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocationofOperator();
                if (currentState != state_stop) {
                    if (currentState == state_halt) {
                        Util.showToast("Machine is already in halt state.", OperatorMeterReadingActivity.this);
                    }   else {
                        Util.showToast("Please stop meter reading", OperatorMeterReadingActivity.this);
                    }
                }else {
                    if (currentState == state_halt) {
                        Util.showToast("Machine is already in halt state.", OperatorMeterReadingActivity.this);
                    } else {
                        showMultiSelectBottomsheet("Halt Reason","halt",ListHaltReasons);

                    *//*if (currentState==state_start){
                        editor.putInt("State", state_pause);
                        editor.apply();
                        updateStatusAndProceed(state_pause);
                        updateStatusAndProceed(state_halt);
                        clearReadingImages();
                    }else if (currentState==state_pause){
                        *//**//*editor.putInt("State", state_pause);
                        editor.apply();
                        updateStatusAndProceed(state_pause);*//**//*
                        updateStatusAndProceed(state_halt);
                        clearReadingImages();
                    }*//*
                    }
                }

            }
        });*/


        toolbar_edit_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocationofOperator();
                if (currentState != state_stop) {
                    if (currentState == state_halt) {
                        Util.showToast("Machine is already in halt state.", OperatorMeterReadingActivity.this);
                    }   else {
                        Util.showToast("Please stop meter reading", OperatorMeterReadingActivity.this);
                    }
                }else {
                    if (currentState == state_halt) {
                        Util.showToast("Machine is already in halt state.", OperatorMeterReadingActivity.this);
                    } else {
                        strReasonId ="";
                        showMultiSelectBottomsheet("Halt Reason","halt",ListHaltReasons);

                        /*updateStatusAndProceed(state_halt);
                        clearReadingImages();*/
                    /*if (currentState==state_start){
                        editor.putInt("State", state_pause);
                        editor.apply();
                        updateStatusAndProceed(state_pause);
                        updateStatusAndProceed(state_halt);
                        clearReadingImages();
                    }else if (currentState==state_pause){
                        *//*editor.putInt("State", state_pause);
                        editor.apply();
                        updateStatusAndProceed(state_pause);*//*
                        updateStatusAndProceed(state_halt);
                        clearReadingImages();
                    }*/
                    }
                }

            }
        });

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocationofOperator();
           /*     if (Permissions.isCameraPermissionGranted(OperatorMeterReadingActivity.this, this)) {

                    if (TextUtils.isEmpty(et_smeter_read.getText())) {
                        Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                        et_smeter_read.requestFocus();
                    } else if (flag) {
                        openCameraIntent();
                        flag = false;
                        isStartImage = true;
                    } else {
                        editor.putInt("systemClockTime", 0);
                        editor.apply();
                        editor.putInt("State", state_start);
                        editor.apply();
                        updateStatusAndProceed(state_start);
                        flag = true;
                        callStartButtonClick();
                    }
                }*/
           callStartButtonClick();
            }
        });


        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocationofOperator();
                /*if (TextUtils.isEmpty(et_emeter_read.getText())) {
                    Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                    et_emeter_read.requestFocus();
                    //takePhotoFromCamera();

                } else if (flag) {

                    openCameraIntent();
                    isStartImage = false;
                    flag = false;
                } else {
                    stopService();
                    editor.putInt("State", state_stop);
                    editor.apply();
                    updateStatusAndProceed(state_stop);
                    flag = true;
                    image = "";
                    clearReadingImages();
                    et_smeter_read.requestFocus();
                }*/
                if (preferences.getInt("State", 0) != state_stop) {
                    callStopButtonClick();
                }else {
                    Util.showToast("Please enter Start meter reading", OperatorMeterReadingActivity.this);
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
                        totalHours = intent.getIntExtra("TOTAL_HOURS", 0);
                        currentHours = intent.getIntExtra("CURRENT_HOURS", 0);
                        hours = currentHours;
                        if (!TextUtils.isEmpty(timestr)) {
                         //   Log.e("current Time", timestr);
                            Log.e("Total_hours", "" + totalHours);
                            Log.e("current_hours", "" + currentHours);
                        }

                    }
                }, new IntentFilter(ForegroundService.ACTION_LOCATION_BROADCAST)
        );
    }

    private void callStartButtonClick() {
        if (Permissions.isCameraPermissionGranted(OperatorMeterReadingActivity.this, this)) {

            if (TextUtils.isEmpty(et_smeter_read.getText())) {
                Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
                //et_smeter_read.requestFocus();
                clearReadingImages();
                showReadingDialog(this,1);
            } else if (flag) {
                if (Permissions.isCameraPermissionGranted(this, this)) {
                openCameraIntent();
                flag = false;
                isStartImage = true;
                }
            } else {
                editor.putInt("systemClockTime", 0);
                editor.apply();
                editor.putInt("State", state_start);
                editor.apply();
                currentState = state_start;
                        updateStatusAndProceed(state_start);
                flag = true;
                btnStopService.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                btnStartService.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    btnStartService.setEnabled(true);
                }, 1000);
            }
        }
    }
    private void callStopButtonClick() {
        if (TextUtils.isEmpty(et_emeter_read.getText())) {
            Util.showToast("Please enter meter reading", OperatorMeterReadingActivity.this);
           // et_emeter_read.requestFocus();
            //takePhotoFromCamera();
            showReadingDialog(this,2);
        } else if (flag) {
            if (Permissions.isCameraPermissionGranted(this, this)) {
                openCameraIntent();
                isStartImage = false;
                flag = false;
            }
        } else {
            stopService();
            Log.e("currentstate--1", "----"+currentState);
            editor.putInt("State", state_stop);
            editor.apply();
            currentState = state_stop;
            updateStatusAndProceed(state_stop);
            flag = true;
            image = "";
            clearDataOnStop();
            //clearReadingImages();
           // et_smeter_read.requestFocus();
            btnStopService.setBackgroundTintList(getResources().getColorStateList(R.color.button_gray_color));

            btnStopService.setEnabled(false);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                btnStopService.setEnabled(true);
            }, 1000);

            Log.e("currentstate--2", "----"+currentState);
        }
    }

    public void startService() {
        if(!isMyServiceRunning(ForegroundService.class)){

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Operator meter reading");
        ContextCompat.startForegroundService(this, serviceIntent);

        /*editor.putInt("State", state_start);
        editor.apply();*/
        //buttonPauseService.setVisibility(View.VISIBLE);
        }
    }

    private boolean  isMyServiceRunning(Class<ForegroundService> foregroundServiceClass) {

            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (foregroundServiceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getInt("State", 0) == state_start) {
        }
        setStartImage(preferences.getString(Constants.OperatorModule.MACHINE_START_IMAGE,""));
        if (preferences.getInt("et_smeter_read", 0)==0)
        {

        }else {
            et_smeter_read.setText(String.valueOf(preferences.getInt("et_smeter_read", 0)));
        }

        Log.e("method--", "---OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("method--", "---OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("method--", "---OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("method--2", "---OnDestroy");
    }

    public void saveOperatorStateData(String machine_id, String workTime,String status, String statusCode, String lat, String lon, String meter_reading, int hours, int totalHours, String image) {
        operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id(machine_id);
        operatorRequestResponseModel.setWorkTime(workTime);
        operatorRequestResponseModel.setStatus_code(statusCode);
        operatorRequestResponseModel.setStatus(status);
        operatorRequestResponseModel.setLat(lat);
        operatorRequestResponseModel.setLong(lon);
        operatorRequestResponseModel.setMeter_reading(meter_reading);
        operatorRequestResponseModel.setHours(hours);
        operatorRequestResponseModel.setTotalHours(totalHours);
        operatorRequestResponseModel.setReasonId("5daeb2b25b3fc94c4866c33f");//strReasonId
        operatorRequestResponseModel.setImage(image);


        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().insert(operatorRequestResponseModel);
        //uploadImage(image);
        if (Util.isConnected(OperatorMeterReadingActivity.this)) {
            SyncAdapterUtils.manualRefresh();
        }

        List<OperatorRequestResponseModel> list = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();

        Log.e("list--2","---"+list.size());
        for (int i = 0; i < list.size(); i++) {
            Log.e("list--2--2","---"+list.get(i).getTotalHours());
        }

        //List<OperatorRequestResponseModel> list = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();
        /*for (int i = 0; i < list.size(); i++) {
            //Log.e("method--2","---"+list.get(i).getStatus());
            if (false) {
                //DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().deleteSinglSynccedOperatorRecord(list.get(i).get_id());

                if (isStartImage) {
                    Glide.with(this)
                            .applyDefaultRequestOptions(requestOptions)
                            .load(imageStartReading)   //Uri.parse(list.get(0).getImage()))
                            .into(img_start_meter);
                } else {
                    Glide.with(this)
                            .applyDefaultRequestOptions(requestOptions)
                            .load(imageEndReading)
                            .into(img_end_meter);
                }


            }
            Log.e("method--2", "---" + list.get(i).getStatus());
           *//* Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(image)
                    .into(img_start_meter);*//*

            Log.e("method--", "---" + new Gson().toJson(list.get(i)));
        }*/
        clearDataForNewDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {

                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) {
                        return;
                    }
                    finalUri = Util.getUri(imageFilePath);
                    UCrop.of(photoURI, finalUri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(500, 500)
                            .start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            image = resultUri.getPath();
            if (isStartImage) {
                imageStartReading = image;
                //CALL METHOD HERE
                setStartImage(imageStartReading);
                imageStartReading = image;
                editor.putString(Constants.OperatorModule.MACHINE_START_IMAGE,imageStartReading);
                editor.apply();
                callStartButtonClick();
            } else {
                imageEndReading = image;
                //CALL METHOD HERE
                setEndImage(imageEndReading);
                imageEndReading = image;
                editor.putString(Constants.OperatorModule.MACHINE_END_IMAGE,imageEndReading);
                editor.apply();
                callStopButtonClick();
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            flag = true;
            final Throwable cropError = UCrop.getError(data);
        }else if (resultCode !=RESULT_OK){
            flag = true;
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    public void clearReadingImages() {

        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load("")   //Uri.parse(list.get(0).getImage()))
                .into(img_start_meter);

        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load("")
                .into(img_end_meter);

        et_smeter_read.setText("");
        et_emeter_read.setText("");
    }

    //api call to upload record -
    private void uploadImage(String receivedImage) {
        String imageToSend = receivedImage;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Toast.makeText(getApplicationContext(), jsonString, Toast.LENGTH_LONG).show();
                            Log.d("response Received -", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(operatorRequestResponseModel));
                params.put("imageArraySize", String.valueOf("1"));//add string parameters
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                //   Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                //for (int i = 0;i<imageHashmap.size(); i++)
                {
                    // String key=(String)myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageToSend);
//                    if(imageType.equals("dieselReceipt")) {
//                        imageHashmap.put("diesel"+i, bitmap);
//                    } else if(imageType.equals("registerImage")) {
//                        imageHashmap.put("register"+i, bitmap);
//                    }
                    params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(OperatorMeterReadingActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, getPackageName() + ".file_provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


// input reading dialog
public String showReadingDialog(final Activity context, int pos){
    Dialog dialog;
    Button btnSubmit,btn_cancel;
    EditText edt_reason;
    Activity activity =context;

    dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_meter_reading_input_layout);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    edt_reason = dialog.findViewById(R.id.edt_reason);
    btn_cancel = dialog.findViewById(R.id.btn_cancel);
    btnSubmit = dialog.findViewById(R.id.btn_submit);

    btn_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

    btnSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                   /*Intent loginIntent = new Intent(context, LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(loginIntent);*/
            String strReason  = edt_reason.getText().toString();


            if (strReason.trim().length() < 1 ) {
                String msg = "Please enter the valid meter reading";//getResources().getString(R.string.msg_enter_name);
                //et_primary_mobile.requestFocus();
                Util.showToast(msg,OperatorMeterReadingActivity.this);
            }else {

                //-----------------------
                if (TextUtils.isEmpty(strReason)) {
                    Util.logger("Empty Reading", "Reading Can not be blank");
                    Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Reading Can not be blank",
                            Snackbar.LENGTH_LONG);
                } else {
                    /*if (fragment instanceof TMUserLeavesApprovalFragment) {
                        ((TMUserLeavesApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserAttendanceApprovalFragment) {
                        ((TMUserAttendanceApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserProfileApprovalFragment) {
                        ((TMUserProfileApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserFormsApprovalFragment) {
                        ((TMUserFormsApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }*/
                    onReceiveReason(strReason, pos);
                    dialog.dismiss();
                }
            }
        }
    });
    dialog.show();


    return "";
}

    public void onReceiveReason(String strReason, int pos) {
        //callRejectAPI(strReason,pos);
        Util.logger("Received Reading", "--"+strReason);
        /*matrimonyFragmentPresenter.VerifyUserProfile(strReason,"",matrimonyMeetList.get(currentPosition).getId());
        mobileNumberEntered = strReason;*/
        if (pos==1){
            et_smeter_read.setText(strReason);
            editor.putInt("et_smeter_read", Integer.parseInt(strReason));
            editor.apply();
            callStartButtonClick();
        }else {
            editor.putInt("et_emeter_read", Integer.parseInt(strReason));
            editor.apply();
            if (isMeterReadingRight(strReason))
            {
                et_emeter_read.setText(strReason);
                callStopButtonClick();
            }else {
                Util.showToast("Please enter correct meter reading", OperatorMeterReadingActivity.this);
            }
        }

    }

    private boolean isMeterReadingRight(String endMeterReading) {
        Util.logger("emeter Reading", "--"+preferences.getInt("et_emeter_read", 0));
        Util.logger("smeter Reading", "--"+preferences.getInt("et_smeter_read", 0));
        if ((preferences.getInt("et_emeter_read", 0) - preferences.getInt("et_smeter_read", 0)) > 0) {
            return true;
        } else {
            return false;
        }
    }




    private void GetLocationofOperator(){
        if (gpsTracker.isGPSEnabled(this, this)) {
            location = gpsTracker.getLocation();
            if (location != null) {
                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                Util.logger("Lat -Long--", lat+"--"+lon);

            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            Util.showToast("Unable to get location.", this);
        }
    }

    public void showPendingApprovalRequests(OperatorMachineData operatorMachineData) {
        machine_id = operatorMachineData.getMachine_id();
        tv_machine_code.setText(operatorMachineData.getMachine_code());
        editor.putString("machine_id",machine_id);
        editor.putString("machine_code",machine_id);
        editor.apply();
        for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().size() ; i++) {
            ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().get(i).getValue());
        }
        Gson gson = new Gson();
        editor.putString("operatorMachineData",gson.toJson(operatorMachineData));
        editor.apply();
    }




    public void clearDataForNewDate(){
        if (preferences.getLong("todaysDate", 0)==0){
            setTodaysDate();
        }else {
            Calendar today = Calendar.getInstance();
            long diff = today.getTimeInMillis() - preferences.getLong("todaysDate", 0); //result in millis
            long days = diff / (24 * 60 * 60 * 1000);
            Util.logger("Date difference", "-day-" + days);
            if (days>=1){
                setTodaysDate();
                //clear data here
                editor.putInt("State",0);
                editor.putInt("et_emeter_read",0);
                editor.putInt("et_smeter_read",0);

                editor.putInt("systemTime",0);
                editor.putInt("systemClockTime",0);
                editor.putInt("totalHours", 0);
                editor.apply();
            }else {

            }
        }

    }
    public void setTodaysDate(){
        Calendar thatDay = Calendar.getInstance();
        /*thatDay.set(DAY_OF_MONTH,Calendar.getInstance().get(DAY_OF_MONTH));
        thatDay.set(Calendar.MONTH,Calendar.getInstance().get(MONTH)); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, Calendar.getInstance().YEAR);*/
        thatDay.set(Calendar.HOUR_OF_DAY,00);
        thatDay.set(Calendar.MINUTE,00);
        thatDay.set(Calendar.SECOND,00);
        editor.putLong("todaysDate", thatDay.getTimeInMillis());
        editor.apply();

        //clear data here
        editor.putInt("State",0);
        editor.putInt("et_emeter_read",0);
        editor.putInt("et_smeter_read",0);

        editor.putInt("systemTime",0);
        editor.putInt("systemClockTime",0);
        editor.putInt("totalHours", 0);
        editor.apply();
    }

    public void clearDataOnStop(){
        editor.putInt("et_emeter_read",0);
        editor.putInt("et_smeter_read",0);

        editor.putInt("systemTime",0);
        editor.putInt("systemClockTime",0);
        editor.putInt("totalHours", 0);
        editor.apply();

        editor.putString(Constants.OperatorModule.MACHINE_START_IMAGE,"");
        editor.putString(Constants.OperatorModule.MACHINE_END_IMAGE,"");
        editor.apply();
        clearReadingImages();
        Util.logger("statenow", "statenow" + preferences.getInt("State", 0));
        Log.e("totalHours---destrdata", "---"+preferences.getInt("totalHours", 0));
    }



// connectivity broadcast--
private void initConnectivityReceiver() {
    /*connectionReceiver = new ConnectivityReceiver();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    Platform.getInstance().registerReceiver(connectionReceiver, filter);*/

    connectionReceiver = new ConnectivityReceiver();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(new ConnectivityReceiver(), intentFilter);
    connectivityReceiverListener =this::onNetworkConnectionChanged;
}

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            SyncAdapterUtils.manualRefresh();
        }else {

        }
    }


    public void setStartImage(String image){
        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load(image)   //Uri.parse(list.get(0).getImage()))
                .into(img_start_meter);
    }

    public void setEndImage(String image){
        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load(image)
                .into(img_end_meter);
    }

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
       String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
       Gson gson = new Gson();
       OperatorMachineData operatorMachineData = gson.fromJson(operatorMachineDataStr,OperatorMachineData.class);
        strReasonId = operatorMachineData.getNonutilisationTypeData().get(selectedPosition).getValue();
        updateStatusAndProceed(state_halt);
        clearReadingImages();

    }

    private void showMultiSelectBottomsheet(String Title,String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(this, selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }
}