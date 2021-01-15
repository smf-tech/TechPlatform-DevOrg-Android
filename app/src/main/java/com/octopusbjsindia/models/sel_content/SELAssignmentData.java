package com.octopusbjsindia.models.sel_content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SELAssignmentData implements Serializable {

    @SerializedName("formName")
    @Expose
    private String formName;
    @SerializedName("formId")
    @Expose
    private String formId;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

}
