package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.request.PMRequestCall;
import com.platform.view.activities.ProcessListActivity;

import java.lang.ref.WeakReference;

public class ProcessListActivityPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProcessListActivityPresenter.class.getName();
    @SuppressWarnings("CanBeFinal")
    private WeakReference<ProcessListActivity> processListActivity;

    public ProcessListActivityPresenter(ProcessListActivity activity) {
        processListActivity = new WeakReference<>(activity);
    }

    public void getProcessDetails(String processId) {
        PMRequestCall requestCall = new PMRequestCall();
        requestCall.setListener(this);

        processListActivity.get().showProgressBar();
        requestCall.getProcessDetails(processId);
    }

    @Override
    public void onSuccessListener(String response) {
        Log.i(TAG, "Success: " + response);
        processListActivity.get().hideProgressBar();

        processListActivity.get().showNextScreen(response);
    }

    @Override
    public void onFailureListener(String message) {

    }

    @Override
    public void onErrorListener(VolleyError error) {

    }
}
