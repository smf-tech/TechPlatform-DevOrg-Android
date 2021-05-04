package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HospitalModel implements Serializable {

    @SerializedName("hospital_type_id")
    @Expose
    private String hospitalTypeId;

    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("hospital_name")
    @Expose
    private String hospitalName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("person_name")
    @Expose
    private String personName;
    @SerializedName("designation")
    @Expose
    private String designation;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
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

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;

    public String getHospitalTypeId() {
        return hospitalTypeId;
    }

    public void setHospitalTypeId(String hospitalTypeId) {
        this.hospitalTypeId = hospitalTypeId;
    }
}
