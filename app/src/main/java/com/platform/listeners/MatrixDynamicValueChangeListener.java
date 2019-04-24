package com.platform.listeners;

import com.platform.models.forms.Column;

import java.util.HashMap;
import java.util.List;

public interface MatrixDynamicValueChangeListener {
    void onMatrixDynamicValueChanged(String elementName, List<HashMap<String, String>> matrixDynamicValuesList);

    void showChoicesByUrlOffline(String response, Column column, long rowIndex, HashMap<String, String> matrixDynamicInnerMap);
}
