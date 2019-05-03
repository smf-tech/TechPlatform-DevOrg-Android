package com.platform.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LeaveDetail implements Serializable {
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("leaveTypes")
    @Expose
    private List<LeaveType> leaveTypes = null;
    @SerializedName("balanceLeaves")
    @Expose
    private Integer balanceLeaves;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<LeaveType> getLeaveTypes() {
        return leaveTypes;
    }

    public void setLeaveTypes(List<LeaveType> leaveTypes) {
        this.leaveTypes = leaveTypes;
    }

    public Integer getBalanceLeaves() {
        return balanceLeaves;
    }

    public void setBalanceLeaves(Integer balanceLeaves) {
        this.balanceLeaves = balanceLeaves;
    }

}
