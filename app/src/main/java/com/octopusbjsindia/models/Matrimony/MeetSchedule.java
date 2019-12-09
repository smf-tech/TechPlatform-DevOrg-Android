package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeetSchedule implements Serializable {
    @SerializedName("dateTime")
    @Expose
    private long dateTime;
    @SerializedName("meetStartTime")
    @Expose
    private String meetStartTime;
    @SerializedName("meetEndTime")
    @Expose
    private String meetEndTime;

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getMeetStartTime() {
        return meetStartTime;
    }

    public void setMeetStartTime(String meetStartTime) {
        this.meetStartTime = meetStartTime;
    }

    public String getMeetEndTime() {
        return meetEndTime;
    }

    public void setMeetEndTime(String meetEndTime) {
        this.meetEndTime = meetEndTime;
    }
}
