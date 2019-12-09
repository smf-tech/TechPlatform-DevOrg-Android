package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;
import com.octopusbjsindia.models.tm.PendingRequest;

public interface TMApprovedRequestCallListener {

    void onApprovedRequestsFetched(String response);

    @SuppressWarnings("EmptyMethod")
    void onRequestStatusChanged(@SuppressWarnings("unused") String response, @SuppressWarnings("unused") PendingRequest pendingRequest);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
