package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JurisdictionLevelResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private JurisdictionLevelData data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JurisdictionLevelData getData() {
        return data;
    }

    public void setData(JurisdictionLevelData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
