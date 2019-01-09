package com.platform.models.profile;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrganizationRole {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String orgName;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("org_id")
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
}
