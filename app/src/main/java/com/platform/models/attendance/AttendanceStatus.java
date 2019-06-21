package com.platform.models.attendance;

public class AttendanceStatus {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetimestamp() {
        return datetimestamp;
    }

    public void setDatetimestamp(String datetimestamp) {
        this.datetimestamp = datetimestamp;
    }

    private  String status,datetimestamp;
}
