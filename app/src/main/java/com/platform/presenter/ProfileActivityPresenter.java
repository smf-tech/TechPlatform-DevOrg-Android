package com.platform.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.models.profile.OrganizationProjectsResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.models.user.User;
import com.platform.models.user.UserInfo;
import com.platform.request.ImageRequestCall;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.EditProfileActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ProfileActivityPresenter implements ProfileRequestCallListener,
        ImageRequestCallListener {

    private final String TAG = ProfileActivityPresenter.class.getName();
    private WeakReference<EditProfileActivity> profileActivity;

    public ProfileActivityPresenter(EditProfileActivity activity) {
        profileActivity = new WeakReference<>(activity);
    }

    public void submitProfile(final UserInfo userInfo) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.submitUserProfile(userInfo);
    }

    public void getOrganizations() {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizations();
    }

    public void getOrganizationProjects(String orgId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizationProjects(orgId);
    }

    public void getOrganizationRoles(String orgId, String projectId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizationRoles(orgId, projectId);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName);
    }

    @SuppressLint("StaticFieldLeak")
    public void uploadProfileImage(File file, String type) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, null);
                return null;
            }
        }.execute();
    }

    @Override
    public void onProfileUpdated(String response) {
        User user = new Gson().fromJson(response, User.class);

        // Save response
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }

        profileActivity.get().hideProgressBar();
        profileActivity.get().showNextScreen(user);
    }

    @Override
    public void onOrganizationsFetched(String response) {
        profileActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
            if (orgResponse != null && orgResponse.getData() != null
                    && !orgResponse.getData().isEmpty()
                    && orgResponse.getData().size() > 0) {
                profileActivity.get().showOrganizations(orgResponse.getData());
            }
        }
    }

    @Override
    public void onJurisdictionFetched(String response, String level) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                profileActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), level);
            }
        }
    }

    @Override
    public void onOrganizationProjectsFetched(String orgId, String response) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            Util.saveUserProjectsInPref(orgId, response);
            OrganizationProjectsResponse projectsResponse
                    = new Gson().fromJson(response, OrganizationProjectsResponse.class);

            if (projectsResponse != null && projectsResponse.getData() != null
                    && !projectsResponse.getData().isEmpty() && projectsResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationProjects(projectsResponse.getData());
            }
        }
    }

    @Override
    public void onOrganizationRolesFetched(String orgId, String response) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            Util.saveUserRoleInPref(orgId, response);
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);

            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationRoles(orgRolesResponse.getData());
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            profileActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, profileActivity);
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    profileActivity);
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                profileActivity);
                    }
                } else {
                    profileActivity.get().showErrorMessage(error.getLocalizedMessage());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
    }

    @Override
    public void onImageUploadedListener(final String response, final String formName) {

        Log.e(TAG, "onImageUploadedListener:\n" + response);
        profileActivity.get().runOnUiThread(() -> Util.showToast(
                profileActivity.get().getResources().getString(R.string.image_upload_success), profileActivity.get()));
        profileActivity.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);

                profileActivity.get().onImageUploaded(url);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
