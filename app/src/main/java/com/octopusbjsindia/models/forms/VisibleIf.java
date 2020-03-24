package com.octopusbjsindia.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisibleIf {

    @SerializedName("conditionType")
    @Expose
    private String conditionType;

    @SerializedName("conditionsList")
    @Expose
    private List<VisibleIfListObject> conditionsList = null;

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public List<VisibleIfListObject> getConditionsList() {
        return conditionsList;
    }

    public void setConditionsList(List<VisibleIfListObject> conditionsList) {
        this.conditionsList = conditionsList;
    }
}
