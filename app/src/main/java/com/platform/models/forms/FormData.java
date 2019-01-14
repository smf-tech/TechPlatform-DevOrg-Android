package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class FormData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("json")
    @Expose
    private String json;
    @SerializedName("creator_id")
    @Expose
    private String creatorId;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("editable")
    @Expose
    private String editable;
    @SerializedName("multiple_entry")
    @Expose
    private String multipleEntry;
    @SerializedName("assigned_roles")
    @Expose
    private List<String> assignedRoles = null;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("microservice_id")
    @Expose
    private String microServiceId;
    @SerializedName("entity_id")
    @Expose
    private String entityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMicroServiceId() {
        return microServiceId;
    }

    public void setMicroServiceId(String microServiceId) {
        this.microServiceId = microServiceId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
