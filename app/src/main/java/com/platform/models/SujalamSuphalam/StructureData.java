package com.platform.models.SujalamSuphalam;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class StructureData implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "structureId")
    @NonNull
    @SerializedName("structureId")
    @Expose
    private String structureId;
    @ColumnInfo(name = "structureName")
    @SerializedName("structureName")
    @Expose
    private String structureName;
    @ColumnInfo(name = "structureCode")
    @SerializedName("structureCode")
    @Expose
    private String structureCode;
    @ColumnInfo(name = "state")
    @SerializedName("state")
    @Expose
    private String state;
    @ColumnInfo(name = "stateId")
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @ColumnInfo(name = "district")
    @SerializedName("district")
    @Expose
    private String district;
    @ColumnInfo(name = "districtId")
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @ColumnInfo(name = "taluka")
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @ColumnInfo(name = "talukaId")
    @SerializedName("talukaId")
    @Expose
    private String talukaId;
    @ColumnInfo(name = "structureWorkType")
    @SerializedName("structureWorkType")
    @Expose
    private String structureWorkType;
    @ColumnInfo(name = "structureStatus")
    @SerializedName("structureStatus")
    @Expose
    private String structureStatus;
    @ColumnInfo(name = "structureStatusCode")
    @SerializedName("structureStatusCode")
    @Expose
    private int structureStatusCode;
    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    @Expose
    private double lat;
    @ColumnInfo(name = "long")
    @SerializedName("long")
    @Expose
    private double _long;
    @ColumnInfo(name = "structureDepartmentName")
    @SerializedName("structureDepartmentName")
    @Expose
    private String structureDepartmentName;
    @ColumnInfo(name = "structureSubDepartmentName")
    @SerializedName("structureSubDepartmentName")
    @Expose
    private String structureSubDepartmentName;
    @ColumnInfo(name = "structureType")
    @SerializedName("structureType")
    @Expose
    private String structureType;
    @ColumnInfo(name = "structureMachineList")
    @SerializedName("structureMachineList")
    @Expose
    private String structureMachineList;
    @ColumnInfo(name = "updatedDate")
    @SerializedName("updatedDate")
    @Expose
    private String updatedDate;
    @Ignore
    @SerializedName("deployedMachineDetails")
    @Expose
    private List<DeployedMachine> deployedMachineDetails;
    @Ignore
    private boolean isSavedOffine = false;

    public boolean isSelectedForDeployment() {
        return isSelectedForDeployment;
    }

    public void setSelectedForDeployment(boolean selectedForDeployment) {
        isSelectedForDeployment = selectedForDeployment;
    }

    @Ignore
    private boolean isSelectedForDeployment = false;

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

    public int getStructureStatusCode() {
        return structureStatusCode;
    }

    public void setStructureStatusCode(int structureStatusCode) {
        this.structureStatusCode = structureStatusCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double get_long() {
        return _long;
    }

    public void set_long(double _long) {
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isSavedOffine() {
        return isSavedOffine;
    }

    public List<DeployedMachine> getDeployedMachineDetails() {
        return deployedMachineDetails;
    }

    public void setDeployedMachineDetails(List<DeployedMachine> deployedMachineDetails) {
        this.deployedMachineDetails = deployedMachineDetails;
    }

    public void setSavedOffine(boolean savedOffine) {
        isSavedOffine = savedOffine;
    }
}
