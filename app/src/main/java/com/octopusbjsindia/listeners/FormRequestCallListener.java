package com.octopusbjsindia.listeners;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.octopusbjsindia.models.forms.Elements;
import com.octopusbjsindia.models.forms.FormData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public interface FormRequestCallListener {

    void onFailureListener(String message);

    void onErrorListener(VolleyError error);

    void onFormCreatedUpdated(String message, String requestObject, String formId,
                              String callType, @Nullable String oid);

    void onSuccessListener(String response);

    void onChoicesPopulated(String response, Elements elements, int pageIndex, int elementIndex,
                            int columnIndex, long rowIndex, FormData formData, HashMap<String,
            String> matrixDynamicInnerMap);

    void onSubmitClick(String submitType, String url, String formId, String oid, final List<Map<String,
            String>> imageUrlList);

    void onFormDetailsLoadedListener(String response);
}
