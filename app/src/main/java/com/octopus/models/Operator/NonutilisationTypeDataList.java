package com.octopus.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NonutilisationTypeDataList {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("value")
    @Expose
    private String value;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
