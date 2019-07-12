package com.platform.models.planner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.events.Event;
import com.platform.models.leaves.LeaveDetail;

import java.util.ArrayList;
import java.util.List;

public class SubmoduleData {
    @SerializedName("subModule")
    @Expose
    private String subModule;
    @SerializedName("attendanceData")
    @Expose
    private List<attendanceData> attendanceData = null;
    @SerializedName("eventData")
    @Expose
    private ArrayList<Event> eventData = null;
    @SerializedName("taskData")
    @Expose
    private ArrayList<Event> taskData = null;
    @SerializedName("leave")
    @Expose
    private List<LeaveDetail> leave = null;

    public SubmoduleData(String subModule) {
        this.subModule = subModule;
    }

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public List<attendanceData> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<attendanceData> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public ArrayList<Event> getEventData() {
        return eventData;
    }

    public void setEventData(ArrayList<Event> eventData) {
        this.eventData = eventData;
    }

    public ArrayList<Event> getTaskData() {
        return taskData;
    }

    public void setTaskData(ArrayList<Event> taskData) {
        this.taskData = taskData;
    }

    public List<LeaveDetail> getLeave() {
        return leave;
    }

    public void setLeave(List<LeaveDetail> leave) {
        this.leave = leave;
    }
}
