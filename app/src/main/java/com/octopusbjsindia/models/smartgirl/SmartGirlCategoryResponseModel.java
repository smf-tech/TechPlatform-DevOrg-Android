package com.octopusbjsindia.models.smartgirl;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmartGirlCategoryResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<SmartGirlCategoryResponseData> smartGirlCategoryResponseData = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SmartGirlCategoryResponseData> getData() {
        return smartGirlCategoryResponseData;
    }

    public void setData(List<SmartGirlCategoryResponseData> smartGirlCategoryResponseData) {
        this.smartGirlCategoryResponseData = smartGirlCategoryResponseData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
