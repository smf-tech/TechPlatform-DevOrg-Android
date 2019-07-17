package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("metadata")
    @Expose
    private List<Object> metadata = null;
    @SerializedName("data")
    @Expose
    private List<EventTask> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Object> metadata) {
        this.metadata = metadata;
    }

    public List<EventTask> getData() {
        return data;
    }

    public void setData(List<EventTask> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
