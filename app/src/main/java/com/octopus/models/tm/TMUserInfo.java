package com.octopus.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TMUserInfo {

    @SerializedName("name")
    @Expose
    private String userName;

    public String getUserName() {
        return userName;
    }
}
