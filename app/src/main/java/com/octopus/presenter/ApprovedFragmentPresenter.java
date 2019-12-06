package com.octopus.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.Platform;
import com.octopus.R;
import com.octopus.listeners.TMApprovedRequestCallListener;
import com.octopus.models.tm.PendingRequest;
import com.octopus.models.tm.PendingRequestsResponse;
import com.octopus.request.TMApprovedRequestCall;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;
import com.octopus.view.fragments.TMUserApprovedFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ApprovedFragmentPresenter implements TMApprovedRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserApprovedFragment> fragmentWeakReference;

    public ApprovedFragmentPresenter(TMUserApprovedFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllApprovedRequests() {
        TMApprovedRequestCall requestCall = new TMApprovedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllApprovedRequests();
    }

    @Override
    public void onApprovedRequestsFetched(String response) {
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
        TMApprovedRequestCall requestCall = new TMApprovedRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.approveRejectRequest(requestStatus, pendingRequest);
    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

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
