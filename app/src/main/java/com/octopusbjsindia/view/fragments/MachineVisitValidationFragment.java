package com.octopusbjsindia.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineWorkingHoursAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MachineWorkingHoursRecord;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.MachineVisitValidationFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.adapters.MachineWorkingHoursAdapter;
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

public class MachineVisitValidationFragment extends Fragment implements APIDataListener, View.OnClickListener,
        OnDateSelectedListener, OnMonthChangedListener {
    private View machineVisitValidationFragmenttView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineVisitValidationFragmentPresenter presenter;
    private String machineId, machineCode, currentStructureId, newStructureId;
    private RecyclerView rvWorkingHours;
    private ImageView ivCalendarMode, imgRegisterOne, imgRegisterTwo;
    private EditText etMachineCode, etStructureCode;
    private TextView etWorkingHours;
    private Button btnMatch, btnMismatch, btnSubmit;
    private boolean isMonth = true;
    private MaterialCalendarView calendarView;
    private Date selectedDate;
    private MachineWorkingHoursAdapter machineWorkingHoursAdapter;
    private final ArrayList<MachineWorkingHoursRecord> machineWorkingHoursList = new ArrayList<>();
    private int selectedMonth;
    private SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private SimpleDateFormat mmFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineVisitValidationFragment.class.getName();
    private RequestQueue rQueue;
    private Bitmap mProfileCompressBitmap = null;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    private ImageView clickedImageView;
    private GPSTracker gpsTracker;
    private Location location;
    private String currentPhotoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineVisitValidationFragmenttView = inflater.inflate(R.layout.fragment_machine_visit_validation,
                container, false);
        return machineVisitValidationFragmenttView;
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
        progressBarLayout = machineVisitValidationFragmenttView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineVisitValidationFragmenttView.findViewById(R.id.pb_profile_act);
        ivCalendarMode = machineVisitValidationFragmenttView.findViewById(R.id.tv_calendar_mode);
        ivCalendarMode.setOnClickListener(this);
        calendarView = machineVisitValidationFragmenttView.findViewById(R.id.calendarView);
        //etStructureCode = machineVisitValidationFragmenttView.findViewById(R.id.et_structure_code);
        etMachineCode = machineVisitValidationFragmenttView.findViewById(R.id.et_machine_code);
        etWorkingHours = machineVisitValidationFragmenttView.findViewById(R.id.et_working_hours);
        btnMatch = machineVisitValidationFragmenttView.findViewById(R.id.btn_match);
        btnMismatch = machineVisitValidationFragmenttView.findViewById(R.id.btn_mismatch);
        btnMatch.setOnClickListener(this);
        btnMismatch.setOnClickListener(this);
        btnMatch.setEnabled(false);
        btnMismatch.setEnabled(false);
        btnSubmit = machineVisitValidationFragmenttView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        imgRegisterOne = machineVisitValidationFragmenttView.findViewById(R.id.img_register_one);
        imgRegisterOne.setOnClickListener(this);
        imgRegisterTwo = machineVisitValidationFragmenttView.findViewById(R.id.img_register_two);
        imgRegisterTwo.setOnClickListener(this);
        machineWorkingHoursAdapter = new MachineWorkingHoursAdapter(machineWorkingHoursList, this);
        rvWorkingHours = machineVisitValidationFragmenttView.findViewById(R.id.rv_working_hours);
        rvWorkingHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWorkingHours.setAdapter(machineWorkingHoursAdapter);
        presenter = new MachineVisitValidationFragmentPresenter(this);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        isMonth = !isMonth;
        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        Date d = new Date();
        selectedMonth=Integer.parseInt(mmFormat.format(d.getTime()));
        etMachineCode.setText(machineCode);
        //etStructureCode.setText(currentStructureId);
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            location = gpsTracker.getLocation();
        }
        if(!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        //Calendar instance = Calendar.getInstance();

        Calendar instance1 = Calendar.getInstance();
        instance1.set(2019, Calendar.JANUARY, 1);
        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setMaximumDate(Calendar.getInstance().getTime())
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
//        calendarView.setSelectedDate(instance.getTime());
//        calendarView.setCurrentDate(instance.getTime());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri=Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(getContext(),this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri=Uri.fromFile(new File(currentPhotoPath));
                    Crop.of(outputUri, finalUri).start(getContext(),this);
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
                    imageHashmap.put("image" + imageCount, bitmap);
                    imageCount++;
                } else {
                    Util.showToast(getString(R.string.msg_big_image), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
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
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.MACHINE_VISIT_VALIDATION_FORM;
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
                params.put("formData", new Gson().toJson(machineWorkingHoursList));
                if(location != null) {
                    params.put("lat", String.valueOf(location.getLatitude()));
                    params.put("long ", String.valueOf(location.getLongitude()));
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
                10000,
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

    public void setWorkingHoursData(MachineWorkingHoursAPIResponse machineWorkingHoursAPIResponse) {
        //set data received in api
        if(machineWorkingHoursAPIResponse.getStatus() == 200) {
            MachineWorkingHoursRecord machineWorkingHoursRecord = machineWorkingHoursAPIResponse.getData();
            etWorkingHours.setText(machineWorkingHoursRecord.getWorkingHours());
            if(machineWorkingHoursRecord.isActionTaken()) {
                btnMatch.setEnabled(false);
                btnMismatch.setEnabled(false);
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "You have already validated" +
                                " working hours for this date.",
                        Snackbar.LENGTH_LONG);
            } else {
                btnMatch.setEnabled(true);
                btnMismatch.setEnabled(true);
            }
        } else if(machineWorkingHoursAPIResponse.getStatus() == 400) {
            btnMatch.setEnabled(false);
            btnMismatch.setEnabled(false);
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), machineWorkingHoursAPIResponse.getMessage(),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.clearData();
            presenter = null;
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
            case R.id.btn_match:
                MachineWorkingHoursRecord machineWorkingHoursRecord = new MachineWorkingHoursRecord();
                machineWorkingHoursRecord.setMachineId(machineId);
                machineWorkingHoursRecord.setStructureAssigned(currentStructureId);
                machineWorkingHoursRecord.setWorkingDate(selectedDate.getTime());
                machineWorkingHoursRecord.setWorkingHours(etWorkingHours.getText().toString());
                machineWorkingHoursRecord.setWorkingStatus(true);
                machineWorkingHoursList.add(machineWorkingHoursRecord);
                btnMatch.setEnabled(false);
                btnMismatch.setEnabled(false);
                machineWorkingHoursAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_mismatch:
                MachineWorkingHoursRecord machineWorkingHoursRecord2 = new MachineWorkingHoursRecord();
                machineWorkingHoursRecord2.setMachineId(machineId);
                machineWorkingHoursRecord2.setStructureAssigned(currentStructureId);
                machineWorkingHoursRecord2.setWorkingDate(selectedDate.getTime());
                machineWorkingHoursRecord2.setWorkingHours(etWorkingHours.getText().toString());
                machineWorkingHoursRecord2.setWorkingStatus(false);
                machineWorkingHoursList.add(machineWorkingHoursRecord2);
                btnMatch.setEnabled(false);
                btnMismatch.setEnabled(false);
                machineWorkingHoursAdapter.notifyDataSetChanged();
                break;
            case R.id.img_register_one:
                clickedImageView = imgRegisterOne;
                onAddImageClick();
                break;
            case R.id.img_register_two:
                clickedImageView = imgRegisterTwo;
                onAddImageClick();
                break;
            case R.id.btn_submit:
                if(Util.isConnected(getActivity())) {
                    if(machineWorkingHoursList.size()>0) {
                        if(imageCount>0) {
                            uploadData();
                        } else {
                            Util.showToast("Please select image.", getActivity());
                        }
                    } else {
                        Util.showToast("Please add machine working hours.", getActivity());
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate = date.getDate();
        etWorkingHours.setText("");
        if(Util.isConnected(getContext())) {
            if(machineWorkingHoursList.size()>0) {
                for (MachineWorkingHoursRecord machineWorkingHoursRecord : machineWorkingHoursList) {
                    if (machineWorkingHoursRecord.getWorkingDate() == selectedDate.getTime()) {
                        Util.showToast(getResources().getString(R.string.date_already_selected), getActivity());
                        break;
                    } else {
                        presenter.getWorkingHoursDetails(String.valueOf(selectedDate.getTime()), machineId);
                    }
                }
            } else {
                presenter.getWorkingHoursDetails(String.valueOf(selectedDate.getTime()), machineId);
            }
        } else{
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (selectedMonth != Integer.parseInt(mmFormat.format(date.getDate()))) {
            selectedMonth=Integer.parseInt(mmFormat.format(date.getDate()));
        }
    }
}
