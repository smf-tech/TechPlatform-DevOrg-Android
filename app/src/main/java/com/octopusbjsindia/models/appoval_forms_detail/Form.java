package com.octopusbjsindia.models.appoval_forms_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Form {

    @SerializedName("submit_count")
    @Expose
    private Integer submit_count;
    @SerializedName("form_id")
    @Expose
    private String form_id;

    public Integer getSubmit_count() {
        return submit_count;
    }

    public void setSubmit_count(Integer submit_count) {
        this.submit_count = submit_count;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

}