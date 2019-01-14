package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JurisdictionLevel {
    @SerializedName("_id")
    private String id;

    @SerializedName("state_id")
    private String stateId;

    @SerializedName("district_id")
    private String districtId;

    @SerializedName("Name")
    private String jurisdictionLevelName;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("created_at")
    private String createdAt;

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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
