package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineDetailData;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.MachineMouActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.fragments.MachineMouFirstFragment;
import com.octopusbjsindia.view.fragments.MachineMouFourthFragment;
import com.octopusbjsindia.view.fragments.MachineMouSecondFragment;
import com.octopusbjsindia.view.fragments.MachineMouThirdFragment;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class MachineMouActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener {
    private ImageView ivBackIcon;
    private FragmentManager fManager;
    private Fragment fragment;
    private MachineDetailData machineDetailData;
    private static final String TAG = MachineMouActivity.class.getName();
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private String machineId;
    private int statusCode;
    private MachineMouActivityPresenter machineMouActivityPresenter;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    public Uri chequeImageUri, operatorLicenseImageUri;
    private RequestQueue rQueue;
    private Location location;
    private GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_mou);
        init();
    }

    private void init(){
        ivBackIcon = findViewById(R.id.toolbar_back_action);
        ivBackIcon.setOnClickListener(this);
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        machineMouActivityPresenter = new MachineMouActivityPresenter(this);
        machineDetailData = new MachineDetailData();
        if(getIntent().getStringExtra("SwitchToFragment")!=null){
            if(getIntent().getStringExtra("SwitchToFragment").equals("MachineMouFirstFragment")){
                if(getIntent().getIntExtra("statusCode", 0) ==
                        Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
                    statusCode = getIntent().getIntExtra("statusCode",0);
                    toolbar_title.setText(R.string.create_machine);
                    fManager = getSupportFragmentManager();
                    fragment = new MachineMouFirstFragment();
                    FragmentTransaction fTransaction = fManager.beginTransaction();
                    fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null).commit();
                } else {
                    if(getIntent().getIntExtra("statusCode", 0) ==
                            Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
                        toolbar_title.setText(R.string.machine_eligible_form_title);
                    } else {
                        toolbar_title.setText("Machine Details");
                    }
                    machineId = getIntent().getStringExtra("machineId");
                    statusCode = getIntent().getIntExtra("statusCode",0);
                    if(Util.isConnected(this)) {
                        machineMouActivityPresenter.getMachineDetails(machineId, statusCode);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
            }
        }
    }

    public void openFragment(String switchToFragment) {
        fManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(switchToFragment)) {
            switch (switchToFragment) {
                case "MachineMouSecondFragment":
                    fragment = new MachineMouSecondFragment();
                    break;
                case "MachineMouThirdFragment":
                    fragment = new MachineMouThirdFragment();
                    break;
                case "MachineMouFourthFragment":
                    fragment = new MachineMouFourthFragment();
                    break;
            }
        }
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null)
                .commit();
    }

    public MachineDetailData getMachineDetailData() {
        return machineDetailData;
    }

    public HashMap<String, Bitmap> getImageHashmap() {
        return imageHashmap;
    }

    public void setMachineDetailData(MachineDetailData machineDetail) {
        this.machineDetailData = machineDetail;
        fManager = getSupportFragmentManager();
        fragment = new MachineMouFirstFragment();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.machine_mou_frame_layout, fragment).addToBackStack(null).commit();
    }

    public void uploadData() {
        showProgressBar();
//        gpsTracker = new GPSTracker(this);
//        if (gpsTracker.isGPSEnabled(this, this)) {
//            location = gpsTracker.getLocation();
//            if (location != null) {
//                getMachineDetailData().setFormLat(String.valueOf(location.getLatitude()));
//                getMachineDetailData().setFormLong(String.valueOf(location.getLongitude()));
//            }
//        }
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.MACHINE_MOU_FORM;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse responseOBJ = new Gson().fromJson(jsonString, CommonResponse.class);
                            hideProgressBar();
                            if (responseOBJ.getStatus() == 200) {
                                Util.showToast(responseOBJ.getMessage(), this);
                                backToMachineList();
                            } else if (responseOBJ.getStatus() == 300) {
                                Util.showToast(responseOBJ.getMessage(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
                            }
                            Log.d("response -", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getApplication(), getResources().getString(R.string.msg_failure),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(getMachineDetailData()));
//                if (location != null) {
//                    params.put("lat", String.valueOf(location.getLatitude()));
//                    params.put("long ", String.valueOf(location.getLongitude()));
//                }
                params.put("imageArraySize", String.valueOf(getImageHashmap().size()));//add string parameters
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
                Iterator myVeryOwnIterator = getImageHashmap().keySet().iterator();
                for (int i = 0; i < getImageHashmap().size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), getImageHashmap().get(key));
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
        rQueue = Volley.newRequestQueue(this);
        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void backToMachineList() {
        finish();
        Intent intent = new Intent(this, SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
        intent.putExtra("viewType", 2);
        intent.putExtra("title", "Machine List");
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
            try {
                fManager.popBackStackImmediate();
            } catch (IllegalStateException e) {
                Log.e("TAG", e.getMessage());
            }
            if (fManager.getBackStackEntryCount() == 0) {
                finish();
            }
    }

    public void openFragmentAtPosition(int position) {
        try {
            fManager.getBackStackEntryAt(position);
        } catch (IllegalStateException e) {
            Log.e("TAG", e.getMessage());
        }
        if (fManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
        if (machineMouActivityPresenter != null) {
            machineMouActivityPresenter.clearData();
            machineMouActivityPresenter = null;
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
        this.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        this.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (this != null) {
            this.onBackPressed();
        }
    }
}
