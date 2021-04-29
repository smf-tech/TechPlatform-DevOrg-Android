package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyRecordRequestModel {
    @SerializedName("machine_code")
    @Expose
    private String MachineCode;

    @SerializedName("machine_id")
    @Expose
    private String machineId;
    @SerializedName("slot_id")
    @Expose
    private String slotId;
    @SerializedName("hours_usages")
    @Expose
    private Double hoursUsages;
    @SerializedName("benefited_patient_no")
    @Expose
    private Integer benefitedPatientNo;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }



    public Integer getBenefitedPatientNo() {
        return benefitedPatientNo;
    }

    public void setBenefitedPatientNo(Integer benefitedPatientNo) {
        this.benefitedPatientNo = benefitedPatientNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public Double getHoursUsages() {
        return hoursUsages;
    }

    public void setHoursUsages(Double hoursUsages) {
        this.hoursUsages = hoursUsages;
    }
}
