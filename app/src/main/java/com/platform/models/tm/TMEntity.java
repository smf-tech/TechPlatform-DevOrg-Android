package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMEntity {

    @SerializedName("user")
    @Expose
    private TMUserInfo user;

    public TMUserInfo getUser() {
        return user;
    }

    public void setUser(TMUserInfo user) {
        this.user = user;
    }
}
