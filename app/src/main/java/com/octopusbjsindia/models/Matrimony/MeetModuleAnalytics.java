package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeetModuleAnalytics implements Serializable {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("availableValue")
    @Expose
    private float availableValue;
    @SerializedName("totalValue")
    @Expose
    private float totalValue;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAvailableValue() {
        return availableValue;
    }

    public void setAvailableValue(float availableValue) {
        this.availableValue = availableValue;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }
}
