package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OxygenMachineList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("donor_name")
    @Expose
    private String donorName;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("hospitalName")
    @Expose
    private String hospitalName;
    @SerializedName("hospitalId")
    @Expose
    private String hospitalId;
    @SerializedName("inchargeName")
    @Expose
    private String inchargeName;
    @SerializedName("inchargeMobileNumber")
    @Expose
    private String inchargeMobileNumber;
    @SerializedName("workingHrsCount")
    @Expose
    private Integer workingHrsCount;
    @SerializedName("benefitedPatientCount")
    @Expose
    private Integer benefitedPatientCount;
    @SerializedName("deployDate")
    @Expose
    private String deployDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getInchargeName() {
        return inchargeName;
    }

    public void setInchargeName(String inchargeName) {
        this.inchargeName = inchargeName;
    }

    public String getInchargeMobileNumber() {
        return inchargeMobileNumber;
    }

    public void setInchargeMobileNumber(String inchargeMobileNumber) {
        this.inchargeMobileNumber = inchargeMobileNumber;
    }

    public Integer getWorkingHrsCount() {
        return workingHrsCount;
    }

    public void setWorkingHrsCount(Integer workingHrsCount) {
        this.workingHrsCount = workingHrsCount;
    }

    public Integer getBenefitedPatientCount() {
        return benefitedPatientCount;
    }

    public void setBenefitedPatientCount(Integer benefitedPatientCount) {
        this.benefitedPatientCount = benefitedPatientCount;
    }

    public String getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(String deployDate) {
        this.deployDate = deployDate;
    }

}
