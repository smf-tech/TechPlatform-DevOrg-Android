package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Jurisdiction {
    @SerializedName("_id")
    private String id;

    @SerializedName("state_id")
    private String stateId;

    @SerializedName("jurisdiction_id")
    private String jurisdictionId;

    @SerializedName("level")
    private int level;

    @SerializedName("levelName")
    private String levelName;

    @SerializedName("updatedDateTime")
    private Long updatedAt;

    @SerializedName("createdDateTime")
    private Long createdAt;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getJurisdictionId() {
        return jurisdictionId;
    }

    public void setJurisdictionId(String jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
