package com.platform.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.google.gson.Gson;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.MachineWorkingHoursRecord;
import com.platform.presenter.MachineVisitValidationFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.adapters.MachineWorkingHoursAdapter;
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

public class MachineVisitValidationFragment extends Fragment implements APIDataListener, View.OnClickListener,
        OnDateSelectedListener, OnMonthChangedListener {
    private View machineVisitValidationFragmenttView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineVisitValidationFragmentPresenter machineVisitValidationFragmentPresenter;
    private String machineId, currentStructureId, newStructureId;
    private RecyclerView rvWorkingHours;
    private ImageView ivCalendarMode, imgRegisterOne, imgRegisterTwo;
    private EditText etMachineCode, etStructureCode, etWorkingHours;
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
    private ArrayList<HashMap<String, String>> arraylist;
    private String upload_URL = "http://13.235.124.3/api/machineVisit";
    private Bitmap mProfileCompressBitmap = null;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int i = 0;

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
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        //newStructureId = getActivity().getIntent().getStringExtra("newStructureId");
        init();
    }

    private void init() {
        progressBarLayout = machineVisitValidationFragmenttView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineVisitValidationFragmenttView.findViewById(R.id.pb_profile_act);
        ivCalendarMode = machineVisitValidationFragmenttView.findViewById(R.id.tv_calendar_mode);
        ivCalendarMode.setOnClickListener(this);
        calendarView = machineVisitValidationFragmenttView.findViewById(R.id.calendarView);
        etStructureCode = machineVisitValidationFragmenttView.findViewById(R.id.et_structure_code);
        etMachineCode = machineVisitValidationFragmenttView.findViewById(R.id.et_machine_code);
        etWorkingHours = machineVisitValidationFragmenttView.findViewById(R.id.et_working_hours);
        btnMatch = machineVisitValidationFragmenttView.findViewById(R.id.btn_match);
        btnMismatch = machineVisitValidationFragmenttView.findViewById(R.id.btn_mismatch);
        btnMatch.setOnClickListener(this);
        btnMismatch.setOnClickListener(this);
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
        machineVisitValidationFragmentPresenter = new MachineVisitValidationFragmentPresenter(this);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setOnDateChangedListener(this);
        isMonth = !isMonth;
        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());
        Date d = new Date();
        selectedMonth=Integer.parseInt(mmFormat.format(d.getTime()));
        etMachineCode.setText(machineId);
        etStructureCode.setText(currentStructureId);
        etWorkingHours.setText("9:30");
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);
        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            ivCalendarMode.setRotation(180);
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
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
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
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
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;

                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(imageFile)) {
                        imgRegisterOne.setImageURI(finalUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), finalUri);
                        imageHashmap.put("image"+i, bitmap);
                        i++;
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
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

    private void uploadImage(){

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Toast.makeText(getActivity().getApplicationContext(),jsonString,Toast.LENGTH_LONG).show();
                            Log.d("response -",jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(machineWorkingHoursList));
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));//add string parameters
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0;i<imageHashmap.size(); i++) {
                    String key=(String)myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put("image"+i, new DataPart(key, getFileDataFromDrawable(drawable),
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

    public void setData() {
        //set data received in api

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineVisitValidationFragmentPresenter != null) {
            machineVisitValidationFragmentPresenter.clearData();
            machineVisitValidationFragmentPresenter = null;
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
                machineWorkingHoursRecord.setWorkingDate(847478928);
                machineWorkingHoursRecord.setWorkingStatus("false");
                machineWorkingHoursList.add(machineWorkingHoursRecord);
                machineWorkingHoursRecord.setMachineId(machineId);
                machineWorkingHoursRecord.setWorkingDate(667478928);
                machineWorkingHoursRecord.setWorkingStatus("false");
                machineWorkingHoursList.add(machineWorkingHoursRecord);
                btnMatch.setClickable(false);
                btnMismatch.setClickable(false);
                machineWorkingHoursAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_mismatch:
                MachineWorkingHoursRecord machineWorkingHoursRecord2 = new MachineWorkingHoursRecord();
                machineWorkingHoursRecord2.setMachineId(machineId);
                machineWorkingHoursRecord2.setStructureAssigned(currentStructureId);
                machineWorkingHoursRecord2.setWorkingDate(439848982);
                machineWorkingHoursRecord2.setWorkingHours(etWorkingHours.getText().toString());
                machineWorkingHoursRecord2.setWorkingStatus("true");
                machineWorkingHoursList.add(machineWorkingHoursRecord2);
                btnMatch.setClickable(false);
                btnMismatch.setClickable(false);
                machineWorkingHoursAdapter.notifyDataSetChanged();
                break;
            case R.id.img_register_one:
                onAddImageClick();
                break;
            case R.id.img_register_two:
                onAddImageClick();
                break;
            case R.id.btn_submit:
                uploadImage();
                machineVisitValidationFragmentPresenter.submitWorkingHours();
                break;
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Toast.makeText(getActivity(), "date:" + date, Toast.LENGTH_SHORT).show();
        selectedDate = date.getDate();
        btnMatch.setClickable(true);
        btnMismatch.setClickable(true);
        if(Util.isConnected(getContext())) {
            //machineVisitValidationFragmentPresenter.getWorkingHourDetails(selectedDate.getTime(), machineId);
        }else{
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (selectedMonth != Integer.parseInt(mmFormat.format(date.getDate()))) {
            btnMatch.setClickable(true);
            btnMismatch.setClickable(true);
            selectedMonth=Integer.parseInt(mmFormat.format(date.getDate()));
        }
    }
}
