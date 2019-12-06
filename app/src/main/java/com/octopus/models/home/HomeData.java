package com.octopus.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class HomeData implements Serializable {

    @SerializedName("default_modules")
    @Expose
    private List<Modules> defaultModules = null;
    @SerializedName("on_approve_modules")
    @Expose
    private List<Modules> onApproveModules = null;

    public List<Modules> getDefaultModules() {
        return defaultModules;
    }

    public void setDefaultModules(List<Modules> defaultModules) {
        this.defaultModules = defaultModules;
    }

    public List<Modules> getOnApproveModules() {
        return onApproveModules;
    }

    public void setOnApproveModules(List<Modules> onApproveModules) {
        this.onApproveModules = onApproveModules;
    }
}
