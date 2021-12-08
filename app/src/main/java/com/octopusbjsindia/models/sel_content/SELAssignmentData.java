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
    private String answersPdfUrl;
    private String answersPdfName;
    private boolean isDownloadStarted = false;

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

    public String getAnswersPdfUrl() {
        return answersPdfUrl;
    }

    public void setAnswersPdfUrl(String answersPdfUrl) {
        this.answersPdfUrl = answersPdfUrl;
    }

    public String getAnswersPdfName() {
        return answersPdfName;
    }

    public void setAnswersPdfName(String answersPdfName) {
        this.answersPdfName = answersPdfName;
    }

    public boolean isDownloadStarted() {
        return isDownloadStarted;
    }

    public void setDownloadStarted(boolean downloadStarted) {
        isDownloadStarted = downloadStarted;
    }

}
