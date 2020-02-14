package com.octopusbjsindia.models.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageDetail {

    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("category_title")
    @Expose
    private String categoryTitle;
    @SerializedName("content_title")
    @Expose
    private String contentTitle;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
