package com.octopusbjsindia.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.UserLocation;

@SuppressWarnings("unused")
public class Approver {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("approve_status")
    @Expose
    private String approveStatus;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedAt;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdAt;
    @SerializedName("dob")
    @Expose
    private Long dob;
    @SerializedName("firebase_id")
    @Expose
    private String firebaseId;
    @SerializedName("location")
    @Expose
    private UserLocation location;
    @SerializedName("org_id")
    @Expose
    private JurisdictionType orgId;
    @SerializedName("role_id")
    @Expose
    private JurisdictionType roleId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
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

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public String getOrgId() {
        return orgId.getId();
    }

    public void setOrgId(JurisdictionType orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId.getId();
    }

    public void setRoleId(JurisdictionType roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
