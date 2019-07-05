package com.platform.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MonthlyLeaveHolidayData {

    @SerializedName("leaveData")
    @Expose
    private List<LeaveData> leaveData = null;
    @SerializedName("holidayData")
    @Expose
    private List<HolidayData> holidayData = null;

    public List<LeaveData> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(List<LeaveData> leaveData) {
        this.leaveData = leaveData;
    }

    public List<HolidayData> getHolidayData() {
        return holidayData;
    }

    public void setHolidayData(List<HolidayData> holidayData) {
        this.holidayData = holidayData;
    }
}
