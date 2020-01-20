package com.octopusbjsindia.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendanceResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data implements Serializable {

        @SerializedName("attendanceId")
        @Expose
        private String attendanceId;
        @SerializedName("Data")
        @Expose
        private AttendaceData data;

        public String getAttendanceId() {
            return attendanceId;
        }

        public void setAttendanceId(String attendanceId) {
            this.attendanceId = attendanceId;
        }

        public AttendaceData getData() {
            return data;
        }

        public void setData(AttendaceData data) {
            this.data = data;
        }

    }
}
