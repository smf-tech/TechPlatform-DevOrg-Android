package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Validator {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("minLength")
    @Expose
    private int minLength;
    @SerializedName("maxLength")
    @Expose
    private int maxLength;
    @SerializedName("minValue")
    @Expose
    private int minValue;
    @SerializedName("maxValue")
    @Expose
    private int maxValue;
    @SerializedName("allowDigits")
    @Expose
    private Boolean isDigitAllowed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getDigitAllowed() {
        return isDigitAllowed;
    }

    public void setDigitAllowed(Boolean digitAllowed) {
        isDigitAllowed = digitAllowed;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
