package com.octopus.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@SuppressWarnings("unused")
@Entity
public class SavedForm {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "form_id")
    private String formId;

    @ColumnInfo(name = "form_name")
    private String formName;

    @ColumnInfo(name = "is_synced")
    private Boolean isSynced;

    @ColumnInfo(name = "form_category")
    private String formCategory;

    @ColumnInfo(name = "request_object")
    private String requestObject;

    @ColumnInfo(name = "createdDateTime")
    private Long createdAt;

    public Boolean isSynced() {
        return isSynced;
    }

    public void setSynced(Boolean synced) {
        isSynced = synced;
    }

    public String getFormCategory() {
        return formCategory;
    }

    public void setFormCategory(String formCategory) {
        this.formCategory = formCategory;
    }

    public String getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(String requestObject) {
        this.requestObject = requestObject;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
