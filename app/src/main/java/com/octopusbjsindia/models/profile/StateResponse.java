package com.octopusbjsindia.models.profile;

import java.util.List;

@SuppressWarnings("unused")
public class StateResponse {
    private String status;

    private List<State> data;

    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<State> getData() {
        return data;
    }

    public void setData(List<State> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
