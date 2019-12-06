package com.octopus.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.Platform;
import com.octopus.R;
import com.octopus.listeners.PlatformRequestCallListener;
import com.octopus.models.reports.Reports;
import com.octopus.request.ReportsRequestCall;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;
import com.octopus.view.fragments.ReportsFragment;

import java.lang.ref.WeakReference;

public class ReportsFragmentPresenter implements PlatformRequestCallListener {
    private final String TAG = ReportsFragmentPresenter.class.getName();
    private static WeakReference<ReportsFragment> fragmentWeakReference;

    public ReportsFragmentPresenter(ReportsFragment reportsFragment) {
        fragmentWeakReference = new WeakReference<>(reportsFragment);
    }

    public void getAllReports() {
        ReportsRequestCall requestCall = new ReportsRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllReports();
    }

    @Override
    public void onSuccessListener(String response) {
        fragmentWeakReference.get().hideProgressBar();

        Reports data = new Gson().fromJson(response, Reports.class);
        fragmentWeakReference.get().showNextScreen(data);
    }

    @Override
    public void onFailureListener(String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
        }
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();

            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, fragmentWeakReference.get().getActivity());
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    fragmentWeakReference.get().getActivity());
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                fragmentWeakReference.get().getActivity());
                    }
                } else {
                    Util.showToast(fragmentWeakReference.get().getString(R.string.unexpected_error_occurred),
                            fragmentWeakReference.get().getActivity());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
        Log.e(TAG, "onErrorListener :" + error);
    }
}
