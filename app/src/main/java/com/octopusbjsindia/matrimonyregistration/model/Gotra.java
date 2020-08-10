package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gotra implements Serializable {
    @SerializedName("self_gotra")
    @Expose
    private String selfGotra;
    @SerializedName("mama_gotra")
    @Expose
    private String mamaGotra;
    @SerializedName("dada_gotra")
    @Expose
    private String dadaGotra;
    @SerializedName("nana_gotra")
    @Expose
    private String nanaGotra;


    public String getSelfGotra() {
        return selfGotra;
    }

    public void setSelfGotra(String selfGotra) {
        this.selfGotra = selfGotra;
    }

    public String getMamaGotra() {
        return mamaGotra;
    }

    public void setMamaGotra(String mamaGotra) {
        this.mamaGotra = mamaGotra;
    }

    public String getDadaGotra() {
        return dadaGotra;
    }

    public void setDadaGotra(String dadaGotra) {
        this.dadaGotra = dadaGotra;
    }

    public String getNanaGotra() {
        return nanaGotra;
    }

    public void setNanaGotra(String nanaGotra) {
        this.nanaGotra = nanaGotra;
    }
}
