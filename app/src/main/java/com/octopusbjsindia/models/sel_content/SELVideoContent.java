package com.octopusbjsindia.models.sel_content;

import java.io.Serializable;

public class SELVideoContent implements Serializable {
    private String _id;
    private String title;
    private String video_url;
    private String thumbnail_url;
    private String form_Id;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return video_url;
    }

    public void setVideoUrl(String videoUrl) {
        this.video_url = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnail_url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnail_url = thumbnailUrl;
    }

    public String getFormId() {
        return form_Id;
    }

    public void setFormId(String formId) {
        this.form_Id = formId;
    }
}
