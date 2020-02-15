
package com.octopusbjsindia.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Datum implements Serializable
{

    @SerializedName("subModule")
    @Expose
    private String subModule;
    @SerializedName("attendance")
    @Expose
    private List<Attendance> attendance = null;
    @SerializedName("holidayList")
    @Expose
    private List<HolidayList> holidayList = null;
    @SerializedName("totalWorkingHours")
    @Expose
    private String totalWorkingHours;

    private final static long serialVersionUID = 214296246910665796L;

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }

    public List<HolidayList> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(List<HolidayList> holidayList) {
        this.holidayList = holidayList;
    }

    public String getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(String totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }
}
