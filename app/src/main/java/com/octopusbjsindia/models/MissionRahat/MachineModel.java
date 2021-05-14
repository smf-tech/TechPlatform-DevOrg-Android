package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MachineModel implements Serializable {
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

//    public String getDistrictId() {
//        return districtId;
//    }
//
//    public void setDistrictId(String districtId) {
//        this.districtId = districtId;
//    }
//
//    public String getDistrictName() {
//        return districtName;
//    }
//
//    public void setDistrictName(String districtName) {
//        this.districtName = districtName;
//    }

    public String getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(String modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
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

    public String getDonerId() {
        return donerId;
    }

    public void setDonerId(String donerId) {
        this.donerId = donerId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("model_type_id")
    @Expose
    private String modelTypeId;
    @SerializedName("model_name")
    @Expose
    private String machineModel;
    @SerializedName("capacity_id")
    @Expose
    private String mahineCapacityId;
    @SerializedName("capacity_name")
    @Expose
    private String mahineCapacity;
    @SerializedName("donor_id")
    @Expose
    private String donerId;
    @SerializedName("donor_name")
    @Expose
    private String donorName;
}
