package com.octopusbjsindia.models.forms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.database.DataConverter;
import com.octopusbjsindia.models.LocaleData;

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

    @ColumnInfo(name = "form_name")
    private String formName;

    @ColumnInfo(name = "form_status")
    private int mFormStatus;

    @ColumnInfo(name = "form_category")
    private String formCategory;

    @ColumnInfo(name = "request_object")
    private String requestObject;

    @ColumnInfo(name = "createdDateTime")
    private Long createdAt;

    @ColumnInfo(name = "rejection_reason")
    private String rejectionReason;

    public String getFormApprovalStatus() {
        return formApprovalStatus;
    }

    public void setFormApprovalStatus(String formApprovalStatus) {
        this.formApprovalStatus = formApprovalStatus;
    }

    @ColumnInfo(name = "form_approvalStatus")
    private String formApprovalStatus;

    @ColumnInfo(name = "oid")
    private String oid;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "formCategoryLocale")
    private LocaleData formCategoryLocale;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "formNameLocale")
    private LocaleData formNameLocale;

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
        if (formNameLocale != null) {
            this.formName = formNameLocale.getLocaleValue();
        }
        return formName;
    }

    public void setFormName(final String formName) {
        this.formName = formName;
    }

    public String getFormCategory() {
        if (formNameLocale != null) {
            this.formCategory = formCategoryLocale.getLocaleValue();
        }
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Long createdAt) {
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

    public String getOid() {
        return oid;
    }

    public void setOid(final String oid) {
        this.oid = oid;
    }

    public LocaleData getFormCategoryLocale() {
        return formCategoryLocale;
    }

    public void setFormCategoryLocale(LocaleData formCategoryLocale) {
        this.formCategoryLocale = formCategoryLocale;
    }

    public LocaleData getFormNameLocale() {
        return formNameLocale;
    }

    public void setFormNameLocale(LocaleData formNameLocale) {
        this.formNameLocale = formNameLocale;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
