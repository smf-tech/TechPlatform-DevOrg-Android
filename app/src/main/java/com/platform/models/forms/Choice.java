package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.LocaleData;

@SuppressWarnings("unused")
public class Choice {
    @SerializedName("text")
    @Expose
    private LocaleData text;
    @SerializedName("value")
    @Expose
    private String value;

    public LocaleData getText() {
        return text;
    }

    public void setText(LocaleData text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
