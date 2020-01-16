package com.octopusbjsindia.models.SujalamSuphalam;

import androidx.room.Ignore;

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
    @Ignore
    private String ownedByValue;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @SerializedName("taluka")
    @Expose
    private String taluka;
    @SerializedName("talukaId")
    @Expose
    private String talukaId;
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
    @SerializedName("tc_name")
    @Expose
    private String tcName;
    @SerializedName("tc_contact_number")
    @Expose
    private String tcContactNumber;
    @SerializedName("machine_location")
    @Expose
    private String machineLocation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;
    @SerializedName("deployedStrutureId")
    @Expose
    private String deployedStrutureId;
    @SerializedName("deployedStrutureCode")
    @Expose
    private String deployedStrutureCode;
    @SerializedName("operator_name")
    @Expose
    private String operatorName;
    @SerializedName("operator_contact_number")
    @Expose
    private String operatorContactNumber;
    @SerializedName("haltReason")
    @Expose
    private String haltReason;
    @SerializedName("mouURL")
    @Expose
    private String mouURL;
    @SerializedName("isMouUploaded")
    @Expose
    private Boolean isMouUploaded;

    public Boolean getMouUploaded() {
        return isMouUploaded;
    }

    public void setMouUploaded(Boolean mouUploaded) {
        isMouUploaded = mouUploaded;
    }

    public Boolean getMachineSignOff() {
        return isMachineSignOff;
    }

    public void setMachineSignOff(Boolean machineSignOff) {
        isMachineSignOff = machineSignOff;
    }

    @SerializedName("isMachineSignOff")
    @Expose
    private Boolean isMachineSignOff;

    public String getMachineLocation() {
        return machineLocation;
    }

    public void setMachineLocation(String machineLocation) {
        this.machineLocation = machineLocation;
    }

    public String getMachineMobileNumber() {
        return machineMobileNumber;
    }

    public void setMachineMobileNumber(String machineMobileNumber) {
        this.machineMobileNumber = machineMobileNumber;
    }

    @SerializedName("machine_mobile_number")
    @Expose
    private String machineMobileNumber;
    @SerializedName("updatedDate")
    @Expose
    private String lastUpdatedTime;
    @SerializedName("is_meter_working")
    @Expose
    private String isMeterWorking;
    @SerializedName("RTO_numner")
    @Expose
    private String rtoNumber;
    @SerializedName("chassis_no")
    @Expose
    private String chasisNo;

    public String getIsMeterWorking() {
        return isMeterWorking;
    }

    public void setIsMeterWorking(String isMeterWorking) {
        this.isMeterWorking = isMeterWorking;
    }

    public String getRtoNumber() {
        return rtoNumber;
    }

    public void setRtoNumber(String rtoNumber) {
        this.rtoNumber = rtoNumber;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public String getExcavationCapacity() {
        return excavationCapacity;
    }

    public void setExcavationCapacity(String excavationCapacity) {
        this.excavationCapacity = excavationCapacity;
    }

    @SerializedName("excavation_capacity")
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

    public String getOwnedByValue() {
        return ownedByValue;
    }

    public void setOwnedByValue(String ownedByValue) {
        this.ownedByValue = ownedByValue;
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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
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

    public String getDeployedStrutureCode() {
        return deployedStrutureCode;
    }

    public void setDeployedStrutureCode(String deployedStrutureCode) {
        this.deployedStrutureCode = deployedStrutureCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorContactNumber() {
        return operatorContactNumber;
    }

    public void setOperatorContactNumber(String operatorContactNumber) {
        this.operatorContactNumber = operatorContactNumber;
    }

    public String getProviderContactNumber() {
        return providerContactNumber;
    }

    public void setProviderContactNumber(String providerContactNumber) {
        this.providerContactNumber = providerContactNumber;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getHaltReason() {
        return haltReason;
    }

    public void setHaltReason(String haltReason) {
        this.haltReason = haltReason;
    }

    public String getMouURL() {
        return mouURL;
    }

    public void setMouURL(String mouURL) {
        this.mouURL = mouURL;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    public String getTcContactNumber() {
        return tcContactNumber;
    }

    public void setTcContactNumber(String tcContactNumber) {
        this.tcContactNumber = tcContactNumber;
    }
}
