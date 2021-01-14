package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SgDashboardResponseModelList {

    @SerializedName("subModule")
    @Expose
    private String subModule;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("jurisdictions")
    @Expose
    private Jurisdictions jurisdictions;

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Jurisdictions getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(Jurisdictions jurisdictions) {
        this.jurisdictions = jurisdictions;
    }
}
