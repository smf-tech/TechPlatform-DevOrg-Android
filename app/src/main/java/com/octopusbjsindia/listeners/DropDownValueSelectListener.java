package com.octopusbjsindia.listeners;

import com.octopusbjsindia.models.forms.Elements;

public interface DropDownValueSelectListener {
    void onDropdownValueSelected(Elements formData, String value, String formId);

    void onEmptyDropdownSelected(Elements formData);
}
