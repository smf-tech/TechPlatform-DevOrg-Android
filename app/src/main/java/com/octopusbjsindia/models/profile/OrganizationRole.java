package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class OrganizationRole {
    @Expose
    private String id;
    @Expose
    private String displayName;
    @Expose
    private JurisdictionLevel jurisdictionLevel;
    @Expose
    private Project project;

    private String orgName;

    private String orgId;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public JurisdictionLevel getJurisdictionLevel() {
        return jurisdictionLevel;
    }

    public void setJurisdictionLevel(JurisdictionLevel jurisdictionLevel) {
        this.jurisdictionLevel = jurisdictionLevel;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
