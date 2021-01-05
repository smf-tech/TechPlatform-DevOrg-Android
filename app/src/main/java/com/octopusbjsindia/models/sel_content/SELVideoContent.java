package com.octopusbjsindia.models.sel_content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SELVideoContent implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @SerializedName("form_Id")
    @Expose
    private List<SELAssignmentData> assignmentList = new ArrayList<SELAssignmentData>();
    @SerializedName("content")
    @Expose
    private List<SELReadingData> readingDataList = new ArrayList<SELReadingData>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<SELAssignmentData> getAssignmentList() {
        return assignmentList;
    }

    public List<SELReadingData> getReadingDataList() {
        return readingDataList;
    }
}
