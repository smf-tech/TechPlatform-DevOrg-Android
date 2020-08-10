package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OccupationalDetails implements Serializable {
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("employer_company")
    @Expose
    private String employerCompany;
    @SerializedName("business_description")
    @Expose
    private String businessDescription;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmployerCompany() {
        return employerCompany;
    }

    public void setEmployerCompany(String employerCompany) {
        this.employerCompany = employerCompany;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

}
