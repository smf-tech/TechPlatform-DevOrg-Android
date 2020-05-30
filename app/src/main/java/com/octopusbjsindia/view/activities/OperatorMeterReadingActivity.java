package com.octopusbjsindia.view.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel;
import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.OperatorMeterReadingActivityPresenter;
import com.octopusbjsindia.receivers.AlarmReceiver;
import com.octopusbjsindia.receivers.ConnectivityReceiver;
import com.octopusbjsindia.services.ForegroundService;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

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

import static com.octopusbjsindia.receivers.ConnectivityReceiver.connectivityReceiverListener;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;
import static com.octopusbjsindia.utility.Util.snackBarToShowMsg;

public class OperatorMeterReadingActivity extends BaseActivity implements APIDataListener, ConnectivityReceiver.ConnectivityReceiverListener, SingleSelectBottomSheet.MultiSpinnerListener {
    //Alarm
    Gson gson = new Gson();
    Dialog alarmDialog;
    private final long[] mVibratePattern = {0, 500, 500};
    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private int hour_of_day;
    private int minute_of_hour;
    private int minute_of_pause;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    //------

    private ArrayList<AlarmRequest> alarmRequests = new ArrayList<AlarmRequest>();
    private long mLastClickTime = 0;
    private String strReasonId="";
    ArrayList<String> ListHaltReasons = new ArrayList<>();
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private BroadcastReceiver connectionReceiver;
    private GPSTracker gpsTracker;
    private TextView tv_version_code,tv_device_name;
    private Location location;
    ImageView gear_action_start,gear_action_stop;
    private OperatorMeterReadingActivityPresenter operatorMeterReadingActivityPresenter;
    private static final String TAG = OperatorMeterReadingActivity.class.getCanonicalName();
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    Uri photoURI;
    Uri finalUri;
    String currentPhotoPath = "";
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
    public TextView tv_text,tv_machine_code,tv_machine_state,tv_date_today;
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

    private String state_start_string = "Working";
    private String state_stop_string = "Stopped";
    private String state_pause_string = "Break";
    private String state_halt_string = "Halted";

    private int currentState = 0;
    private RequestOptions requestOptions,requestOptionsjcb;
private Toolbar toolbar;
private ImageView toolbar_edit_action,toolbar_action;
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
            saveOperatorStateData(machine_id, workTime, "start",""+state_start, lat, lon, et_smeter_read.getText().toString(), hours, totalHours, image);
            image = "";
            gear_action_start.setVisibility(View.VISIBLE);
            setWorkingAnime();
            gear_action_stop.setVisibility(View.GONE);
            Util.showToast("Machine started Working.",this);
            tv_machine_state.setText(state_start_string);
            currentState =state_start;
             cancelAlarm();
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
            tv_machine_state.setText(state_pause_string);
            currentState =state_pause;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("startTime", 0);
            editor.apply();
            //setHalfHourReminder();
            //setAlarm();
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
            tv_machine_state.setText(state_stop_string);
            currentState =state_stop;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("startTime", 0);
            editor.apply();
        }else if (currentState == state_halt){
            stopService();
            workTime = String.valueOf(new Date().getTime());//String.valueOf(Util.getDateInepoch(""));
            Log.e("Timestamp--", "---" + workTime);
            saveOperatorStateData(machine_id, workTime, "halt",""+state_halt, lat, lon, meter_reading, hours, totalHours, image);
            gear_action_start.setVisibility(View.GONE);
            gear_action_stop.setVisibility(View.VISIBLE);
            Util.showToast("Machine facing issue and stopped working.",this);
            tv_machine_state.setText(state_halt_string);
            currentState =state_halt;
        }
        Log.e("currentstate--4", "----"+currentState);
    }

    private void setHalfHourReminder() {
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            10 * 1000, alarmIntent);
        }else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            10 * 1000, alarmIntent);
        }
        Log.e("alarm Receiver","Receiver Called");
    }
    private void CancelAlarm(){
        try {
            alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmIntent.cancel();
            alarmMgr.cancel(alarmIntent);
            Log.e("alarm cancel","cancel Called");
        }catch (Exception e){
            Log.e("alarm cancel","cancel Called"+e.getMessage());
            e.printStackTrace();
        }

    }

    //  new
    private void setAlarm()
    {
        
        Log.e("alarm Difference",String.valueOf(getDifferencebetweenAlarms()));
        Gson gson = new Gson();
        PendingIntent sender;
        AlarmRequest alarmRequest = new AlarmRequest();
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent;
        intent = new Intent(this, AlarmReceiver.class);

        if (getDifferencebetweenAlarms()>31 ||getDifferencebetweenAlarms()<0){
            alarmRequests.clear();
            //alarm.toIntent(intent);
            sender = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //alarmMgr.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +20*1000, sender);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().getTimeInMillis() +
                                preferences.getInt("minute_of_pause",0)*60*1000, sender);
                Log.e("alarm SET-M","alarm SET");

                alarmRequest.alarmid =1001;
                alarmRequest.AlarmName ="testalarm";
                alarmRequests.add(alarmRequest);

                String stringValue = gson.toJson(alarmRequests);
                editor.putString("alarmRequestsArray",stringValue);
                editor.commit();
            }else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                preferences.getInt("minute_of_pause",0)*60*1000, sender);
                Log.e("alarm SET","alarm SET");

                alarmRequest.alarmid =1001;
                alarmRequest.AlarmName ="testalarm";
                alarmRequests.add(alarmRequest);

                String stringValue = gson.toJson(alarmRequests);
                editor.putString("alarmRequestsArray",stringValue);
                editor.commit();
            }
        }else {
           // setDailyAlarm();

            }






// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.


            //-------

            /*alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis() +
                            60*2000, sender);
            alarmRequest = new AlarmRequest();
            alarmRequest.alarmid =1002;
            alarmRequest.AlarmName ="testalarm2";
            alarmRequests.add(alarmRequest);*/


            String receivedStringArray = preferences.getString("alarmRequestsArray","");
            TypeToken<List<AlarmRequest>> token = new TypeToken<List<AlarmRequest>>() {};
            List<AlarmRequest> AlarmRequestList = gson.fromJson(receivedStringArray, token.getType());
            Log.e("alarm list","alarm list"+AlarmRequestList.size());
            Log.e("alarm list","alarm list"+AlarmRequestList.get(0).AlarmName);


        }

    private int getDifferencebetweenAlarms() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, preferences.getInt("hour_of_day",0));
        calendar.set(Calendar.MINUTE, preferences.getInt("minute_of_hour",0));
        long diffInMs = (calendar.getTimeInMillis() - (new Date(System.currentTimeMillis()).getTime()));


        Log.e("alarm curentmili","cureent"+calendar.getTimeInMillis());
        Log.e("alarm aftermili","after"+(new Date(System.currentTimeMillis()).getTime()));
        return (int) (diffInMs/(60*1000));
    }


    private void cancelAlarm()
    {
        if(mRingtone!=null){
            alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            Gson gson = new Gson();
            String receivedStringArray = preferences.getString("alarmRequestsArray","");
            TypeToken<List<AlarmRequest>> token = new TypeToken<List<AlarmRequest>>() {};
            List<AlarmRequest> AlarmRequestList = gson.fromJson(receivedStringArray, token.getType());
            int Requestcode = AlarmRequestList.get(0).alarmid;
        PendingIntent sender;
        Intent intent;
        intent = new Intent(this, AlarmReceiver.class);
        sender = PendingIntent.getBroadcast(this,Requestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(sender);
            mRingtone.stop();
            AlarmRequestList.remove(0);
            String stringValue = gson.toJson(AlarmRequestList);
            editor.putString("alarmRequestsArray",stringValue);
            editor.commit();
            Log.e("alarm Canceled","alarm Canceled");
            Log.e("alarm Canceled","alarm Requestcode was"+Requestcode);
          //  setDailyAlarm();
        }

    }


    public void UpdateAlarmData(){

    }


    private void setWorkingAnime() {
        gear_action_stop.setVisibility(View.GONE);
        gear_action_start.setVisibility(View.VISIBLE);
        Glide.with(this)
                .applyDefaultRequestOptions(requestOptionsjcb)
                .load(R.drawable.jcb_gif)   //Uri.parse(list.get(0).getImage()))
                .into(gear_action_start);
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
            setWorkingAnime();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
            setIntent(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_meter_reading_new);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.i("oncreate", "oncreate called");
        /*String toOpen = getIntent().getStringExtra("fromAlarm");
        if(toOpen != null){
            Log.i("toOpen", "toOpen called");
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mRingtone.play();
        }*/
        //CancelAlarm();
        tv_version_code = findViewById(R.id.tv_version_code);
        tv_device_name = findViewById(R.id.tv_device_name);
        toolbar =  findViewById(R.id.operator_toolbar);
        toolbar_edit_action =  findViewById(R.id.toolbar_edit_action);
        toolbar_action  =  findViewById(R.id.toolbar_action);
        gear_action_start = findViewById(R.id.gear_action_start);
        gear_action_stop = findViewById(R.id.gear_action_stop);
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_meter);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());

        requestOptionsjcb= new RequestOptions().placeholder(R.drawable.jcb_stopped);
        requestOptionsjcb = requestOptions.apply(RequestOptions.noTransformation());

        gpsTracker = new GPSTracker(OperatorMeterReadingActivity.this);
       // SyncAdapterUtils.periodicSyncRequest();
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
        tv_machine_code = findViewById(R.id.tv_machine_code_new);
        tv_machine_state= findViewById(R.id.tv_machine_state);
        tv_date_today= findViewById(R.id.tv_date_today);
        et_emeter_read = findViewById(R.id.et_emeter_read);
        et_smeter_read = findViewById(R.id.et_smeter_read);
        img_start_meter = findViewById(R.id.img_start_meter);
        img_end_meter = findViewById(R.id.img_end_meter);

        tv_date_today.setText(Util.getCurrentDate());
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
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                    Log.e("clickTime retuned", "" + "Return");
                    mLastClickTime = SystemClock.elapsedRealtime();
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.e("clickTime cuurent", "" + SystemClock.elapsedRealtime());
                Log.e("clickTime Lastt", "" + mLastClickTime);

                buttonPauseService.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    buttonPauseService.setEnabled(true);
                }, 1000);
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

        toolbar_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncMachineOperatorData();
            }
        });

        toolbar_edit_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAlarm();

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

                        if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                            Log.e("clickTime retuned", "" + "Return");
                            mLastClickTime = SystemClock.elapsedRealtime();
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        strReasonId ="";
                        String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                        Gson gson = new Gson();
                        OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr,OperatorMachineCodeDataModel.class);
