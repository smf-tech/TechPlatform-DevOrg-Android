package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class UserLocation {
    @SerializedName("country")
    private List<JurisdictionType> countryId;

    @SerializedName("state")
    private List<JurisdictionType> stateId;

    @SerializedName("district")
    private List<JurisdictionType> districtIds;

    @SerializedName("city")
    private List<JurisdictionType> cityIds;

    @SerializedName("taluka")
    private List<JurisdictionType> talukaIds;

    @SerializedName("cluster")
    private List<JurisdictionType> clusterIds;

    @SerializedName("gram_panchayat")
    private List<JurisdictionType> granpanchayatIds;

    @SerializedName("village")
    private List<JurisdictionType> villageIds;

    @SerializedName("school")
    private List<JurisdictionType> schoolIds;

    @SerializedName("learning_center")
    private List<JurisdictionType> learningCenterIds;

    public List<JurisdictionType> getCountryId() {
        return countryId;
    }

    public void setCountryId(List<JurisdictionType> countryId) {
        this.countryId = countryId;
    }

    public List<JurisdictionType> getStateId() {
        return stateId;
    }

    public void setStateId(List<JurisdictionType> stateId) {
        this.stateId = stateId;
    }

    public List<JurisdictionType> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<JurisdictionType> districtIds) {
        this.districtIds = districtIds;
    }

    public List<JurisdictionType> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<JurisdictionType> cityIds) {
        this.cityIds = cityIds;
    }

    public List<JurisdictionType> getTalukaIds() {
        return talukaIds;
    }

    public void setTalukaIds(List<JurisdictionType> talukaIds) {
        this.talukaIds = talukaIds;
    }

    public List<JurisdictionType> getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(List<JurisdictionType> clusterIds) {
        this.clusterIds = clusterIds;
    }

    public List<JurisdictionType> getVillageIds() {
        return villageIds;
    }

    public void setVillageIds(List<JurisdictionType> villageIds) {
        this.villageIds = villageIds;
    }

    public List<JurisdictionType> getSchoolIds() {
        return schoolIds;
    }

    public void setSchoolIds(List<JurisdictionType> schoolIds) {
        this.schoolIds = schoolIds;
    }

    public List<JurisdictionType> getGranpanchayatIds() {
        return granpanchayatIds;
    }

    public void setGranpanchayatIds(List<JurisdictionType> granpanchayatIds) {
        this.granpanchayatIds = granpanchayatIds;
    }

    public List<JurisdictionType> getLearningCenterIds() {
        return learningCenterIds;
    }

    public void setLearningCenterIds(List<JurisdictionType> learningCenterIds) {
        this.learningCenterIds = learningCenterIds;
    }
}
