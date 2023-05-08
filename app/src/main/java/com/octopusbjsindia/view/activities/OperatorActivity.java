package com.octopusbjsindia.view.activities;

import static com.octopusbjsindia.utility.Constants.CAMERA_REQUEST;
import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;
import static com.octopusbjsindia.utility.Constants.FORM_DATE;
import static com.octopusbjsindia.utility.Util.getDateInLong;
import static com.octopusbjsindia.utility.Util.getFileDataFromDrawable;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;
import static com.octopusbjsindia.utility.Util.showDateDialogEnableAfterMin;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel;
import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.OperatorActivityPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.widgets.SingleSelectBottomSheet;
import com.sagar.selectiverecycleviewinbottonsheetdialog.CustomBottomSheetDialogFragment;
import com.sagar.selectiverecycleviewinbottonsheetdialog.interfaces.CustomBottomSheetDialogInterface;
import com.sagar.selectiverecycleviewinbottonsheetdialog.model.SelectionListObject;
import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class OperatorActivity extends AppCompatActivity implements APIDataListener,
        /* SingleSelectBottomSheet.MultiSpinnerListener*/CustomBottomSheetDialogInterface, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    public static final int RC_CAMERA_AND_LOCATION = 4;
    private final String TAG = "OperatorActivity";
    private ImageView img_start_meter, img_end_meter, clickedImageView;
    private TextView tv_machine_code, tvVersionCode, tvDeviceName;
    private ImageView iv_jcb;
    private EditText et_emeter_read, et_smeter_read, etDate;
    private Button btnStartService, btnStopService;
    private MaterialButton btnEnterTodayRecord, btnSkipToNextDay;
    private MaterialCardView meterReadingCard;
    private LinearLayout lytActionOnHalt;
    private String machine_id = "", machine_code = "", machine_status = "", structure_id = "";
    private ArrayList<SelectionListObject> ListHaltReasons = new ArrayList<>();
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
    private String strReason = "";
    private GPSTracker gpsTracker;
    private Location location;
    private MaterialToolbar toolbar;
    private long serverCurrentTimeStamp;
    private int allowedPastDaysForRecord;
    private boolean isImagesMandatory = true;
    private static final String STATUS_WORKING = "Working";
    private static final String STATUS_STOP = "Stop";
    private static final String STATUS_HALT = "halt";
    private OperatorRequestResponseModel lastWorkingRecordData;
    private OperatorRequestResponseModel previousLatestRecord;
    //private OperatorRequestResponseModel nextLatestRecord;
    private OperatorRequestResponseModel submittedStopRecord;
    private OperatorMachineCodeDataModel sharedPrefOperatorMachineData;
    private OperatorRequestResponseModel previousDBOrServerRecord;
    final Handler handler = new Handler();
    private boolean isOperator = false;
    private boolean isMachineFirstRecord = false; // here if machine is new machine then first time user can select any date between given range instead on entering in sequence from first date

    private ConstraintLayout lastRecordLayout, lastHaltRecord;
    private TextView tv_lastReadingDate, tv_lastStartReading, tv_lastStopReading,
            tv_lastHaltReadingDate, tv_haltReason;

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
        lastRecordLayout = findViewById(R.id.lyt_past_record);
        lastHaltRecord = findViewById(R.id.lyt_halt_record);
        tv_lastReadingDate = findViewById(R.id.txt_last_reading_date);
        tv_lastHaltReadingDate = findViewById(R.id.txt_last_halt_reading_date);
        tv_lastStartReading = findViewById(R.id.txt_last_start_reading);
        tv_lastStopReading = findViewById(R.id.txt_last_stop_reading);
        tv_haltReason = findViewById(R.id.txt_halt_reason);
        lytActionOnHalt = findViewById(R.id.lyt_action_on_halt);
        btnEnterTodayRecord = findViewById(R.id.btn_enter_record);
        btnSkipToNextDay = findViewById(R.id.btn_skip_to_next);
        meterReadingCard = findViewById(R.id.lyt_reading_card);


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


        /**For Non-FA role*/
        if (machine_id != null && structure_id != null && machine_code != null) {
            isOperator = false;
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24);

            lastWorkingRecordData = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                    getLastWorkingRecord(machine_id);
            tv_machine_code.setText(machine_code);
            if (lastWorkingRecordData != null) {
                setWorkingMachineData(lastWorkingRecordData);
                machine_status = STATUS_WORKING;
            } else { //new date entry
                updateUIForNewEntry();
            }

            /**to get last record from db for reference**/
            previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                    getPreviousLatestRecord(machine_id);
            if (previousDBOrServerRecord != null) {
                if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_STOP)) {
                    setLastRecordView(previousDBOrServerRecord);
                } else if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_HALT)) {
                    //following data is actually used for calculating next date for record entry
                    OnMachineHalt(previousDBOrServerRecord);

                    //following record used only for reference for user to see what his last stop entry was
                    OperatorRequestResponseModel previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestStopRecord(machine_id);
                    setLastRecordView(previousDBOrServerRecord);
                }
            } else { //here machine is new with no last records
                isMachineFirstRecord = true;
            }

            //api call to get list of halt reason & last meter record
            if (Util.isConnected(this)) {
                presenter.getAllFiltersRequests(machine_code);
            } else {
                String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                Gson gson = new Gson();
                sharedPrefOperatorMachineData = gson.fromJson(operatorMachineDataStr,
                        OperatorMachineCodeDataModel.class);
                if (sharedPrefOperatorMachineData != null) {
                    //serverCurrentTimeStamp = sharedPrefOperatorMachineData.getCurrentTimeStamp();
                    allowedPastDaysForRecord = sharedPrefOperatorMachineData.getAllowedPastDaysForRecord();
                    isImagesMandatory = sharedPrefOperatorMachineData.isImagesMandatory();
                    if (previousDBOrServerRecord == null &&
                            sharedPrefOperatorMachineData.getMachineLastRecord() != null) {
                        previousDBOrServerRecord = sharedPrefOperatorMachineData.getMachineLastRecord();
                    }
                }
                Snackbar.make(toolbar, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }

        } else {   /** For FA/Operator roles */
            toolbar.setNavigationIcon(null);
            isOperator = true;

            if (Util.isConnected(this)) {
                presenter.getAllFiltersRequests("no_machine");
                SyncAdapterUtils.manualRefresh();
            } else {
                String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                Gson gson = new Gson();
                sharedPrefOperatorMachineData = gson.fromJson(operatorMachineDataStr,
                        OperatorMachineCodeDataModel.class);
                if (sharedPrefOperatorMachineData != null && !sharedPrefOperatorMachineData.getMachine_id().isEmpty()) {
                    machine_id = sharedPrefOperatorMachineData.getMachine_id();
                    machine_code = sharedPrefOperatorMachineData.getMachine_code();
                    structure_id = sharedPrefOperatorMachineData.getStructure_id();
                    tv_machine_code.setText(machine_code);
                    isImagesMandatory = sharedPrefOperatorMachineData.isImagesMandatory();
                    //serverCurrentTimeStamp = sharedPrefOperatorMachineData.getCurrentTimeStamp();
                    allowedPastDaysForRecord = sharedPrefOperatorMachineData.getAllowedPastDaysForRecord();

                    lastWorkingRecordData = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getLastWorkingRecord(machine_id);
                    if (lastWorkingRecordData != null) {
                        setWorkingMachineData(lastWorkingRecordData);
                        machine_status = STATUS_WORKING;
                    } else { //new date entry
                        updateUIForNewEntry();
                    }

                    /**to get last record from db for reference*/
                    previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestRecord(machine_id);

                    if (previousDBOrServerRecord != null) {
                        if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_STOP)) {
                            setLastRecordView(previousDBOrServerRecord);
                        } else if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_HALT)) {
                            //following data is actually used for calculating next date for record entry
                            OnMachineHalt(previousDBOrServerRecord);

                            //following record used only for reference for user to see what his last stop entry was
                            OperatorRequestResponseModel previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                    getPreviousLatestStopRecord(machine_id);
                            setLastRecordView(previousDBOrServerRecord);
                        }
                    } else if (previousDBOrServerRecord == null && sharedPrefOperatorMachineData.getMachineLastRecord() != null) {
                        previousDBOrServerRecord = sharedPrefOperatorMachineData.getMachineLastRecord();
                    } else { //here machine is new with no last records
                        isMachineFirstRecord = true;
                    }

                }
                Snackbar.make(toolbar, "No internet connection", Snackbar.LENGTH_SHORT).show();
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
                if (isImagesMandatory && startUri == null)
                    msg = "Please select Start meter reading photo";
                else if (et_smeter_read.getText().toString().length() <= 0)
                    msg = "Please enter start meter reading";
                else if (machine_code.isEmpty())
                    msg = "Machine not assigned. Contact to DPM.";
                else if (etDate.getText().toString().isEmpty()) {
                    msg = "Please select meter reading date.";
                }
                if (msg.length() == 0) {
                    // Get previous machine reading entry for validation
                    Long selectedTimestamp = getDateInLong(etDate.getText().toString());
                    //todo need validation when previous latest record from db is of the days after current selected date
                    previousLatestRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestRecord(machine_id, selectedTimestamp);

                    if (previousLatestRecord != null) { //this is previous latest record
                        if (Float.parseFloat(et_smeter_read.getText().toString()) >= Float.parseFloat(
                                previousLatestRecord.getStop_meter_reading())) {
                            addStartMeterRecord();
                        } else {
                            Toast.makeText(this, "Start meter reading should be greater than " +
                                    "previous record's stop reading", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // This section means previous entry for this machine is not available.
                        addStartMeterRecord();
                    }
                } else {
                    // Util.showToast(msg, OperatorActivity.this);
                    Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonStopService:
                if (isImagesMandatory && stopUri == null) {
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
                if (msg.length() == 0) {
                    addStopMeterRecord();
                } else {
                    // Util.showToast(msg, OperatorActivity.this);
                    Snackbar.make(toolbar, msg, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_date:
                long currentDateTimeStamp = System.currentTimeMillis();
                if (serverCurrentTimeStamp != 0) {
                    Date serverCurrentDate = Util.getDateFromTimestamp2(serverCurrentTimeStamp, FORM_DATE);
                    String serverCurrentDateString = new SimpleDateFormat(FORM_DATE).format(serverCurrentDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(serverCurrentDate);
                    calendar.add(Calendar.DAY_OF_YEAR, -allowedPastDaysForRecord);
                    Date allowedPastDate = calendar.getTime();
                    long longAllowedPastDate = allowedPastDate.getTime();

                    String allowedPastDateString = new SimpleDateFormat(FORM_DATE).format(allowedPastDate);
                    if (previousDBOrServerRecord != null) {
                        long nextDayTimestamp = Util.getNextDayTimestamp(previousDBOrServerRecord.getMeterReadingTimestamp());
                        Date nextDate = Util.getDateFromTimestamp2(nextDayTimestamp, FORM_DATE);

                        if (longAllowedPastDate > previousDBOrServerRecord.getMeterReadingTimestamp()) {
                            showDateDialogForDate(this, etDate, allowedPastDate);
                        } else {
                            if (nextDayTimestamp > currentDateTimeStamp) {
                                Snackbar.make(view, "You have already submitted all machine's records till today.", Snackbar.LENGTH_SHORT).show();
                            } else {
                                showDateDialogForDate(this, etDate, nextDate);
                            }
                        }
                    } else {
                        if (isMachineFirstRecord)
                            showDateDialogEnableBetweenMinMax(this, etDate, allowedPastDateString, serverCurrentDateString);
                        else showDateDialogForDate(this, etDate, allowedPastDate);
                    }
                } else {
                    if (sharedPrefOperatorMachineData != null) {
                        if (sharedPrefOperatorMachineData.getCurrentTimeStamp() > System.currentTimeMillis()) {
                            Snackbar.make(toolbar, "Your system date seems to be wrong. Please update it or contact to admin.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Date serverCurrentDate = Util.getDateFromTimestamp2(serverCurrentTimeStamp, FORM_DATE);
                            String serverCurrentDateString = new SimpleDateFormat(FORM_DATE).format(serverCurrentDate);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(serverCurrentDate);
                            calendar.add(Calendar.DAY_OF_YEAR, -allowedPastDaysForRecord);
                            Date allowedPastDate = calendar.getTime();

                            SimpleDateFormat formatterWithoutTimezone = new SimpleDateFormat(FORM_DATE, Locale.getDefault());
                            String formattedDate = formatterWithoutTimezone.format(allowedPastDate);
                            long longAllowedPastDate = Util.getDateInLong(formattedDate);

                            String allowedPastDateString = new SimpleDateFormat(FORM_DATE).format(allowedPastDate);
                            if (previousDBOrServerRecord != null) {
                                long nextDayTimestamp = Util.getNextDayTimestamp(previousDBOrServerRecord.getMeterReadingTimestamp());
                                Date nextDate = Util.getDateFromTimestamp2(nextDayTimestamp, FORM_DATE);

                                if (longAllowedPastDate > previousDBOrServerRecord.getMeterReadingTimestamp()) {
                                    showDateDialogForDate(this, etDate, allowedPastDate);
                                } else {
                                    if (nextDayTimestamp > currentDateTimeStamp) {
                                        Snackbar.make(view, "You have already submitted all machine's records till today.", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        showDateDialogForDate(this, etDate, nextDate);
                                    }
                                }
                            } else {
                                if (isMachineFirstRecord)
                                    showDateDialogEnableBetweenMinMax(this, etDate, allowedPastDateString, serverCurrentDateString);
                                else showDateDialogForDate(this, etDate, allowedPastDate);
                            }
                        }
                    } else {
                        Date serverCurrentDate = Util.getDateFromTimestamp2(serverCurrentTimeStamp, FORM_DATE);
                        String serverCurrentDateString = new SimpleDateFormat(FORM_DATE).format(serverCurrentDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(serverCurrentDate);
                        calendar.add(Calendar.DAY_OF_YEAR, -allowedPastDaysForRecord);
                        Date allowedPastDate = calendar.getTime();
                        long longAllowedPastDate = allowedPastDate.getTime();

                         String allowedPastDateString = new SimpleDateFormat(FORM_DATE).format(allowedPastDate);
                        if (previousDBOrServerRecord != null) {
                            long nextDayTimestamp = Util.getNextDayTimestamp(previousDBOrServerRecord.getMeterReadingTimestamp());
                            Date nextDate = Util.getDateFromTimestamp2(nextDayTimestamp, FORM_DATE);

                            if (longAllowedPastDate > previousDBOrServerRecord.getMeterReadingTimestamp()) {
                                showDateDialogForDate(this, etDate, allowedPastDate);
                            } else {
                                if (nextDayTimestamp > currentDateTimeStamp) {
                                    Snackbar.make(view, "You have already submitted all machine's records till today.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    showDateDialogForDate(this, etDate, nextDate);
                                }
                            }
                        } else {
                            if (isMachineFirstRecord)
                                showDateDialogEnableBetweenMinMax(this, etDate, allowedPastDateString, serverCurrentDateString);
                            else showDateDialogForDate(this, etDate, allowedPastDate);
                        }
                    }
                }
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
            if (etDate.getText().toString().isEmpty()) {
                Snackbar.make(toolbar, "Please select machine reading date before adding halt reason", Snackbar.LENGTH_SHORT).show();
            } else {
                if (machine_status.equals(STATUS_WORKING)) {
                    Snackbar.make(toolbar, "Machine is in Working state.", Snackbar.LENGTH_SHORT).show();
                } else {
                    strReasonId = "";
                    strReason = "";
                    String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                    Gson gson = new Gson();
                    OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
                    ListHaltReasons.clear();
                    if (operatorMachineData != null && operatorMachineData.getNonutilisationTypeData() != null) {
                        for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
                            //ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                            SelectionListObject reason = new SelectionListObject(
                                    operatorMachineData.getNonutilisationTypeData().getEn().get(i).get_id(),
                                    operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue(),
                                    false, false
                            );
                            ListHaltReasons.add(reason);
                        }
                        CustomBottomSheetDialogFragment customBottomsheet = null;
                        showSelectiveBottomSheet(customBottomsheet, "Halt Reason",
                                ListHaltReasons, false);
                        // showMultiSelectBottomsheet("Halt Reason", STATUS_HALT, ListHaltReasons);
                    }
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

    // we have updated UI on date selection
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
            updateUIOnDateSelected(true);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMinDate(minDateLong);
        dateDialog.getDatePicker().setMaxDate(maxDateLong);
        dateDialog.show();
    }

    // we have updated UI on date selection
    private void showDateDialogForDate(Context context, final EditText editText, Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        long dateLong = date.getTime();

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date1 = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date1);
            updateUIOnDateSelected(true);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(dateLong);
        dateDialog.getDatePicker().setMinDate(dateLong);
        dateDialog.setTitle(context.getString(R.string.select_date_title));
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
        if (startUri != null) {
            operatorRequestResponseModel.setStartImage(startUri.getPath());
        }
        operatorRequestResponseModel.setStructureId(structure_id);
        operatorRequestResponseModel.setSynced(false);
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
            machine_status = STATUS_WORKING;
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
        } else {
            Snackbar.make(toolbar, "Something went wrong", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void addStopMeterRecord() {
        submittedStopRecord = new OperatorRequestResponseModel();
        String status = STATUS_STOP;
        String status_code = "" + state_stop;
        String stopImage = null;
        if (stopUri != null) {
            stopImage = stopUri.getPath();
            submittedStopRecord.setStopImage(stopImage);
        }

        String stopMeterReading = et_emeter_read.getText().toString();
        String meterReadingDate = etDate.getText().toString();
        String latitude = "";
        String longitude = "";

        submittedStopRecord.setMachine_id(machine_id);
        submittedStopRecord.setMeterReadingDate(meterReadingDate);
        submittedStopRecord.setMeterReadingTimestamp(getDateInLong(etDate.getText().toString()));
        submittedStopRecord.setStatus(status);
        submittedStopRecord.setStatus_code(status_code);
        submittedStopRecord.setStop_meter_reading(stopMeterReading);
        submittedStopRecord.setStart_meter_reading(et_smeter_read.getText().toString());
        submittedStopRecord.setStructureId(structure_id);
        submittedStopRecord.setSynced(false);

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
        submittedStopRecord.setLat(latitude);
        submittedStopRecord.setLong(longitude);

        int rowId = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                updateMachineRecord(status, status_code, stopImage, stopMeterReading, latitude,
                        longitude, machine_id, meterReadingDate, false);

        if (rowId != -1) {
            machine_status = STATUS_STOP;
            isMachineFirstRecord = false;
            Snackbar.make(toolbar, "Record saved locally", Snackbar.LENGTH_SHORT).show();
            setStoppedMachineData();

            //update serverDbrecord
//            previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance())
//                    .getOperatorRequestResponseModelDao().getPreviousLatestRecord(data.getMachine_id());
            previousDBOrServerRecord = submittedStopRecord;
            setLastRecordView(previousDBOrServerRecord);

            lastHaltRecord.setVisibility(View.GONE);
            if (Util.isConnected(this)) {
                uploadMachineLog(submittedStopRecord);
            } else {
                //this to show below snackbar after db entry successful info snack shown
                handler.postDelayed(() -> {
                    Snackbar.make(toolbar, "No internet to upload machine record", Snackbar.LENGTH_SHORT).show();
                }, 1000);
            }
        } else {
            Snackbar.make(toolbar, "Something went wrong", Snackbar.LENGTH_SHORT).show();
        }

    }

   /* private void showMultiSelectBottomsheet(String Title, String selectedOption, ArrayList<String> List) {

        bottomSheetDialogFragment = new SingleSelectBottomSheet(this, selectedOption, List, this::onValuesSelected);
        bottomSheetDialogFragment.show();
        bottomSheetDialogFragment.toolbarTitle.setText(Title);
        bottomSheetDialogFragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }*/

    private void showSelectiveBottomSheet(CustomBottomSheetDialogFragment bottomSheet, String Title, ArrayList<SelectionListObject> List, Boolean isMultiSelect) {
        bottomSheet = new CustomBottomSheetDialogFragment(this, Title, List, isMultiSelect);
        bottomSheet.show(this.getSupportFragmentManager(), CustomBottomSheetDialogFragment.TAG);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

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
        }/* else if (requestCode == CAMERA_REQUEST) {

            Permissions.showPermissionAgainElseDialog(this,grantResults,
                    "App needs permission to upload meter records.",
                    "You have denied some permissions. In order to upload meter record app need this permission. Now you can turn on this permission manually from apps permission settings. Click to go to the settings page.");

        }*/
    }

/*
    private void setNextDayDate(OperatorRequestResponseModel data){
        long nextDayTimestamp = Util.getNextDayTimestamp(data.getMeterReadingTimestamp());
        if (allowedPastDaysForRecord)
        etDate.setText(Util.getDateFromTimestamp(nextDayTimestamp,FORM_DATE));

    }
*/

    private void setLastRecordView(OperatorRequestResponseModel model) {
        if (model != null) {
            lastRecordLayout.setVisibility(View.VISIBLE);
            if (model.getMeterReadingDate() != null) {
                Util.setTextElseDash(tv_lastReadingDate, model.getMeterReadingDate());
            }
            if (model.getStart_meter_reading() != null) {
                Util.setTextElseDash(tv_lastStartReading, model.getStart_meter_reading());
            }
            if (model.getStop_meter_reading() != null) {
                Util.setTextElseDash(tv_lastStopReading, model.getStop_meter_reading());
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
                            imageHashmap.put("start", bitmap);
                            startUri = resultUri;
                        } else if (imageType.equals("Stop")) {
                            imageHashmap.put("stop", bitmap); //image0
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
        allowedPastDaysForRecord = operatorMachineData.getAllowedPastDaysForRecord();
        isImagesMandatory = operatorMachineData.isImagesMandatory();

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
                machine_status = STATUS_WORKING;
            } else { //new date entry
                updateUIForNewEntry();
            }

            /**to get last record from db for reference*/
            //here we get "machine_id" which is needed to get last record from db
            previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                    getPreviousLatestRecord(machine_id);

            if (previousDBOrServerRecord != null) {
                if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_STOP)) {
                    setLastRecordView(previousDBOrServerRecord);
                } else if (previousDBOrServerRecord.getStatus().equalsIgnoreCase(STATUS_HALT)) {
                    //following data is actually used for calculating next date for record entry
                    OnMachineHalt(previousDBOrServerRecord);

                    //following record used only for reference for user to see what his last stop entry was
                    OperatorRequestResponseModel previousDBOrServerRecord = DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            getPreviousLatestStopRecord(machine_id);
                    setLastRecordView(previousDBOrServerRecord);
                }
            } else {
                if (operatorMachineData.getMachineLastRecord() != null) {
                    previousDBOrServerRecord = operatorMachineData.getMachineLastRecord();
                    setLastRecordView(previousDBOrServerRecord);

                    //following entries needed for adding record to db
                    previousDBOrServerRecord.setMachine_id(operatorMachineData.getMachine_id());
                    previousDBOrServerRecord.setStructureId(operatorMachineData.getStructure_id());

                    //add record to db for offline case
                    previousDBOrServerRecord.setSynced(true);
                    DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            insert(previousDBOrServerRecord);
                } else { //here machine is new with no last records
                    isMachineFirstRecord = true;
                }
            }

        } else {
            //for non FA case if last record doesn't exists in db
            if (previousDBOrServerRecord == null) {
                if (operatorMachineData.getMachineLastRecord() != null) {
                    previousDBOrServerRecord = operatorMachineData.getMachineLastRecord();
                    setLastRecordView(previousDBOrServerRecord);

                    //following entries needed for adding record to db
                    previousDBOrServerRecord.setMachine_id(machine_id);
                    previousDBOrServerRecord.setStructureId(structure_id);

                    //add record to db for offline case
                    previousDBOrServerRecord.setSynced(true);
                    DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                            insert(previousDBOrServerRecord);
                } else { //here machine is new with no last records
                    isMachineFirstRecord = true;
                }
            }
        }

        for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
            SelectionListObject reason = new SelectionListObject(
                    operatorMachineData.getNonutilisationTypeData().getEn().get(i).get_id(),
                    operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue(),
                    false, false
            );
            ListHaltReasons.add(reason);
        }

        editor.apply();
    }

    private void setWorkingMachineData(OperatorRequestResponseModel lastWorkingRecordData) {
        etDate.setText(lastWorkingRecordData.getMeterReadingDate());
        et_smeter_read.setText(lastWorkingRecordData.getStart_meter_reading());
        if (lastWorkingRecordData.getStartImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(lastWorkingRecordData.getStartImage());
            img_start_meter.setImageBitmap(bitmap);
        }
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

    private void updateUIOnDateSelected(boolean isDateEnabled) {
        etDate.setEnabled(isDateEnabled);
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

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void onAddImageClick() {
        /*if (Permissions.checkAndRequestStorageCameraPermissions(this, this)) {
            showPictureDialog();
        }*/

        if (EasyPermissions.hasPermissions(this, Permissions.storagePermissionsList())) {
            showPictureDialog();
        } else {
            EasyPermissions.requestPermissions(this,
                    "App needs permission to upload meter records.",
                    RC_CAMERA_AND_LOCATION,
                    Permissions.storagePermissionsList());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // showPictureDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
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
                response -> {
                    rQueue.getCache().clear();
                    try {
                        String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                        if (commonResponse.getStatus() == 200) {
                            //Util.showToast(commonResponse.getMessage(), OperatorActivity.this);
                            Snackbar.make(toolbar, commonResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                            imageHashmap.clear();
                            startUri = null;
                            stopUri = null;

                            //to update db entry to sync
                            data.setSynced(true);
                            if (data.getStatus().equalsIgnoreCase(STATUS_WORKING) || data.getStatus().equalsIgnoreCase(STATUS_HALT)) {
                                DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().insert(data);
                            } else if (data.getStatus().equalsIgnoreCase(STATUS_STOP)) {
                                DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                        updateMachineRecord(data.getStatus(), data.getStatus_code(), data.getStopImage(), data.getStop_meter_reading(), data.getLat(),
                                                data.getLong(), machine_id, data.getMeterReadingDate(), true);
                            }

                            //to delete unwanted db records
                            if (data.getStatus().equalsIgnoreCase(STATUS_STOP)) {
                                //to delete all previous record other than this latest entry
                                long submittedRecordTimestamp = data.getMeterReadingTimestamp();
                                DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                        deletePreviousMachineRecord(data.getMachine_id(), submittedRecordTimestamp);
                            }

                            if (data.getStatus().equalsIgnoreCase(STATUS_HALT)) {
                                long submittedRecordTimestamp = data.getMeterReadingTimestamp();
                                DatabaseManager.getDBInstance(Platform.getInstance()).getOperatorRequestResponseModelDao().
                                        deletePreviousHaltMachineRecord(data.getMachine_id(), submittedRecordTimestamp);
                            }

                        } else {
                            Util.showToast(commonResponse.getMessage(), OperatorActivity.this);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(OperatorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                // params.put("imageArraySize", String.valueOf(imageHashmap.size()));

                if (data.getStatus().equalsIgnoreCase("Working")) {
                    if (data.getStartImage() != null) {
                        params.put("imageArraySize", "1");
                    } else {
                        params.put("imageArraySize", "0");
                    }
                } else if (data.getStatus().equalsIgnoreCase("Stop")) {
                    if (data.getStopImage() != null) {
                        params.put("imageArraySize", "1");
                    } else {
                        params.put("imageArraySize", "0");
                    }
                }
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
              /*  Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0; i < imageHashmap.size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }*/

                if (data.getStatus().equalsIgnoreCase("Working")) {
                    if (data.getStartImage() != null) {
                        drawable = new BitmapDrawable(getResources(), imageHashmap.get("start"));
                        params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                                "image/jpeg"));
                    }
                } else if (data.getStatus().equalsIgnoreCase("Stop")) {
                    if (data.getStopImage() != null) {
                        drawable = new BitmapDrawable(getResources(), imageHashmap.get("stop"));
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

    private void OnMachineHalt(OperatorRequestResponseModel data) {
        etDate.setEnabled(false);
        etDate.setText(data.getMeterReadingDate());
        lastHaltRecord.setVisibility(View.VISIBLE);
        tv_lastHaltReadingDate.setText(data.getMeterReadingDate());
        tv_haltReason.setText(data.getHaltReason());

        meterReadingCard.setVisibility(View.GONE);
        lytActionOnHalt.setVisibility(View.VISIBLE);

        btnEnterTodayRecord.setOnClickListener(v -> {
            meterReadingCard.setVisibility(View.VISIBLE);
            lytActionOnHalt.setVisibility(View.GONE);
            updateUIOnDateSelected(false);
        });

        btnSkipToNextDay.setOnClickListener(v -> {
            meterReadingCard.setVisibility(View.VISIBLE);
            lytActionOnHalt.setVisibility(View.GONE);
            etDate.setEnabled(true);
            etDate.setText(null);
            previousDBOrServerRecord = data;

        });
    }

    private void addHaltRecord() {
        OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id(machine_id);
        operatorRequestResponseModel.setMeterReadingDate(etDate.getText().toString());
        operatorRequestResponseModel.setMeterReadingTimestamp(getDateInLong(etDate.getText().toString()));
        operatorRequestResponseModel.setStatus_code("" + state_halt);
        operatorRequestResponseModel.setStatus(STATUS_HALT);
        operatorRequestResponseModel.setReasonId(strReasonId);
        operatorRequestResponseModel.setHaltReason(strReason);
        operatorRequestResponseModel.setStructureId(structure_id);
        operatorRequestResponseModel.setSynced(false);
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

        // on successful data entry, it returns rowId. After this entry, we set halt action UI.
        if (rowId != -1) {
            machine_status = STATUS_HALT;
            isMachineFirstRecord = false;
            Snackbar.make(toolbar, "Halt record saved locally", Snackbar.LENGTH_SHORT).show();

            //no image in halt case to clear imageHashmap
            imageHashmap.clear();
            startUri = null;
            stopUri = null;

            OnMachineHalt(operatorRequestResponseModel);

            if (Util.isConnected(this)) {
                uploadMachineLog(operatorRequestResponseModel);
            } else {
                //this to show below snackbar after db entry successful info snack shown
                handler.postDelayed(() -> {
                    Snackbar.make(toolbar, "No internet to upload machine record", Snackbar.LENGTH_SHORT).show();
                }, 1000);
            }
        } else {
            Snackbar.make(toolbar, "Something went wrong", Snackbar.LENGTH_SHORT).show();
        }

    }


    /* @Override
     public void onValuesSelected(int selectedPosition, String spinnerName, String selectedValues) {
         String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
         Gson gson = new Gson();
         OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
         strReasonId = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).get_id();
         strReason = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).getValue();

         if (strReason.equalsIgnoreCase("others")) {

             final View dialogView = LayoutInflater.from(this)
                     .inflate(R.layout.dialog_input_lyout, null);

             TextInputEditText etReason = dialogView.findViewById(R.id.et_reason);

             new MaterialAlertDialogBuilder(this)
                     .setTitle("Enter other reason")
                     //.setMessage("hello")
                     .setCancelable(false)
                     .setView(dialogView)
                     .setPositiveButton("Submit", (dialog, whichButton) -> {
                         String reason = etReason.getText().toString().trim();
                         if (!TextUtils.isEmpty(reason)){
                             strReason = reason;
                             addHaltRecord();
                         }else {
                             Snackbar.make(toolbar, "Reason cannot be blank", Snackbar.LENGTH_SHORT).show();
                         }
                     })
                     .setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss())
                     .show();
         } else {
             addHaltRecord();
         }
     }
 */
    @Override
    public void onCustomBottomSheetSelection(@NonNull String s) {
        if (s.equalsIgnoreCase("Halt Reason")) {
            String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
            Gson gson = new Gson();
            OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);

            for (SelectionListObject obj : ListHaltReasons) {
                if (obj.isSelected()) {
                    strReasonId = obj.getId();
                    strReason = obj.getValue();
                    break;
                }
            }

            if (strReason != null && !TextUtils.isEmpty(strReason)
                    && !strReason.equalsIgnoreCase("")) {
                if (strReason.equalsIgnoreCase("others")) {

                    final View dialogView = LayoutInflater.from(this)
                            .inflate(R.layout.dialog_input_lyout, null);

                    TextInputEditText etReason = dialogView.findViewById(R.id.et_reason);

                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Enter other reason")
                            .setCancelable(false)
                            .setView(dialogView)
                            .setPositiveButton("Submit", (dialog, whichButton) -> {
                                String reason = etReason.getText().toString().trim();
                                if (!TextUtils.isEmpty(reason)) {
                                    strReason = reason;
                                    addHaltRecord();
                                } else {
                                    Snackbar.make(toolbar, "Reason cannot be blank", Snackbar.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss())
                            .show();
                } else {
                    addHaltRecord();
                }
            }
        }
    }
}