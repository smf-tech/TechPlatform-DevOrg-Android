package com.octopusbjsindia.models.smartgirl;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class SmartGirlCategoryResponseData {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("name")
        @Expose
        private Name name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("project_id")
        @Expose
        private String project_id;
        @SerializedName("is_deleted")
        @Expose
        private Integer is_deleted;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("createdDateTime")
        @Expose
        private Integer createdDateTime;
        @SerializedName("updatedDateTime")
        @Expose
        private Integer updatedDateTime;
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

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public Integer getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(Integer is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(Integer createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public Integer getUpdatedDateTime() {
            return updatedDateTime;
        }

        public void setUpdatedDateTime(Integer updatedDateTime) {
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

    }
