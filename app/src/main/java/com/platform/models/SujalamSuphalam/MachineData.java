package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MachineData  implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("make_model")
    @Expose
    private String makeModel;
    @SerializedName("owned_by")
    @Expose
    private String ownedBy;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @SerializedName("manufactured_year")
    @Expose
    private String manufacturedYear;
    @SerializedName("machinetype")
    @Expose
    private String machinetype;
    @SerializedName("machine_code")
    @Expose
    private String machineCode;
    @SerializedName("disel_tank_capacity")
    @Expose
    private String diselTankCapacity;
    @SerializedName("provider_name")
    @Expose
    private String providerName;
    @SerializedName("provider_contact_number")
    @Expose
    private String providerContactNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("deployedStrutureId")
    @Expose
    private String deployedStrutureId;

    public String isMeterWorking() {
        return isMeterWorking;
    }

    public void setMeterWorking(String meterWorking) {
        isMeterWorking = meterWorking;
    }

    public String getRtoNumber() {
        return rtoNumber;
    }

    public void setRtoNumber(String rtoNumber) {
        this.rtoNumber = rtoNumber;
    }

    public String getChasisNumber() {
        return chasisNumber;
    }

    public void setChasisNumber(String chasisNumber) {
        this.chasisNumber = chasisNumber;
    }

    public String getExcavationCapacity() {
        return excavationCapacity;
    }

    public void setExcavationCapacity(String excavationCapacity) {
        this.excavationCapacity = excavationCapacity;
    }

    @SerializedName("isMeterWorking")
    @Expose
    private String isMeterWorking;
    @SerializedName("rtoNumber")
    @Expose
    private String rtoNumber;
    @SerializedName("chasisNumber")
    @Expose
    private String chasisNumber;
    @SerializedName("excavationCapacity")
    @Expose
    private String excavationCapacity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getManufacturedYear() {
        return manufacturedYear;
    }

    public void setManufacturedYear(String manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getDiselTankCapacity() {
        return diselTankCapacity;
    }

    public void setDiselTankCapacity(String diselTankCapacity) {
        this.diselTankCapacity = diselTankCapacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDeployedStrutureId() {
        return deployedStrutureId;
    }

    public void setDeployedStrutureId(String deployedStrutureId) {
        this.deployedStrutureId = deployedStrutureId;
    }
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderContactNumber() {
        return providerContactNumber;
    }

    public void setProviderContactNumber(String providerContactNumber) {
        this.providerContactNumber = providerContactNumber;
    }
}
