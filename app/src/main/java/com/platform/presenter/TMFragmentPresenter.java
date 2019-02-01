package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.TMRequestCallListener;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMRequestCall;
import com.platform.view.fragments.TMFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class TMFragmentPresenter implements TMRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMFragment> fragmentWeakReference;

    public TMFragmentPresenter(TMFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests() {
        TMRequestCall requestCall = new TMRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    @Override
    public void onPendingRequestsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            PendingRequestsResponse pendingRequestsResponse
                    = new Gson().fromJson(response, PendingRequestsResponse.class);
            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
                    && !pendingRequestsResponse.getData().isEmpty()
                    && pendingRequestsResponse.getData().size() > 0) {
                fragmentWeakReference.get().showPendingRequests(pendingRequestsResponse.getData());
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail: " + message);
        fragmentWeakReference.get().hideProgressBar();
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Override
    public void onErrorListener(VolleyError volleyError) {

        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            String message = error.getMessage();
            Log.i(TAG, "Error: " + message);
        }

        fragmentWeakReference.get().hideProgressBar();
    }
}
