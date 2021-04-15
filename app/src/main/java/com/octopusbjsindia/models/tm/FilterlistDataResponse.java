package com.octopusbjsindia.models.tm;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterlistDataResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("approvalType")
    @Expose
    private String approvalType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("filterSet")
    @Expose
    private ArrayList<FilterSet> filterSet = null;

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<FilterSet> getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(ArrayList<FilterSet> filterSet) {
        this.filterSet = filterSet;
    }

}