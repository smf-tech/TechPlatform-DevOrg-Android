package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiltTransportRecord implements Serializable {
    @SerializedName("structure_id")
    @Expose
    private String structureId;

//    @SerializedName("machine_id")
//    @Expose
//    private String machineId;

    @SerializedName("transport_date")
    @Expose
    private long siltTransportDate;

    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
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

    @SerializedName("b_type_id")
    @Expose
    private String bTypeId;

    @SerializedName("survey_no")
    @Expose
    private String surveyNo;

    @SerializedName("area")
    @Expose
    private String area;

    @SerializedName("b_first_name")
    @Expose
    private String bFirstName;
    @SerializedName("b_last_name")
    @Expose
    private String bLastName;
    @SerializedName("b_mobile")
    @Expose
    private String bMobile;

    @SerializedName("tractor_trips")
    @Expose
    private String tractorTripsCount;

    @SerializedName("tipper_trips")
    @Expose
    private String tipperTripsCount;

    @SerializedName("hyva_trips")
    @Expose
    private String hyvaTripsCount;

//    @SerializedName("farmer_count")
//    @Expose
//    private String farmersCount;

//    @SerializedName("beneficiaries_count")
//    @Expose
//    private String beneficiariesCount;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("log")
    @Expose
    private double log;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

//    public String getMachineId() {
//        return machineId;
//    }
//
//    public void setMachineId(String machineId) {
//        this.machineId = machineId;
//    }

    public long getSiltTransportDate() {
        return siltTransportDate;
    }

    public void setSiltTransportDate(long siltTransportDate) {
        this.siltTransportDate = siltTransportDate;
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

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }


    public String getbTypeId() {
        return bTypeId;
    }

    public void setbTypeId(String bTypeId) {
        this.bTypeId = bTypeId;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getArea() {
        return area;
    }

    public String getHyvaTripsCount() {
        return hyvaTripsCount;
    }

    public void setHyvaTripsCount(String hyvaTripsCount) {
        this.hyvaTripsCount = hyvaTripsCount;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getbFirstName() {
        return bFirstName;
    }

    public void setbFirstName(String bFirstName) {
        this.bFirstName = bFirstName;
    }

    public String getbLastName() {
        return bLastName;
    }

    public void setbLastName(String bLastName) {
        this.bLastName = bLastName;
    }

    public String getbMobile() {
        return bMobile;
    }

    public void setbMobile(String bMobile) {
        this.bMobile = bMobile;
    }


    public String getTractorTripsCount() {
        return tractorTripsCount;
    }

    public void setTractorTripsCount(String tractorTripsCount) {
        this.tractorTripsCount = tractorTripsCount;
    }

    public String getTipperTripsCount() {
        return tipperTripsCount;
    }

    public void setTipperTripsCount(String tipperTripsCount) {
        this.tipperTripsCount = tipperTripsCount;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

//    public String getFarmersCount() {
//        return farmersCount;
//    }
//
//    public void setFarmersCount(String farmersCount) {
//        this.farmersCount = farmersCount;
//    }
//
//    public String getBeneficiariesCount() {
//        return beneficiariesCount;
//    }
//
//    public void setBeneficiariesCount(String beneficiariesCount) {
//        this.beneficiariesCount = beneficiariesCount;
//    }

}
