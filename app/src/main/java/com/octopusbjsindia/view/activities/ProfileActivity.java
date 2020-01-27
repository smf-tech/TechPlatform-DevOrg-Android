package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.MultyProjectData;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.MultyProjectAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, APIDataListener {

    ProfileActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.profile));

        presenter = new ProfileActivityPresenter(this);
        if(Util.isConnected(this)){
            presenter.getMultProfile();
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network),this);
        }

        ImageView backButton = findViewById(R.id.toolbar_back_action);
        backButton.setOnClickListener(this);

        ImageView editButton = findViewById(R.id.toolbar_edit_action);
        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(this);

        UserInfo userInfo = Util.getUserObjectFromPref();

        ImageView profilePic = findViewById(R.id.user_profile_pic);
        RequestOptions requestOptions = new RequestOptions().placeholder(0);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

        if (!TextUtils.isEmpty(userInfo.getProfilePic())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(userInfo.getProfilePic())
                    .into(profilePic);
        }
        ((TextView) findViewById(R.id.user_profile_name)).setText(userInfo.getUserName());
        ((TextView) findViewById(R.id.user_profile_mobile)).setText(userInfo.getUserMobileNumber());

        if (!TextUtils.isEmpty(userInfo.getUserEmailId())) {
            ((TextView) findViewById(R.id.user_profile_email)).setText(userInfo.getUserEmailId());
        } else {
            findViewById(R.id.user_profile_email).setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.user_profile_org)).setText(userInfo.getOrgName());

        String projects = getStringValue(userInfo.getProjectIds());
        ((TextView) findViewById(R.id.user_profile_project)).setText(projects);

        LinearLayout parent = findViewById(R.id.user_profile_details);
        LinearLayout roleView = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.layout_profile_item, parent, false);
        ((TextView) roleView.findViewById(R.id.user_profile_label)).setText(getString(R.string.role));
        ((TextView) roleView.findViewById(R.id.user_profile_text)).setText(userInfo.getRoleNames());
        parent.addView(roleView);

        UserLocation userLocation = userInfo.getUserLocation();
        if (userLocation.getStateId() != null && userLocation.getStateId().size() > 0) {
            String states = getStringValue(userLocation.getStateId());
            setJurisdictionLevel(parent, getString(R.string.state), states);
        }

        if (userLocation.getDistrictIds() != null && userLocation.getDistrictIds().size() > 0) {
            String districts = getStringValue(userLocation.getDistrictIds());
            setJurisdictionLevel(parent, getString(R.string.district), districts);
        }

        if (userLocation.getTalukaIds() != null && userLocation.getTalukaIds().size() > 0) {
            String taluka = getStringValue(userLocation.getTalukaIds());
            setJurisdictionLevel(parent, getString(R.string.taluka), taluka);
        }

        if (userLocation.getVillageIds() != null && userLocation.getVillageIds().size() > 0) {
            String villages = getStringValue(userLocation.getVillageIds());
            setJurisdictionLevel(parent, getString(R.string.village), villages);
        }

        if (userLocation.getClusterIds() != null && userLocation.getClusterIds().size() > 0) {
            String clusters = getStringValue(userLocation.getClusterIds());
            setJurisdictionLevel(parent, getString(R.string.cluster), clusters);
        }
    }

    private void setJurisdictionLevel(LinearLayout parent, String label, String value) {
        LinearLayout jurisdictionView = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.layout_profile_item, parent, false);
        ((TextView) jurisdictionView.findViewById(R.id.user_profile_label)).setText(label);
        ((TextView) jurisdictionView.findViewById(R.id.user_profile_text)).setText(value);
        parent.addView(jurisdictionView);
    }

    private void showEditProfileScreen() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(Constants.Login.ACTION, Constants.Login.ACTION_EDIT);
        startActivityForResult(intent, Constants.IS_ROLE_CHANGE);
    }

    private String getStringValue(List<JurisdictionType> inputArray) {
        if (inputArray != null && inputArray.size() > 0) {
            StringBuilder strProject = new StringBuilder();
            for (JurisdictionType obj : inputArray) {
                strProject.append(obj.getName()).append(", ");
            }

            strProject = strProject.delete(strProject.length() - 2, strProject.length() - 1);
            return strProject.toString();
        }

        return "";
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_edit_action) {
            if (Util.isConnected(this)) {
                showEditProfileScreen();
            } else {
                Util.showToast(getString(R.string.msg_no_network), this);
            }
        } else if (view.getId() == R.id.toolbar_back_action) {
            onBackPressed();
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

    public void displayProjects(ArrayList<MultyProjectData> data) {
        RecyclerView rvMembers = findViewById(R.id.rv_projects);
        MultyProjectAdapter multyProjectAdapter = new MultyProjectAdapter(data,this, presenter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMembers.setLayoutManager(new GridLayoutManager(this, 2));
        rvMembers.setAdapter(multyProjectAdapter);
    }
}
