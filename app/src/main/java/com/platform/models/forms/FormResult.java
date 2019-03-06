package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.database.DataConverter;
import com.platform.models.LocaleData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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

    @ColumnInfo(name = "form_title")
    @SerializedName("form_title")
    @Expose
    private String formTitle;

    @ColumnInfo(name = "result")
    private String result;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "form_name")
    private LocaleData formName;

    @ColumnInfo(name = "form_status")
    private int mFormStatus;

    @ColumnInfo(name = "form_category")
    private String formCategory;

    @ColumnInfo(name = "request_object")
    private String requestObject;

    @ColumnInfo(name = "created_at")
    private String createdAt;

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

    public LocaleData getFormName() {
        return formName;
    }

    public void setFormName(final LocaleData formName) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFormStatus() {
        return mFormStatus;
    }

    public void setFormStatus(final int formStatus) {
        mFormStatus = formStatus;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
}
