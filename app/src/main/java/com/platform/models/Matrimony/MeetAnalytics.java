package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MeetAnalytics {
    @SerializedName("displayLabel")
    @Expose
    private String displayLabel;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dataModules")
    @Expose
    private ArrayList<MeetModuleAnalytics> dataModules = null;

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

    public ArrayList<MeetModuleAnalytics> getDataModules() {
        return dataModules;
    }

    public void setDataModules(ArrayList<MeetModuleAnalytics> dataModules) {
        this.dataModules = dataModules;
    }
}
