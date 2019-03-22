package com.platform.utility;

import android.content.Context;
import android.text.TextUtils;

import com.platform.R;
import com.platform.models.forms.Validator;

public class Validation {

    private static final String SPACE = " ";

    public static String editTextMinMaxValueValidation(String fieldName, String fieldValue, Validator validator, final Context context) {
        if (validator.getMinValue() != null) {
            double fieldIntValue = Double.parseDouble(fieldValue);
            if ((fieldIntValue <= validator.getMinValue())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + SPACE + context.getString(R.string.value_should_not_be_less_than) + SPACE + validator.getMinValue();
                }
            } else if (validator.getMaxValue() != null) {
                if (fieldIntValue >= validator.getMaxValue()) {
                    if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                        return validator.getText().getLocaleValue();
                    } else {
                        return fieldName + SPACE + context.getString(R.string.value_should_not_be_greater_than) + SPACE + validator.getMaxValue();
                    }
                }
            }
        }
        return "";
    }

    public static String editTextMinMaxLengthValidation(String fieldName, String fieldValue, Validator validator, final Context context) {
        if (validator.getMinLength() != null) {
            if ((fieldValue.length() < validator.getMinLength())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + SPACE + context.getString(R.string.length_should_not_be_less_than) + SPACE + validator.getMinLength();
                }
            }
        } else if (validator.getMaxLength() != null) {
            if (fieldValue.length() > validator.getMaxLength()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + SPACE + context.getString(R.string.length_should_not_be_greater_than) + SPACE + validator.getMaxLength();
                }
            }

        }
        return "";
    }

    public static String editTextMaxLengthValidation(String fieldName, String fieldValue, Integer maxLength, final Context context) {
        if (maxLength != null && (fieldValue.length() < maxLength)) {
            return fieldName + SPACE + context.getString(R.string.length_should_not_be_less_than) + SPACE + maxLength;
        }
        return "";
    }

    public static String requiredValidation(String fieldName, String fieldValue, boolean isRequired, final Context context) {
        if (isRequired && TextUtils.isEmpty(fieldValue)) {
            return fieldName + SPACE + context.getString(R.string.cant_be_empty);
        }

        return "";
    }
}
