package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryName {

    @SerializedName("default")
    @Expose
    private String _default;

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

}
