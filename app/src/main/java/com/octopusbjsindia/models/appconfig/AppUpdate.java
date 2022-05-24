package com.octopusbjsindia.models.appconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppUpdate {

    @SerializedName("logo_path")
    @Expose
    private String logo_path;
    @SerializedName("terms_url_mob")
    @Expose
    private String terms_url_mob;

    @SerializedName("support")
    @Expose
    private String support;

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

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getTerms_url_mob() {
        return terms_url_mob;
    }

    public void setTerms_url_mob(String terms_url_mob) {
        this.terms_url_mob = terms_url_mob;
    }
}
