package com.octopusbjsindia.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoleAccessObject {
    @SerializedName("action_code")
    @Expose
    private Integer actionCode;
    @SerializedName("action_name")
    @Expose
    private String actionName;

    public Integer getActionCode() {
        return actionCode;
    }

    public void setActionCode(Integer actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
