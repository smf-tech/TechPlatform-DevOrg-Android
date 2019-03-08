package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;

import androidx.annotation.Nullable;

public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreatedUpdated(String message, String requestObject, String formId,
                              String callType, @Nullable String oid);

    void onSuccessListener(String response);

    void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex, FormData formData);

    @SuppressWarnings("unused")
    void onSubmitClick(String submitType, String url, String formId, String oid);

    void onFormDetailsLoadedListener(String response);
}
