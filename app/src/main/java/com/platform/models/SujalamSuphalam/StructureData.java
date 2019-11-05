package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructureData {

    @SerializedName("structureId")
    @Expose
    private String structureId;
    @SerializedName("structureName")
    @Expose
    private String structureName;
    @SerializedName("structureCode")
    @Expose
    private String structureCode;
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
    @SerializedName("structureWorkType")
    @Expose
    private String structureWorkType;
    @SerializedName("structureStatus")
    @Expose
    private String structureStatus;
    @SerializedName("structureStatusCode")
    @Expose
    private String structureStatusCode;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("structureDepartmentName")
    @Expose
    private String structureDepartmentName;
    @SerializedName("structureSubDepartmentName")
    @Expose
    private String structureSubDepartmentName;
    @SerializedName("structureType")
    @Expose
    private String structureType;
    @SerializedName("structureMachineList")
    @Expose
    private String structureMachineList;

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

    public String getStructureWorkType() {
        return structureWorkType;
    }

    public void setStructureWorkType(String structureWorkType) {
        this.structureWorkType = structureWorkType;
    }

    public String getStructureStatus() {
        return structureStatus;
    }

    public void setStructureStatus(String structureStatus) {
        this.structureStatus = structureStatus;
    }

    public String getStructureStatusCode() {
        return structureStatusCode;
    }

    public void setStructureStatusCode(String structureStatusCode) {
        this.structureStatusCode = structureStatusCode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getStructureDepartmentName() {
        return structureDepartmentName;
    }

    public void setStructureDepartmentName(String structureDepartmentName) {
        this.structureDepartmentName = structureDepartmentName;
    }

    public String getStructureSubDepartmentName() {
        return structureSubDepartmentName;
    }

    public void setStructureSubDepartmentName(String structureSubDepartmentName) {
        this.structureSubDepartmentName = structureSubDepartmentName;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getStructureMachineList() {
        return structureMachineList;
    }

    public void setStructureMachineList(String structureMachineList) {
        this.structureMachineList = structureMachineList;
    }

}
