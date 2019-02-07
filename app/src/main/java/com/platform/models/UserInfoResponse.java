package com.platform.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserInfoResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private UserInfo data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
