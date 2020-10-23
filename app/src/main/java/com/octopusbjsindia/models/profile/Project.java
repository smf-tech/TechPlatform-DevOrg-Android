package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Project {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String jurisdictionTypeId;
    @Expose
    private List<Jurisdiction> jurisdictions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJurisdictionTypeId() {
        return jurisdictionTypeId;
    }

    public void setJurisdictionTypeId(String jurisdictionTypeId) {
        this.jurisdictionTypeId = jurisdictionTypeId;
    }

    public List<Jurisdiction> getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(List<Jurisdiction> jurisdictions) {
        this.jurisdictions = jurisdictions;
    }
}
