package com.octopusbjsindia.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetAttendanceCodeRequest {
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("AttendanceCode")
    @Expose
    private String attendanceCode;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        this.attendanceCode = attendanceCode;
    }
}
