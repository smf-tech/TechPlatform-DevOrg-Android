package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlatformRequestCallListener;
import com.octopusbjsindia.request.PMRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ProcessListActivity;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ProcessListActivityPresenter implements PlatformRequestCallListener {

    private final String TAG = ProcessListActivityPresenter.class.getName();
    private WeakReference<ProcessListActivity> processListActivity;

    public ProcessListActivityPresenter(ProcessListActivity activity) {
        processListActivity = new WeakReference<>(activity);
    }

    public void getProcessList() {
        PMRequestCall requestCall = new PMRequestCall();
        requestCall.setListener(this);

        processListActivity.get().showProgressBar();
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
        if (processListActivity != null && processListActivity.get() != null) {
            processListActivity.get().hideProgressBar();
            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, processListActivity);
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    processListActivity);
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                processListActivity);
                    }
                } else {
                    processListActivity.get().showErrorMessage(error.getLocalizedMessage());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
    }
}
