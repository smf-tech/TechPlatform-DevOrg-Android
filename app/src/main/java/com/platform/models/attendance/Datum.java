
package com.platform.models.attendance;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}
