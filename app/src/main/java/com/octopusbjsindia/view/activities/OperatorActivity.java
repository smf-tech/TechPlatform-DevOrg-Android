package com.octopusbjsindia.view.activities;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;
import static com.octopusbjsindia.utility.Constants.FORM_DATE;
import static com.octopusbjsindia.utility.Util.getDateInLong;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;
import static com.octopusbjsindia.utility.Util.showDateDialogEnableAfterMin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel;
import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.OperatorActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;
import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class OperatorActivity extends AppCompatActivity implements APIDataListener,
        SingleSelectBottomSheet.MultiSpinnerListener, View.OnClickListener {

    private final String TAG = "OperatorActivity";
    private ImageView img_start_meter, img_end_meter, clickedImageView;
    private TextView tv_machine_code, tvVersionCode, tvDeviceName;
    private ImageView iv_jcb;
    private EditText et_emeter_read, et_smeter_read, etDate;
    private Button btnStartService, btnStopService;
    private String machine_id = "", machine_code = "", machine_status = "", structure_id = "";
    private ArrayList<String> ListHaltReasons = new ArrayList<>();
    private Uri outputUri, finalUri, startUri, stopUri;
    private String imageType;
    private String currentPhotoPath = "";
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private SingleSelectBottomSheet bottomSheetDialogFragment;
    private int state_start = 112;
    private int state_stop = 110;
    private int state_halt = 111;
    private RequestQueue rQueue;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SimpleDateFormat df;
    private String strReasonId = "";
    private GPSTracker gpsTracker;
    private Location location;
    private MaterialToolbar toolbar;
    private long serverCurrentTimeStamp;
    private static final String STATUS_WORKING = "Working";
    private static final String STATUS_STOP = "Stop";
    private OperatorRequestResponseModel lastWorkingRecordData;
    private OperatorRequestResponseModel previousLatestRecord;
    private OperatorRequestResponseModel nextLatestRecord;
    private OperatorRequestResponseModel submittedStopRecord;
    final Handler handler = new Handler();
    private boolean isOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        preferences = getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        toolbar = findViewById(R.id.operator_toolbar);
        setSupportActionBar(toolbar);
        tvVersionCode = findViewById(R.id.tv_version_code);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tv_machine_code = findViewById(R.id.tv_machine_code_new);
        etDate = findViewById(R.id.et_date);
        etDate.setOnClickListener(this);
        et_emeter_read = findViewById(R.id.et_emeter_read);
        et_smeter_read = findViewById(R.id.et_smeter_read);
        img_start_meter = findViewById(R.id.img_start_meter);
        img_end_meter = findViewById(R.id.img_end_meter);
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        // toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        iv_jcb = findViewById(R.id.jcb);

        OperatorActivityPresenter presenter = new OperatorActivityPresenter(this);
        machine_code = getIntent().getStringExtra("machineCode");
        machine_id = getIntent().getStringExtra("machineId");
        structure_id = getIntent().getStringExtra("structureId");
        setDeviceInfo();

        etDate.setFocusable(false);
        etDate.setOnClickListener(this);
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        img_start_meter.setOnClickListener(this);
        img_end_meter.setOnClickListener(this);
        //toolbar_edit_action.setOnClickListener(this);
        //toolbar_back_action.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // TODO for now this code is called after machine details api.
        if (machine_id != null && structure_id != null && machine_code != null) { // For NON-FA roles
            isOperator = false;
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24);
            lastWorkingRecordData = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                    getLastWorkingRecord(machine_id);
            tv_machine_code.setText(machine_code);
            if (lastWorkingRecordData != null) {
                setWorkingMachineData(lastWorkingRecordData);
            } else { //new date entry
                updateUIForNewEntry();
            }

            //api call to get list of halt reason
            String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
            Gson gson = new Gson();
            OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr,
                    OperatorMachineCodeDataModel.class);
            if (operatorMachineData == null) {
                presenter.getAllFiltersRequests(machine_code);
            }

        } else {   // For FA/Operator roles
            toolbar.setNavigationIcon(null);
            isOperator = true;
            String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
            Gson gson = new Gson();
            OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr,
                    OperatorMachineCodeDataModel.class);
            if (operatorMachineData != null && !operatorMachineData.getMachine_id().isEmpty()) {
                machine_id = operatorMachineData.getMachine_id();
                machine_code = operatorMachineData.getMachine_code();
                tv_machine_code.setText(machine_code);

                lastWorkingRecordData = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                        getLastWorkingRecord(machine_id);
                if (lastWorkingRecordData != null) {
                    setWorkingMachineData(lastWorkingRecordData);
                }
            } else {
                presenter.getAllFiltersRequests("no_machine");
            }
        }

        //get lat,long of location
        gpsTracker = new GPSTracker(this);
        if (Permissions.isLocationPermissionGranted(this, this)) {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    private void setDeviceInfo() {
        try {
            String appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersionCode.setText("v" + appVersion);
            String deviceName = android.os.Build.MODEL;
            String deviceMake = Build.MANUFACTURER;
            tvDeviceName.setText(deviceMake + " " + deviceName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        String msg = "";
        switch (view.getId()) {
            case R.id.buttonStartService:
                if (startUri == null)
                    msg = "Please select Start meter reading photo";
                else if (et_smeter_read.getText().toString().length() <= 0)
                    msg = "Please enter start meter reading";
                else if (machine_code.isEmpty())
                    msg = "Machine not assigned. Contact to DPM.";
                else if (etDate.getText().toString().isEmpty()) {
                    msg = "Please select meter reading date.";
                }
                if (msg.length() <= 0) {
                    // Get previous machine reading entry for validation
                    Long selectedTimestamp = getDateInLong(etDate.getText().toString());
                    //todo need validation when previous latest record from db is of the days after current selected date
                    previousLatestRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestRecord(machine_id, selectedTimestamp);
                    //todo get next days entry and check current start& stop reading should be less than its start reading
                    nextLatestRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestRecordAfter(machine_id, selectedTimestamp);

                    if (previousLatestRecord != null) { //this is previous latest record
                        if (Integer.parseInt(et_smeter_read.getText().toString()) >= Integer.parseInt(
                                previousLatestRecord.getStop_meter_reading())) {

                            if (nextLatestRecord != null) { //this is next latest record
                                if (Integer.parseInt(et_smeter_read.getText().toString()) < Integer.parseInt(
                                        nextLatestRecord.getStart_meter_reading())) {
                                    addStartMeterRecord();
                                } else {
                                    Toast.makeText(this, "Start meter reading should be less than " +
                                            "next available record's start reading.", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                addStartMeterRecord();
                            }
                        } else {
                            Toast.makeText(this, "Start meter reading should be greater than " +
                                    "previous record's stop reading", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (nextLatestRecord != null) { //this is next latest record
                            if (Integer.parseInt(et_smeter_read.getText().toString()) < Integer.parseInt(
                                    nextLatestRecord.getStart_meter_reading())) {
                                addStartMeterRecord();
                            } else {
                                Toast.makeText(this, "Start meter reading should be less than " +
                                        "next available record's start reading.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // This section means previous entry for this machine is not available.
                            addStartMeterRecord();
                        }
                    }
                } else {
                   // Util.showToast(msg, OperatorActivity.this);
                    Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonStopService:
                if (stopUri == null) {
                    msg = "Please select Stop meter reading photo";
                } else if (et_emeter_read.getText().toString().length() <= 0) {
                    msg = "Please Enter Stop meter reading";
                } else if (Float.parseFloat(et_emeter_read.getText().toString()) <
                        Float.parseFloat(et_smeter_read.getText().toString())) {
                    msg = "Stop meter reading cannot be less than Start meter reading.";
                } else if ((Float.parseFloat(et_emeter_read.getText().toString()) -
                        Float.parseFloat(et_smeter_read.getText().toString()) > 24)) {
                    msg = "Difference between start and end meter readings can not exceed 24.";
                } else if (machine_code.isEmpty()) {
                    msg = "Machine not assigned. Contact to DPM.";
                }
                if (msg.length() <= 0) {

                    Long selectedTimestamp = getDateInLong(etDate.getText().toString());
                    nextLatestRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestRecordAfter(machine_id, selectedTimestamp);

                    if (nextLatestRecord != null) {
                        if (Integer.parseInt(et_emeter_read.getText().toString()) <= Integer.parseInt(
                                nextLatestRecord.getStart_meter_reading())) {
                            addStopMeterRecord();
                        } else {
                            Toast.makeText(this, "Stop meter reading should be less than " +
                                    "next available record's start reading.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // This section means previous entry for this machine is not available.
                        addStopMeterRecord();
                    }
                } else {
                   // Util.showToast(msg, OperatorActivity.this);
                    Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_date:
                Date serverCurrentDate = Util.getDateFromTimestamp2(serverCurrentTimeStamp, FORM_DATE);
                String serverCurrentDateString = new SimpleDateFormat(FORM_DATE).format(serverCurrentDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(serverCurrentDate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date allowedPastDate = calendar.getTime();
                String allowedPastDateString = new SimpleDateFormat(FORM_DATE).format(allowedPastDate);
                showDateDialogEnableBetweenMinMax(this, etDate, allowedPastDateString, serverCurrentDateString);
                break;
            case R.id.img_start_meter:
                imageType = "Start";
                clickedImageView = img_start_meter;
                onAddImageClick();
                break;
            case R.id.img_end_meter:
                imageType = "Stop";
                clickedImageView = img_end_meter;
                onAddImageClick();
                break;
         /*   case R.id.toolbar_edit_action:
                PopupMenu popup = new PopupMenu(OperatorActivity.this, toolbar_edit_action);
                popup.getMenuInflater().inflate(R.menu.opretor_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equalsIgnoreCase("Halt Reason")) {
                            if (machine_status.equals(STATUS_WORKING)) {
                                Util.showToast("Machine is in Working state.", OperatorActivity.this);
                            } else {
                                strReasonId = "";
                                String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                                Gson gson = new Gson();
                                OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
                                ListHaltReasons.clear();
                                if (operatorMachineData!=null&& operatorMachineData.getNonutilisationTypeData()!=null) {
                                    for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
                                        ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                                    }
                                    showMultiSelectBottomsheet("Halt Reason", "halt", ListHaltReasons);
                                }else { //this is non-FA case

                                }
                            }
                        } else {
                            // following should happen only in FA case
                            Intent i = new Intent(OperatorActivity.this, SiltTransportationRecordActivity.class);
                            i.putExtra("machineId", machine_id);
                            i.putExtra("structureId", structure_id);
                            startActivity(i);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opretor_popup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.halt) {
            if (machine_status.equals(STATUS_WORKING)) {
               // Util.showToast("Machine is in Working state.", OperatorActivity.this);
                Snackbar.make(toolbar, "Machine is in Working state.", Snackbar.LENGTH_SHORT).show();
            } else {
                strReasonId = "";
                String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                Gson gson = new Gson();
                OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
                ListHaltReasons.clear();
                if (operatorMachineData != null && operatorMachineData.getNonutilisationTypeData() != null) {
                    for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
                        ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                    }
                    showMultiSelectBottomsheet("Halt Reason", "halt", ListHaltReasons);
                }
            }
            return true;
        }
        if (id == R.id.dpr) {
            Intent i = new Intent(OperatorActivity.this, SiltTransportationRecordActivity.class);
            i.putExtra("machineId", machine_id);
            i.putExtra("structureId", structure_id);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDateDialogEnableBetweenMinMax(Context context, final EditText editText,
                                                   String minDate, String maxDate) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        long minDateLong = getDateInLong(minDate);
        long maxDateLong = getDateInLong(maxDate);
        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
            updateUIOnDateSelected();
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMinDate(minDateLong);
        dateDialog.getDatePicker().setMaxDate(maxDateLong);
        dateDialog.show();
    }

    private void addStartMeterRecord() {
        OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id(machine_id);
        operatorRequestResponseModel.setMeterReadingDate(etDate.getText().toString());
        operatorRequestResponseModel.setMeterReadingTimestamp(getDateInLong(etDate.getText().toString()));
        operatorRequestResponseModel.setStatus(STATUS_WORKING);
        operatorRequestResponseModel.setStatus_code("" + state_start);
        operatorRequestResponseModel.setStart_meter_reading(et_smeter_read.getText().toString());
        operatorRequestResponseModel.setStartImage(startUri.getPath());
        operatorRequestResponseModel.setStructureId(structure_id);
        //set location
        if (location != null) {
            operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
            operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
        } else {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
                if (location != null) {
                    operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
                    operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
                }
            } else {
                Toast.makeText(this, "Not able to get location.", Toast.LENGTH_SHORT).show();
            }
        }
        long rowId = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                insert(operatorRequestResponseModel);
        // on successful data entry, it returns rowId. After this entry, we disable respective UI.
        if (rowId != -1) {
            Snackbar.make(toolbar, "Record saved locally", Snackbar.LENGTH_SHORT).show();
            setStartMachineData();
            if (Util.isConnected(this)) {
                uploadMachineLog(operatorRequestResponseModel);
            } else {
                //this to show below snackbar after db entry successful info snack shown
                handler.postDelayed(() -> {
                    Snackbar.make(toolbar, "No internet to upload machine record", Snackbar.LENGTH_SHORT).show();
                }, 1000);
            }
        }
        machine_status = STATUS_WORKING;
    }

    private void addStopMeterRecord() {
        String status = STATUS_STOP;
        String status_code = "" + state_stop;
        String stopImage = stopUri.getPath();
        String stopMeterReading = et_emeter_read.getText().toString();
        String meterReadingDate = etDate.getText().toString();
        String latitude = "";
        String longitude = "";

        submittedStopRecord = new OperatorRequestResponseModel();
        submittedStopRecord.setMachine_id(machine_id);
        submittedStopRecord.setMeterReadingDate(meterReadingDate);
        submittedStopRecord.setMeterReadingTimestamp(getDateInLong(etDate.getText().toString()));
        submittedStopRecord.setStatus(status);
        submittedStopRecord.setStatus_code(status_code);
        submittedStopRecord.setStopImage(stopImage);
        submittedStopRecord.setStop_meter_reading(stopMeterReading);
        submittedStopRecord.setStructureId(structure_id);

        //set location
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        } else {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }
            } else {
                Toast.makeText(this, "Not able to get location.", Toast.LENGTH_LONG).show();
            }
        }

        int rowId = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                updateMachineRecord(status, status_code, stopImage, stopMeterReading, latitude,
                        longitude, machine_id, meterReadingDate);

        if (rowId != -1) {
            Snackbar.make(toolbar, "Record saved locally", Snackbar.LENGTH_SHORT).show();
            setStoppedMachineData();
            if (Util.isConnected(this)) {
                uploadMachineLog(submittedStopRecord);
            } else {
                //this to show below snackbar after db entry successful info snack shown
                handler.postDelayed(() -> {
                    Snackbar.make(toolbar, "No internet to upload machine record", Snackbar.LENGTH_SHORT).show();
                }, 1000);
            }
        }
        machine_status = STATUS_STOP;
    }

  /*  private void checkDate() {
        String startDateStr = preferences.getString("startDate", "");
        machine_status = preferences.getString("machineStatus", "");
        if (!(startDateStr.equals(""))) {
            Date current = Calendar.getInstance().getTime();
            Date startDate = null;
            Date currentDate = null;
            try {
                currentDate = df.parse(df.format(current));
                startDate = df.parse(startDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (machine_status.equalsIgnoreCase(STATUS_STOP) |
                    machine_status.equalsIgnoreCase("submit")) {
                if (!(startDate.equals(currentDate))) {
                    editor.putString("machineStatus", "");
                    editor.putString("stopDate", "");
                    editor.putString("startDate", "");
                    editor.putString("stopReading", "");
                    editor.putString("stopUri", "");
                    editor.putString("stopDate", "");
                    editor.apply();
                    machine_status = "";
                    img_start_meter.setImageDrawable(getResources().getDrawable(R.drawable.ic_start_meter_reading));
                    et_smeter_read.setText("");
                    img_end_meter.setImageDrawable(getResources().getDrawable(R.drawable.ic_end_meter_reading));
                    et_emeter_read.setText("");
                } else {
                    editor.putString("machineStatus", "submit");
                    editor.apply();
                    machine_status = "submit";
                }
            }
        }
        setButtons();
    }*/

   /* private void setButtons() {
        if (machine_status.equals("")) {
            img_start_meter.setEnabled(true);
            et_smeter_read.setEnabled(true);
            btnStartService.setEnabled(true);
            img_end_meter.setEnabled(false);
            et_emeter_read.setEnabled(false);
            btnStopService.setEnabled(false);
            Glide.with(OperatorActivity.this)
                    .load(R.drawable.jcb_stopped)   //Uri.parse(list.get(0).getImage()))
                    .into(iv_jcb);
        } else if (machine_status.equals("submit")) {
            Uri imageUri1 = Uri.parse(preferences.getString("startUri", ""));
            img_start_meter.setImageURI(imageUri1);
            et_smeter_read.setText(preferences.getString("startReading", ""));
            Uri imageUri2 = Uri.parse(preferences.getString("stopUri", ""));
            img_end_meter.setImageURI(imageUri2);
            et_emeter_read.setText(preferences.getString("stopReading", ""));
            img_start_meter.setEnabled(false);
            et_smeter_read.setEnabled(false);
            btnStartService.setEnabled(false);
            img_end_meter.setEnabled(false);
            et_emeter_read.setEnabled(false);
            btnStopService.setEnabled(false);
            Glide.with(OperatorActivity.this)
                    .load(R.drawable.jcb_stopped)
                    .into(iv_jcb);
        } else if (machine_status.equalsIgnoreCase(STATUS_WORKING)) {
            Uri imageUri = Uri.parse(preferences.getString("startUri", ""));
            img_start_meter.setImageURI(imageUri);
            et_smeter_read.setText(preferences.getString("startReading", ""));
            img_start_meter.setEnabled(false);
            et_smeter_read.setEnabled(false);
            btnStartService.setEnabled(false);
            img_end_meter.setEnabled(true);
            et_emeter_read.setEnabled(true);
            btnStopService.setEnabled(true);
            Glide.with(OperatorActivity.this)
                    .load(R.drawable.jcb_gif)   //Uri.parse(list.get(0).getImage()))
                    .into(iv_jcb);
        } else if (machine_status.equalsIgnoreCase(STATUS_STOP)) {
            Uri imageUri1 = Uri.parse(preferences.getString("startUri", ""));
            img_start_meter.setImageURI(imageUri1);
            et_smeter_read.setText(preferences.getString("startReading", ""));
            img_start_meter.setEnabled(true);
            et_smeter_read.setEnabled(true);
            btnStartService.setEnabled(true);

            Uri imageUri2 = Uri.parse(preferences.getString("stopUri", ""));
            img_end_meter.setImageURI(imageUri2);
            et_emeter_read.setText(preferences.getString("stopReading", ""));
            img_end_meter.setEnabled(false);
            et_emeter_read.setEnabled(false);
            btnStopService.setEnabled(false);
            Glide.with(OperatorActivity.this)
                    .load(R.drawable.jcb_stopped)
                    .into(iv_jcb);
        }
    }*/

    private void showMultiSelectBottomsheet(String Title, String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(this, selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (gpsTracker.canGetLocation()) {
                    location = gpsTracker.getLocation();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                //Crop.of(finalUri, finalUri).start(this);
                Util.openCropActivityFreeCrop(this, finalUri, finalUri);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
                    Util.openCropActivityFreeCrop(this, outputUri, finalUri);
                    //Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                if (data != null) {
                    final Uri resultUri = UCrop.getOutput(data);
                    final File imageFile = new File(Objects.requireNonNull(resultUri).getPath());
                    Bitmap bitmap = Util.compressImageToBitmap(imageFile);

                    if (Util.isValidImageSize(imageFile)) {
                       /* Glide.with(this)
                                .load(new File(finalUri.getPath()))
                                .placeholder(new ColorDrawable(Color.RED))
                                .into(clickedImageView);*/
                        clickedImageView.setImageURI(resultUri);
                        if (imageType.equals("Start")) {
                            imageHashmap.put("image0", bitmap);
                            startUri = resultUri;
                        } else if (imageType.equals("Stop")) {
                            imageHashmap.put("image0", bitmap); //image0
                            stopUri = resultUri;
                        }
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        } else if (requestCode == 100) {
            if (gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void showPendingApprovalRequests(OperatorMachineCodeDataModel operatorMachineData) {
        Gson gson = new Gson();
        editor.putString("operatorMachineData", gson.toJson(operatorMachineData));
        serverCurrentTimeStamp = operatorMachineData.getCurrentTimeStamp();
        if (isOperator) {
            machine_id = operatorMachineData.getMachine_id();
            structure_id = operatorMachineData.getStructure_id();
            machine_code = operatorMachineData.getMachine_code();

            if (machine_code != null && !(machine_code.isEmpty())) {
                tv_machine_code.setText(machine_code);
            } else {
                tv_machine_code.setText("Not Assigned");
                Util.showToast("Machine not assigned. Contact to DPM.", this);
            }

            // GET Working machine record
            lastWorkingRecordData = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                    getLastWorkingRecord(machine_id);

            if (lastWorkingRecordData != null) {
                setWorkingMachineData(lastWorkingRecordData);
            } else { //new date entry
                updateUIForNewEntry();
            }
        }

        for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
            ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
        }

        editor.apply();
    }

    private void setWorkingMachineData(OperatorRequestResponseModel lastWorkingRecordData) {
        etDate.setText(lastWorkingRecordData.getMeterReadingDate());
        et_smeter_read.setText(lastWorkingRecordData.getStart_meter_reading());
        Bitmap bitmap = BitmapFactory.decodeFile(lastWorkingRecordData.getStartImage());
        img_start_meter.setImageBitmap(bitmap);
        etDate.setEnabled(false);
        Glide.with(OperatorActivity.this)
                .load(R.drawable.jcb_gif)
                .into(iv_jcb);
        et_smeter_read.setEnabled(false);
        img_start_meter.setEnabled(false);
        btnStartService.setEnabled(false);
    }

    private void setStartMachineData() {
        etDate.setEnabled(false);
        et_smeter_read.setEnabled(false);
        img_start_meter.setEnabled(false);
        btnStartService.setEnabled(false);
        et_emeter_read.setEnabled(true);
        img_end_meter.setEnabled(true);
        Glide.with(OperatorActivity.this)
                .load(R.drawable.jcb_gif)
                .into(iv_jcb);
        btnStopService.setEnabled(true);
    }

    private void setStoppedMachineData() {
        etDate.setEnabled(true);
        etDate.setText(null);
        Glide.with(OperatorActivity.this)
                .load(R.drawable.jcb_stopped)
                .into(iv_jcb);
        et_smeter_read.setEnabled(false);
        et_emeter_read.setEnabled(false);
        et_smeter_read.setText(null);
        et_emeter_read.setText(null);
        img_start_meter.setEnabled(false);
        img_end_meter.setEnabled(false);
        img_start_meter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_start_meter_reading, null));
        img_end_meter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_end_meter_reading, null));
        btnStartService.setEnabled(false);
        btnStopService.setEnabled(false);
    }

    private void updateUIForNewEntry() {
        etDate.setEnabled(true);
        etDate.setText(null);
        Glide.with(OperatorActivity.this)
                .load(R.drawable.jcb_stopped)
                .into(iv_jcb);
        et_smeter_read.setEnabled(false);
        et_emeter_read.setEnabled(false);
        et_smeter_read.setText(null);
        et_emeter_read.setText(null);

        img_start_meter.setEnabled(false);
        img_end_meter.setEnabled(false);
        img_start_meter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_start_meter_reading, null));
        img_end_meter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_end_meter_reading, null));

        btnStartService.setEnabled(false);
        btnStopService.setEnabled(false);
    }

    private void updateUIOnDateSelected() {
        etDate.setEnabled(true);
        Glide.with(OperatorActivity.this)
                .load(R.drawable.jcb_stopped)
                .into(iv_jcb);
        et_smeter_read.setEnabled(true);
        et_emeter_read.setEnabled(false);
        et_smeter_read.setText(null);
        img_start_meter.setEnabled(true);
        btnStartService.setEnabled(true);
        btnStopService.setEnabled(false);
    }


    public void removeMachineid() {
        editor.putBoolean("isMachineRemoved", true);
        editor.apply();
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};
        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else
                uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private File getImageFile() {
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


    private void uploadMachineLog(OperatorRequestResponseModel data) {
        final String upload_URL = BuildConfig.BASE_URL + Urls.OperatorApi.MACHINE_WORKLOG;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (commonResponse.getStatus() == 200) {
                                //Util.showToast(commonResponse.getMessage(), OperatorActivity.this);
                                Snackbar.make(toolbar, commonResponse.getMessage(), Snackbar.LENGTH_SHORT).show();

                                //todo check for next day entry in db if exist then delete this entry from db
                              /*  if (machine_status.equalsIgnoreCase(STATUS_STOP)) {
                                    Long submittedRecordTimestamp = submittedStopRecord.getMeterReadingTimestamp();
                                    Long nextDayTimeStamp = Util.getNextDayTimestamp(submittedRecordTimestamp);
                                    Long previousDayTimeStamp = Util.getPreviousDayTimestamp(submittedRecordTimestamp);

                                    boolean rowIdNext = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                            getRecordForGivenTimestamp(machine_id, nextDayTimeStamp);

                                    boolean rowIdPrevious = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                            getRecordForGivenTimestamp(machine_id, previousDayTimeStamp);

                                    if (rowIdNext) {
                                        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                                deleteMachineRecord(submittedStopRecord);
                                    }
                                    if (rowIdPrevious) {
                                        DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                                deleteSpecificMachineRecord(machine_id, previousDayTimeStamp);
                                    }
                                }*/
                            } else {
                                Util.showToast(commonResponse.getMessage(), OperatorActivity.this);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(OperatorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OperatorActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(data));
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));
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
                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0; i < imageHashmap.size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                12000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(this);
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

    @Override
    public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
        String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
        Gson gson = new Gson();
        OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
        strReasonId = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).get_id();
        OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id(machine_id);
        operatorRequestResponseModel.setMeterReadingDate(new SimpleDateFormat(FORM_DATE).format(new Date()));
        operatorRequestResponseModel.setMeterReadingTimestamp(System.currentTimeMillis());
        operatorRequestResponseModel.setStatus_code("" + state_halt);
        operatorRequestResponseModel.setStatus("halt");
        operatorRequestResponseModel.setReasonId(strReasonId);
        //no image in halt case to clear imageHashmap
        imageHashmap.clear();
        startUri = null;
        stopUri = null;
        if (Util.isConnected(this)) {
            uploadMachineLog(operatorRequestResponseModel);
        } else {
            Snackbar.make(toolbar, "No internet connection", Snackbar.LENGTH_SHORT).show();
        }

    }
}