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

    @SerializedName("date")
    @Expose
    private long workingDate;

    @SerializedName("working_hours")
    @Expose
    private String workingHours;

    @SerializedName("is_validate ")
    @Expose
    private boolean workingStatus;

    @SerializedName("structure_assigned")
    @Expose
    private String structureAssigned;

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

    public long getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(long workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public boolean getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(boolean workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getStructureAssigned() {
        return structureAssigned;
    }

    public void setStructureAssigned(String structureAssigned) {
        this.structureAssigned = structureAssigned;
    }

}
