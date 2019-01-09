package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class State {
    @SerializedName("_id")
    private String id;

    @SerializedName("Name")
    private String orgName;

    @SerializedName("jurisdictions")
    private List<Jurisdiction> jurisdictions;

    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(List<Jurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
