package com.octopus.models.role;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Role {

    @SerializedName("_id")
    private String id;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("jurisdiction")
    private String jurisdiction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }
}
