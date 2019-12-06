package com.octopus.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;

import java.io.Serializable;

public class LocaleData implements Serializable {

    @SerializedName("default")
    @Expose
    private String en;
    @SerializedName("mr")
    @Expose
    private String mr;
    @SerializedName("hi")
    @Expose
    private String hi;

    public LocaleData(String defaultValue) {
        this.en = defaultValue;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getDefaultValue() {
        return en;
    }

    public String getLocaleValue() {
        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
            if (!TextUtils.isEmpty(mr)) {
                return mr;
            } else {
                return en;
            }
        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
            if (!TextUtils.isEmpty(hi)) {
                return hi;
            } else {
                return en;
            }
        } else {
            return en;
        }
    }

//    public void setLocaleValue(String name) {
//        if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_MARATHI)) {
//            mr = name;
//        } else if (Util.getLocaleLanguageCode().equalsIgnoreCase(Constants.App.LANGUAGE_HINDI)) {
//            hi = name;
//        } else {
//            en = name;
//        }
//    }
}
