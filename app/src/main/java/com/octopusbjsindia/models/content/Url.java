
package com.octopusbjsindia.models.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Url {

    @SerializedName("mr")
    @Expose
    private String mr;
    @SerializedName("hi")
    @Expose
    private String hi;
    @SerializedName("default")
    @Expose
    private String _default;

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

}
