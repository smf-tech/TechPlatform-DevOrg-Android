package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructureVisitMonitoringRequest {
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("is_safety_signage")
    @Expose
    private boolean isSafetySignage;
    @SerializedName("is_guidelines_followed")
    @Expose
    private boolean isGuidelinesFollowed;
    @SerializedName("status_record_id")
    @Expose
    private String statusRecordId;
    @SerializedName("issue_related")
    @Expose
    private String issueRelated;
    @SerializedName("issue_description")
    @Expose
    private String issueDescription;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public boolean getIsSafetySignage() {
        return isSafetySignage;
    }

    public void setIsSafetySignage(boolean isSafetySignage) {
        this.isSafetySignage = isSafetySignage;
    }

    public boolean getIsGuidelinesFollowed() {
        return isGuidelinesFollowed;
    }

    public void setIsGuidelinesFollowed(boolean isGuidelinesFollowed) {
        this.isGuidelinesFollowed = isGuidelinesFollowed;
    }

    public String getStatusRecordId() {
        return statusRecordId;
    }

    public void setStatusRecordId(String statusRecordId) {
        this.statusRecordId = statusRecordId;
    }

    public String getIssueRelated() {
        return issueRelated;
    }

    public void setIssueRelated(String issueRelated) {
        this.issueRelated = issueRelated;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

}
