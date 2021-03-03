package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VACStructureMasterRequest {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("project_id")
    @Expose
    private String projectId;
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
    @SerializedName("beneficiary_id")
    @Expose
    private String beneficiaryId;
    @SerializedName("intervention_id")
    @Expose
    private String interventionId;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("scope_of_work")
    @Expose
    private String scopeOfWork;
    @SerializedName("work_hours")
    @Expose
    private String workHours;
    @SerializedName("diesel_required")
    @Expose
    private String dieselRequired;
    @SerializedName("working_days_required")
    @Expose
    private String workingDaysRequired;
    @SerializedName("consent_ngh_tran_work")
    @Expose
    private String consentNghTranWork  ;
    @SerializedName("machine_transport")
    @Expose
    private String machineTransport;
    @SerializedName("is_silt_trasported")
    @Expose
    private String isSiltTrasported;
    @SerializedName("ff_name")
    @Expose
    private String ffName;
    @SerializedName("ff_mobile_number")
    @Expose
    private String ffMobileNumber;
    @SerializedName("pre_staructure_image")
    @Expose
    private String preStaructureImage;
    @SerializedName("structure_lenght")
    @Expose
    private String structureLenght;
    @SerializedName("structure_width")
    @Expose
    private String structureWidth;
    @SerializedName("structure_depth")
    @Expose
    private String structureDepth;
    @SerializedName("beneficiary_name")
    @Expose
    private String beneficiaryName;
    @SerializedName("beneficiary_contact_number")
    @Expose
    private String beneficiaryContactNumber;
    @SerializedName("category_beneficiary_farmer")
    @Expose
    private String categoryBeneficiaryFarmer;
    @SerializedName("water_source")
    @Expose
    private String waterSource;
    @SerializedName("gat_number")
    @Expose
    private String gatNumber;
    @SerializedName("income_form_farm")
    @Expose
    private String incomeFormFarm;
    @SerializedName("crops_years")
    @Expose
    private String cropsYears;
    @SerializedName("crop_type")
    @Expose
    private String cropType;
    @SerializedName("beneficiary_details")
    @Expose
    private ArrayList<BeneficiaryDetail> beneficiaryDetails = null;
    @SerializedName("responsible_person_number")
    @Expose
    private String responsiblePersonNumber;
    @SerializedName("responsible_person_name")
    @Expose
    private String responsiblePersonName;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getScopeOfWork() {
        return scopeOfWork;
    }

    public void setScopeOfWork(String scopeOfWork) {
        this.scopeOfWork = scopeOfWork;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getDieselRequired() {
        return dieselRequired;
    }

    public void setDieselRequired(String dieselRequired) {
        this.dieselRequired = dieselRequired;
    }

    public String getWorkingDaysRequired() {
        return workingDaysRequired;
    }

    public void setWorkingDaysRequired(String workingDaysRequired) {
        this.workingDaysRequired = workingDaysRequired;
    }

    public String getConsentNghTranWork() {
        return consentNghTranWork;
    }

    public void setConsentNghTranWork(String consentNghTranWork) {
        this.consentNghTranWork = consentNghTranWork;
    }

    public String getMachineTransport() {
        return machineTransport;
    }

    public void setMachineTransport(String machineTransport) {
        this.machineTransport = machineTransport;
    }

    public String getIsSiltTrasported() {
        return isSiltTrasported;
    }

    public void setIsSiltTrasported(String isSiltTrasported) {
        this.isSiltTrasported = isSiltTrasported;
    }

    public String getFfName() {
        return ffName;
    }

    public void setFfName(String ffName) {
        this.ffName = ffName;
    }

    public String getFfMobileNumber() {
        return ffMobileNumber;
    }

    public void setFfMobileNumber(String ffMobileNumber) {
        this.ffMobileNumber = ffMobileNumber;
    }

    public String getPreStaructureImage() {
        return preStaructureImage;
    }

    public void setPreStaructureImage(String preStaructureImage) {
        this.preStaructureImage = preStaructureImage;
    }

    public String getStructureLenght() {
        return structureLenght;
    }

    public void setStructureLenght(String structureLenght) {
        this.structureLenght = structureLenght;
    }

    public String getStructureWidth() {
        return structureWidth;
    }

    public void setStructureWidth(String structureWidth) {
        this.structureWidth = structureWidth;
    }

    public String getStructureDepth() {
        return structureDepth;
    }

    public void setStructureDepth(String structureDepth) {
        this.structureDepth = structureDepth;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryContactNumber() {
        return beneficiaryContactNumber;
    }

    public void setBeneficiaryContactNumber(String beneficiaryContactNumber) {
        this.beneficiaryContactNumber = beneficiaryContactNumber;
    }

    public String getCategoryBeneficiaryFarmer() {
        return categoryBeneficiaryFarmer;
    }

    public void setCategoryBeneficiaryFarmer(String categoryBeneficiaryFarmer) {
        this.categoryBeneficiaryFarmer = categoryBeneficiaryFarmer;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public String getGatNumber() {
        return gatNumber;
    }

    public void setGatNumber(String gatNumber) {
        this.gatNumber = gatNumber;
    }

    public String getIncomeFormFarm() {
        return incomeFormFarm;
    }

    public void setIncomeFormFarm(String incomeFormFarm) {
        this.incomeFormFarm = incomeFormFarm;
    }

    public String getCropsYears() {
        return cropsYears;
    }

    public void setCropsYears(String cropsYears) {
        this.cropsYears = cropsYears;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public ArrayList<BeneficiaryDetail> getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(ArrayList<BeneficiaryDetail> beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public String getResponsiblePersonNumber() {
        return responsiblePersonNumber;
    }

    public void setResponsiblePersonNumber(String responsiblePersonNumber) {
        this.responsiblePersonNumber = responsiblePersonNumber;
    }

    public String getResponsiblePersonName() {
        return responsiblePersonName;
    }

    public void setResponsiblePersonName(String responsiblePersonName) {
        this.responsiblePersonName = responsiblePersonName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
