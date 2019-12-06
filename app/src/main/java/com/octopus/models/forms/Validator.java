package com.octopus.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopus.models.LocaleData;

@SuppressWarnings("unused")
public class Validator {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private LocaleData text;
    @SerializedName("expression")
    @Expose
    private String expression;
    @SerializedName("regex")
    @Expose
    private String regex;
    @SerializedName("minValue")
    @Expose
    private Double minValue;
    @SerializedName("maxValue")
    @Expose
    private Double maxValue;
    @SerializedName("minLength")
    @Expose
    private Integer minLength;
    @SerializedName("maxLength")
    @Expose
    private Integer maxLength;
    @SerializedName("allowDigits")
    @Expose
    private Boolean isDigitAllowed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocaleData getText() {
        return text;
    }

    public void setText(LocaleData text) {
        this.text = text;
    }

    public Boolean getDigitAllowed() {
        return isDigitAllowed;
    }

    public void setDigitAllowed(Boolean digitAllowed) {
        isDigitAllowed = digitAllowed;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
