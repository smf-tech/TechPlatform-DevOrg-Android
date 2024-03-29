package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;

public interface PresenterListener {
    void onFailureListener(String requestID, String message);

    void onErrorListener(String requestID, VolleyError error);

    void onSuccessListener(String requestID, String response);
}
