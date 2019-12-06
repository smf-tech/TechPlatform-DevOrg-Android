
package com.octopus.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attendance implements Serializable
{

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("check_in")
        @Expose
        private CheckIn checkIn;
        @SerializedName("check_out")
        @Expose
        private CheckOut checkOut;
        @SerializedName("status")
        @Expose
        private Status status;
        @SerializedName("created_on")
        @Expose
        private String createdOn;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("updated_on")
        @Expose
        private String updatedOn;
        @SerializedName("updated_by")
        @Expose
        private String updatedBy;
        @SerializedName("org_id")
        @Expose
        private String orgId;
        @SerializedName("project_id")
        @Expose
        private String projectId;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        @SerializedName("created_at")
        @Expose
        private String createdAt;

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    @SerializedName("totalHours")
        @Expose
        private String totalHours;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public CheckIn getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(CheckIn checkIn) {
            this.checkIn = checkIn;
        }

        public CheckOut getCheckOut() {
            return checkOut;
        }

        public void setCheckOut(CheckOut checkOut) {
            this.checkOut = checkOut;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
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
