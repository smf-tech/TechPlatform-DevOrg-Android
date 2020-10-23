package com.octopusbjsindia.models.profile;

import java.util.List;

@SuppressWarnings("unused")
public class OrganizationResponse {
    private int status;

    private List<Organization> data;

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
