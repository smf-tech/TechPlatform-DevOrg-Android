package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MeetAnalyticsData implements Serializable {
    @SerializedName("displayLabel")
    @Expose
    private String displayLabel;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dataModules")
    @Expose
    private ArrayList<MeetAnalyticsDataModule> dataModules = null;

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<MeetAnalyticsDataModule> getDataModules() {
        return dataModules;
    }

    public void setDataModules(ArrayList<MeetAnalyticsDataModule> dataModules) {
        this.dataModules = dataModules;
    }
}
