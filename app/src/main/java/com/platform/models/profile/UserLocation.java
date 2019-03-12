package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class UserLocation {

    @SerializedName("state")
    private List<String> stateId;

    @SerializedName("district")
    private List<String> districtIds;

    @SerializedName("taluka")
    private List<String> talukaIds;

    @SerializedName("cluster")
    private List<String> clusterIds;

    @SerializedName("village")
    private List<String> villageIds;

    public List<String> getStateId() {
        return stateId;
    }

    public void setStateId(List<String> stateId) {
        this.stateId = stateId;
    }

    public List<String> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<String> districtIds) {
        this.districtIds = districtIds;
    }

    public List<String> getTalukaIds() {
        return talukaIds;
    }

    public void setTalukaIds(List<String> talukaIds) {
        this.talukaIds = talukaIds;
    }

    public List<String> getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(List<String> clusterIds) {
        this.clusterIds = clusterIds;
    }

    public List<String> getVillageIds() {
        return villageIds;
    }

    public void setVillageIds(List<String> villageIds) {
        this.villageIds = villageIds;
    }
}
