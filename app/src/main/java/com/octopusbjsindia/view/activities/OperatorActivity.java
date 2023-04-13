package com.octopusbjsindia.view.activities;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel;
import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class OperatorActivity extends AppCompatActivity implements APIDataListener,
        SingleSelectBottomSheet.MultiSpinnerListener, View.OnClickListener {

    private final String TAG = "OperatorActivity";
    private ImageView img_start_meter, img_end_meter, clickedImageView;
    private TextView tv_machine_code, tvVersionCode, tvDeviceName;
    private ImageView iv_jcb, toolbar_edit_action;
    private EditText et_emeter_read, et_smeter_read;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        OperatorActivityPresenter presenter = new OperatorActivityPresenter(this);
        machine_code = getIntent().getStringExtra("machineCode");
        if(machine_code!="") {
            presenter.getAllFiltersRequests(machine_code);
        } else {
            presenter.getAllFiltersRequests("");
        }

        preferences = getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        tvVersionCode = findViewById(R.id.tv_version_code);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tv_machine_code = findViewById(R.id.tv_machine_code_new);
        et_emeter_read = findViewById(R.id.et_emeter_read);
        et_smeter_read = findViewById(R.id.et_smeter_read);
        img_start_meter = findViewById(R.id.img_start_meter);
        img_end_meter = findViewById(R.id.img_end_meter);
        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
        iv_jcb = findViewById(R.id.jcb);

        checkDate();
        setDeviceInfo();

//        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_meter);
//        requestOptions = requestOptions.apply(RequestOptions.noTransformation());

        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        img_start_meter.setOnClickListener(this);
        img_end_meter.setOnClickListener(this);
        toolbar_edit_action.setOnClickListener(this);

        //get lat,long of location
        gpsTracker = new GPSTracker(this);
        if(Permissions.isLocationPermissionGranted(this, this)) {
            if(gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    private void setDeviceInfo() {
        try {
            String appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersionCode.setText("Version-" + appVersion);
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
                if (msg.length() <= 0) {
                    OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
                    operatorRequestResponseModel.setMachine_id(machine_id);
                    operatorRequestResponseModel.setStatus_code("" + state_start);
                    operatorRequestResponseModel.setStatus("Working");
                    operatorRequestResponseModel.setMeter_reading(et_smeter_read.getText().toString());
                    operatorRequestResponseModel.setStructureId(structure_id);
                    //set location
                    if (location != null) {
                        operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
                        operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
                    } else {
                        if(gpsTracker.canGetLocation()) {
                            location = gpsTracker.getLocation();
                            Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
                            if (location != null ) {
                                operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
                                operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
                            }
                        } else {
                            Toast.makeText(this, "Not able to get location.", Toast.LENGTH_LONG).show();
                        }
                    }

                    uploadMachineLog(operatorRequestResponseModel);
                    editor.putString("machineStatus", "Working");
                    editor.putString("startReading", et_smeter_read.getText().toString());
                    editor.putString("startUri", startUri.toString());
                    Date current = Calendar.getInstance().getTime();
                    editor.putString("startDate", df.format(current));
                    editor.putString("stopReading", "");
                    editor.putString("stopUri", "");
                    editor.putString("stopDate", "");
                    editor.apply();
                    machine_status = "Working";

                    checkDate();
                } else {
                    Util.showToast(msg, OperatorActivity.this);
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
                }  else if (machine_code.isEmpty()) {
                    msg = "Machine not assigned. Contact to DPM.";
                }
                if (msg.length() <= 0) {
                    OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
                    operatorRequestResponseModel.setMachine_id(machine_id);
                    operatorRequestResponseModel.setStatus_code("" + state_stop);
                    operatorRequestResponseModel.setStatus("stop");
                    operatorRequestResponseModel.setMeter_reading(et_emeter_read.getText().toString());
                    operatorRequestResponseModel.setStructureId(structure_id);

                    //set location
                    if (location != null) {
                        operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
                        operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
                    } else {
                        if(gpsTracker.canGetLocation()) {
                            location = gpsTracker.getLocation();
                            Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
                            if (location != null ) {
                                operatorRequestResponseModel.setLat(String.valueOf(location.getLatitude()));
                                operatorRequestResponseModel.setLong(String.valueOf(location.getLongitude()));
                            }
                        } else {
                            Toast.makeText(this, "Not able to get location.", Toast.LENGTH_LONG).show();
                        }
                    }

                    uploadMachineLog(operatorRequestResponseModel);
                    editor.putString("machineStatus", "stop");
                    editor.putString("stopReading", et_emeter_read.getText().toString());
                    editor.putString("stopUri", stopUri.toString());
                    Date current = Calendar.getInstance().getTime();
                    editor.putString("stopDate", df.format(current));
                    editor.apply();
                    machine_status = "stop";

                    checkDate();
                } else {
                    Util.showToast(msg, OperatorActivity.this);
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
            case R.id.toolbar_edit_action:
                PopupMenu popup = new PopupMenu(OperatorActivity.this, toolbar_edit_action);
                popup.getMenuInflater().inflate(R.menu.opretor_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equalsIgnoreCase("Halt Reason")) {
                            if (machine_status.equals("Working")) {
                                Util.showToast("Machine is in Working state.", OperatorActivity.this);
                            } else {
                                strReasonId = "";
                                String operatorMachineDataStr = preferences.getString("operatorMachineData", "");
                                Gson gson = new Gson();
                                OperatorMachineCodeDataModel operatorMachineData = gson.fromJson(operatorMachineDataStr, OperatorMachineCodeDataModel.class);
                                ListHaltReasons.clear();
                                for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
                                    ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
                                }
                                showMultiSelectBottomsheet("Halt Reason", "halt", ListHaltReasons);
                            }
                        } else {
                            Intent i = new Intent(OperatorActivity.this, SiltTransportationRecordActivity.class);
                            i.putExtra("machineId", machine_id);
                            i.putExtra("structureId", structure_id);
                            startActivity(i);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;
        }
    }

    private void checkDate() {
//        stopDate  startDate
        String startDateStr = preferences.getString("startDate", "");
//        String stopDateStr = preferences.getString("stopDate", "");
        machine_status = preferences.getString("machineStatus", "");
        if (!(startDateStr.equals(""))) {
//            setButtons();
//        } else if (stopDateStr.equals("")) {
//            setButtons();
//        } else {
            Date current = Calendar.getInstance().getTime();
            Date startDate = null;
//            Date stopDate = null;
            Date currentDate = null;
            try {
                currentDate = df.parse(df.format(current));
                startDate = df.parse(startDateStr);
//                stopDate = df.parse(stopDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            if (startDate.getTime() != currentDate.getTime()) {
//                editor.putString("machineStatus", "");
//                editor.putString("stopDate", "");
//                editor.putString("startDate", "");
//                editor.putString("stopReading", "");
//                editor.putString("stopUri", "");
//                editor.putString("stopDate", "");
//                editor.apply();
//                setButtons();
//            } else {
//                editor.putString("machineStatus", "submit");
//                editor.apply();
//                setButtons();
//            }

//            if(machine_status.equals("")){
//                setButtons();
//            } else if(machine_status.equalsIgnoreCase("Working")){
//
//            } else
            if (machine_status.equalsIgnoreCase("stop") |
                    machine_status.equalsIgnoreCase("submit")){
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
    }

    private void setButtons() {
//        machine_status = preferences.getString("machineStatus", "");
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
                    .load(R.drawable.jcb_stopped)   //Uri.parse(list.get(0).getImage()))
                    .into(iv_jcb);
        } else if (machine_status.equalsIgnoreCase("Working")) {
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
        } else if (machine_status.equalsIgnoreCase("stop")) {
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
                    .load(R.drawable.jcb_stopped)   //Uri.parse(list.get(0).getImage()))
                    .into(iv_jcb);
        }
    }

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
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if(gpsTracker.canGetLocation()) {
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
//                outputUri = data.getData();
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                Bitmap bitmap = Util.compressImageToBitmap(imageFile);
                Glide.with(this)
                        .load(new File(finalUri.getPath()))
                        .placeholder(new ColorDrawable(Color.RED))
                        .into(clickedImageView);
                if (Util.isValidImageSize(imageFile)) {
                    if (imageType.equals("Start")) {
                        imageHashmap.put("image", bitmap);
                        startUri = finalUri;
                    } else if (imageType.equals("Stop")) {
                        imageHashmap.put("image", bitmap);
                        stopUri = finalUri;
                    }
                } else {
                    Util.showToast(getString(R.string.msg_big_image), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if(requestCode == 100) {
            if(gpsTracker.canGetLocation()) {
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
        machine_id = operatorMachineData.getMachine_id();
        structure_id = operatorMachineData.getStructure_id();
        machine_code = operatorMachineData.getMachine_code();
        if(machine_code != null && !(machine_code.isEmpty())){
            tv_machine_code.setText(machine_code);
        } else {
            tv_machine_code.setText("Not Assigned");
            Util.showToast("Machine not assigned. Contact to DPM.",this);
        }


        for (int i = 0; i < operatorMachineData.getNonutilisationTypeData().getEn().size(); i++) {
            ListHaltReasons.add(operatorMachineData.getNonutilisationTypeData().getEn().get(i).getValue());
        }
        editor.putString("machine_id", machine_id);
        editor.putString("machine_code", machine_code);
        editor.apply();
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

        Log.d("uploadMachineLog", "URL: " + upload_URL);
        Log.d("uploadMachineLog", "Data: " + new Gson().toJson(data));
        //String imageToSend = data.getImage();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("response Received -", jsonString);
                            Util.showToast("Submitted Successfully.", OperatorActivity.this);
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
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get("image"));
                    params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
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
//        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
//            strReasonId = operatorMachineData.getNonutilisationTypeData().getMr().get(selectedPosition).get_id();
//        }else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
//            strReasonId = operatorMachineData.getNonutilisationTypeData().getHi().get(selectedPosition).get_id();
//        }else {
//            strReasonId = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).get_id();
//        }
        strReasonId = operatorMachineData.getNonutilisationTypeData().getEn().get(selectedPosition).get_id();
        OperatorRequestResponseModel operatorRequestResponseModel = new OperatorRequestResponseModel();
        operatorRequestResponseModel.setMachine_id(machine_id);
        operatorRequestResponseModel.setStatus_code("" + state_halt);
        operatorRequestResponseModel.setStatus("halt");
        operatorRequestResponseModel.setReasonId(strReasonId);
        uploadMachineLog(operatorRequestResponseModel);
//        updateStatusAndProceed(state_halt);
//        clearReadingImages();
    }
}