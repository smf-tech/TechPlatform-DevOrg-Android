package com.platform.utility;

import android.text.TextUtils;

import com.platform.models.forms.Validator;

public class Validation {
    public static String editTextValidation(String fieldName, String fieldValue, boolean isRequired, Validator validator) {
        if (isRequired && TextUtils.isEmpty(fieldValue)) {
            return fieldName + " can't be empty";
        }
        if (validator.getMinValue() != null && (fieldValue.length() < validator.getMinValue())) {
            return fieldName + " should not be less than " + validator.getMinValue();
        } else if (validator.getMinLength() != null && (fieldValue.length() < validator.getMinLength())) {
            return fieldName + " should not be less than " + validator.getMinLength();
        }
        return "";
    }
}
