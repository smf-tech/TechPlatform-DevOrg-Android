package com.octopus.models.tm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class LocationOfRequester {

    @SerializedName("state")
    @Expose
    private List<String> state = null;
    @SerializedName("district")
    @Expose
    private List<String> district = null;
    @SerializedName("taluka")
    @Expose
    private List<String> taluka = null;

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public List<String> getDistrict() {
        return district;
    }

    public void setDistrict(List<String> district) {
        this.district = district;
    }

    public List<String> getTaluka() {
        return taluka;
    }

    public void setTaluka(List<String> taluka) {
        this.taluka = taluka;
    }

}