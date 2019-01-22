package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.listeners.FormRequestCallListener;
import com.platform.request.FormRequestCall;
import com.platform.view.fragments.FormFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener {

    private final String TAG = FormActivityPresenter.class.getName();
    private final Gson gson;
    private WeakReference<FormFragment> formFragment;

    public FormActivityPresenter(FormFragment fragment) {
        this.formFragment = new WeakReference<>(fragment);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void createForm(String formId, HashMap<String, String> requestObjectMap) {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        formRequestCall.createFormResponse(formId, requestObjectMap);
    }

    public void getProcessDetails(String processId) {
        FormRequestCall requestCall = new FormRequestCall();
        requestCall.setListener(this);

        formFragment.get().showProgressBar();
        requestCall.getProcessDetails(processId);
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "Request failed :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e(TAG, "Request Error :" + error);
    }

    @Override
    public void onFormCreated(String message) {
        Log.e(TAG, "Request succeed " + message);
    }

    @Override
    public void onSuccessListener(String response) {
        Log.e(TAG, "Process Details " + response);

        formFragment.get().hideProgressBar();
        formFragment.get().showNextScreen(response);
    }
}
