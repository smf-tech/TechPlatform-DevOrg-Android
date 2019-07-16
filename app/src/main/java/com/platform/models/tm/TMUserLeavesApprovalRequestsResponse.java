package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMUserLeavesApprovalRequestsResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private TMUserLeavesApprovalRequest data;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TMUserLeavesApprovalRequest getData() {
        return data;
    }

    public void setData(TMUserLeavesApprovalRequest data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}