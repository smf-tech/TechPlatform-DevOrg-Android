package com.platform.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.utility.Constants;
import com.platform.utility.Util;

public class LocaleData {

    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("mr")
    @Expose
    private String mr;
    @SerializedName("hi")
    @Expose
    private String hi;

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

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

    public String getLocaleValue() {
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            return mr;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            return hi;
        } else {
            return _default;
        }
    }

    public void setLocaleValue(String name) {
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            mr = name;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            hi = name;
        } else {
            _default = name;
        }
    }
}
