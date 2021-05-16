package com.octopusbjsindia.models.MissionRahat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PatientInfoRequestModel {



    public long getMachineStartDate() {
        return machineStartDate;
    }

    public void setMachineStartDate(long machineStartDate) {
        this.machineStartDate = machineStartDate;
    }

    public long getMachineEndDate() {
        return machineEndDate;
    }

    public void setMachineEndDate(long machineEndDate) {
        this.machineEndDate = machineEndDate;
    }

    @SerializedName("machine_id")
    @Expose
    private String machineId;



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("icmr_code")
    @Expose
    private String icmrCode;
    @SerializedName("adhar_card")
    @Expose
    private String adharCard;
    @SerializedName("mobile_number")
    @Expose
    private long mobileNumber;
    @SerializedName("start_saturation_level")
    @Expose
    private Double startSaturationLevel;
    @SerializedName("end_saturation_level")
    @Expose
    private Double endSaturationLevel;
    @SerializedName("start_date")
    @Expose
    private long machineStartDate;
    @SerializedName("end_date")
    @Expose
    private long machineEndDate;

    @SerializedName("no_of_days")
    @Expose
    private Integer noOfDays;
    @SerializedName("remark")
    @Expose
    private String remark;
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIcmrCode() {
        return icmrCode;
    }

    public void setIcmrCode(String icmrCode) {
        this.icmrCode = icmrCode;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getStartSaturationLevel() {
        return startSaturationLevel;
    }

    public void setStartSaturationLevel(Double startSaturationLevel) {
        this.startSaturationLevel = startSaturationLevel;
    }

    public Double getEndSaturationLevel() {
        return endSaturationLevel;
    }

    public void setEndSaturationLevel(Double endSaturationLevel) {
        this.endSaturationLevel = endSaturationLevel;
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }


}