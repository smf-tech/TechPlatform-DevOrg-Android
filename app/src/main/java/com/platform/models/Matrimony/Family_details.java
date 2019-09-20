package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Family_details {

    @SerializedName("family_type")
    @Expose
    private String family_type;
    @SerializedName("gotra")
    @Expose
    private Gotra gotra;
    @SerializedName("father_name")
    @Expose
    private String father_name;
    @SerializedName("father_occupation")
    @Expose
    private String father_occupation;
    @SerializedName("family_income")
    @Expose
    private String family_income;
    @SerializedName("mother_name")
    @Expose
    private String mother_name;
    @SerializedName("mother_occupation")
    @Expose
    private String mother_occupation;
    @SerializedName("brother_count")
    @Expose
    private String brother_count;
    @SerializedName("sister_count")
    @Expose
    private String sister_count;

    public String getFamily_type() {
        return family_type;
    }

    public void setFamily_type(String family_type) {
        this.family_type = family_type;
    }

    public Gotra getGotra() {
        return gotra;
    }

    public void setGotra(Gotra gotra) {
        this.gotra = gotra;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getFather_occupation() {
        return father_occupation;
    }

    public void setFather_occupation(String father_occupation) {
        this.father_occupation = father_occupation;
    }

    public String getFamily_income() {
        return family_income;
    }

    public void setFamily_income(String family_income) {
        this.family_income = family_income;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getMother_occupation() {
        return mother_occupation;
    }

    public void setMother_occupation(String mother_occupation) {
        this.mother_occupation = mother_occupation;
    }

    public String getBrother_count() {
        return brother_count;
    }

    public void setBrother_count(String brother_count) {
        this.brother_count = brother_count;
    }

    public String getSister_count() {
        return sister_count;
    }

    public void setSister_count(String sister_count) {
        this.sister_count = sister_count;
    }

}