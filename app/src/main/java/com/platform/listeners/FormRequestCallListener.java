package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;

public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreatedUpdated(String message);

    void onSuccessListener(String response);

    void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex, FormData formData);

    @SuppressWarnings("unused")
    void onSubmitClick(String submitType);

    void onFormDetailsLoadedListener(String response);
}
