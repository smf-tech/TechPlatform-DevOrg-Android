package com.platform.models.pm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.common.Category;
import com.platform.models.common.Entity;
import com.platform.models.common.Microservice;
import com.platform.models.common.Project;
import com.platform.models.forms.FormData;

@SuppressWarnings("unused")
public class ProcessData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("editable")
    @Expose
    private String editable;
    @SerializedName("multiple_entry")
    @Expose
    private String multipleEntry;
    @SerializedName("microservice")
    @Expose
    private Microservice microservice;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("entity")
    @Expose
    private Entity entity;

    public ProcessData() {
    }

    public ProcessData(final FormData data) {
        this.category = data.getCategory();
        this.entity = data.getEntity();
        this.microservice = data.getMicroService();
        this.project = data.getProject();
        this.id = data.getId();
        this.active = data.getActive();
        this.editable = data.getEditable();
        this.multipleEntry = data.getMultipleEntry();
        this.name = data.getName();
    }

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
}
