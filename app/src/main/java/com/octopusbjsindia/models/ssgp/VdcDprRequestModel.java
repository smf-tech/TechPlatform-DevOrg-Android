package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VdcDprRequestModel {

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
    @SerializedName("start_meter_reading")
    @Expose
    private String startMeterReading;
    @SerializedName("start_meter_reading_image")
    @Expose
    private String startMeterReadingImage;
    @SerializedName("end_meter_reading")
    @Expose
    private String endMeterReading;
    @SerializedName("end_meter_reading_image")
    @Expose
    private String endMeterReadingImage;
    @SerializedName("structure_status")
    @Expose
    private String structureStatus;
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("structure_image")
    @Expose
    private String structureImage;
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

    public String getStartMeterReading() {
        return startMeterReading;
    }

    public void setStartMeterReading(String startMeterReading) {
        this.startMeterReading = startMeterReading;
    }

    public String getStartMeterReadingImage() {
        return startMeterReadingImage;
    }

    public void setStartMeterReadingImage(String startMeterReadingImage) {
        this.startMeterReadingImage = startMeterReadingImage;
    }

    public String getEndMeterReading() {
        return endMeterReading;
    }

    public void setEndMeterReading(String endMeterReading) {
        this.endMeterReading = endMeterReading;
    }

    public String getEndMeterReadingImage() {
        return endMeterReadingImage;
    }

    public void setEndMeterReadingImage(String endMeterReadingImage) {
        this.endMeterReadingImage = endMeterReadingImage;
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

    public String getStructureImage() {
        return structureImage;
    }

    public void setStructureImage(String structureImage) {
        this.structureImage = structureImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}