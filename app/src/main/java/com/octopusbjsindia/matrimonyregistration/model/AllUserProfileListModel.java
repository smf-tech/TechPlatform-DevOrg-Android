package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllUserProfileListModel {

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
    private AllUserProfile allUserProfile;

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

    public AllUserProfile getAllUserProfile() {
        return allUserProfile;
    }

    public void setAllUserProfile(AllUserProfile allUserProfile) {
        this.allUserProfile = allUserProfile;
    }

   /* public AllUserProfile getData() {
        return allUserProfile;
    }

    public void setData(AllUserProfile allUserProfile) {
        this.allUserProfile = allUserProfile;
    }*/

}
