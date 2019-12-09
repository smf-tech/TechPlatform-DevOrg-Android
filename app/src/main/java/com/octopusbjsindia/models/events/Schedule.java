package com.octopusbjsindia.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Schedule implements Serializable {
    @SerializedName("startdatetime")
    @Expose
    private long startdatetime;
    @SerializedName("enddatetime")
    @Expose
    private long enddatetime;

    public long getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(long startdatetime) {
        this.startdatetime = startdatetime;
    }

    public long getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(long enddatetime) {
        this.enddatetime = enddatetime;
    }
}
