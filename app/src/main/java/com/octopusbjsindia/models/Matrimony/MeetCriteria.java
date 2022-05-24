package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MeetCriteria implements Serializable {
    @SerializedName("minAge")
    @Expose
    private Integer minAge = 0;
    @SerializedName("maxAge")
    @Expose
    private Integer maxAge = 0;
    @SerializedName("maritalCriteria")
    @Expose
    private List<String> maritalCriteria = null;
    @SerializedName("qualificationCriteria")
    @Expose
    private List<String> qualificationCriteria = null;

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
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
