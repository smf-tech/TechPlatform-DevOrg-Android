package com.octopusbjsindia.models.form_component;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MvUserNameResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<MvUserNameResponse> mvUserNameResponses = null;

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


    public List<MvUserNameResponse> getMvUserNameResponses() {
        return mvUserNameResponses;
    }

    public void setMvUserNameResponses(List<MvUserNameResponse> mvUserNameResponses) {
        this.mvUserNameResponses = mvUserNameResponses;
    }
}
