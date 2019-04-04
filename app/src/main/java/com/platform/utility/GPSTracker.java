package com.platform.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.platform.R;

import androidx.annotation.Nullable;

@SuppressLint("Registered")
public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private Location location;
    private final LocationManager locationManager;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private double latitude;
    private double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    private final String TAG = GPSTracker.class.getName();

    public GPSTracker(Context context) {
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation() {
        try {
            // getting GPS status
            isGPSEnabled = locationManager != null && locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager != null && locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    getLocationByNetwork();
                }

                if (isGPSEnabled) {
                    getLocationByGPS();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return location;
    }

    @SuppressLint("MissingPermission")
    private void getLocationByNetwork() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        Log.d("Network", "Network");

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationByGPS() {
        if (location == null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            Log.d("GPS Enabled", "GPS Enabled");

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    public <T> boolean isGPSEnabled(Activity context, T objectInstance) {
        if (Permissions.isLocationPermissionGranted(context, objectInstance)) {
            return locationManager != null &&
                    (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        }

        return false;
    }

    public String getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return String.valueOf(latitude);
    }

    public String getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return String.valueOf(longitude);
    }

//    public void stopUsingGPS() {
//        if (locationManager != null) {
//            locationManager.removeUpdates(GPSTracker.this);
//        }
//    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canGetLocation() {
        // getting GPS status
        isGPSEnabled = locationManager != null && locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager != null && locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        this.canGetLocation = isGPSEnabled || isNetworkEnabled;
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getString(R.string.gps_popup_title));

        // Setting Dialog Message
        alertDialog.setMessage(context.getString(R.string.msg_enable_gps));

        // On pressing Settings button
        alertDialog.setPositiveButton(context.getString(R.string.gps_settings), (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, 100);
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        // Showing Alert Message
        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
