package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class JurisdictionLevelResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<JurisdictionLevel> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JurisdictionLevel> getData() {
        return data;
    }

    public void setData(List<JurisdictionLevel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
