package com.platform.listeners;

import com.android.volley.VolleyError;

public interface CreateEventListener {

    void onEventsFetched(String response);

    void onFormsFetched(String response);

    void onEventSubmitted(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
