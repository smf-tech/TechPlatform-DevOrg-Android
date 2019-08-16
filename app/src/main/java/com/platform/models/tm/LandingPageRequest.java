package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LandingPageRequest {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("approvalType")
    @Expose
    private String approvalType;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("pendingCount")
    @Expose
    private int pendingCount;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}