package com.platform.listeners;

import com.android.volley.VolleyError;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreatedUpdated(String message, String requestObject, String formId,
                              String callType, @Nullable String oid);

    void onSuccessListener(String response);

    void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex, int columnIndex, FormData formData);

    @SuppressWarnings("unused")
    void onSubmitClick(String submitType, String url, String formId, String oid, final List<Map<String, String>> imageUrlList);

    void onFormDetailsLoadedListener(String response);
}
