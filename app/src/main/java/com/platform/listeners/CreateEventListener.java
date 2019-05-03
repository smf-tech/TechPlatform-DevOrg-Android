package com.platform.listeners;

import com.android.volley.VolleyError;

public interface CreateEventListener {

    void onCategoryFetched(String response);

    void onEventsFetched(String response);

    void onEventSubmitted(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);
}
