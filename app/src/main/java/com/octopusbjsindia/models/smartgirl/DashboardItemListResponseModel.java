package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardItemListResponseModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DashboardItemListResponse dashboardItemListResponse;

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

    public DashboardItemListResponse getDashboardItemListResponse() {
        return dashboardItemListResponse;
    }

    public void setDashboardItemListResponse(DashboardItemListResponse dashboardItemListResponse) {
        this.dashboardItemListResponse = dashboardItemListResponse;
    }
}
