package com.platform.models.forms;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@SuppressWarnings("unused")
public class FormResult {

    @PrimaryKey
    @ColumnInfo(name = "result_id")
    @SerializedName("_id")
    @Expose
    @NonNull
    private String _id = "0";

    @ColumnInfo(name = "form_id")
    @SerializedName("form_id")
    @Expose
    private String formId;

    @ColumnInfo(name = "result")
    private String result;

    @ColumnInfo(name = "form_name")
    private String formName;

    @ColumnInfo(name = "form_status")
    private int mFormStatus;

    @ColumnInfo(name = "form_category")
    private String formCategory;

    @ColumnInfo(name = "request_object")
    private String requestObject;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull final String _id) {
        this._id = _id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(final String formId) {
        this.formId = formId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(final String formName) {
        this.formName = formName;
    }

    public String getFormCategory() {
        return formCategory;
    }

    public void setFormCategory(final String formCategory) {
        this.formCategory = formCategory;
    }

    public String getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(final String requestObject) {
        this.requestObject = requestObject;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final long createdAt) {
        this.createdAt = createdAt;
    }

    public int getFormStatus() {
        return mFormStatus;
    }

    public void setFormStatus(final int formStatus) {
        mFormStatus = formStatus;
    }
}
