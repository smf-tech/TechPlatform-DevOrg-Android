package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkshopBachListResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private WorkshopBachListResponse workshopBachListResponse;

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


    public WorkshopBachListResponse getWorkshopBachListResponse() {
        return workshopBachListResponse;
    }

    public void setWorkshopBachListResponse(WorkshopBachListResponse workshopBachListResponse) {
        this.workshopBachListResponse = workshopBachListResponse;
    }
}
