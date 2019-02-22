package com.platform.models.forms;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.database.DataConverter;
import com.platform.models.common.Category;
import com.platform.models.common.Entity;
import com.platform.models.common.Microservice;
import com.platform.models.common.Project;

import java.util.List;

@SuppressWarnings("unused")
@android.arch.persistence.room.Entity
public class FormData {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("_id")
    @NonNull
    @Expose
    private String id = "";

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "components")
    @SerializedName("json")
    @Expose
    private Components components;

    @ColumnInfo(name = "is_active")
    @SerializedName("active")
    @Expose
    private String active;

    @ColumnInfo(name = "is_editable")
    @SerializedName("editable")
    @Expose
    private String editable;

    @ColumnInfo(name = "multiple_entry")
    @SerializedName("multiple_entry")
    @Expose
    private String multipleEntry;

    @Ignore
    @SerializedName("assigned_roles")
    @Expose
    private List<String> assignedRoles = null;

    @Ignore
    @SerializedName("microservice")
    @Expose
    private Microservice microService;

    @Ignore
    @SerializedName("project")
    @Expose
    private Project project;

    @Ignore
    @SerializedName("category")
    @Expose
    private Category category;

    @Ignore
    @SerializedName("entity")
    @Expose
    private Entity entity;

    @Ignore
    @SerializedName("form_keys")
    @Expose
    private List<String> formKeys;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
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

    public List<String> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(List<String> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Microservice getMicroService() {
        return microService;
    }

    public void setMicroService(Microservice microService) {
        this.microService = microService;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    private List<String> getFormKeys() {
        return formKeys;
    }

    private void setFormKeys(final List<String> formKeys) {
        this.formKeys = formKeys;
    }
}
