package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("data")
    private List<Organization> data;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
