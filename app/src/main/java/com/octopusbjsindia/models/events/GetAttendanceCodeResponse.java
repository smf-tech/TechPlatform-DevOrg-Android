package com.octopusbjsindia.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAttendanceCodeResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("AttencdenceCode")
    @Expose
    private Integer attencdenceCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAttencdenceCode() {
        return attencdenceCode;
    }

    public void setAttencdenceCode(Integer attencdenceCode) {
        this.attencdenceCode = attencdenceCode;
    }

}
