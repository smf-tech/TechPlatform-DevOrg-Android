package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllMatrimonyMeetsAPIResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<MatrimonyMeet> data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("earliestMeetId")
    @Expose
    private String earliestMeetId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MatrimonyMeet> getData() {
        return data;
    }

    public void setData(List<MatrimonyMeet> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEarliestMeetId() {
        return earliestMeetId;
    }

    public void setEarliestMeetId(String earliestMeetId) {
        this.earliestMeetId = earliestMeetId;
    }

}
