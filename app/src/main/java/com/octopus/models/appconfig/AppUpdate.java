package com.octopus.models.appconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUpdate {

    @SerializedName("octopusAppVersion")
    @Expose
    private String octopusAppVersion;
    @SerializedName("octopusForceUpdateRequired")
    @Expose
    private Boolean octopusForceUpdateRequired;
    @SerializedName("octopusAppVersioniOS")
    @Expose
    private String octopusAppVersioniOS;
    @SerializedName("octopusForceUpdateRequirediOS")
    @Expose
    private Boolean octopusForceUpdateRequirediOS;

    public String getOctopusAppVersion() {
        return octopusAppVersion;
    }

    public void setOctopusAppVersion(String octopusAppVersion) {
        this.octopusAppVersion = octopusAppVersion;
    }

    public Boolean getOctopusForceUpdateRequired() {
        return octopusForceUpdateRequired;
    }

    public void setOctopusForceUpdateRequired(Boolean octopusForceUpdateRequired) {
        this.octopusForceUpdateRequired = octopusForceUpdateRequired;
    }

    public String getOctopusAppVersioniOS() {
        return octopusAppVersioniOS;
    }

    public void setOctopusAppVersioniOS(String octopusAppVersioniOS) {
        this.octopusAppVersioniOS = octopusAppVersioniOS;
    }

    public Boolean getOctopusForceUpdateRequirediOS() {
        return octopusForceUpdateRequirediOS;
    }

    public void setOctopusForceUpdateRequirediOS(Boolean octopusForceUpdateRequirediOS) {
        this.octopusForceUpdateRequirediOS = octopusForceUpdateRequirediOS;
    }

}
