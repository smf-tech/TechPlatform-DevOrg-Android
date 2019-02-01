package com.platform.models.tm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PendingRequest {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String requesterName;

    @SerializedName("email")
    private String requesterEmail;

    @SerializedName("phone")
    private String requesterPhone;

    @SerializedName("dob")
    private String requesterDOB;

    @SerializedName("gender")
    private String requesterGender;

    @SerializedName("org_id")
    private String requesterOrgId;

    @SerializedName("role_id")
    private String requesterRoleId;

    @SerializedName("role_ids")
    private List<String> requesterRoleIds;

    @SerializedName("approved")
    private boolean isApproved;

    @SerializedName("approve_status")
    private String approveStatus;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }

    public String getRequesterDOB() {
        return requesterDOB;
    }

    public void setRequesterDOB(String requesterDOB) {
        this.requesterDOB = requesterDOB;
    }

    public String getRequesterGender() {
        return requesterGender;
    }

    public void setRequesterGender(String requesterGender) {
        this.requesterGender = requesterGender;
    }

    public String getRequesterOrgId() {
        return requesterOrgId;
    }

    public void setRequesterOrgId(String requesterOrgId) {
        this.requesterOrgId = requesterOrgId;
    }

    public String getRequesterRoleId() {
        return requesterRoleId;
    }

    public void setRequesterRoleId(String requesterRoleId) {
        this.requesterRoleId = requesterRoleId;
    }

    public List<String> getRequesterRoleIds() {
        return requesterRoleIds;
    }

    public void setRequesterRoleIds(List<String> requesterRoleIds) {
        this.requesterRoleIds = requesterRoleIds;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
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
}
