package com.octopus.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMUserFormsApprovalRequest {

    @SerializedName("data")
    @Expose
    private List<Form_detail> data = null;
    @SerializedName("message")
    @Expose
    private String message;


    public List<Form_detail> getData() {
        return data;
    }

    public void setData(List<Form_detail> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Form_detail {

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
            private _id _id;
            @SerializedName("userName")
            @Expose
            private String userName;
            @SerializedName("survey_id")
            @Expose
            private String survey_id;
            @SerializedName("form_title")
            @Expose
            private String form_title;
            @SerializedName("microservice_id")
            @Expose
            private String microservice_id;
            @SerializedName("survey_name")
            @Expose
            private Survey_name survey_name;

        @SerializedName("entity_id")
        @Expose
        private String entity_id;

            public _id get_id() {
            return _id;
        }

            public void set_id(_id _id) {
            this._id = _id;
        }

            public String getUserName() {
            return userName;
        }

            public void setUserName(String userName) {
            this.userName = userName;
        }

            public String getSurvey_id() {
            return survey_id;
        }

            public void setSurvey_id(String survey_id) {
            this.survey_id = survey_id;
        }

            public String getForm_title() {
            return form_title;
        }

            public void setForm_title(String form_title) {
            this.form_title = form_title;
        }

            public String getMicroservice_id() {
            return microservice_id;
        }

            public void setMicroservice_id(String microservice_id) {
            this.microservice_id = microservice_id;
        }

            public Survey_name getSurvey_name() {
            return survey_name;
        }

            public void setSurvey_name(Survey_name survey_name) {
            this.survey_name = survey_name;
        }

        public String getEntity_id() {
            return entity_id;
        }

        public void setEntity_id(String entity_id) {
            this.entity_id = entity_id;
        }
    }

    public class Survey_name {


        @SerializedName("default")
        @Expose
        private String _default;
        @SerializedName("mr")
        @Expose
        private String mr;
        @SerializedName("hi")
        @Expose
        private String hi;

        public String getDefault() {
            return _default;
        }

        public void setDefault(String _default) {
            this._default = _default;
        }

        public String getMr() {
            return mr;
        }

        public void setMr(String mr) {
            this.mr = mr;
        }

        public String getHi() {
            return hi;
        }

        public void setHi(String hi) {
            this.hi = hi;
        }

    }

    public class _id {

        @SerializedName("$oid")
        @Expose
        private String $oid;

        public String get$oid() {
            return $oid;
        }

        public void set$oid(String $oid) {
            this.$oid = $oid;
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





