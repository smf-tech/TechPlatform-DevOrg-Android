package com.octopusbjsindia.view.activities;

import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.StructureBoundaryData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.stories.CommentResponse;
import com.octopusbjsindia.presenter.MapsActivityPresenter;
import com.octopusbjsindia.utility.Util;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, APIDataListener {

    final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_BOUNDARY = "StructureBoundary";
    private GoogleMap mMap;

    StructureData structureData;
    ArrayList<LatLng> locationList;
    private RelativeLayout progressBar;
    private StructureBoundaryData requestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);
        locationList = (ArrayList<LatLng>) getIntent().getSerializableExtra(STRUCTURE_BOUNDARY);

        MapsActivityPresenter presenter = new MapsActivityPresenter(this);


        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData = new StructureBoundaryData();
                requestData.setStructureId(structureData.getStructureId());
                Gson gson = new GsonBuilder().create();
                String locationListjson =gson.toJson(locationList);
                requestData.setStructureBoundary(locationListjson);
                if(Util.isConnected(MapsActivity.this)){
                    presenter.submitBoundatys(requestData);
                } else {
                    DatabaseManager.getDBInstance(Platform.getInstance()).getStructureBoundaryDao()
                            .insert(requestData);
                    Util.showToast("Boundary data saved offline, will be sicked",MapsActivity.this);
                    finish();
                }
            }
        });

        findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        progressBar = findViewById(R.id.progress_bar);

        setTitle("Record Boundary");
    }
    public void setTitle(String title) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
   }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText("Alert");
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText("Are you sure you want to discard.");
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Yes");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            //Close dialog
            dialog.dismiss();
            super.onBackPressed();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText("No");
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            //Close dialog
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(locationList)
                .width(5)
                .color(Color.BLUE);

        googleMap.addPolyline(polylineOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationList.get(0), 19));
        mMap.addMarker(new MarkerOptions().position(locationList.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(locationList.get(locationList.size()-1)).title("Stop"));

    }

    @Override
    public void onFailureListener(String requestID, String message) {
        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureBoundaryDao()
                .insert(requestData);
        Util.showToast("Boundary data saved offline, will be sicked",MapsActivity.this);
        finish();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        DatabaseManager.getDBInstance(Platform.getInstance()).getStructureBoundaryDao()
                .insert(requestData);
        Util.showToast("Boundary data saved offline. will be sicked",MapsActivity.this);
        finish();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommentResponse masterDataResponse = new Gson().fromJson(response, CommentResponse.class);
        if(masterDataResponse.getStatus()==200){
            Util.showToast(masterDataResponse.getMessage(),this);
            finish();
        } else {
            DatabaseManager.getDBInstance(Platform.getInstance()).getStructureBoundaryDao()
                    .insert(requestData);
            Util.showToast("Boundary data saved offline. will be sicked",MapsActivity.this);
        }
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null ) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }
}
