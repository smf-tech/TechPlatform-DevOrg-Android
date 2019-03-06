package com.platform.models.pm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.database.DataConverter;
import com.platform.models.LocaleData;
import com.platform.models.common.Category;
import com.platform.models.common.Entity;
import com.platform.models.common.Microservice;
import com.platform.models.common.Project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@SuppressWarnings("unused")
@androidx.room.Entity
public class ProcessData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("_id")
    @Expose
    private String id = "";

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private LocaleData name;

    @ColumnInfo(name = "form_title")
    @SerializedName("form_title")
    @Expose
    private String formTitle;

    @ColumnInfo(name = "active")
    @SerializedName("active")
    @Expose
    private String active;

    @ColumnInfo(name = "editable")
    @SerializedName("editable")
    @Expose
    private String editable;

    @ColumnInfo(name = "multiple_entry")
    @SerializedName("multiple_entry")
    @Expose
    private String multipleEntry;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "microservice")
    @SerializedName("microservice")
    @Expose
    private Microservice microservice;

    @Ignore
    @SerializedName("project")
    @Expose
    private Project project;

    @ColumnInfo(name = "submit_count")
    @Expose
    private String submitCount;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "category")
    @SerializedName("category")
    @Expose
    private Category category;

    @Ignore
    @SerializedName("entity")
    @Expose
    private Entity entity;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(String submitCount) {
        this.submitCount = submitCount;
    }

    public LocaleData getName() {
        return name;
    }

    public void setName(LocaleData name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getMultipleEntry() {
        return multipleEntry;
    }

    public void setMultipleEntry(String multipleEntry) {
        this.multipleEntry = multipleEntry;
    }

    public Microservice getMicroservice() {
        return microservice;
    }

    public void setMicroservice(Microservice microservice) {
        this.microservice = microservice;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }
}
