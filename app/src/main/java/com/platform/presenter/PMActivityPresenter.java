package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.pm.Processes;
import com.platform.request.PMRequestCall;
import com.platform.view.activities.PMActivity;

import java.lang.ref.WeakReference;

public class PMActivityPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProfileActivityPresenter.class.getName();
    @SuppressWarnings("CanBeFinal")
    private WeakReference<PMActivity> pmActivity;

    public PMActivityPresenter(PMActivity activity) {
        pmActivity = new WeakReference<>(activity);
    }

    public void getAllProcess() {
        PMRequestCall requestCall = new PMRequestCall();
        requestCall.setListener(this);

        pmActivity.get().showProgressBar();
        requestCall.getAllProcess();
    }

    @Override
    public void onSuccessListener(String response) {
        Log.i(TAG, "Success: " + response);
        pmActivity.get().hideProgressBar();
        Processes data = new Gson().fromJson(response, Processes.class);
        pmActivity.get().showNextScreen(data);
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail: " + message);
        pmActivity.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error: " + error);
        pmActivity.get().hideProgressBar();
    }
}
