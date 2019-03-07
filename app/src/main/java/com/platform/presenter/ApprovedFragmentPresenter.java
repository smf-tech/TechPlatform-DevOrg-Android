package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.TMApprovedRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.request.TMRequestCall;
import com.platform.view.fragments.TMUserApprovedFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class ApprovedFragmentPresenter implements TMApprovedRequestCallListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<TMUserApprovedFragment> fragmentWeakReference;

    public ApprovedFragmentPresenter(TMUserApprovedFragment tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
    }

    public void getAllApprovedRequests() {
        TMRequestCall requestCall = new TMRequestCall();

        fragmentWeakReference.get().showProgressBar();
        requestCall.getAllPendingRequests();
    }

    @Override
    public void onApprovedRequestsFetched(String response) {

    }

    @Override
    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {

    }

    @Override
    public void onFailureListener(String message) {

    }

    @Override
    public void onErrorListener(VolleyError error) {

    }

//    @Override
//    public void onPendingRequestsFetched(String response) {
//        fragmentWeakReference.get().hideProgressBar();
//        if (!TextUtils.isEmpty(response)) {
//            PendingRequestsResponse pendingRequestsResponse
//                    = new Gson().fromJson(response, PendingRequestsResponse.class);
//            if (pendingRequestsResponse != null && pendingRequestsResponse.getData() != null
//                    && !pendingRequestsResponse.getData().isEmpty()
//                    && pendingRequestsResponse.getData().size() > 0) {
//                fragmentWeakReference.get().showPendingRequests(pendingRequestsResponse.getData());
//            }
//        }
//    }
//
//    @Override
//    public void onRequestStatusChanged(String response, PendingRequest pendingRequest) {
//        fragmentWeakReference.get().hideProgressBar();
//        if (!TextUtils.isEmpty(response)) {
//            fragmentWeakReference.get().updateRequestStatus(response, pendingRequest);
//        }
//    }
//
//    @Override
//    public void onFailureListener(String message) {
//        fragmentWeakReference.get().hideProgressBar();
//        if (!TextUtils.isEmpty(message)) {
//            Log.e(TAG, "onFailureListener :" + message);
//        }
//    }
//
//    @SuppressWarnings("ThrowableNotThrown")
//    @Override
//    public void onErrorListener(VolleyError volleyError) {
//
//        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//            String message = error.getMessage();
//            Log.i(TAG, "Error: " + message);
//        }
//
//        fragmentWeakReference.get().hideProgressBar();
//    }
}
