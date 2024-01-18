package com.octopusbjsindia.models.SujalamSuphalam;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RWBDonor {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("donor_type")
    @Expose
    private String donorType;

    @SerializedName("state_name")
    @Expose
    private String stateName;

    @SerializedName("state_id")
    @Expose
    private String stateId;

    @SerializedName("district_id")
    @Expose
    private String districtId;

    @SerializedName("district_name")
    @Expose
    private String districtName;

    @SerializedName("taluka_id")
    @Expose
    private String talukaId;

    @SerializedName("taluka_name")
    @Expose
    private String talukaName;

    @SerializedName("village_id")
    @Expose
    private String villageId;

    @SerializedName("village_name")
    @Expose
    private String villageName;

    @SerializedName("other_village")
    @Expose
    private String otherVillageName;

    @SerializedName("full_name")
    @Expose
    private String fullName;

    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;

    @SerializedName("company_firm_name")
    @Expose
    private String companyFirmName;

    @SerializedName("email_id")
    @Expose
    private String emailId;

    @SerializedName("full_address")
    @Expose
    private String fullAddress;

    @SerializedName("pin_code")
    @Expose
    private String pincode;

    @SerializedName("pan_number")
    @Expose
    private String panCardNumber;

    @SerializedName("pan_img")
    @Expose
    private String panCardImage;

    @SerializedName("aadhar_number")
    @Expose
    private String aadharCardNumber;

    @SerializedName("aadhar_img")
    @Expose
    private String aadharCardImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDonorType() {
        return donorType;
    }

    public void setDonorType(String donorType) {
        this.donorType = donorType;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getOtherVillageName() {
        return otherVillageName;
    }

    public void setOtherVillageName(String otherVillageName) {
        this.otherVillageName = otherVillageName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCompanyFirmName() {
        return companyFirmName;
    }

    public void setCompanyFirmName(String companyFirmName) {
        this.companyFirmName = companyFirmName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(String panCardNumber) {
        this.panCardNumber = panCardNumber;
    }

    public String getPanCardImage() {
        return panCardImage;
    }

    public void setPanCardImage(String panCardImage) {
        this.panCardImage = panCardImage;
    }

    public String getAadharCardNumber() {
        return aadharCardNumber;
    }

    public void setAadharCardNumber(String aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
    }

    public String getAadharCardImage() {
        return aadharCardImage;
    }

    public void setAadharCardImage(String aadharCardImage) {
        this.aadharCardImage = aadharCardImage;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
