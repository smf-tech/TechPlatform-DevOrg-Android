package com.octopus.models.appconfig;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfigResponseModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AppConfigResponse appConfigResponse;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppConfigResponse getAppConfigResponse() {
        return appConfigResponse;
    }

    public void setAppConfigResponse(AppConfigResponse appConfigResponse) {
        this.appConfigResponse = appConfigResponse;
    }

   /* public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }*/

}