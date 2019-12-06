package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Occupational_details {

    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("employer_company")
    @Expose
    private String employer_company;
    @SerializedName("business_description")
    @Expose
    private String business_description;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmployer_company() {
        return employer_company;
    }

    public void setEmployer_company(String employer_company) {
        this.employer_company = employer_company;
    }

    public String getBusiness_description() {
        return business_description;
    }

    public void setBusiness_description(String business_description) {
        this.business_description = business_description;
    }

}