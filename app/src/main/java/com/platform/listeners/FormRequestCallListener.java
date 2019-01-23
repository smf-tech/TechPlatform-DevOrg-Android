package com.platform.listeners;

import com.android.volley.VolleyError;

public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreated(String message);

    void onSubmitClick();
}
