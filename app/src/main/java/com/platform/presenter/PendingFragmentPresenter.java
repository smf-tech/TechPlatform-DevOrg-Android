package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.TMPendingRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMPendingRequestCall;
import com.platform.utility.Util;
import com.platform.view.fragments.TMUserPendingFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class PendingFragmentPresenter implements TMPendingRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserPendingFragment> fragmentWeakReference;

    public PendingFragmentPresenter(TMUserPendingFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllPendingRequests() {
        TMPendingRequestCall requestCall = new TMPendingRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMPendingRequestCall requestCall = new TMPendingRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
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
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
        }
    }

    @Override
    public void onFailureListener(String message) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, "onFailureListener :" + message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();

            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == 504) {
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
    }
}
