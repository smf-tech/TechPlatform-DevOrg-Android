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
    private List<attendanceData> attendanceData = null;
    @SerializedName("eventData")
    @Expose
    private ArrayList<EventTask> eventData = null;
    @SerializedName("taskData")
    @Expose
    private ArrayList<EventTask> taskData = null;
    @SerializedName("leave")
    @Expose
    private LeaveData leave = null;

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

    public LeaveData getLeave() {
        return leave;
    }

    public void setLeave(LeaveData leave) {
        this.leave = leave;
    }
}
