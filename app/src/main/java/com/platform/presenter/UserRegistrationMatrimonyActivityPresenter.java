package com.platform.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.listeners.MatrimonyMasterDataRequestCallListener;
import com.platform.models.Matrimony.MatrimonyMasterRequestModel;
import com.platform.models.Matrimony.MatrimonyUserRegRequestModel;
import com.platform.models.tm.PendingRequest;
import com.platform.request.ImageRequestCall;
import com.platform.request.MatrimonyMasterDataRequestCall;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;

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

        //fragmentWeakReference.get().showProgressBar();
        requestCall.getAllMasterDataRequests();
    }
    public void UserRegistrationRequests(MatrimonyUserRegRequestModel matrimonyUserRegRequestModel) {
        MatrimonyMasterDataRequestCall requestCall = new MatrimonyMasterDataRequestCall();
        requestCall.setListener(this);

        //fragmentWeakReference.get().showProgressBar();
        requestCall.submitUserRegistrationDataRequests(matrimonyUserRegRequestModel);
    }

    @Override
    public void MatrimonyMasterDataRequestsFetched(String response) {
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
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().profileCreatedSuccessfully(response);
        }
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        //fragmentWeakReference.get().
    }

    @Override
    public void onFailureListener(String message) {

    }

    @Override
    public void onErrorListener(VolleyError error) {

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
