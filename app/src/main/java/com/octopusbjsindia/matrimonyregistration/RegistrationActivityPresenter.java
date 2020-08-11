package com.octopusbjsindia.matrimonyregistration;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.listeners.PresenterListener;
import com.octopusbjsindia.matrimonyregistration.model.MatrimonialProfile;
import com.octopusbjsindia.matrimonyregistration.model.ProfileDetailResponse;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivityPresenter implements PresenterListener, ImageRequestCallListener, APIPresenterListener {
    private final String TAG = this.getClass().getName();
    private WeakReference<RegistrationActivity> mContax;

    public RegistrationActivityPresenter(RegistrationActivity tmFragment) {
        mContax = new WeakReference<>(tmFragment);
    }

    public void getMatrimonyMaster() {

        mContax.get().showProgressBar();

        final String url = BuildConfig.BASE_URL + String.format(Urls.Matrimony.MATRIMONY_MASTER);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("MASTER_DATA_LIST", url);

    }

    public void getProfile(String id, String meetId) {
        mContax.get().showProgressBar();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        if (!TextUtils.isEmpty(meetId) && meetId.length() > 0) {
            params.put("meet_id", meetId);
        }
        String bodyParams = new Gson().toJson(params);
        final String url = BuildConfig.BASE_URL + String.format(Urls.Matrimony.GET_PROFILE_DETAILS);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.postDataApiCall("GET_PROFILE_DETAILS", bodyParams, url);
    }


    /*public void getBusinessMaster() {
        mContax.get().showProgressBar();
        final String url = BuildConfig.BASE_URL + String.format(URLs.Matrimonial.BUSINESS_MASTER);
        RequestCall requestCall = new RequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getApiCall("BUSINESS_MASTER", url);
    }*/

    public void UserRegistrationRequests(MatrimonialProfile matrimonyUserRegRequestModel) {
        //final String url = BuildConfig.BASE_URL + String.format(Urls.Matrimony.USER_REGI_SUBMIT_API);
        final String url = BuildConfig.BASE_URL + String.format(Urls.Matrimony.USER_UPDATE_SUBMIT_API);
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        Gson gson = new Gson();
        String paramJson = gson.toJson(matrimonyUserRegRequestModel);
        requestCall.postDataApiCall("USER_REGI", paramJson, url);
    }


    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);
        mContax.get().runOnUiThread(() -> Util.showToast(mContax.get(),
                "Image uploaded successfully"));
        //mContax.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);
                mContax.get().imageUploadedSuccessfully(url, formName);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onFailureListener(String error) {

    }


    public void uploadProfileImage(File file, String type, final String formName) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mContax.get().showProgressBar();
            }

            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, formName, null, null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mContax.get().hideProgressBar();
            }
        }.execute();

    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContax.get().showMessage(requestID, message, 300);
        mContax.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContax.get().showMessage(requestID, error.getMessage(), 400);
        mContax.get().hideProgressBar();
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContax.get().hideProgressBar();
        Gson gson = new Gson();
        switch (requestID) {
            case "MASTER_DATA_LIST":
                if (!TextUtils.isEmpty(response)) {
                    MatrimonyMasterRequestModel matrimonyMasterRequestModel
                            = new Gson().fromJson(response, MatrimonyMasterRequestModel.class);
                    if (matrimonyMasterRequestModel != null && matrimonyMasterRequestModel.getData() != null
                            && !matrimonyMasterRequestModel.getData().isEmpty()
                            && matrimonyMasterRequestModel.getData().size() > 0) {
                        if (matrimonyMasterRequestModel.getData().get(0).getMaster_data() != null && matrimonyMasterRequestModel.getData().get(0).getMaster_data().size() > 0) {
                            mContax.get().saveMasterData(matrimonyMasterRequestModel.getData().get(0).getMaster_data());
                        }
                    }
                }
                break;

            case "USER_REGI":
                if (!TextUtils.isEmpty(response)) {
                    mContax.get().profileCreatedSuccessfully(response);
                }
                break;
            case "GET_PROFILE_DETAILS":
                if (requestID.equalsIgnoreCase("GET_PROFILE_DETAILS")) {
                    ProfileDetailResponse profileResponse = gson.fromJson(response, ProfileDetailResponse.class);
                    mContax.get().initView(profileResponse.getData());
                }
                break;


        }
    }


}
