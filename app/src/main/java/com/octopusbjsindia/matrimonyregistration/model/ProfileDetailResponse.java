package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetailResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ProfileDetailData data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private Integer code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProfileDetailData getData() {
        return data;
    }

    public void setData(ProfileDetailData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
