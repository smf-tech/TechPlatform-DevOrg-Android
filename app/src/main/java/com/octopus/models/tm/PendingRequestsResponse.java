package com.octopus.models.tm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PendingRequestsResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<PendingRequest> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PendingRequest> getData() {
        return data;
    }

    public void setData(List<PendingRequest> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
