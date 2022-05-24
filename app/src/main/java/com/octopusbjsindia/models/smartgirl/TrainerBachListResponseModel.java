package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainerBachListResponseModel {

    /*@SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<TrainerBachList> TrainerBachListdata = null;
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

    public List<TrainerBachList> getTrainerBachListdata() {
        return TrainerBachListdata;
    }

    public void setTrainerBachListdata(List<TrainerBachList> trainerBachListdata) {
        TrainerBachListdata = trainerBachListdata;
    }*/
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

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

    public TrainerBachListResponse getTrainerBachListResponse() {
        return trainerBachListResponse;
    }

    public void setTrainerBachListResponse(TrainerBachListResponse trainerBachListResponse) {
        this.trainerBachListResponse = trainerBachListResponse;
    }

    @SerializedName("data")
    @Expose
    private TrainerBachListResponse trainerBachListResponse;
}
