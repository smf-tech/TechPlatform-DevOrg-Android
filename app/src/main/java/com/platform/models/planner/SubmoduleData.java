package com.platform.models.planner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.events.EventTask;
import com.platform.models.leaves.LeaveDetail;

import java.util.ArrayList;
import java.util.List;

public class SubmoduleData {
    @SerializedName("subModule")
    @Expose
    private String subModule;
    @SerializedName("attendanceData")
    @Expose
    private List<Object> attendanceData = null;
    @SerializedName("eventData")
    @Expose
    private ArrayList<EventTask> eventData = null;
    @SerializedName("taskData")
    @Expose
    private ArrayList<EventTask> taskData = null;
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

    public List<Object> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(List<Object> attendanceData) {
        this.attendanceData = attendanceData;
    }

    public ArrayList<EventTask> getEventData() {
        return eventData;
    }

    public void setEventData(ArrayList<EventTask> eventData) {
        this.eventData = eventData;
    }

    public ArrayList<EventTask> getTaskData() {
        return taskData;
    }

    public void setTaskData(ArrayList<EventTask> taskData) {
        this.taskData = taskData;
    }

    public List<LeaveDetail> getLeave() {
        return leave;
    }

    public void setLeave(List<LeaveDetail> leave) {
        this.leave = leave;
    }
}
