package com.octopusbjsindia.models.forms;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinRateDescription {

    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("de")
    @Expose
    private String de;

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

}