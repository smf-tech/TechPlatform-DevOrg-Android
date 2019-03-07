package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.tm.PendingRequest;

public interface TMApprovedRequestCallListener {

    void onApprovedRequestsFetched(String response);

    void onRequestStatusChanged(String response, PendingRequest pendingRequest);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
