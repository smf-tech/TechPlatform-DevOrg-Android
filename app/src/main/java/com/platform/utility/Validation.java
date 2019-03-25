package com.platform.utility;

import android.content.Context;
import android.text.TextUtils;

import com.platform.R;
import com.platform.models.forms.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

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

    public static String expressionValidation(String fieldName, String field1Value, String field2Value, String inputType, Validator validator, final Context context) {
        if (validator != null && !TextUtils.isEmpty(validator.getExpression())) {
            switch (inputType) {
                case Constants.FormInputType.INPUT_TYPE_DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORM_DATE, Locale.US);

                    try {
                        Date date1 = sdf.parse(field1Value);
                        Date date2 = sdf.parse(field2Value);

                        if (validator.getExpression().contains(Constants.Expression.GREATER_THAN_EQUALS) && date1.compareTo(date2) < 0) {
                            if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                                return validator.getText().getLocaleValue();
                            } else {
                                return fieldName + SPACE + context.getString(R.string.not_in_proper_format);
                            }
                        } else if (validator.getExpression().contains(Constants.Expression.LESS_THAN_EQUALS) && date1.compareTo(date2) > 0) {
                            if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                                return validator.getText().getLocaleValue();
                            } else {
                                return fieldName + SPACE + context.getString(R.string.not_in_proper_format);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    break;
                case Constants.FormInputType.INPUT_TYPE_TIME:
                    break;
            }
        }

        return "";
    }

    public static String regexValidation(String fieldName, String fieldValue, Validator validator, final Context context) {
        if (validator != null && !TextUtils.isEmpty(validator.getRegex())) {
            if (!Pattern.compile(validator.getRegex()).matcher(fieldValue).matches()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + SPACE + context.getString(R.string.not_in_proper_format);
                }
            }
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
