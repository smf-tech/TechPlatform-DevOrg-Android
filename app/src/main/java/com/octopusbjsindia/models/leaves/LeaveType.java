package com.octopusbjsindia.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LeaveType implements Serializable {
    @SerializedName("leaveType")
    @Expose
    private String leaveType;
    @SerializedName("allocatedLeaves")
    @Expose
    private int allocatedLeaves;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getAllocatedLeaves() {
        return allocatedLeaves;
    }

    public void setAllocatedLeaves(int allocatedLeaves) {
        this.allocatedLeaves = allocatedLeaves;
    }


}
