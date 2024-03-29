package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineWorklogList {

    @SerializedName("machineId")
    @Expose
    private String machineId;

    @SerializedName("workDate")
    @Expose
    private String workDate;



    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("start_id")
    @Expose
    private String start_id;
    @SerializedName("startReading")
    @Expose
    private String startReading;
    @SerializedName("startMeterReadingImage")
    @Expose
    private String startMeterReadingImage;

    @SerializedName("end_id")
    @Expose
    private String end_id;

    @SerializedName("endReading")
    @Expose
    private String endReading;
    @SerializedName("endMeterReadingImage")
    @Expose
    private String endMeterReadingImage;
    @SerializedName("totalHrsCunt")
    @Expose
    private String totalHrsCunt;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartReading() {
        return startReading;
    }

    public void setStartReading(String startReading) {
        this.startReading = startReading;
    }

    public String getStartMeterReadingImage() {
        return startMeterReadingImage;
    }

    public void setStartMeterReadingImage(String startMeterReadingImage) {
        this.startMeterReadingImage = startMeterReadingImage;
    }

    public String getEndReading() {
        return endReading;
    }

    public void setEndReading(String endReading) {
        this.endReading = endReading;
    }

    public String getEndMeterReadingImage() {
        return endMeterReadingImage;
    }

    public void setEndMeterReadingImage(String endMeterReadingImage) {
        this.endMeterReadingImage = endMeterReadingImage;
    }

    public String getTotalHrsCunt() {
        return totalHrsCunt;
    }

    public void setTotalHrsCunt(String totalHrsCunt) {
        this.totalHrsCunt = totalHrsCunt;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getStart_id() {
        return start_id;
    }

    public void setStart_id(String start_id) {
        this.start_id = start_id;
    }

    public String getEnd_id() {
        return end_id;
    }

    public void setEnd_id(String end_id) {
        this.end_id = end_id;
    }
}
