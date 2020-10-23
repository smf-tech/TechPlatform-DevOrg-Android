package com.octopusbjsindia.models.profile;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationRolesResponse {
    private String status;

    private List<OrganizationRole> data;

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