/*                        for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getEn().size() ; i++) {
                            ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                        }*/
                        ListHaltReasons.clear();
                        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)){
                            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getMr().size() ; i++) {
                                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getMr().get(i).getValue());
                            }
                        }else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)){
                            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getHi().size() ; i++) {
                                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getHi().get(i).getValue());
                            }
                        }else {
                            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getEn().size() ; i++) {
                                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                            }
                        }

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
                    if (!preferences.getBoolean("isMachineRemoved",false)){

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                        Log.e("clickTime retuned", "" + "Return");
                        mLastClickTime = SystemClock.elapsedRealtime();
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
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
                }else {
                    showDialogMachineRemoved(OperatorMeterReadingActivity.this,1);
                }
            }
        });


        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
                    Log.e("clickTime retuned", "" + "Return");
                    mLastClickTime = SystemClock.elapsedRealtime();
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

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
                    Util.showToast("Please Start the machine.", OperatorMeterReadingActivity.this);
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
                           // Log.e("Total_hours", "" + totalHours);
                          //  Log.e("current_hours", "" + currentHours);
                        }

                    }
                }, new IntentFilter(ForegroundService.ACTION_LOCATION_BROADCAST)
        );

        try {
            String appVersion  = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tv_version_code.setText("Version-"+appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String deviceName = android.os.Build.MODEL;
            String deviceMake = Build.MANUFACTURER;
            tv_device_name.setText(deviceMake+" "+deviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }

      //  setDailyAlarm();
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
                //openCameraIntent();
                    openCamera();
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
                //openCameraIntent();
                openCamera();
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

        /*String toOpen = getIntent().getStringExtra("fromAlarm");
        if(toOpen != null){
            Log.i("toOpen", "toOpen called");

            if (mRingtone != null && mRingtone.isPlaying()) {

            } else {
                mRingtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                mRingtone.setLooping(true);
                mRingtone.play();
                Log.e("mRingtone--", "---OnPlaying");
            }
            *//*Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();*//*

            if (alarmDialog != null && alarmDialog.isShowing()) {

            } else {
                showAlarmDialog(this, 1);
            }
        }*/

        if (preferences.getInt("State", 0) == state_start) {
        }
        setStartImage(preferences.getString(Constants.OperatorModule.MACHINE_START_IMAGE,""));
        if (preferences.getFloat("et_smeter_read", 0)==0)
        {

        }else {
            et_smeter_read.setText(String.valueOf(preferences.getFloat("et_smeter_read", 0)));
        }
        if (!preferences.getString("machine_code", "").equalsIgnoreCase("")){
            tv_machine_code.setText(preferences.getString("machine_code", ""));
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
        if(mRingtone!=null && mRingtone.isPlaying()){
            //  cancelAlarm();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("method--2", "---OnDestroy");
    }

    public void saveOperatorStateData(String machine_id, String workTime,String status, String statusCode, String lat, String lon, String meter_reading, int hours, int totalHours, String image) {
        operatorRequestResponseModel = new OperatorRequestResponseModel();
        if (!preferences.getString("machine_id", "").equalsIgnoreCase("")) {
            operatorRequestResponseModel.setMachine_id(preferences.getString("machine_id", ""));
        }
        operatorRequestResponseModel.setWorkTime(workTime);
        operatorRequestResponseModel.setStatus_code(statusCode);
        operatorRequestResponseModel.setStatus(status);
        operatorRequestResponseModel.setLat(lat);
        operatorRequestResponseModel.setLong(lon);
        operatorRequestResponseModel.setMeter_reading(meter_reading);
        operatorRequestResponseModel.setHours(hours);
        int totalHourstoSend =  preferences.getInt("totalHours", 0);
        operatorRequestResponseModel.setTotalHours(totalHourstoSend);
        operatorRequestResponseModel.setReasonId(strReasonId);//
        operatorRequestResponseModel.setImage(image);

        String JsontoString = new Gson().toJson(operatorRequestResponseModel);
        Log.e("REQJSOs entry",JsontoString);
        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().insert(operatorRequestResponseModel);
        //uploadImage(image);
        if (Util.isConnected(OperatorMeterReadingActivity.this)) {
            //SyncAdapterUtils.periodicSyncRequest();
            SyncAdapterUtils.manualRefresh();
            Log.e("REQJSOs manualRefresh","call given");
        }

        List<OperatorRequestResponseModel> list = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();

        Log.e("list--2","---"+list.size());
        for (int i = 0; i < list.size(); i++) {
            Log.e("list--2--2","---"+list.get(i).getStatus()+" "+list.get(i).getWorkTime());
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

               /* try {
                    *//*String imageFilePath = getImageName();
                    if (imageFilePath == null) {
                        return;
                    }*//*
                    Uri uri = Uri.parse(imageFilePath);
                    finalUri = Util.getUri(imageFilePath);
                    UCrop.of(photoURI, uri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(500, 500)
                            .start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }*/

               // Uri uri = Uri.parse(currentPhotoPath);
                Uri uri=Uri.fromFile(new File(currentPhotoPath));
                openCropActivity(uri, uri);
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
            if (isStartImage) {
                et_smeter_read.setText("");
            }else {
                et_emeter_read.setText("");
            }
            final Throwable cropError = UCrop.getError(data);
        }else if (resultCode !=RESULT_OK){
            flag = true;
            if (isStartImage) {
                et_smeter_read.setText("");
            }else {
                et_emeter_read.setText("");
            }
        }
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .withAspectRatio(5f, 5f)
                .start(this);
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


    private File getImageFile() {
        /*String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Octopus"
        );
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;*/


        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.Image.IMAGE_STORAGE_DIRECTORY);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        currentPhotoPath = file.getPath();

        return file;

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
    /*private void uploadImage(String receivedImage) {
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
    }*/

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

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(); // 1
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
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
            editor.putFloat("et_smeter_read", Float.parseFloat(strReason));
            editor.apply();
            callStartButtonClick();
        }else {
            editor.putFloat("et_emeter_read", Float.parseFloat(strReason));
            editor.apply();
            if (isMeterReadingRight(strReason))
            {
                et_emeter_read.setText(strReason);
                callStopButtonClick();
            }else {
                if ((preferences.getFloat("et_emeter_read", 0) - preferences.getFloat("et_smeter_read", 0)) <=12) {
                    Util.showToast("End meter reading should be greater than Start meter reading", OperatorMeterReadingActivity.this);
                }else {
                    Util.showToast("End meter should not be greater than 12 hours.", OperatorMeterReadingActivity.this);
                }

            }
        }

    }

    private boolean isMeterReadingRight(String endMeterReading) {
        Util.logger("emeter Reading", "--"+preferences.getFloat("et_emeter_read", 0));
        Util.logger("smeter Reading", "--"+preferences.getFloat("et_smeter_read", 0));
        if ((preferences.getFloat("et_emeter_read", 0) - preferences.getFloat("et_smeter_read", 0)) > 0 && (preferences.getFloat("et_emeter_read", 0) - preferences.getFloat("et_smeter_read", 0)) <=12) {
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


    public void removeMachineid() {
        editor.putBoolean("isMachineRemoved",true);
        editor.apply();

    }

    public void showPendingApprovalRequests(OperatorMachineCodeDataModel operatorMachineData) {

        hour_of_day = operatorMachineData.getHour_of_day();
        minute_of_hour = operatorMachineData.getMinute_of_hour();
        minute_of_pause  = operatorMachineData.getMinute_of_pause();


        machine_id = operatorMachineData.getMachine_id();
        editor.putBoolean("isMachineRemoved",false);
        editor.apply();
        tv_machine_code.setText(operatorMachineData.getMachine_code());
        editor.putString("machine_id",machine_id);
        editor.putString("machine_code",operatorMachineData.getMachine_code());
        editor.putInt("hour_of_day",hour_of_day);
        editor.putInt("minute_of_hour",minute_of_hour);
        editor.putInt("minute_of_pause",minute_of_pause);

        editor.apply();

        if (Locale.getDefault().getLanguage().equalsIgnoreCase("mr")){
            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getMr().size() ; i++) {
                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getMr().get(i).getValue());
            }
        }else if (Locale.getDefault().getLanguage().equalsIgnoreCase("hi")){
            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getHi().size() ; i++) {
                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getHi().get(i).getValue());
            }
        }else {
            for (int i = 0; i <operatorMachineData.getNonutilisationTypeData().getEn().size() ; i++) {
                ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
            }
        }

        Gson gson = new Gson();
        editor.putString("operatorMachineData",gson.toJson(operatorMachineData));
        editor.apply();

      //  setDailyAlarm();
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
                if (preferences.getInt("State", 0) != state_stop) {
                et_emeter_read.setText("00");
                flag = false;
                image ="";
                callStopButtonClick();
                }
                setTodaysDate();
                //clear data here
                editor.putInt("State",0);
                editor.putFloat("et_emeter_read",0);
                editor.putFloat("et_smeter_read",0);

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
        editor.putFloat("et_emeter_read",0);
        editor.putFloat("et_smeter_read",0);

        editor.putInt("systemTime",0);
        editor.putInt("systemClockTime",0);
        editor.putInt("totalHours", 0);
        editor.apply();
    }

    public void clearDataOnStop(){
        editor.putFloat("et_emeter_read",0);
        editor.putFloat("et_smeter_read",0);

        editor.putInt("systemTime",0);
        editor.putInt("systemClockTime",0);
        editor.putInt("totalHours", 0);
        editor.apply();
        totalHours = 0;
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
            //SyncAdapterUtils.periodicSyncRequest();
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
        OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr,OperatorMachineCodeDataModel.class);
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            strReasonId = operatorMachineData.getNonutilisationTypeData().getMr().get(selectedPosition).get_id();
        }else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            strReasonId = operatorMachineData.getNonutilisationTypeData().getHi().get(selectedPosition).get_id();
        }else {
            strReasonId = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).get_id();
        }
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



    private class AlarmRequest {
        public int getAlarmid() {
            return alarmid;
        }

        public void setAlarmid(int alarmid) {
            this.alarmid = alarmid;
        }

        public String getAlarmName() {
            return AlarmName;
        }

        public void setAlarmName(String alarmName) {
            AlarmName = alarmName;
        }

        int alarmid;
        String AlarmName;
    }

    public void setDailyAlarm(){

        Gson gson = new Gson();
        PendingIntent sender;
        AlarmRequest alarmRequest = new AlarmRequest();
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent;
        intent = new Intent(this, AlarmReceiver.class);

        sender = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmRequests.clear();
        // 8pm alarm
        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, preferences.getInt("hour_of_day",0));
        calendar.set(Calendar.MINUTE, preferences.getInt("minute_of_hour",0));

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
       // sender = PendingIntent.getBroadcast(this, 1002, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       // alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);

        Log.e("alarm SET 2","alarm SET");
        Log.e("alarm hour_of_day","cureent"+preferences.getInt("hour_of_day",0));
        Log.e("alarm minute_of_hour","cureent"+preferences.getInt("minute_of_hour",0));
        Log.e("alarm minute_of_pause","cureent"+preferences.getInt("minute_of_pause",0));

        alarmRequest = new AlarmRequest();
        alarmRequest.alarmid =1001;
        alarmRequest.AlarmName ="testalarm2";
        alarmRequests.add(alarmRequest);

        String stringValue = gson.toJson(alarmRequests);
        editor.putString("alarmRequestsArray",stringValue);
        editor.commit();
    }

