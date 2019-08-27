package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class UserLocation {

    @SerializedName("state")
    private List<JurisdictionType> stateId;

    @SerializedName("district")
    private List<JurisdictionType> districtIds;

    public List<JurisdictionType> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<JurisdictionType> cityIds) {
        this.cityIds = cityIds;
    }

    @SerializedName("city")
    private List<JurisdictionType> cityIds;

    @SerializedName("taluka")
    private List<JurisdictionType> talukaIds;

    @SerializedName("cluster")
    private List<JurisdictionType> clusterIds;

    @SerializedName("village")
    private List<JurisdictionType> villageIds;

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
}
