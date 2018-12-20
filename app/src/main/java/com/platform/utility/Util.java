package com.platform.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static void setError(final EditText inputEditText, String errorMessage) {
        final int padding = 10;
        inputEditText.setCompoundDrawablePadding(padding);
        inputEditText.setError(errorMessage);
        inputEditText.requestFocus();

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static Map<String, String> requestHeader() {
        Map<String, String> headers = new HashMap<>();
//        headers.put("ClientId", BuildConfig.CLIENT_ID);
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return headers;
    }
}
