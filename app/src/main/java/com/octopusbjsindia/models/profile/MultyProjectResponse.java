package com.octopusbjsindia.models.profile;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class MultyProjectResponse {
    @Expose
    private String status;
    @Expose
    private String message;
    @Expose
    private ArrayList<MultyProjectData> data = null;

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

    public ArrayList<MultyProjectData> getData() {
        return data;
    }

    public void setData(ArrayList<MultyProjectData> data) {
        this.data = data;
    }

}
