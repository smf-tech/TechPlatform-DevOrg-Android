package com.platform.utility;

import android.text.TextUtils;

import com.platform.models.forms.Validator;

public class Validation {

    public static String editTextMinMaxValueValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator.getMinValue() != null) {
            double fieldIntValue = Double.parseDouble(fieldValue);
            if ((fieldIntValue <= validator.getMinValue())) {
                if (!TextUtils.isEmpty(validator.getText())) {
                    return validator.getText();
                } else {
                    return fieldName + " value should not be less than " + validator.getMinValue();
                }
            } else if (validator.getMaxValue() != null) {
                if (fieldIntValue >= validator.getMaxValue()) {
                    if (!TextUtils.isEmpty(validator.getText())) {
                        return validator.getText();
                    } else {
                        return fieldName + " value should not be greater than " + validator.getMaxValue();
                    }
                }
            }
        }
        return "";
    }

    public static String editTextMinMaxLengthValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator.getMinLength() != null) {
            if ((fieldValue.length() <= validator.getMinLength())) {
                if (!TextUtils.isEmpty(validator.getText())) {
                    return validator.getText();
                } else {
                    return fieldName + " length should not be less than " + validator.getMinLength();
                }
            } else if (validator.getMaxLength() != null) {
                if (fieldValue.length() >= validator.getMaxLength()) {
                    if (!TextUtils.isEmpty(validator.getText())) {
                        return validator.getText();
                    } else {
                        return fieldName + " value should not be greater than " + validator.getMaxLength();
                    }
                }
            }
        }
        return "";
    }

    public static String editTextMaxLengthValidation(String fieldName, String fieldValue, Integer maxLength) {
        if (maxLength != null && (fieldValue.length() < maxLength)) {
            return fieldName + " length should not be less than " + maxLength;
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
