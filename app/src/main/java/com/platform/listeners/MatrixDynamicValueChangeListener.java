package com.platform.listeners;

import com.platform.models.forms.Elements;

import java.util.HashMap;
import java.util.List;

public interface MatrixDynamicValueChangeListener {
    void onMatrixDynamicValueChanged(String elementName, List<HashMap<String, String>> matrixDynamicValuesList);

    void showChoicesByUrlOffline(String response, Elements elements);
}
