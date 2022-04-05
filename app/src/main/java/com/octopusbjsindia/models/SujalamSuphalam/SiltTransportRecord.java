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

    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("districtId")
    @Expose
    private String districtId;

    @SerializedName("districtName")
    @Expose
    private String districtName;
        @SerializedName("talukaId")
    @Expose
    private String talukaId;
    @SerializedName("talukaName")
    @Expose
    private String talukaName;
    @SerializedName("villageId")
    @Expose
    private String villageId;
    @SerializedName("villageName")
    @Expose
    private String villageName;

    @SerializedName("b_type_id")
    @Expose
    private String bTypeId;

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

//    @SerializedName("farmer_count")
//    @Expose
//    private String farmersCount;

//    @SerializedName("beneficiaries_count")
//    @Expose
//    private String beneficiariesCount;

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
