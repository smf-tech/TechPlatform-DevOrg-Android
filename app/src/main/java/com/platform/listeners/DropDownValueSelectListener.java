package com.platform.listeners;

import com.platform.models.forms.Elements;

public interface DropDownValueSelectListener {
    void onDropdownValueSelected(Elements formData, String value, String formId);

    void onEmptyDropdownSelected(Elements formData);
}
