package com.octopusbjsindia.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("action_by")
    @Expose
    private String actionBy;
    @SerializedName("action_on")
    @Expose
    private double actionOn;
    @SerializedName("rejection_reason")
    @Expose
    private String rejectionReason;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public double getActionOn() {
        return actionOn;
    }

    public void setActionOn(double actionOn) {
        this.actionOn = actionOn;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
