package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class JurisdictionLevelResponse {
    @Expose
    private String status;
    @Expose
    private List<JurisdictionLocation> data = null;
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JurisdictionLocation> getData() {
        return data;
    }

    public void setData(List<JurisdictionLocation> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
