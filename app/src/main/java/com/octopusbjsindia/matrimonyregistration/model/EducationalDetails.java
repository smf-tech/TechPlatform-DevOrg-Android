package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EducationalDetails implements Serializable {
    @SerializedName("education_level")
    @Expose
    private String educationLevel;
    @SerializedName("qualification_degree")
    @Expose
    private String qualificationDegree;
    @SerializedName("income")
    @Expose
    private String income;

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getQualificationDegree() {
        return qualificationDegree;
    }

    public void setQualificationDegree(String qualificationDegree) {
        this.qualificationDegree = qualificationDegree;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
