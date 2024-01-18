package com.octopusbjsindia.models.SujalamSuphalam;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RWBDonorApiResponse{

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("data")
    @Expose
    private List<RWBDonor> data;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<RWBDonor> getData() {
        return data;
    }

    public void setData(List<RWBDonor> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
