package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineCode {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("date_deployed")
    @Expose
    private String dateDeployed;
    @SerializedName("structure_code")
    @Expose
    private String structureCode;
    @SerializedName("machine_code")
    @Expose
    private String machineCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deployed")
    @Expose
    private Boolean isDeployed;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDateDeployed() {
        return dateDeployed;
    }

    public void setDateDeployed(String dateDeployed) {
        this.dateDeployed = dateDeployed;
    }

    public String getStructureCode() {
        return structureCode;
    }

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeployed() {
        return isDeployed;
    }

    public void setDeployed(Boolean deployed) {
        isDeployed = deployed;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
