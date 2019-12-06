package com.octopus.listeners;

import com.android.volley.VolleyError;
import com.octopus.models.tm.PendingRequest;

public interface TMUserProfileApprovalRequestCallListener {
    void TMUserProfileApprovalRequestsFetched(String response);

    void onRequestStatusChanged(String response, PendingRequest pendingRequest);

    void onRequestStatusChanged(String response, int position);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
