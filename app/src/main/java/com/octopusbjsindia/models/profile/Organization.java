package com.octopusbjsindia.models.profile;

import android.text.TextUtils;

import com.octopusbjsindia.utility.Constants;

@SuppressWarnings("unused")
public class Organization {
    private String id;

    private String orgName;

    private String serviceType;

    private Long updatedAt;

    private Long createdAt;

    private String type;

    private String associateOrgId;

    public String getId() {
        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(Constants.AssociatesType.ORG)) {
            return id;
        } else {
            return getAssociateOrgId();
        }
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String getAssociateOrgId() {
        return associateOrgId;
    }

    public void setAssociateOrgId(String associateOrgId) {
        this.associateOrgId = associateOrgId;
    }
}
