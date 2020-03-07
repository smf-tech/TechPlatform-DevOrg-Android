package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdditionalTrainerListResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<TrainerListResponse> trainerListResponseList = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TrainerListResponse> getTrainerListResponseList() {
        return trainerListResponseList;
    }

    public void setTrainerListResponseList(List<TrainerListResponse> trainerListResponseList) {
        this.trainerListResponseList = trainerListResponseList;
    }
}