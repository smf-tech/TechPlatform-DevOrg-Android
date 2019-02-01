package com.platform.listeners;

import com.android.volley.VolleyError;

public interface TMRequestCallListener {
    void onPendingRequestsFetched(String response);

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

}
