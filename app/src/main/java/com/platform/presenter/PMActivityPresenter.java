package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.pm.Processes;
import com.platform.request.PMRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.PMActivity;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class PMActivityPresenter implements PlatformRequestCallListener {

    private final String TAG = PMActivityPresenter.class.getName();
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
        pmActivity.get().hideProgressBar();
        Processes data = new Gson().fromJson(response, Processes.class);
        pmActivity.get().showNextScreen(data);
    }

    @Override
    public void onFailureListener(String message) {
        pmActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        pmActivity.get().hideProgressBar();
        Log.e(TAG, "onErrorListener :" + error);
        Util.showToast(error.getMessage(), pmActivity.get().getBaseContext());
    }
}
