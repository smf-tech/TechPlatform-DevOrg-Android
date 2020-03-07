package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainerBachListResponseModel {

    @SerializedName("status")
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
    }
}
