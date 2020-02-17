package com.octopusbjsindia.models.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("file_size")
    @Expose
    private String fileSize;
    @SerializedName("language_details")
    @Expose
    private List<LanguageDetail> languageDetails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public List<LanguageDetail> getLanguageDetails() {
        return languageDetails;
    }

    public void setLanguageDetails(List<LanguageDetail> languageDetails) {
        this.languageDetails = languageDetails;
    }

}
