package com.octopus.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationProjectsResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<OrganizationProject> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrganizationProject> getData() {
        return data;
    }

    public void setData(List<OrganizationProject> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
