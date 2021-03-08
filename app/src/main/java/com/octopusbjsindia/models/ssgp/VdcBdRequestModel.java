package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VdcBdRequestModel {

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
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("structure_type")
    @Expose
    private String structureType;
    @SerializedName("beneficiary_name")
    @Expose
    private String beneficiaryName;
    @SerializedName("beneficiary_number")
    @Expose
    private String beneficiaryNumber;
    @SerializedName("category_beneficiary_farmer")
    @Expose
    private String categoryBeneficiaryFarmer;
    @SerializedName("arrigation_sur_water")
    @Expose
    private String arrigationSurWater;
    @SerializedName("participant_key3")
    @Expose
    private String participantKey3;
    @SerializedName("total_participant_no")
    @Expose
    private String totalParticipantNo;
    @SerializedName("gat_number")
    @Expose
    private String gatNumber;
    @SerializedName("annual_income")
    @Expose
    private String annualIncome;
    @SerializedName("crop_number_time")
    @Expose
    private String cropNumberTime;
    @SerializedName("type_of_crop")
    @Expose
    private String typeOfCrop;
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

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryNumber() {
        return beneficiaryNumber;
    }

    public void setBeneficiaryNumber(String beneficiaryNumber) {
        this.beneficiaryNumber = beneficiaryNumber;
    }

    public String getCategoryBeneficiaryFarmer() {
        return categoryBeneficiaryFarmer;
    }

    public void setCategoryBeneficiaryFarmer(String categoryBeneficiaryFarmer) {
        this.categoryBeneficiaryFarmer = categoryBeneficiaryFarmer;
    }

    public String getArrigationSurWater() {
        return arrigationSurWater;
    }

    public void setArrigationSurWater(String arrigationSurWater) {
        this.arrigationSurWater = arrigationSurWater;
    }

    public String getParticipantKey3() {
        return participantKey3;
    }

    public void setParticipantKey3(String participantKey3) {
        this.participantKey3 = participantKey3;
    }

    public String getTotalParticipantNo() {
        return totalParticipantNo;
    }

    public void setTotalParticipantNo(String totalParticipantNo) {
        this.totalParticipantNo = totalParticipantNo;
    }

    public String getGatNumber() {
        return gatNumber;
    }

    public void setGatNumber(String gatNumber) {
        this.gatNumber = gatNumber;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getCropNumberTime() {
        return cropNumberTime;
    }

    public void setCropNumberTime(String cropNumberTime) {
        this.cropNumberTime = cropNumberTime;
    }

    public String getTypeOfCrop() {
        return typeOfCrop;
    }

    public void setTypeOfCrop(String typeOfCrop) {
        this.typeOfCrop = typeOfCrop;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
