package com.platform.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.utility.Constants;
import com.platform.utility.Util;

public class LocaleData {

    @SerializedName("default")
    @Expose
    private String en;
    @SerializedName("mr")
    @Expose
    private String mr;
    @SerializedName("hi")
    @Expose
    private String hi;

    String getEn() {
        return en;
    }

    String getMr() {
        return mr;
    }

    String getHi() {
        return hi;
    }

    public String getLocaleValue() {
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            return mr;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            return hi;
        } else {
            return en;
        }
    }

    public void setLocaleValue(String name) {
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            mr = name;
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            hi = name;
        } else {
            en = name;
        }
    }
}
