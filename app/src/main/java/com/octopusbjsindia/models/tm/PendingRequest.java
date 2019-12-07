package com.octopusbjsindia.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PendingRequest {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String requesterName;

    @SerializedName("firstname")
    private String requesterFirstName;

    @SerializedName("middlename")
    private String requesterMiddleName;

    @SerializedName("lastname")
    private String requesterLastName;

    @SerializedName("profile_pic")
    private String requesterProfilePic;

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

    @SerializedName("entity_id")
    @Expose
    private String entityId;
    @SerializedName("entity_type")
    @Expose
    private String entityType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedDateTime;
    @SerializedName("entity")
    @Expose
    private TMEntity entity;
    @SerializedName("approver_ids")
    @Expose
    private List<String> approverIds = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequesterFirstName() {
        return requesterFirstName;
    }

    public void setRequesterFirstName(String requesterFirstName) {
        this.requesterFirstName = requesterFirstName;
    }

    public String getRequesterMiddleName() {
        return requesterMiddleName;
    }

    public void setRequesterMiddleName(String requesterMiddleName) {
        this.requesterMiddleName = requesterMiddleName;
    }

    public String getRequesterLastName() {
        return requesterLastName;
    }

    public void setRequesterLastName(String requesterLastName) {
        this.requesterLastName = requesterLastName;
    }

    public String getRequesterProfilePic() {
        return requesterProfilePic;
    }

    public void setRequesterProfilePic(String requesterProfilePic) {
        this.requesterProfilePic = requesterProfilePic;
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

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Long updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public TMEntity getEntity() {
        return entity;
    }

    public void setEntity(TMEntity entity) {
        this.entity = entity;
    }
}
