package com.platform.listeners;

import com.platform.models.forms.Column;

import java.util.HashMap;

public interface MatrixDynamicDropDownValueSelectListener {
    void onDropdownValueSelected(HashMap<String, String> matrixDynamicInnerMap, Column formData,
                                 String value, String formId);

    void onEmptyDropdownSelected(HashMap<String, String> matrixDynamicInnerMap, Column formData);
}
