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
    private Components components;
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
    @SerializedName("microservice")
    @Expose
    private Microservice microService;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("entity")
    @Expose
    private Entity entity;

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
}
