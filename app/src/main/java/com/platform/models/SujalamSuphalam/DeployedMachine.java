package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeployedMachine implements Serializable {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("machineUpdatedDate")
    @Expose
    private String machineUpdatedDate;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMachineUpdatedDate() {
        return machineUpdatedDate;
    }

    public void setMachineUpdatedDate(String machineUpdatedDate) {
        this.machineUpdatedDate = machineUpdatedDate;
    }
}
