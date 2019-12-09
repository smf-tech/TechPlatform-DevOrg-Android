package com.octopusbjsindia.models.tm;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterlistDataResponse {

    @SerializedName("id")
    @Expose
    private int id;

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    @SerializedName("approvalType")
    @Expose
    private String approvalType;

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("filterSet")
    @Expose
    private List<FilterSet> filterSet = null;

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

    public List<FilterSet> getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(List<FilterSet> filterSet) {
        this.filterSet = filterSet;
    }

}