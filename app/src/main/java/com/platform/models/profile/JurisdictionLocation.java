package com.platform.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JurisdictionLocation {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("jurisdiction_type_id")
    @Expose
    private String jurisdictionTypeId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("village_id")
    @Expose
    private String villageId;
    @SerializedName("country")
    @Expose
    private JurisdictionType country;
    @SerializedName("state")
    @Expose
    private JurisdictionType state;
    @SerializedName("district")
    @Expose
    private JurisdictionType district;
    @SerializedName("city")
    @Expose
    private JurisdictionType city;
    @SerializedName("taluka")
    @Expose
    private JurisdictionType taluka;
    @SerializedName("village")
    @Expose
    private JurisdictionType village;
    @SerializedName("cluster")
    @Expose
    private JurisdictionType cluster;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdAt;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedAt;

    public String getJurisdictionTypeId() {
        return jurisdictionTypeId;
    }

    public void setJurisdictionTypeId(String jurisdictionTypeId) {
        this.jurisdictionTypeId = jurisdictionTypeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public JurisdictionType getCountry() {
        return country;
    }

    public void setCountry(JurisdictionType country) {
        this.country = country;
    }

    public JurisdictionType getState() {
        return state;
    }

    public void setState(JurisdictionType state) {
        this.state = state;
    }

    public JurisdictionType getDistrict() {
        return district;
    }

    public void setDistrict(JurisdictionType district) {
        this.district = district;
    }

    public JurisdictionType getCity() { return city; }

    public void setCity(JurisdictionType city) { this.city = city; }

    public JurisdictionType getTaluka() {
        return taluka;
    }

    public void setTaluka(JurisdictionType taluka) {
        this.taluka = taluka;
    }

    public JurisdictionType getVillage() {
        return village;
    }

    public void setVillage(JurisdictionType village) {
        this.village = village;
    }

    public JurisdictionType getCluster() {
        return cluster;
    }

    public void setCluster(JurisdictionType cluster) {
        this.cluster = cluster;
    }
}
