package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.tm.PendingRequest;

public interface ProfileDetailRequestCallListener {
    //void onFilterListRequestsFetched(String response);

    //void onRequestStatusChanged(String response, PendingRequest pendingRequest);
    void onRequestStatusChanged(String response, int position ,String requestType);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
