package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GpStructureListModel {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GpStructureList> getGpStructureList() {
        return gpStructureList;
    }

    public void setGpStructureList(List<GpStructureList> gpStructureList) {
        this.gpStructureList = gpStructureList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("data")
    @Expose
    private List<GpStructureList> gpStructureList = null;
    @SerializedName("message")
    @Expose
    private String message;
}
