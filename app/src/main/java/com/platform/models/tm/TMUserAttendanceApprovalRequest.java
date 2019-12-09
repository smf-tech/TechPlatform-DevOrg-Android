package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMUserAttendanceApprovalRequest  {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("check_in")
    @Expose
    private Check_in check_in;
    @SerializedName("check_out")
    @Expose
    private Check_out check_out;
    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("created_on")
    @Expose
    private String created_on;
    @SerializedName("created_by")
    @Expose
    private String created_by;
    @SerializedName("updated_on")
    @Expose
    private String updated_on;
    @SerializedName("updated_by")
    @Expose
    private String updated_by;
    @SerializedName("org_id")
    @Expose
    private String org_id;
    @SerializedName("project_id")
    @Expose
    private String project_id;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Check_in getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Check_in check_in) {
        this.check_in = check_in;
    }

    public Check_out getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Check_out check_out) {
        this.check_out = check_out;
    }



    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public class Check_in {

        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("address")
        @Expose
        private String address;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    public class Check_out {

        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("address")
        @Expose
        private String address;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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