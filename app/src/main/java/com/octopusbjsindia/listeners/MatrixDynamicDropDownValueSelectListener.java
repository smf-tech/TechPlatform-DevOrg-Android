package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.forms.Column;

import java.util.HashMap;

@SuppressWarnings("unused")
public interface MatrixDynamicDropDownValueSelectListener {
    void onDropdownValueSelected(HashMap<String, String> matrixDynamicInnerMap, Column formData,
                                 String value, String formId);

    void onEmptyDropdownSelected(HashMap<String, String> matrixDynamicInnerMap, Column formData);
}
