package com.platform.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Model {

    @SerializedName("ResultCode")
    private String resultCode;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
