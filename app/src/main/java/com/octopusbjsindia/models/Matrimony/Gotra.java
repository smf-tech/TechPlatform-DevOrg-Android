package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gotra implements Serializable {

    @SerializedName("self_gotra")
    @Expose
    private String self_gotra;
    @SerializedName("mama_gotra")
    @Expose
    private String mama_gotra;
    @SerializedName("dada_gotra")
    @Expose
    private String dada_gotra;
    @SerializedName("nana_gotra")
    @Expose
    private String nana_gotra;

    public String getSelf_gotra() {
        return self_gotra;
    }

    public void setSelf_gotra(String self_gotra) {
        this.self_gotra = self_gotra;
    }

    public String getMama_gotra() {
        return mama_gotra;
    }

    public void setMama_gotra(String mama_gotra) {
        this.mama_gotra = mama_gotra;
    }

    public String getDada_gotra() {
        return dada_gotra;
    }

    public void setDada_gotra(String dada_gotra) {
        this.dada_gotra = dada_gotra;
    }

    public String getNana_gotra() {
        return nana_gotra;
    }

    public void setNana_gotra(String nana_gotra) {
        this.nana_gotra = nana_gotra;
    }
}
