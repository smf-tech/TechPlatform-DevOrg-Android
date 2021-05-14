package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AssignMachinesModel implements Serializable {

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getMahineCapacityId() {
        return mahineCapacityId;
    }

    public void setMahineCapacityId(String mahineCapacityId) {
        this.mahineCapacityId = mahineCapacityId;
    }

    public String getMahineCapacity() {
        return mahineCapacity;
    }

    public void setMahineCapacity(String mahineCapacity) {
        this.mahineCapacity = mahineCapacity;
    }

    public String getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(String machineCount) {
        this.machineCount = machineCount;
    }

    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("taluka_name")
    @Expose
    private String talukaName;
    @SerializedName("capacity_id")
    @Expose
    private String mahineCapacityId;
    @SerializedName("capacity_name")
    @Expose
    private String mahineCapacity;
    @SerializedName("machine_count")
    @Expose
    private String machineCount;

}
