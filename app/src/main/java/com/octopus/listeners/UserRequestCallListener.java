package com.octopus.listeners;

import com.android.volley.VolleyError;

public interface UserRequestCallListener {

    void onSuccessListener(String response);

    void onUserProfileSuccessListener(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
