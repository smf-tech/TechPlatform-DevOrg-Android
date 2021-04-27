package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HospitalDetailsRespone {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("hospitalName")
    @Expose
    private String hospitalName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("ownerContactDetails")
    @Expose
    private String ownerContactDetails;
    @SerializedName("approvedMachineQuanity")
    @Expose
    private Integer approvedMachineQuanity;
    @SerializedName("machineCodeList")
    @Expose
    private String machineCodeList;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getApprovedMachineQuanity() {
        return approvedMachineQuanity;
    }

    public void setApprovedMachineQuanity(Integer approvedMachineQuanity) {
        this.approvedMachineQuanity = approvedMachineQuanity;
    }

    public String getMachineCodeList() {
        return machineCodeList;
    }

    public void setMachineCodeList(String machineCodeList) {
        this.machineCodeList = machineCodeList;
    }

}
