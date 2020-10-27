package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JurisdictionLevel {

    @SerializedName("_id")
    private String id;

    @SerializedName("levelName")
    private String levelName;

    @SerializedName("state_id")
    private String stateId;

    @SerializedName("district_id")
    private String districtId;

    @SerializedName("Name")
    private String jurisdictionLevelName;

    @SerializedName("updatedDateTime")
    private Long updatedAt;

    @SerializedName("createdDateTime")
    private Long createdAt;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getJurisdictionLevelName() {
        return jurisdictionLevelName;
    }

    public void setJurisdictionLevelName(String jurisdictionLevelName) {
        this.jurisdictionLevelName = jurisdictionLevelName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
