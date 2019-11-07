package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Structure {
    @SerializedName("name")
    @Expose
    private String name;
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
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("structure_type")
    @Expose
    private String structureType;
    @SerializedName("department_id")
    @Expose
    private String departmentId;
    @SerializedName("sub_department_id")
    @Expose
    private String subDepartmentId;
    @SerializedName("host_village_id")
    @Expose
    private String hostVillageID;
    @SerializedName("host_village")
    @Expose
    private String hostVillage;
    @SerializedName("host_village_population")
    @Expose
    private String hostVillagePopulation;
    @SerializedName("work_type")
    @Expose
    private String workType;
    @SerializedName("total_population")
    @Expose
    private String totalPopulation;
    @SerializedName("water_storage")
    @Expose
    private String waterStorage;
    @SerializedName("technical_section_number")
    @Expose
    private String technicalSectionNumber;
    @SerializedName("technical_section_date")
    @Expose
    private String technicalSectionDate;
    @SerializedName("administrative_estimate_amount")
    @Expose
    private String administrativeEstimateAmount;
    @SerializedName("apprx_working_hrs")
    @Expose
    private String apprxWorkingHrs;
    @SerializedName("apprx_diesel_consumption_lt")
    @Expose
    private String apprxDieselConsumptionLt;
    @SerializedName("apprx_diesel_consumption_rs")
    @Expose
    private String apprxDieselConsumptionRs;
    @SerializedName("apprx_estimate_qunty")
    @Expose
    private String apprxEstimateQunty;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double _long;
    @SerializedName("ff_id")
    @Expose
    private String ffId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getSubDepartmentId() {
        return subDepartmentId;
    }

    public void setSubDepartmentId(String subDepartmentId) {
        this.subDepartmentId = subDepartmentId;
    }

    public String getHostVillage() {
        return hostVillage;
    }

    public void setHostVillage(String hostVillage) {
        this.hostVillage = hostVillage;
    }

    public String getHostVillagePopulation() {
        return hostVillagePopulation;
    }

    public void setHostVillagePopulation(String hostVillagePopulation) {
        this.hostVillagePopulation = hostVillagePopulation;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(String totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public String getWaterStorage() {
        return waterStorage;
    }

    public void setWaterStorage(String waterStorage) {
        this.waterStorage = waterStorage;
    }

    public String getTechnicalSectionNumber() {
        return technicalSectionNumber;
    }

    public void setTechnicalSectionNumber(String technicalSectionNumber) {
        this.technicalSectionNumber = technicalSectionNumber;
    }

    public String getTechnicalSectionDate() {
        return technicalSectionDate;
    }

    public void setTechnicalSectionDate(String technicalSectionDate) {
        this.technicalSectionDate = technicalSectionDate;
    }

    public String getAdministrativeEstimateAmount() {
        return administrativeEstimateAmount;
    }

    public void setAdministrativeEstimateAmount(String administrativeEstimateAmount) {
        this.administrativeEstimateAmount = administrativeEstimateAmount;
    }

    public String getApprxWorkingHrs() {
        return apprxWorkingHrs;
    }

    public void setApprxWorkingHrs(String apprxWorkingHrs) {
        this.apprxWorkingHrs = apprxWorkingHrs;
    }

    public String getApprxDieselConsumptionLt() {
        return apprxDieselConsumptionLt;
    }

    public void setApprxDieselConsumptionLt(String apprxDieselConsumptionLt) {
        this.apprxDieselConsumptionLt = apprxDieselConsumptionLt;
    }

    public String getApprxDieselConsumptionRs() {
        return apprxDieselConsumptionRs;
    }

    public void setApprxDieselConsumptionRs(String apprxDieselConsumptionRs) {
        this.apprxDieselConsumptionRs = apprxDieselConsumptionRs;
    }

    public String getApprxEstimateQunty() {
        return apprxEstimateQunty;
    }

    public void setApprxEstimateQunty(String apprxEstimateQunty) {
        this.apprxEstimateQunty = apprxEstimateQunty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return _long;
    }

    public void setLong(double _long) {
        this._long = _long;
    }

    public String getFfId() {
        return ffId;
    }

    public void setFfId(String ffId) {
        this.ffId = ffId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHostVillageID() {
        return hostVillageID;
    }

    public void setHostVillageID(String hostVillageID) {
        this.hostVillageID = hostVillageID;
    }
}
