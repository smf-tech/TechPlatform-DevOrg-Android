package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.BuildConfig;
import com.platform.listeners.APIPresenterListener;
import com.platform.request.APIRequestCall;
import com.platform.utility.Urls;
import com.platform.view.activities.MatrimonyBookletActivity;

import java.lang.ref.WeakReference;

public class MatrimonyBookletActivityPresenter implements APIPresenterListener {

    private static final String DOWNLOAD_MEET_BOOKLET = "DownloadMeetBooklet";
    private WeakReference<MatrimonyBookletActivity> fragmentWeakReference;
    private final String TAG = MatrimonyBookletActivityPresenter.class.getName();

    public MatrimonyBookletActivityPresenter(MatrimonyBookletActivity tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void clearData() {
        fragmentWeakReference = null;
    }

    public void downloadBooklet(String meetId, String gridTypeId){
        final String downloadMeetBookletUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.DOWNLOAD_MEET_BOOKLET, meetId, gridTypeId);
        Log.d(TAG, "downloadMeetBookletUrl: url" + downloadMeetBookletUrl);
        fragmentWeakReference.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(DOWNLOAD_MEET_BOOKLET, downloadMeetBookletUrl);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().onErrorListener(requestID,error);
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
                if (requestID.equalsIgnoreCase(MatrimonyBookletActivityPresenter.DOWNLOAD_MEET_BOOKLET)) {
                    fragmentWeakReference.get().onSuccessListener(requestID, response);
                }
            }
        } catch (Exception e) {
            fragmentWeakReference.get().onFailureListener(requestID,e.getMessage());
        }
    }
}
