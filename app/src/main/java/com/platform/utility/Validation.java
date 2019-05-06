package com.platform.utility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.platform.R;
import com.platform.models.forms.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Validation {

    public static String editTextMinMaxValueValidation(String fieldName, String fieldValue,
                                                       Validator validator, Context context) {

        if (validator.getMinValue() != null) {
            double fieldIntValue = Double.parseDouble(fieldValue);
            if ((fieldIntValue <= validator.getMinValue())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " " + context.getString(R.string.value_should_not_be_less_than)
                            + " " + validator.getMinValue();
                }
            } else if (validator.getMaxValue() != null) {
                if (fieldIntValue >= validator.getMaxValue()) {
                    if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                        return validator.getText().getLocaleValue();
                    } else {
                        return fieldName + " " + context.getString(R.string.value_should_not_be_greater_than)
                                + " " + validator.getMaxValue();
                    }
                }
            }
        }
        return "";
    }

    public static String editTextMinMaxLengthValidation(String fieldName, String fieldValue,
                                                        Validator validator, Context context) {

        if (validator.getMinLength() != null) {
            if ((fieldValue.length() < validator.getMinLength())) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " " + context.getString(R.string.length_should_not_be_less_than)
                            + " " + validator.getMinLength();
                }
            }
        } else if (validator.getMaxLength() != null) {
            if (fieldValue.length() > validator.getMaxLength()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " " + context.getString(R.string.length_should_not_be_greater_than)
                            + " " + validator.getMaxLength();
                }
            }

        }
        return "";
    }

    public static String editTextMaxLengthValidation(String fieldName, String fieldValue,
                                                     Integer maxLength, Context context) {

        if (maxLength != null && (fieldValue.length() < maxLength)) {
            return fieldName + " " + context.getString(R.string.length_should_not_be_less_than)
                    + " " + maxLength;
        }
        return "";
    }

    public static String expressionValidation(String fieldName, String field1Value, String field2Value,
                                              String field3Value, String inputType, Validator validator,
                                              Context context) {

        if (validator != null && !TextUtils.isEmpty(validator.getExpression()) && !TextUtils.isEmpty(inputType)) {
            switch (inputType) {
                case Constants.FormInputType.INPUT_TYPE_DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORM_DATE, Locale.getDefault());

                    try {
                        Date date1 = sdf.parse(field1Value);
                        Date date2 = sdf.parse(field2Value);

                        if (validator.getExpression().contains(Constants.Expression.GREATER_THAN_EQUALS)
                                && date1.compareTo(date2) < 0) {

                            if (validator.getText() != null &&
                                    !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                                return validator.getText().getLocaleValue();
                            } else {
                                return fieldName + " " + context.getString(R.string.no_proper_format);
                            }
                        } else if (validator.getExpression().contains(Constants.Expression.LESS_THAN_EQUALS)
                                && date1.compareTo(date2) > 0) {

                            if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                                return validator.getText().getLocaleValue();
                            } else {
                                return fieldName + " " + context.getString(R.string.no_proper_format);
                            }
                        }
                    } catch (ParseException e) {
                        Log.e("TAG", e.getMessage());
                    }

                    break;

                case Constants.FormInputType.INPUT_TYPE_TIME:
                    break;

                case Constants.FormInputType.INPUT_TYPE_NUMBER:
                    double field1DoubleValue = Double.parseDouble(field1Value);
                    double field2DoubleValue = Double.parseDouble(field2Value);
                    double field3DoubleValue = 0;

                    if (!TextUtils.isEmpty(field3Value)) {
                        field3DoubleValue = Double.parseDouble(field3Value);
                    }

                    if (validator.getExpression().contains(Constants.Expression.GREATER_THAN_EQUALS)
                            && field1DoubleValue < field2DoubleValue) {

                        if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                            return validator.getText().getLocaleValue();
                        } else {
                            return fieldName + " " + context.getString(R.string.no_proper_format);
                        }
                    } else if (validator.getExpression().contains(Constants.Expression.LESS_THAN_EQUALS)
                            && field1DoubleValue > field2DoubleValue) {

                        if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                            return validator.getText().getLocaleValue();
                        } else {
                            return fieldName + " " + context.getString(R.string.no_proper_format);
                        }
                    } else if (validator.getExpression().contains(Constants.Expression.EQUALS) &&
                            validator.getExpression().contains(Constants.Expression.SUBTRACTION) &&
                            !TextUtils.isEmpty(field3Value) &&
                            field1DoubleValue != field2DoubleValue - field3DoubleValue) {
                        if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                            return validator.getText().getLocaleValue();
                        } else {
                            return fieldName + " " + context.getString(R.string.no_proper_format);
                        }
                    }
                    break;
            }
        }

        return "";
    }

    public static String regexValidation(String fieldName, String fieldValue,
                                         Validator validator, Context context) {

        if (validator != null && !TextUtils.isEmpty(validator.getRegex())) {
            if (!Pattern.compile(validator.getRegex()).matcher(fieldValue).matches()) {
                if (validator.getText() != null && !TextUtils.isEmpty(validator.getText().getLocaleValue())) {
                    return validator.getText().getLocaleValue();
                } else {
                    return fieldName + " " + context.getString(R.string.cant_be_empty);
                }
            }
        }

        return "";
    }

    public static String requiredValidation(String fieldName, String fieldValue,
                                            boolean isRequired, Context context) {

        if (isRequired && TextUtils.isEmpty(fieldValue)) {
            return fieldName + " " + context.getString(R.string.cant_be_empty);
        }

        return "";
    }

    public static String matrixDynamicRequiredValidation(String fieldName, int columnsSize,
                                                         List<HashMap<String, String>> valuesList,
                                                         Context context) {

        if (valuesList != null && !valuesList.isEmpty()) {
            for (HashMap<String, String> valuesMap :
                    valuesList) {
                if (valuesMap.size() != columnsSize) {
                    return fieldName + " " + context.getString(R.string.cant_be_empty);
                }
            }
        } else {
            return fieldName + " " + context.getString(R.string.cant_be_empty);
        }

        return "";
    }
}
