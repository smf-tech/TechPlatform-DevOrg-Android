package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;

public interface PlatformRequestCallListener {

    void onSuccessListener(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
