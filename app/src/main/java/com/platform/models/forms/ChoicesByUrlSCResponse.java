package com.platform.models.forms;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ChoicesByUrlSCResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<StructureCode> data;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StructureCode> getData() {
        return data;
    }

    public void setData(List<StructureCode> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
