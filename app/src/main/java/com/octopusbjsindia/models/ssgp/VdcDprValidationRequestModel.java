package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VdcDprValidationRequestModel {

    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("village_id")
    @Expose
    private String villageId;
    @SerializedName("report_date")
    @Expose
    private long reportDate;
    @SerializedName("machine_id")
    @Expose
    private String machineId;
    @SerializedName("machine_status")
    @Expose
    private String machineStatus;
    @SerializedName("structure_status")
    @Expose
    private String structureStatus;
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("working_hours")
    @Expose
    private String workingHours;
    @SerializedName("machine_halted_reason")
    @Expose
    private String machineHaltedReason;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

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

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public long getReportDate() {
        return reportDate;
    }

    public void setReportDate(long reportDate) {
        this.reportDate = reportDate;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getStructureStatus() {
        return structureStatus;
    }

    public void setStructureStatus(String structureStatus) {
        this.structureStatus = structureStatus;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getMachineHaltedReason() {
        return machineHaltedReason;
    }

    public void setMachineHaltedReason(String machineHaltedReason) {
        this.machineHaltedReason = machineHaltedReason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}