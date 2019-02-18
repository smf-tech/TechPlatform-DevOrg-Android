package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.forms.Elements;

public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreated(String message);

    void onSuccessListener(String response);

    void onChoicesPopulated(String response, Elements formData);

    @SuppressWarnings("unused")
    void onSubmitClick(String submitType);

    void onFormDetailsLoadedListener(String response);
}
