package com.octopusbjsindia.models.content;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class ContentData {

    @PrimaryKey
    @ColumnInfo(name = "contentId")
    @NonNull
    @SerializedName("_id")
    @Expose
    private String id;
    @ColumnInfo(name = "category_id")
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @ColumnInfo(name = "category_name")
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @ColumnInfo(name = "content_title")
    @SerializedName("content_title")
    @Expose
    private String contentTiltle;
    @Expose
    private String downloadedFileName;
    @ColumnInfo(name = "file_type")
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @ColumnInfo(name = "file_size")
    @SerializedName("file_size")
    @Expose
    private String fileSize;
    @ColumnInfo(name = "languageDetailsString")
    @Expose
    private String languageDetailsString;
    @Ignore
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContentTiltle() {
        return contentTiltle;
    }

    public void setContentTiltle(String contentTiltle) {
        this.contentTiltle = contentTiltle;
    }

    public String getDownloadedFileName() {
        return downloadedFileName;
    }

    public void setDownloadedFileName(String downloadedFileName) {
        this.downloadedFileName = downloadedFileName;
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

    public String getLanguageDetailsString() {
        return languageDetailsString;
    }

    public void setLanguageDetailsString(String languageDetailsString) {
        this.languageDetailsString = languageDetailsString;
    }

}
