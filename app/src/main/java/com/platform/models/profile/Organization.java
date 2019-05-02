package com.platform.models.profile;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.platform.utility.Constants;

@SuppressWarnings("unused")
public class Organization {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String orgName;

    @SerializedName("service")
    private String serviceType;

    @SerializedName("updatedDateTime")
    private Long updatedAt;

    @SerializedName("createdDateTime")
    private Long createdAt;

    @SerializedName("type")
    private String type;

    @SerializedName("associateOrgId")
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
