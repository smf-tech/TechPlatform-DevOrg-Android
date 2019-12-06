package com.octopus.listeners;

import com.android.volley.VolleyError;

public interface ProfileDetailRequestCallListener {
    //void onFilterListRequestsFetched(String response);

    //void onRequestStatusChanged(String response, PendingRequest pendingRequest);
    void onRequestStatusChanged(String response, int position ,String requestType);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
