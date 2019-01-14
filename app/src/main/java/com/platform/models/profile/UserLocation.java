package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class UserLocation {

    @SerializedName("state")
    private String stateId;

    @SerializedName("district")
    private ArrayList<String> districtIds;

    @SerializedName("taluka")
    private ArrayList<String> talukaIds;

    @SerializedName("cluster")
    private ArrayList<String> clusterIds;

    @SerializedName("village")
    private ArrayList<String> villageIds;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public ArrayList<String> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(ArrayList<String> districtIds) {
        this.districtIds = districtIds;
    }

    public ArrayList<String> getTalukaIds() {
        return talukaIds;
    }

    public void setTalukaIds(ArrayList<String> talukaIds) {
        this.talukaIds = talukaIds;
    }

    public ArrayList<String> getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(ArrayList<String> clusterIds) {
        this.clusterIds = clusterIds;
    }

    public ArrayList<String> getVillageIds() {
        return villageIds;
    }

    public void setVillageIds(ArrayList<String> villageIds) {
        this.villageIds = villageIds;
    }
}
