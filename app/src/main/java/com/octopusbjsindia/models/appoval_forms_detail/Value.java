package com.octopusbjsindia.models.appoval_forms_detail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("$oid")
    @Expose
    private String $oid;
    @SerializedName("form_title")
    @Expose
    private String form_title;
    @SerializedName("form_id")
    @Expose
    private String form_id;
    @SerializedName("updatedDateTime")
    @Expose
    private Integer updatedDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private String result;

    public String get$oid() {
        return $oid;
    }

    public void set$oid(String $oid) {
        this.$oid = $oid;
    }

    public String getForm_title() {
        return form_title;
    }

    public void setForm_title(String form_title) {
        this.form_title = form_title;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Integer getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Integer updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}