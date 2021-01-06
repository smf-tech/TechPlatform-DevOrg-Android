package com.octopusbjsindia.models.sel_content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SELReadingData implements Serializable {
    @SerializedName("contentName")
    @Expose
    private String contentName;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;
    private boolean isDownloadStarted = false;

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public boolean isDownloadStarted() {
        return isDownloadStarted;
    }

    public void setDownloadStarted(boolean downloadStarted) {
        isDownloadStarted = downloadStarted;
    }

}
