package com.octopusbjsindia.listeners;

import com.android.volley.VolleyError;

public interface FormStatusCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormsLoaded(String response);

    void onMastersFormsLoaded(String response, String formId);

}
