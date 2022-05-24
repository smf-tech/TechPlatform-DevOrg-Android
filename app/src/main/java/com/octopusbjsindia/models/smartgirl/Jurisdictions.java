package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.profile.JurisdictionLevel;
import com.octopusbjsindia.models.profile.Project;

public class Jurisdictions {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("role_code")
    @Expose
    private String roleCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("org_id")
    @Expose
    private String orgId;
    @SerializedName("checkin_time")
    @Expose
    private Integer checkinTime;
    @SerializedName("checkout_time")
    @Expose
    private Integer checkoutTime;
    @SerializedName("is_checktiming_required")
    @Expose
    private Integer isChecktimingRequired;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("guard_name")
    @Expose
    private String guardName;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("jurisdictionLevel")
    @Expose
    private JurisdictionLevel jurisdictionLevel;
    @SerializedName("project")
    @Expose
    private Project project;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Integer checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Integer getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Integer checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Integer getIsChecktimingRequired() {
        return isChecktimingRequired;
    }

    public void setIsChecktimingRequired(Integer isChecktimingRequired) {
        this.isChecktimingRequired = isChecktimingRequired;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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