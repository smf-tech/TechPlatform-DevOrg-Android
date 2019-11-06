package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StructureData implements Serializable {

    @SerializedName("structureId")
    @Expose
    private String structureId;
    @SerializedName("structureName")
    @Expose
    private String structureName;
    @SerializedName("structureCode")
    @Expose
    private String structureCode;
    @SerializedName("structureWorkType")
    @Expose
    private String structureWorkType;
    @SerializedName("structureStatus")
    @Expose
    private String structureStatus;
    @SerializedName("structureStatusCode")
    @Expose
    private int structureStatusCode;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double _long;
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

}
