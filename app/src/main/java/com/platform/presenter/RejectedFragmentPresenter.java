package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.TMRejectedRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.models.tm.PendingRequestsResponse;
import com.platform.request.TMRejectedRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.TMUserRejectedFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class RejectedFragmentPresenter implements TMRejectedRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserRejectedFragment> fragmentWeakReference;

    public RejectedFragmentPresenter(TMUserRejectedFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllRejectedRequests() {
        TMRejectedRequestCall requestCall = new TMRejectedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllRejectedRequests();
    }

    @Override
    public void onRejectedRequestsFetched(String response) {
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

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        TMRejectedRequestCall requestCall = new TMRejectedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

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
    }
}
