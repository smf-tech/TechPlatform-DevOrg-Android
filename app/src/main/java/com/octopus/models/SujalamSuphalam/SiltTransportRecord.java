package com.octopus.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiltTransportRecord implements Serializable {
    @SerializedName("structure_id")
    @Expose
    private String structureId;

    @SerializedName("machine_id")
    @Expose
    private String machineId;

    @SerializedName("transport_date")
    @Expose
    private long siltTransportDate;

    @SerializedName("tractor_trips")
    @Expose
    private String tractorTripsCount;

    @SerializedName("tipper_trips")
    @Expose
    private String tipperTripsCount;

    @SerializedName("farmer_count")
    @Expose
    private String farmersCount;

    @SerializedName("beneficiaries_count")
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
