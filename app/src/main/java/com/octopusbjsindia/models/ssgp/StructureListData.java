package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructureListData {
    @SerializedName("structureId")
    @Expose
    private String structureId;
    @SerializedName("structureName")
    @Expose
    private String structureName;
    @SerializedName("structureCode")
    @Expose
    private String structureCode;
    @SerializedName("structureType")
    @Expose
    private String structureType;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @SerializedName("talukaId")
    @Expose
    private String talukaId;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("structureinterventionType")
    @Expose
    private String structureinterventionType;
    @SerializedName("structureStatus")
    @Expose
    private String structureStatus;
    @SerializedName("structureStatusCode")
    @Expose
    private Integer structureStatusCode;
    @SerializedName("updatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("structureMachineList")
    @Expose
    private String structureMachineList;
    @SerializedName("machineCnt")
    @Expose
    private Integer machineCnt=0;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getStructureCode() {
        return structureCode;
    }

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStructureinterventionType() {
        return structureinterventionType;
    }

    public void setStructureinterventionType(String structureinterventionType) {
        this.structureinterventionType = structureinterventionType;
    }

    public String getStructureStatus() {
        return structureStatus;
    }

    public void setStructureStatus(String structureStatus) {
        this.structureStatus = structureStatus;
    }

    public Integer getStructureStatusCode() {
        return structureStatusCode;
    }

    public void setStructureStatusCode(Integer structureStatusCode) {
        this.structureStatusCode = structureStatusCode;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStructureMachineList() {
        return structureMachineList;
    }

    public void setStructureMachineList(String structureMachineList) {
        this.structureMachineList = structureMachineList;
    }

    public Integer getMachineCnt() {
        return machineCnt;
    }

    public void setMachineCnt(Integer machineCnt) {
        this.machineCnt = machineCnt;
    }

}
