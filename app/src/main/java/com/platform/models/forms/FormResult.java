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

    @ColumnInfo(name = "form_title")
    @SerializedName("form_title")
    @Expose
    private String formTitle;

    @ColumnInfo(name = "result")
    private String result;

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

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
}
