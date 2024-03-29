package com.octopusbjsindia.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

public class MatrixChoice {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
