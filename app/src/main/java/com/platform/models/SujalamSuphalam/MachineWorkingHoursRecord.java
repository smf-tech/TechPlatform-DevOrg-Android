package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineWorkingHoursRecord {

    @SerializedName("machine_id")
    @Expose
    private String machineId;

    @SerializedName("machine_code")
    @Expose
    private String machineCode;

    @SerializedName("working_date")
    @Expose
    private String workingDate;

    @SerializedName("working_hours")
    @Expose
    private String workingHours;

    @SerializedName("working_status")
    @Expose
    private String workingStatus;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

}
