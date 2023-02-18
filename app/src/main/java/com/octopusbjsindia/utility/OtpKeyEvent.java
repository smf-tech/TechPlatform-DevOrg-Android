package com.octopusbjsindia.utility;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.octopusbjsindia.R;

public class OtpKeyEvent implements View.OnKeyListener {

    private TextInputEditText etPrev;
    private TextInputEditText etCurrent;

    public OtpKeyEvent(TextInputEditText etCurrent, TextInputEditText etPrev) {
        this.etPrev = etPrev;
        this.etCurrent = etCurrent;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
            if (etCurrent.getId() != R.id.otp_1 && TextUtils.isEmpty(etCurrent.getText())) {
                //If current is empty then previous EditText's number will also be deleted
                etPrev.setText(null);
                etPrev.requestFocus();
                return true;
            }

            if (!TextUtils.isEmpty(etCurrent.getText())) {
                etCurrent.setText(null);
            }

        }


        return false;
    }



}
