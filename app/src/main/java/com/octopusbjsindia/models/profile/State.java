package com.octopusbjsindia.models.profile;

import java.util.List;

@SuppressWarnings("unused")
public class State {
    private String id;

    private String orgName;

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
