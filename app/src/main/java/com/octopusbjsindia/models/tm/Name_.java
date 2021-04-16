package com.octopusbjsindia.models.tm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Name_ implements Serializable {

    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("hi")
    @Expose
    private String hi;
    @SerializedName("mr")
    @Expose
    private String mr;

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

}