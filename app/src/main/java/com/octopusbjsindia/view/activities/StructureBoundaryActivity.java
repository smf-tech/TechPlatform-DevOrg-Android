package com.octopusbjsindia.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.utility.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StructureBoundaryActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "StructureBoundary";
    final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_BOUNDARY = "StructureBoundary";

    private StructureData structureData;
    private boolean recording = false;
    ArrayList<LatLng> locationList;

    // bunch of location related apis
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private LottieAnimationView lavWalking;
    private TextView tvStartRecording, toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_boundary);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);
        locationList = new ArrayList<LatLng>();

        init();
    }

    private void init() {

        lavWalking = findViewById(R.id.lav_walking);
        tvStartRecording = findViewById(R.id.tv_start_recording);
        toolbarTitle = findViewById(R.id.toolbar_title);

        findViewById(R.id.bt_start).setOnClickListener(this);
        findViewById(R.id.bt_stop).setOnClickListener(this);
        findViewById(R.id.bt_preview).setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
//                Toast.makeText(getApplicationContext(), "Lat=" + mCurrentLocation.getLatitude() +
//                        "Long=" + mCurrentLocation.getLongitude() +
//                        "Alti=" + mCurrentLocation.getAltitude() +
//                        "Accur=" + mCurrentLocation.getAccuracy(), Toast.LENGTH_SHORT).show();
//                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                tvStartRecording.setVisibility(View.VISIBLE);
                tvStartRecording.setText("Location Accuracy = " + mCurrentLocation.getAccuracy());
                if (recording) {
                    locationList.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                } else if (mCurrentLocation.getAccuracy() < 25) {
                    findViewById(R.id.bt_start).setVisibility(View.VISIBLE);
                    findViewById(R.id.lav_location).setVisibility(View.GONE);
                    lavWalking.setVisibility(View.VISIBLE);
                    lavWalking.cancelAnimation();
                }
            }
        };


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        startLocationUpdate();
        setTitle("Record Boundary");
    }

    public void setTitle(String title) {
        toolbarTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
//        ImageView toolbar_edit_action = findViewById(R.id.toolbar_edit_action);
//        toolbar_edit_action.setImageResource(R.drawable.ic_saved_offline);
//        toolbar_edit_action.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.bt_start:
//                tvStartRecording.setVisibility(View.VISIBLE);
                recording = true;
                findViewById(R.id.bt_start).setVisibility(View.GONE);
                findViewById(R.id.bt_stop).setVisibility(View.VISIBLE);
                lavWalking.setVisibility(View.VISIBLE);
                lavWalking.playAnimation();
                break;
            case R.id.bt_stop:
                recording = false;
                stopLocationUpdates();
                findViewById(R.id.bt_stop).setVisibility(View.GONE);
                findViewById(R.id.bt_preview).setVisibility(View.VISIBLE);
//                tvStartRecording.setVisibility(View.GONE);
                lavWalking.cancelAnimation();
                break;
            case R.id.bt_preview:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra(STRUCTURE_DATA, structureData);
                intent.putExtra(STRUCTURE_BOUNDARY, locationList);
                startActivity(intent);
                finish();
                break;

        }
    }

    public void startLocationUpdate() {

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            //noinspection MissingPermission
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(StructureBoundaryActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                        Constants.GPS_REQUEST);

                                Toast.makeText(getApplicationContext(), "No permission location updates!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                        mLocationCallback, Looper.myLooper());
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(StructureBoundaryActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(StructureBoundaryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
