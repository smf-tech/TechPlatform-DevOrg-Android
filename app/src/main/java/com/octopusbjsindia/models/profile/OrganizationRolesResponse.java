package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationRolesResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<OrganizationRole> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrganizationRole> getData() {
        return data;
    }

    public void setData(List<OrganizationRole> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
