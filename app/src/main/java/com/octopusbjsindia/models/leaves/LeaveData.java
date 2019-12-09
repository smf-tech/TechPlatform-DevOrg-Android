package com.octopusbjsindia.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LeaveData implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("leave_type")
    @Expose
    private String leaveType;
    @SerializedName("full_half_day")
    @Expose
    private String fullHalfDay;
    @SerializedName("startdate")
    @Expose
    private long startdate;
    @SerializedName("enddate")
    @Expose
    private long enddate;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("paid_leave")
    @Expose
    private Boolean paidLeave;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rejection_reason")
    @Expose
    private String rejectionReason;

    public int getHalfFullDay() {
        return halfFullDay;
    }

    public void setHalfFullDay(int halfFullDay) {
        this.halfFullDay = halfFullDay;
    }

    @SerializedName("half_full_day")
    @Expose
    private int halfFullDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getFullHalfDay() {
        return fullHalfDay;
    }

    public void setFullHalfDay(String fullHalfDay) {
        this.fullHalfDay = fullHalfDay;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getPaidLeave() {
        return paidLeave;
    }

    public void setPaidLeave(Boolean paidLeave) {
        this.paidLeave = paidLeave;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
