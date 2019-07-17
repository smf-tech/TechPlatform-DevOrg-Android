package com.platform.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMUserFormsApprovalRequest {
    @SerializedName("form_detail")
    @Expose
    private List<Form_detail> form_detail = null;
    @SerializedName("form_title")
    @Expose
    private String form_title;
    @SerializedName("survey_name")
    @Expose
    private Survey_name survey_name;

    public List<Form_detail> getForm_detail() {
        return form_detail;
    }

    public void setForm_detail(List<Form_detail> form_detail) {
        this.form_detail = form_detail;
    }

    public String getForm_title() {
        return form_title;
    }

    public void setForm_title(String form_title) {
        this.form_title = form_title;
    }

    public Survey_name getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(Survey_name survey_name) {
        this.survey_name = survey_name;
    }

    public class Form_detail {

        @SerializedName("_id")
        @Expose
        private _id _id;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("survey_id")
        @Expose
        private String survey_id;

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
}
