package com.octopusbjsindia.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class OtpTextWatcher implements TextWatcher {
    private TextInputEditText etPrev;
    private TextInputEditText etNext;
    private TextView errorText;

    public OtpTextWatcher(TextInputEditText etNext, TextInputEditText etPrev, TextView error) {
        this.etPrev = etPrev;
        this.etNext = etNext;
        this.errorText = error;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (text.length() == 1) {
            etNext.requestFocus();
        } /*else if (text.length() == 0)
            etPrev.requestFocus();*/

        if (errorText.getVisibility() == View.VISIBLE) errorText.setVisibility(View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }


}
