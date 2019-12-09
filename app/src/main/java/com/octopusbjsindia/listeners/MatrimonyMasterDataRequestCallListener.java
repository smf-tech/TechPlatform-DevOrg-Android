package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;
import com.octopusbjsindia.models.tm.PendingRequest;

public interface MatrimonyMasterDataRequestCallListener {
    void MatrimonyMasterDataRequestsFetched(String response);
    void UserRegistrationDataRequestsFetched(String response);

    void onRequestStatusChanged(String response, PendingRequest pendingRequest);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
