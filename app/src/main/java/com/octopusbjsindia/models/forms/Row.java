package com.octopusbjsindia.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

import java.io.Serializable;

public class Row implements Serializable {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("text")
    @Expose
    private LocaleData text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public LocaleData getText() {
        return text;
    }

    public void setText(LocaleData text) {
        this.text = text;
    }
}
