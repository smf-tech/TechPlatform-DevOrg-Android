package com.octopusbjsindia.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.listeners.MatrimonyMasterDataRequestCallListener;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.models.Matrimony.MatrimonyUserRegRequestModel;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.request.MatrimonyMasterDataRequestCall;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.UserRegistrationMatrimonyActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

public class UserRegistrationMatrimonyActivityPresenter implements MatrimonyMasterDataRequestCallListener, ImageRequestCallListener {
    private final String TAG = this.getClass().getName();
    private WeakReference<UserRegistrationMatrimonyActivity> fragmentWeakReference;

    public UserRegistrationMatrimonyActivityPresenter(UserRegistrationMatrimonyActivity tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests() {
        MatrimonyMasterDataRequestCall requestCall = new MatrimonyMasterDataRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllMasterDataRequests();
    }
    public void UserRegistrationRequests(MatrimonyUserRegRequestModel matrimonyUserRegRequestModel) {
        MatrimonyMasterDataRequestCall requestCall = new MatrimonyMasterDataRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.submitUserRegistrationDataRequests(matrimonyUserRegRequestModel);
    }

    @Override
    public void MatrimonyMasterDataRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            MatrimonyMasterRequestModel matrimonyMasterRequestModel
                    = new Gson().fromJson(response, MatrimonyMasterRequestModel.class);
            if (matrimonyMasterRequestModel != null && matrimonyMasterRequestModel.getData() != null
                    && !matrimonyMasterRequestModel.getData().isEmpty()
                    && matrimonyMasterRequestModel.getData().size() > 0) {
                if (matrimonyMasterRequestModel.getData().get(0).getMaster_data()!=null &&matrimonyMasterRequestModel.getData().get(0).getMaster_data().size()>0) {
                    fragmentWeakReference.get().saveMasterData(matrimonyMasterRequestModel.getData().get(0).getMaster_data());
                }
            }
        }

    }

    @Override
    public void UserRegistrationDataRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().profileCreatedSuccessfully(response);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);
        fragmentWeakReference.get().runOnUiThread(() -> Util.showToast(
                fragmentWeakReference.get().getResources().getString(R.string.image_upload_success),fragmentWeakReference.get()));
        fragmentWeakReference.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                fragmentWeakReference.get().imageUploadedSuccessfully(url,formName);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onFailureListener(String message) {
        fragmentWeakReference.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(VolleyError error) {
        fragmentWeakReference.get().hideProgressBar();
    }

    public void uploadProfileImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        //formFragment.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName);
                return null;
            }
        }.execute();

    }
}
