package com.octopusbjsindia.models.profile;

@SuppressWarnings("unused")
public class JurisdictionLevel {
    private String id;

    private String levelName;

    private String stateId;

    private String districtId;

    private String jurisdictionLevelName;

    private Long updatedAt;

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
