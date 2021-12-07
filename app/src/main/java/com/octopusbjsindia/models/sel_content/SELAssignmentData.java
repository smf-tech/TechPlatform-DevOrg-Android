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
    @SerializedName("isFormSubmitted")
    @Expose
    private boolean isFormSubmitted = false;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;

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

    public boolean isFormSubmitted() {
        return isFormSubmitted;
    }

    public void setFormSubmitted(boolean formSubmitted) {
        isFormSubmitted = formSubmitted;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
