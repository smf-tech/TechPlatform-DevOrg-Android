package com.octopus.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMUserLeavesApprovalRequest {

    @SerializedName("application")
    @Expose
    private List<TMUserLeaveApplications> application = null;
    @SerializedName("leave_count")
    @Expose
    private List<Leave_count> leave_count = null;

    public List<TMUserLeaveApplications> getApplication() {
        return application;
    }

    public void setApplication(List<TMUserLeaveApplications> application) {
        this.application = application;
    }

    public List<Leave_count> getLeave_count() {
        return leave_count;
    }

    public void setLeave_count(List<Leave_count> leave_count) {
        this.leave_count = leave_count;
    }

    public class Leave_count {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("leave_balance")
        @Expose
        private List<Leave_balance> leave_balance = null;
        @SerializedName("default")
        @Expose
        private List<Object> _default = null;

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

        public List<Leave_balance> getLeave_balance() {
            return leave_balance;
        }

        public void setLeave_balance(List<Leave_balance> leave_balance) {
            this.leave_balance = leave_balance;
        }

        public List<Object> getDefault() {
            return _default;
        }

        public void setDefault(List<Object> _default) {
            this._default = _default;
        }

    }

    public class Leave_balance {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("balance")
        @Expose
        private String balance;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

    }
}