package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class MemberListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("metadata")
    @Expose
    private List<MetaData> metadata = null;
    @SerializedName("data")
    @Expose
    private List<Participant> data = null;
    @SerializedName("message ")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MetaData> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetaData> metadata) {
        this.metadata = metadata;
    }

    public List<Participant> getData() {
        return data;
    }

    public void setData(List<Participant> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