//Alarm dismiss dialog
    public String showAlarmDialog(final Activity context, int pos){

        Button btnSubmit,btn_cancel;
        EditText edt_reason;
        Activity activity =context;

        alarmDialog = new Dialog(context);
        alarmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alarmDialog.setContentView(R.layout.dialog_alarm_dismiss_layout);
        alarmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        btnSubmit = alarmDialog.findViewById(R.id.btn_submit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        cancelAlarm();
                alarmDialog.dismiss();

            }
        });
        alarmDialog.show();


        return "";
    }


    //-----
//Alarm dismiss dialog
    public String showDialogMachineRemoved(final Activity context, int pos){

        Button btnSubmit,btn_cancel;
        EditText edt_reason;
        Activity activity =context;

        alarmDialog = new Dialog(context);
        alarmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alarmDialog.setContentView(R.layout.dialog_machine_removed_layout);
        alarmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        btnSubmit = alarmDialog.findViewById(R.id.btn_submit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmDialog.dismiss();
                tv_machine_code.setText("");

            }
        });
        alarmDialog.show();


        return "";
    }


    ///manual sync
    private void syncMachineOperatorData() {

        if (Util.isConnected(OperatorMeterReadingActivity.this)) {
            List<OperatorRequestResponseModel> list = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().getAllProcesses();
            for (final OperatorRequestResponseModel data : list) {
                uploadMachineLog(data);
            }
        }else {
            Util.showToast(getResources().getString(R.string.msg_no_network), this);
        }
    }

    private void uploadMachineLog(OperatorRequestResponseModel data) {

        final String upload_URL = BuildConfig.BASE_URL + Urls.OperatorApi.MACHINE_WORKLOG;

        Log.e("sync--", "---" + new Gson().toJson(data));
        String imageToSend = data.getImage();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("response Received -", jsonString);
                            DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                    deleteSinglSynccedOperatorRecord(data.get_id());

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(OperatorMeterReadingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OperatorMeterReadingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(data));
                params.put("imageArraySize", String.valueOf("1"));//add string parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                {
                    if (TextUtils.isEmpty(imageToSend)) {
                        params.put("image0", new DataPart("image0", new byte[0],
                                "image/jpeg"));
                    } else {
                        drawable = new BitmapDrawable(getResources(), imageToSend);

                        params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                                "image/jpeg"));
                    }
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(OperatorMeterReadingActivity.this);
        rQueue.add(volleyMultipartRequest);
    }
}