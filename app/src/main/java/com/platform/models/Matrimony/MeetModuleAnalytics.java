package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetModuleAnalytics {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("availableValue")
    @Expose
    private Integer availableValue;
    @SerializedName("totalValue")
    @Expose
    private Integer totalValue;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAvailableValue() {
        return availableValue;
    }

    public void setAvailableValue(Integer availableValue) {
        this.availableValue = availableValue;
    }

    public Integer getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Integer totalValue) {
        this.totalValue = totalValue;
    }
}
