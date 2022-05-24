package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SGTrainersListResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<SGTrainersList> sgTrainersListList = null;
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

    public List<SGTrainersList> getSgTrainersListList() {
        return sgTrainersListList;
    }

    public void setSgTrainersListList(List<SGTrainersList> sgTrainersListList) {
        this.sgTrainersListList = sgTrainersListList;
    }
}
