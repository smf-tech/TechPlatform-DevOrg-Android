package com.octopusbjsindia.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMUserFormsApprovalRequestsResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private List<TMUserFormsApprovalRequest> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TMUserFormsApprovalRequest> getData() {
        return data;
    }

    public void setData(List<TMUserFormsApprovalRequest> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

