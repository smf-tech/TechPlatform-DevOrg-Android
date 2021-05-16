package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientInfoResponse {
    @SerializedName("_id")
    @Expose
    private String id;
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
    private Long mobileNumber;
    @SerializedName("no_of_days")
    @Expose
    private Integer noOfDays;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("start_date")
    @Expose
    private Long startDate;
    @SerializedName("start_saturation_level")
    @Expose
    private Integer startSaturationLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Integer getStartSaturationLevel() {
        return startSaturationLevel;
    }

    public void setStartSaturationLevel(Integer startSaturationLevel) {
        this.startSaturationLevel = startSaturationLevel;
    }
}

