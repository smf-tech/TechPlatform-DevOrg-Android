package com.platform.listeners;

import java.util.HashMap;
import java.util.List;

public interface MatrixDynamicValueChangeListener {
    void onValueChanged(String elementName, List<HashMap<String, String>> matrixDynamicValuesList);
}
