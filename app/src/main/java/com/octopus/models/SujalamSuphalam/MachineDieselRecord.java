package com.octopus.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MachineDieselRecord implements Serializable {
    @SerializedName("date")
    @Expose
    private long dieselDate;
    @SerializedName("diesel_quantity_ltr")
    @Expose
    private String dieselQuantity;
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("machine_id")
    @Expose
    private String machineId;

    public long getDieselDate() {
        return dieselDate;
    }

    public void setDieselDate(long dieselDate) {
        this.dieselDate = dieselDate;
    }

    public String getDieselQuantity() {
        return dieselQuantity;
    }

    public void setDieselQuantity(String dieselQuantity) {
        this.dieselQuantity = dieselQuantity;
    }

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
}
