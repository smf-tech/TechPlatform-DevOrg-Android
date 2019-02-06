package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.forms.Form;

import java.util.List;

public interface FormStatusCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormsLoaded(String response);

}
