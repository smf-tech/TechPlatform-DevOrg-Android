package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.LocaleData;

@SuppressWarnings("unused")
public class Validator {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private LocaleData text;
    @SerializedName("minValue")
    @Expose
    private Integer minValue;
    @SerializedName("maxValue")
    @Expose
    private Integer maxValue;
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

    public Integer getMinValue() {
        return minValue;
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
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
}
