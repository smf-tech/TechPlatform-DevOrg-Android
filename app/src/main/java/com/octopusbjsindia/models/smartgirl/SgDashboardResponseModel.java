package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SgDashboardResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<SgDashboardResponseModelList> sgDashboardResponseModellist = null;

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

    public List<SgDashboardResponseModelList> getSgDashboardResponseModellist() {
        return sgDashboardResponseModellist;
    }

    public void setSgDashboardResponseModellist(List<SgDashboardResponseModelList> sgDashboardResponseModellist) {
        this.sgDashboardResponseModellist = sgDashboardResponseModellist;
    }
}