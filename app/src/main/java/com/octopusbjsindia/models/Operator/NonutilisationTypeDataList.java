package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NonutilisationTypeDataList {

    @SerializedName("hi")
    @Expose
    private List<Hi> hi = null;
    @SerializedName("mr")
    @Expose
    private List<Mr> mr = null;
    @SerializedName("en")
    @Expose
    private List<En> en = null;

    public List<Hi> getHi() {
        return hi;
    }

    public void setHi(List<Hi> hi) {
        this.hi = hi;
    }

    public List<Mr> getMr() {
        return mr;
    }

    public void setMr(List<Mr> mr) {
        this.mr = mr;
    }

    public List<En> getEn() {
        return en;
    }

    public void setEn(List<En> en) {
        this.en = en;
    }

}
