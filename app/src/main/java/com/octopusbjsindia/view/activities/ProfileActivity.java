package com.octopusbjsindia.view.activities;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.MultyProjectData;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.MultyProjectAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends BaseActivity implements/* View.OnClickListener, */APIDataListener {

    private RelativeLayout progressBar;
    private CircularProgressIndicator progressBarProjects;
    private MaterialToolbar toolbar;
    ProfileActivityPresenter presenter;
    LinearLayout parent;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.profile));
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.ly_progress_bar);
        progressBarProjects = findViewById(R.id.progress_project);

        presenter = new ProfileActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getMultProfile();
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), this);
        }

        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    if (Util.isConnected(ProfileActivity.this)) {
                        if (Util.isConnected(ProfileActivity.this)) {
                            presenter.getUserProfile();
                        } else {
                            Util.showToast(getResources().getString(R.string.msg_no_network), this);
                        }

                    } else {
                        Util.showToast(getString(R.string.msg_no_network), this);
                    }
                }
                return false;
            }
        });

        // ImageView backButton = findViewById(R.id.toolbar_back_action);
        //backButton.setOnClickListener(this);

//        ImageView editButton = findViewById(R.id.toolbar_edit_action);
//        editButton.setVisibility(View.VISIBLE);
//        editButton.setOnClickListener(this);

        parent = findViewById(R.id.user_profile_details);
        initViews();

    }

    private void initViews() {
        userInfo = Util.getUserObjectFromPref();

        ImageView profilePic = findViewById(R.id.user_profile_pic);
        RequestOptions requestOptions = new RequestOptions().placeholder(0);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

        if (!TextUtils.isEmpty(userInfo.getProfilePic())) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(userInfo.getProfilePic())
                    .placeholder(R.drawable.ic_user_avatar)
                    .into(profilePic);
        }
        ((TextView) findViewById(R.id.user_profile_name)).setText(userInfo.getUserName().trim());
        ((TextView) findViewById(R.id.user_profile_mobile)).setText(userInfo.getUserMobileNumber().trim());

        if (!TextUtils.isEmpty(userInfo.getUserEmailId())) {
            ((TextView) findViewById(R.id.user_profile_email)).setText(userInfo.getUserEmailId().trim());
        } else {
            findViewById(R.id.user_profile_email).setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.user_profile_org)).setText(userInfo.getOrgName());

        String projects = getStringValue(userInfo.getProjectIds());
        ((TextView) findViewById(R.id.user_profile_project)).setText(projects);

        if (parent.getChildCount() > 0)
            parent.removeAllViews();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            if (Util.isConnected(this)) {
                if (Util.isConnected(this)) {
                    presenter.getUserProfile();
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } else {
                Util.showToast(getString(R.string.msg_no_network), this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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

  /*  @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_edit_action) {
            if (Util.isConnected(this)) {
                if (Util.isConnected(this)) {
                    presenter.getUserProfile();
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } else {
                Util.showToast(getString(R.string.msg_no_network), this);
            }
        } else if (view.getId() == R.id.toolbar_back_action) {
            onBackPressed();
        }
    }*/

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        showEditProfileScreen();
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void displayProjects(ArrayList<MultyProjectData> data) {
        progressBarProjects.setVisibility(View.GONE);
        String projects = userInfo.getProjectIds().get(0).getId();
        RecyclerView rvMembers = findViewById(R.id.rv_projects);
        MultyProjectAdapter multyProjectAdapter = new MultyProjectAdapter(data, this, presenter,projects);

        // rvMembers.setLayoutManager(new GridLayoutManager(this, 2));
        rvMembers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvMembers.setAdapter(multyProjectAdapter);
    }

    public void updateUI(String response) {
        User user = new Gson().fromJson(response, User.class);
        if (response != null && user.getUserInfo() != null) {
            Constants.GET_MODELS = true;
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }
        Util.showToast("Project switched successfully.", this);
        finish();
//        showDialog(getResources().getString(R.string.alert),
//                "Project switched successfully.",
//                getResources().getString(R.string.ok),
//                "");
//        initViews();

    }

    public void showDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                finish();
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                //Close dialog
                finish();
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
