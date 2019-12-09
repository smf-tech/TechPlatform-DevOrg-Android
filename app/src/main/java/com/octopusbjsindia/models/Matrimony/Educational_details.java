package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Educational_details {

    @SerializedName("education_level")
    @Expose
    private String education_level;
    @SerializedName("qualification_degree")
    @Expose
    private String qualification_degree;
    @SerializedName("income")
    @Expose
    private String income;

    public String getEducation_level() {
        return education_level;
    }

    public void setEducation_level(String education_level) {
        this.education_level = education_level;
    }

    public String getQualification_degree() {
        return qualification_degree;
    }

    public void setQualification_degree(String qualification_degree) {
        this.qualification_degree = qualification_degree;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

}
