package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatrimonyUserProfileRequestModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private List<UserProfileList> userProfileList = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserProfileList> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<UserProfileList> userProfileList) {
        this.userProfileList = userProfileList;
    }

}