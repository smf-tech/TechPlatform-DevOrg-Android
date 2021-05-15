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

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public String getMahineCapacityId() {
        return mahineCapacityId;
    }

    public void setMahineCapacityId(String mahineCapacityId) {
        this.mahineCapacityId = mahineCapacityId;
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
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("capacity_id")
    @Expose
    private String mahineCapacityId;
    @SerializedName("machine_count")
    @Expose
    private String machineCount;

}
