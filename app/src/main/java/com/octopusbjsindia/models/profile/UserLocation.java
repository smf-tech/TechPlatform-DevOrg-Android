package com.octopusbjsindia.models.profile;


import java.util.List;

@SuppressWarnings("unused")
public class UserLocation {
    private List<JurisdictionType> countryId;

    private List<JurisdictionType> stateId;

    private List<JurisdictionType> districtIds;

    private List<JurisdictionType> cityIds;

    private List<JurisdictionType> talukaIds;

    private List<JurisdictionType> clusterIds;

    private List<JurisdictionType> granpanchayatIds;

    private List<JurisdictionType> villageIds;

    private List<JurisdictionType> schoolIds;

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
