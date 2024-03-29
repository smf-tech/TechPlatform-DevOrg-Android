package com.octopusbjsindia.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineDieselRecord;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.adapters.MachineDieselRecordsAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class MachineDieselRecordFragment extends Fragment implements APIDataListener, View.OnClickListener,
        OnDateSelectedListener, OnMonthChangedListener {

    private View machineDieselRecordFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    String machineId, machineCode, currentStructureId;
    private EditText etMachineCode, etDieselQuantity;
    private Button btnAdd, btnSubmit;
    private RecyclerView rvDieselRecords;
    private ImageView ivCalendarMode, imgDieselReceipt, imgRegisterOne, imgRegisterTwo;
    private boolean isMonth = true;
    private MaterialCalendarView calendarView;
    private final ArrayList<MachineDieselRecord> machineDieselRecordsList = new ArrayList<>();
    private MachineDieselRecordsAdapter machineDieselRecordsAdapter;
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineDieselRecordFragment.class.getName();
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int dieselImageCount = 0, registerImageCount = 0;
    private String imageType;
    private ImageView clickedImageView;
    private GPSTracker gpsTracker;
    private Location location;
    private Date selectedDate;
    private String currentPhotoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineDieselRecordFragmentView = inflater.inflate(R.layout.fragment_machine_diesel_record, container, false);
        return machineDieselRecordFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineId = getActivity().getIntent().getStringExtra("machineId");
        machineCode = getActivity().getIntent().getStringExtra("machineCode");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        init();
    }

    private void init() {
        progressBarLayout = machineDieselRecordFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineDieselRecordFragmentView.findViewById(R.id.pb_profile_act);
        //machineDieselRecordFragmentPresenter = new MachineDieselRecordFragmentPresenter(this);
        ivCalendarMode = machineDieselRecordFragmentView.findViewById(R.id.tv_calendar_mode);
        ivCalendarMode.setOnClickListener(this);
        calendarView = machineDieselRecordFragmentView.findViewById(R.id.calendarView);
        //etStructureCode = machineDieselRecordFragmentView.findViewById(R.id.et_structure_code);
        etMachineCode = machineDieselRecordFragmentView.findViewById(R.id.et_machine_code);
        etDieselQuantity = machineDieselRecordFragmentView.findViewById(R.id.et_diesel_quantity);
        btnSubmit = machineDieselRecordFragmentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnAdd = machineDieselRecordFragmentView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        imgDieselReceipt = machineDieselRecordFragmentView.findViewById(R.id.img_diesel_receipt);
        imgDieselReceipt.setOnClickListener(this);
        imgRegisterOne = machineDieselRecordFragmentView.findViewById(R.id.img_register_one);
        imgRegisterOne.setOnClickListener(this);
        imgRegisterTwo = machineDieselRecordFragmentView.findViewById(R.id.img_register_two);
        imgRegisterTwo.setOnClickListener(this);
        machineDieselRecordsAdapter = new MachineDieselRecordsAdapter(machineDieselRecordsList, this);
        rvDieselRecords = machineDieselRecordFragmentView.findViewById(R.id.rv_diesel_record);
        rvDieselRecords.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDieselRecords.setAdapter(machineDieselRecordsAdapter);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        isMonth = !isMonth;
        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        calendarView.getSelectedDate();
        etMachineCode.setText(machineCode);
        //get lat,long of location
        gpsTracker = new GPSTracker(getActivity());
        if(Permissions.isLocationPermissionGranted(getActivity(), this)) {
            if(gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
        if(!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
            case R.id.img_diesel_receipt:
                imageType = "dieselReceipt";
                clickedImageView = imgDieselReceipt;
                onAddImageClick();
                break;
            case R.id.img_register_one:
                imageType = "registerImage";
                clickedImageView = imgRegisterOne;
                onAddImageClick();
                break;
            case R.id.img_register_two:
                imageType = "registerImage";
                clickedImageView = imgRegisterTwo;
                onAddImageClick();
                break;
            case R.id.btn_submit:
                if(Util.isConnected(getActivity())) {
                    if (isAllDataValid()) {
                        uploadData();
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
            case R.id.btn_add:
                int dateAlreadySelected = 0;
                if(selectedDate!= null) {
                    dateAlreadySelected = 1;
                    for (MachineDieselRecord machineDieselRecord : machineDieselRecordsList) {
                        if (machineDieselRecord.getDieselDate() == selectedDate.getTime()) {
                            //dateAlreadySelected = true;
                            dateAlreadySelected = 2;
                            break;
                        }
                    }
                    if(dateAlreadySelected == 2) {
                        Util.showToast(getResources().getString(R.string.date_already_selected), getActivity());
                    }
                } else {
                    Util.showToast("Please select calendar date", getActivity());
                }
                    if (dateAlreadySelected == 1) {
                        if (etDieselQuantity.getText().toString() != null && etDieselQuantity.getText().toString().length() > 0) {
                            if (gpsTracker.canGetLocation()) {
                                location = gpsTracker.getLocation();
                                if (location != null) {
                                    MachineDieselRecord machineDieselRecord = new MachineDieselRecord();
//                            machineDieselRecord.setDieselDate(Util.dateTimeToTimeStamp(selectedDate,
//                                    "00:00"));
                                    machineDieselRecord.setDieselDate(selectedDate.getTime());
                                    machineDieselRecord.setDieselQuantity(etDieselQuantity.getText().toString());
                                    machineDieselRecord.setMachineId(machineId);
                                    machineDieselRecord.setStructureId(currentStructureId);
                                    machineDieselRecordsList.add(machineDieselRecord);
                                } else {
                                    Util.showToast("Unable to get location", getActivity());
                                }
                            } else {
                                gpsTracker.showSettingsAlert();
                            }
                            machineDieselRecordsAdapter.notifyDataSetChanged();
                        } else {
                            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                            .findViewById(android.R.id.content), "Enter diesel quantity.",
                                    Snackbar.LENGTH_LONG);
                        }
                    }
                break;
        }
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        Calendar instance1 = Calendar.getInstance();
        instance1.set(2019, Calendar.JANUARY, 1);
        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            ivCalendarMode.setRotation(180);
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setMaximumDate(Calendar.getInstance().getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
            ivCalendarMode.setRotation(0);
        }
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else
                uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(getActivity(), "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri=Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri=Uri.fromFile(new File(currentPhotoPath));
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                Bitmap bitmap = Util.compressImageToBitmap(imageFile);
                clickedImageView.setImageURI(finalUri);
                if (Util.isValidImageSize(imageFile)) {
                    if(imageType.equals("dieselReceipt")) {
                    imageHashmap.put("diesel"+dieselImageCount, bitmap);
                    dieselImageCount++;
                } else if(imageType.equals("registerImage")) {
                    imageHashmap.put("register"+registerImageCount, bitmap);
                    registerImageCount++;
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
                Toast.makeText(getActivity(), "Location permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
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

    private boolean isAllDataValid() {
        if (machineDieselRecordsList.size() == 0) {
            if(dieselImageCount == 0 || registerImageCount == 0){
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please, click images of diesel receipt and register.",
                        Snackbar.LENGTH_LONG);
                return false;
            }
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, add diesel quantity against calendar date.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void backToMachineList(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
        intent.putExtra("viewType", 2);
        intent.putExtra("title", "Machine List");
        getActivity().startActivity(intent);
    }

    private void uploadData(){
        showProgressBar();
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.MACHINE_DIESEL_RECORD_FORM;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse responseOBJ = new Gson().fromJson(jsonString, CommonResponse.class);
                            hideProgressBar();
                            if(responseOBJ.getStatus() == 200){
                                Util.showToast(responseOBJ.getMessage(), getActivity());
                            } else {
                                Util.showToast(getResources().getString(R.string.msg_something_went_wrong), getActivity());
                            }
                            Log.d("response -",jsonString);
                            backToMachineList();
                        } catch (UnsupportedEncodingException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(machineDieselRecordsList));

//set location
                if (location != null ) {
                    params.put("lat", String.valueOf(location.getLatitude()));
                    params.put("long ", String.valueOf(location.getLongitude()));
                } else {
                    if(gpsTracker.canGetLocation()) {
                        location = gpsTracker.getLocation();
                        if (location != null ) {
                            params.put("lat", String.valueOf(location.getLatitude()));
                            params.put("long ", String.valueOf(location.getLongitude()));
                        }
                    } else {
                        Toast.makeText(getActivity(), "Not able to get location.", Toast.LENGTH_LONG).show();
                    }
                }
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));//add string parameters
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
                    if (getUserObjectFromPref().getOrgId()!=null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds()!=null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds()!=null) {
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
                for (int i = 0;i<imageHashmap.size(); i++) {
                    String key=(String)myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(getActivity());
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        //your background thread is still running. By the time that thread reaches the getActivity().runOnUiThread()
        // code,the activity no longer exists. So check if the activity still exists.
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (progressBarLayout != null && progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate = date.getDate();
        etDieselQuantity.setText("");
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
