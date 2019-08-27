package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMUserProfileApprovalRequest {

    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

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
    @SerializedName("org_id")
    @Expose
    private Org_id org_id;



    @SerializedName("role_id")
    @Expose
    private Role_id role_id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("location")
    @Expose
    private List<UserProfileLocation> location = null;

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

    public Org_id getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Org_id org_id) {
        this.org_id = org_id;
    }

    public Role_id getRole_id() {
        return role_id;
    }

    public void setRole_id(Role_id role_id) {
        this.role_id = role_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserProfileLocation> getLocation() {
        return location;
    }

    public void setLocation(List<UserProfileLocation> location) {
        this.location = location;
    }



    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public class Org_id {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("name")
        @Expose
        private String name;

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

    }
    public class Project_id {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("name")
        @Expose
        private String name;

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

    }

    public class Role_id {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("name")
        @Expose
        private String name;

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

    }

    public class UserProfileLocation {

        @SerializedName("value")
        @Expose
        private List<String> value = null;
        @SerializedName("display_name")
        @Expose
        private String display_name;

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

    }


    public class Status {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("action_by")
        @Expose
        private String action_by;
        @SerializedName("action_on")
        @Expose
        private String action_on;
        @SerializedName("rejection_reason")
        @Expose
        private String rejection_reason;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAction_by() {
            return action_by;
        }

        public void setAction_by(String action_by) {
            this.action_by = action_by;
        }

        public String getAction_on() {
            return action_on;
        }

        public void setAction_on(String action_on) {
            this.action_on = action_on;
        }

        public String getRejection_reason() {
            return rejection_reason;
        }

        public void setRejection_reason(String rejection_reason) {
            this.rejection_reason = rejection_reason;
        }

    }
}