package com.platform.utility;

import android.text.TextUtils;

import com.platform.models.forms.Validator;

public class Validation {

    public static String editTextMinMaxValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator.getMinValue() != null && (fieldValue.length() < validator.getMinValue())) {
            return fieldName + " should not be less than " + validator.getMinValue();
        } else if (validator.getMinLength() != null && (fieldValue.length() < validator.getMinLength())) {
            return fieldName + " should not be less than " + validator.getMinLength();
        }
        return "";
    }

    public static String requiredValidation(String fieldName, String fieldValue, boolean isRequired) {
        if (isRequired && TextUtils.isEmpty(fieldValue)) {
            return fieldName + " can't be empty";
        }

        return "";
    }
}
