package com.octopusbjsindia.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
public class Choice implements Serializable {
    @SerializedName("text")
    @Expose
    private LocaleData text;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;

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

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Choice)) {
            return false;
        }
        Choice choice = (Choice) o;
        return Objects.equals(value, choice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
