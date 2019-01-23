package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.listeners.FormRequestCallListener;
import com.platform.request.FormRequestCall;
import com.platform.view.activities.FormActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
public class FormActivityPresenter implements FormRequestCallListener {

    private final Gson gson;
    private WeakReference<FormActivity> formActivity;
    private String formId;
    private HashMap<String, String> requestedObject;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    private HashMap<String, String> getRequestedObject() {
        return requestedObject;
    }

    public void setRequestedObject(HashMap<String, String> requestedObject) {
        this.requestedObject = requestedObject;
    }

    public FormActivityPresenter(FormActivity activity) {
        this.formActivity = new WeakReference<>(activity);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e("TAG", "Request failed :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.e("TAG", "Request Error :" + error);
    }

    @Override
    public void onFormCreated(String message) {
        Log.e("TAG", "Request succeed " + message);
    }

    @Override
    public void onSubmitClick() {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        formRequestCall.createFormResponse(getFormId(), getRequestedObject());
    }
}
