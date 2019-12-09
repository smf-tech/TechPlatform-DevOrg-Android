package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class JurisdictionLevelData {
    @SerializedName("levelName")
    private String levelName;

    @SerializedName("list")
    private List<JurisdictionLevel> jurisdictionLevelList;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public List<JurisdictionLevel> getJurisdictionLevelList() {
        return jurisdictionLevelList;
    }

    public void setJurisdictionLevelList(List<JurisdictionLevel> jurisdictionLevelList) {
        this.jurisdictionLevelList = jurisdictionLevelList;
    }
}
