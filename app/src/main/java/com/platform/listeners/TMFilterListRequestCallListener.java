package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.tm.PendingRequest;

public interface TMFilterListRequestCallListener {
    void onFilterListRequestsFetched(String response);

    void onRequestStatusChanged(String response, PendingRequest pendingRequest);
    void onRequestStatusChanged(String response, int position);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
