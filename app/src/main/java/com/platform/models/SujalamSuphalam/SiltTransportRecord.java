package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiltTransportRecord implements Serializable {
    @SerializedName("structureId")
    @Expose
    private String structureId;

    @SerializedName("machineId")
    @Expose
    private String machineId;

    @SerializedName("date")
    @Expose
    private long siltTransportDate;

    @SerializedName("tractorTripsCount")
    @Expose
    private String tractorTripsCount;

    @SerializedName("tipperTripsCount")
    @Expose
    private String tipperTripsCount;

    @SerializedName("farmersCount")
    @Expose
    private String farmersCount;

    @SerializedName("beneficiariesCount")
    @Expose
    private String beneficiariesCount;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public long getSiltTransportDate() {
        return siltTransportDate;
    }

    public void setSiltTransportDate(long siltTransportDate) {
        this.siltTransportDate = siltTransportDate;
    }

    public String getTractorTripsCount() {
        return tractorTripsCount;
    }

    public void setTractorTripsCount(String tractorTripsCount) {
        this.tractorTripsCount = tractorTripsCount;
    }

    public String getTipperTripsCount() {
        return tipperTripsCount;
    }

    public void setTipperTripsCount(String tipperTripsCount) {
        this.tipperTripsCount = tipperTripsCount;
    }

    public String getFarmersCount() {
        return farmersCount;
    }

    public void setFarmersCount(String farmersCount) {
        this.farmersCount = farmersCount;
    }

    public String getBeneficiariesCount() {
        return beneficiariesCount;
    }

    public void setBeneficiariesCount(String beneficiariesCount) {
        this.beneficiariesCount = beneficiariesCount;
    }

}
