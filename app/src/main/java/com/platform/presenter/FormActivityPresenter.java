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

public class FormActivityPresenter implements FormRequestCallListener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Gson gson;
    @SuppressWarnings({"FieldCanBeLocal", "CanBeFinal", "unused"})
    private WeakReference<FormActivity> formActivity;

    public FormActivityPresenter(FormActivity activity) {
        this.formActivity = new WeakReference<>(activity);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void createForm(String formId, HashMap<String, String> requestObjectMap) {
        FormRequestCall formRequestCall = new FormRequestCall();
        formRequestCall.setListener(this);

        formRequestCall.createFormResponse(formId, requestObjectMap);
    }

    @Override
    public void onFailureListener(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e("TAG", "Request failed :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {

    }

    @Override
    public void onFormCreated(String message) {
        Log.e("TAG", "Request succeed " + message);
    }
}
