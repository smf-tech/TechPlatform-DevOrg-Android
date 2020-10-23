package com.octopusbjsindia.models.profile;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationProjectsResponse {
    private String status;

    private List<OrganizationProject> data;

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
