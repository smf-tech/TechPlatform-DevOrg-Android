package com.octopus.models.tm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PendingApprovalsRequestsResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<PendingApprovalsRequest> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PendingApprovalsRequest> getData() {
        return data;
    }

    public void setData(List<PendingApprovalsRequest> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
