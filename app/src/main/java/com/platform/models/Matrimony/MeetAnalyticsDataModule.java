package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MeetAnalyticsDataModule implements Serializable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("availableValue")
    @Expose
    private String availableValue;
    @SerializedName("totalValue")
    @Expose
    private String totalValue;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailableValue() {
        return availableValue;
    }

    public void setAvailableValue(String availableValue) {
        this.availableValue = availableValue;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

}
