package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.tm.PendingRequest;

public interface TMRejectedRequestCallListener {

    void onRejectedRequestsFetched(String response);

    void onRequestStatusChanged(String response, PendingRequest pendingRequest);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
