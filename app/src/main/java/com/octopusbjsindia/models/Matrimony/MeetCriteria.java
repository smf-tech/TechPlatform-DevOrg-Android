package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MeetCriteria implements Serializable {
    @SerializedName("minAge")
    @Expose
    private int minAge;
    @SerializedName("maxAge")
    @Expose
    private int maxAge;
    @SerializedName("maritalCriteria")
    @Expose
    private List<String> maritalCriteria = null;
    @SerializedName("qualificationCriteria")
    @Expose
    private List<String> qualificationCriteria = null;

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public List<String> getMaritalCriteria() {
        return maritalCriteria;
    }

    public void setMaritalCriteria(List<String> maritalCriteria) {
        this.maritalCriteria = maritalCriteria;
    }

    public List<String> getQualificationCriteria() {
        return qualificationCriteria;
    }

    public void setQualificationCriteria(List<String> qualificationCriteria) {
        this.qualificationCriteria = qualificationCriteria;
    }
}
