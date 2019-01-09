package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrganizationResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<Organization> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Organization> getData() {
        return data;
    }

    public void setData(List<Organization> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
