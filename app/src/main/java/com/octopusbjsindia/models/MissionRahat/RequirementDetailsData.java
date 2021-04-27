package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequirementDetailsData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("visitedPersonName")
    @Expose
    private String visitedPersonName;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("hospitalName")
    @Expose
    private String hospitalName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("requestedQuantityConcentratorMachine")
    @Expose
    private Integer requestedQuantityConcentratorMachine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisitedPersonName() {
        return visitedPersonName;
    }

    public void setVisitedPersonName(String visitedPersonName) {
        this.visitedPersonName = visitedPersonName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRequestedQuantityConcentratorMachine() {
        return requestedQuantityConcentratorMachine;
    }

    public void setRequestedQuantityConcentratorMachine(Integer requestedQuantityConcentratorMachine) {
        this.requestedQuantityConcentratorMachine = requestedQuantityConcentratorMachine;
    }

}
