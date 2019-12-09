package com.octopusbjsindia.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMUserLeaveApplications {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("leave_type")
    @Expose
    private String leave_type;
    @SerializedName("full_half_day")
    @Expose
    private String full_half_day;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("paid_leave")
    @Expose
    private boolean paid_leave;
    @SerializedName("status")
    @Expose
    private Status status;



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getFull_half_day() {
        return full_half_day;
    }

    public void setFull_half_day(String full_half_day) {
        this.full_half_day = full_half_day;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public boolean isPaid_leave() {
        return paid_leave;
    }

    public void setPaid_leave(boolean paid_leave) {
        this.paid_leave = paid_leave;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

/*    public List<Object> getDefault() {
        return _default;
    }

    public void setDefault(List<Object> _default) {
        this._default = _default;
    }*/

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