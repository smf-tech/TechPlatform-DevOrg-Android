package com.octopus.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MachineListAPIResponse {
    @SerializedName("status")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<MachineData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<MachineData> getData() {
        return data;
    }

    public void setData(List<MachineData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
