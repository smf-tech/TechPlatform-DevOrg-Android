package com.octopusbjsindia.models.reports;

import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.database.DataConverter;
import com.octopusbjsindia.models.LocaleData;

@SuppressWarnings("unused")
public class ReportCategory {

    @SerializedName("_id")
    @Expose
    private String id;

    @TypeConverters(DataConverter.class)
    @SerializedName("name")
    @Expose
    private LocaleData name;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedAt;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocaleData getName() {
        return name;
    }

    public void setName(LocaleData name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
