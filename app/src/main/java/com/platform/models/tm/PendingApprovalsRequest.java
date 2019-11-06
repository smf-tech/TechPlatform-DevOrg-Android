package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class PendingApprovalsRequest{

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("approve_status")
    @Expose
    private String approve_status;
    @SerializedName("createdDateTime")
    @Expose
    private int createdDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private int updatedDateTime;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("location")
    @Expose
    private LocationOfRequester location;
    @SerializedName("dob")
    @Expose
    private long dob;
    @SerializedName("firebase_id")
    @Expose
    private String firebase_id;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("org_id")
    @Expose
    private String org_id;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("project_id")
    @Expose
    private List<String> project_id = null;
    @SerializedName("role_id")
    @Expose
    private String role_id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("is_admin")
    @Expose
    private boolean is_admin;
    @SerializedName("userName")
    @Expose
    private String userName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public int getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(int createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(int updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public LocationOfRequester getLocation() {
        return location;
    }

    public void setLocation(LocationOfRequester location) {
        this.location = location;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public List<String> getProject_id() {
        return project_id;
    }

    public void setProject_id(List<String> project_id) {
        this.project_id = project_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}