package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.tm.PendingRequest;

@SuppressWarnings("EmptyMethod")
public interface TMRejectedRequestCallListener {

    void onRejectedRequestsFetched(String response);

    @SuppressWarnings("unused")
    void onRequestStatusChanged(String response, PendingRequest pendingRequest);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
