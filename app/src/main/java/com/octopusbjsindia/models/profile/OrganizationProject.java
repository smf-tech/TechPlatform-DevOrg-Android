package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrganizationProject {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String orgProjectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgProjectName() {
        return orgProjectName;
    }

    public void setOrgProjectName(String orgProjectName) {
        this.orgProjectName = orgProjectName;
    }
}
