package com.platform.utility;

import android.text.TextUtils;

import com.platform.models.forms.Validator;

public class Validation {

    public static String editTextMinMaxValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator.getMinLength() != null && (fieldValue.length() < validator.getMinLength())) {
            return fieldName + " length should not be less than " + validator.getMinLength();
        }

        if (validator.getMinValue() != null) {
            long fieldIntValue = Long.parseLong(fieldValue);
            if ((fieldIntValue < validator.getMinValue())) {
                return fieldName + " value should not be less than " + validator.getMinValue();
            } else if (validator.getMaxValue() != null) {
                if ((fieldIntValue > validator.getMaxValue())) {
                    return fieldName + " value should not be greater than " + validator.getMaxValue();
                }
            }
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
