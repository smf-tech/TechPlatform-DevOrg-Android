package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class JurisdictionLevelResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<JurisdictionLocationV3> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JurisdictionLocationV3> getData() {
        return data;
    }

    public void setData(List<JurisdictionLocationV3> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
