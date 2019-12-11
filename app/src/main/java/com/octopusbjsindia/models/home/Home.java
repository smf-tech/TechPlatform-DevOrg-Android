package com.octopusbjsindia.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Home implements Serializable {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private HomeData data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_approve_status")
    @Expose
    private String userApproveStatus;



    public HomeData getHomeData() {
        return data;
    }

    public void setHomeData(HomeData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserApproveStatus() {
        return userApproveStatus;
    }

    public void setUserApproveStatus(String userApproveStatus) {
        this.userApproveStatus = userApproveStatus;
    }
}
