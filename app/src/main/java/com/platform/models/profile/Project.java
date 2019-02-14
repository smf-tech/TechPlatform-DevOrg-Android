package com.platform.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Project {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("jurisdiction_type_id")
    @Expose
    private String jurisdictionTypeId;
    @SerializedName("jurisdictions")
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
