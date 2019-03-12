package com.platform.models.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.database.DataConverter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@SuppressWarnings("unused")
@Entity
public class ReportData {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("_id")
    @Expose
    @NonNull
    private String id = "";
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "category")
    @SerializedName("category")
    @Expose
    private ReportCategory category;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedAt;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdAt;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ReportCategory getCategory() {
        return category;
    }

    public void setCategory(ReportCategory category) {
        this.category = category;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
