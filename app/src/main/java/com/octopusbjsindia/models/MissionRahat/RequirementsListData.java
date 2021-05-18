package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequirementsListData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("hospitalName")
    @Expose
    private String hospitalName;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("talukaName")
    @Expose
    private String talukaName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("requiredMachine")
    @Expose
    private Integer requiredMachine;
    @SerializedName("ownerName")
    @Expose
    private String ownerName;
    @SerializedName("ownerContactDetails")
    @Expose
    private String ownerContactDetails;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isMOUDone")
    @Expose
    private boolean isMOUDone;

    @SerializedName("isHandOver")
    @Expose
    private boolean isHandOver;

    @SerializedName("isTakeOver")
    @Expose
    private boolean isTakeOver;

    @SerializedName("taluka_id")
    @Expose
    private String taluka_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRequiredMachine() {
        return requiredMachine;
    }

    public void setRequiredMachine(Integer requiredMachine) {
        this.requiredMachine = requiredMachine;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContactDetails() {
        return ownerContactDetails;
    }

    public void setOwnerContactDetails(String ownerContactDetails) {
        this.ownerContactDetails = ownerContactDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMOUDone() {
        return isMOUDone;
    }

    public void setMOUDone(boolean MOUDone) {
        isMOUDone = MOUDone;
    }


    public boolean isHandOver() {
        return isHandOver;
    }

    public void setHandOver(boolean handOver) {
        isHandOver = handOver;
    }

    public boolean isTakeOver() {
        return isTakeOver;
    }

    public void setTakeOver(boolean takeOver) {
        isTakeOver = takeOver;
    }


    public String getTaluka_id() {
        return taluka_id;
    }

    public void setTaluka_id(String taluka_id) {
        this.taluka_id = taluka_id;
    }
}
