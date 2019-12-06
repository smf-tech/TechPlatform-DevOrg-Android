package com.octopus.listeners;

import com.octopus.models.forms.Elements;

public interface DropDownValueSelectListener {
    void onDropdownValueSelected(Elements formData, String value, String formId);

    void onEmptyDropdownSelected(Elements formData);
}
