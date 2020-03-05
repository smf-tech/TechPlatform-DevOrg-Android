package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.forms.Components;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.view.activities.FormDisplayActivity;

import java.lang.ref.WeakReference;

public class FormDisplayActivityPresenter implements APIPresenterListener {
    private WeakReference<FormDisplayActivity> fragmentWeakReference;
    private final String TAG = FormDisplayActivity.class.getName();
    public static final String GET_FORM_SCHEMA = "getFormSchema";

    public FormDisplayActivityPresenter(FormDisplayActivity tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void getFormSchema() {
        fragmentWeakReference.get().showProgressBar();
        final String getFormSchemaUrl = "http://api.dxsurvey.com/api/Survey/getSurvey?surveyId=1ae0ed89-adec-43e7-a8bd-eef3912d2afc";
        Log.d(TAG, "getFormSchemaUrl: url" + getFormSchemaUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_FORM_SCHEMA, getFormSchemaUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID, message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID, error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (fragmentWeakReference == null) {
            return;
        }
        fragmentWeakReference.get().hideProgressBar();
        try {
            if (response != null) {
                if (requestID.equalsIgnoreCase(FormDisplayActivityPresenter.GET_FORM_SCHEMA)) {
                    Components components = PlatformGson.getPlatformGsonInstance().fromJson(response,
                            Components.class);
                    //fragmentWeakReference.get().parseFormSchema(components);
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID, e.getMessage());
        }
    }
}
