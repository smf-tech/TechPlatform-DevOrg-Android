package com.platform.utility;

import android.text.TextUtils;

import com.platform.models.forms.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Validation {

    public static String editTextMinMaxValueValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator.getMinValue() != null) {
            double fieldIntValue = Double.parseDouble(fieldValue);
            if ((fieldIntValue <= validator.getMinValue())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " value should not be less than " + validator.getMinValue();
                }
            } else if (validator.getMaxValue() != null) {
                if (fieldIntValue >= validator.getMaxValue()) {
                    if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                        return validator.getText().getLocaleValue();
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
            if ((fieldValue.length() < validator.getMinLength())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " length should not be less than " + validator.getMinLength();
                }
            }
        } else if (validator.getMaxLength() != null) {
            if (fieldValue.length() > validator.getMaxLength()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " length should not be greater than " + validator.getMaxLength();
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

    public static String expressionValidation(String fieldName, String field1Value, String field2Value, String inputType, Validator validator) {
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
                                return fieldName + " not in proper format";
                            }
                        } else if (validator.getExpression().contains(Constants.Expression.LESS_THAN_EQUALS) && date1.compareTo(date2) > 0) {
                            if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                                return validator.getText().getLocaleValue();
                            } else {
                                return fieldName + " not in proper format";
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    break;

                case Constants.FormInputType.INPUT_TYPE_TIME:
                    break;

                case Constants.FormInputType.INPUT_TYPE_NUMBER:
                    double field1DoubleValue = Double.parseDouble(field1Value);
                    double field2DoubleValue = Double.parseDouble(field2Value);
                    if (validator.getExpression().contains(Constants.Expression.GREATER_THAN_EQUALS) && field1DoubleValue < field2DoubleValue) {
                        if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                            return validator.getText().getLocaleValue();
                        } else {
                            return fieldName + " not in proper format";
                        }
                    } else if (validator.getExpression().contains(Constants.Expression.LESS_THAN_EQUALS) && field1DoubleValue > field2DoubleValue) {
                        if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                            return validator.getText().getLocaleValue();
                        } else {
                            return fieldName + " not in proper format";
                        }
                    }
                    break;
            }
        }

        return "";
    }

    public static String regexValidation(String fieldName, String fieldValue, Validator validator) {
        if (validator != null && !TextUtils.isEmpty(validator.getRegex())) {
            if (!Pattern.compile(validator.getRegex()).matcher(fieldValue).matches()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " not in proper format";
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
