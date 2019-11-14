package com.platform.models.SujalamSuphalam;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StructureVisitMonitoringData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    private int id;
    @ColumnInfo(name = "structure_id")
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @ColumnInfo(name = "is_safety_signage")
    @SerializedName("is_safety_signage")
    @Expose
    private boolean isSafetySignage;
    @ColumnInfo(name = "is_guidelines_followed")
    @SerializedName("is_guidelines_followed")
    @Expose
    private boolean isGuidelinesFollowed;
    @ColumnInfo(name = "status_record_id")
    @SerializedName("status_record_id")
    @Expose
    private String statusRecordId;
    @ColumnInfo(name = "issue_related")
    @SerializedName("issue_related")
    @Expose
    private String issueRelated;
    @ColumnInfo(name = "issue_description")
    @SerializedName("issue_description")
    @Expose
    private String issueDescription;
    @ColumnInfo(name = "structureImage")
    private String structureImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getStructureImage() {
        return structureImage;
    }

    public void setStructureImage(String structureImage) {
        this.structureImage = structureImage;
    }
}
