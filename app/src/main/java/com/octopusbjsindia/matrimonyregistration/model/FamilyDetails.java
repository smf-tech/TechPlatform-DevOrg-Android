package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FamilyDetails implements Serializable {
    @SerializedName("family_type")
    @Expose
    private String familyType;
    @SerializedName("gotra")
    @Expose
    private Gotra gotra;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("father_occupation")
    @Expose
    private String fatherOccupation;
    @SerializedName("family_income")
    @Expose
    private String familyIncome;
    @SerializedName("mother_name")
    @Expose
    private String motherName;
    @SerializedName("mother_occupation")
    @Expose
    private String motherOccupation;
    @SerializedName("brother_count")
    @Expose
    private String brotherCount;
    @SerializedName("sister_count")
    @Expose
    private String sisterCount;

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public Gotra getGotra() {
        return gotra;
    }

    public void setGotra(Gotra gotra) {
        this.gotra = gotra;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(String familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getBrotherCount() {
        return brotherCount;
    }

    public void setBrotherCount(String brotherCount) {
        this.brotherCount = brotherCount;
    }

    public String getSisterCount() {
        return sisterCount;
    }

    public void setSisterCount(String sisterCount) {
        this.sisterCount = sisterCount;
    }

}
