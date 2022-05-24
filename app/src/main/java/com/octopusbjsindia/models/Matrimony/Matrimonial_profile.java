package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Matrimonial_profile implements Serializable {

    @SerializedName("personal_details")
    @Expose
    private Personal_details personal_details;
    @SerializedName("educational_details")
    @Expose
    private Educational_details educational_details;
    @SerializedName("occupational_details")
    @Expose
    private Occupational_details occupational_details;
    @SerializedName("family_details")
    @Expose
    private Family_details family_details;
    @SerializedName("residential_details")
    @Expose
    private Residential_details residential_details;
    @SerializedName("other_marital_information")
    @Expose
    private Other_marital_information other_marital_information;
    @SerializedName("isBan")
    @Expose
    private boolean isBan;

    @SerializedName("isVerified")
    @Expose
    private boolean isVerified;

    @SerializedName("idApproved")
    @Expose
    private boolean idApproved;

    @SerializedName("educationApproved")
    @Expose
    private boolean educationApproved;
    @SerializedName("profile_unique_id")
    @Expose
    private String profile_unique_id;


    public Personal_details getPersonal_details() {
        return personal_details;
    }

    public void setPersonal_details(Personal_details personal_details) {
        this.personal_details = personal_details;
    }

    public Educational_details getEducational_details() {
        return educational_details;
    }

    public void setEducational_details(Educational_details educational_details) {
        this.educational_details = educational_details;
    }

    public Occupational_details getOccupational_details() {
        return occupational_details;
    }

    public void setOccupational_details(Occupational_details occupational_details) {
        this.occupational_details = occupational_details;
    }

    public Family_details getFamily_details() {
        return family_details;
    }

    public void setFamily_details(Family_details family_details) {
        this.family_details = family_details;
    }

    public Residential_details getResidential_details() {
        return residential_details;
    }

    public void setResidential_details(Residential_details residential_details) {
        this.residential_details = residential_details;
    }

    public Other_marital_information getOther_marital_information() {
        return other_marital_information;
    }

    public void setOther_marital_information(Other_marital_information other_marital_information) {
        this.other_marital_information = other_marital_information;
    }

    public boolean isBan() {
        return isBan;
    }

    public void setBan(boolean ban) {
        isBan = ban;
    }


    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isIdApproved() {
        return idApproved;
    }

    public void setIdApproved(boolean idApproved) {
        this.idApproved = idApproved;
    }

    public boolean isEducationApproved() {
        return educationApproved;
    }

    public void setEducationApproved(boolean educationApproved) {
        this.educationApproved = educationApproved;
    }

    public String getProfile_unique_id() {
        return profile_unique_id;
    }

    public void setProfile_unique_id(String profile_unique_id) {
        this.profile_unique_id = profile_unique_id;
    }
}