package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VDFFRequest {
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("village_id")
    @Expose
    private String villageId;
    @SerializedName("type_n_work_structure")
    @Expose
    private List<StructureWorkType> typeNWorkStructure = null;
    @SerializedName("machine_demand_hr")
    @Expose
    private String machineDemandHr;
    //    @SerializedName("machine_demand_type")     //TODO Removed
//    @Expose
//    private String machineDemandType;
//    @SerializedName("machine_demand_numbers")
//    @Expose
//    private String machineDemandNumbers;
    @SerializedName("machineDemand")
    @Expose
    private String nodalPersonName;
    @SerializedName("nodal_person_number")
    @Expose
    private String nodalPersonNumber;
    @SerializedName("demand_letter_image")
    @Expose
    private ArrayList<String> demandLetterImage; //TODO changed String to ArrayList<String>
    @SerializedName("backhoe_count")             //TODO new added
    @Expose
    private String backhoeCount;
    @SerializedName("excavators_count")          //TODO new added
    @Expose
    private String excavatorsCount;
    @SerializedName("machine_transportation")
    @Expose
    private String machineTransportation;
    @SerializedName("gp_image")
    @Expose
    private String gpImage;
    @SerializedName("is_start_work_immediately")
    @Expose
    private String isStartWorkImmediately;
    @SerializedName("reason_not_start")
    @Expose
    private String reasonNotStart;
    @SerializedName("future_date")
    @Expose
    private String futureDate;
    @SerializedName("no_structure_work")
    @Expose
    private String noStructureWork;
    @SerializedName("comment")
    @Expose
    private String comment;

//    @SerializedName("ho_remark")                           //TODO Removed
//    @Expose
//    private String hoRemark;
//
//
//    public String getHoRemark() {
//        return hoRemark;
//    }
//
//    public void setHoRemark(String hoRemark) {
//        this.hoRemark = hoRemark;
//    }

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

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public List<StructureWorkType> getTypeNWorkStructure() {
        return typeNWorkStructure;
    }

    public void setTypeNWorkStructure(List<StructureWorkType> typeNWorkStructure) {
        this.typeNWorkStructure = typeNWorkStructure;
    }

    public String getMachineDemandHr() {
        return machineDemandHr;
    }

    public void setMachineDemandHr(String machineDemandHr) {
        this.machineDemandHr = machineDemandHr;
    }

    public String getBackhoeCount() {
        return backhoeCount;
    }

    public void setBackhoeCount(String  backhoeCount) {
        this.backhoeCount = backhoeCount;
    }

    public String getExcavatorsCount() {
        return excavatorsCount;
    }

    public void setExcavatorsCount(String excavatorsCount) {
        this.excavatorsCount = excavatorsCount;
    }

    public ArrayList<String> getDemandLetterImage() {
        return demandLetterImage;
    }

    public void setDemandLetterImage(ArrayList<String> demandLetterImage) {
        this.demandLetterImage = demandLetterImage;
    }

    public String getNodalPersonName() {
        return nodalPersonName;
    }

    public void setNodalPersonName(String nodalPersonName) {
        this.nodalPersonName = nodalPersonName;
    }

    public String getNodalPersonNumber() {
        return nodalPersonNumber;
    }

    public void setNodalPersonNumber(String nodalPersonNumber) {
        this.nodalPersonNumber = nodalPersonNumber;
    }

    public String getMachineTransportation() {
        return machineTransportation;
    }

    public void setMachineTransportation(String machineTransportation) {
        this.machineTransportation = machineTransportation;
    }

    public String getGpImage() {
        return gpImage;
    }

    public void setGpImage(String gpImage) {
        this.gpImage = gpImage;
    }

    public String getIsStartWorkImmediately() {
        return isStartWorkImmediately;
    }

    public void setIsStartWorkImmediately(String isStartWorkImmediately) {
        this.isStartWorkImmediately = isStartWorkImmediately;
    }

    public String getReasonNotStart() {
        return reasonNotStart;
    }

    public void setReasonNotStart(String reasonNotStart) {
        this.reasonNotStart = reasonNotStart;
    }

    public String getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(String futureDate) {
        this.futureDate = futureDate;
    }

    public String getNoStructureWork() {
        return noStructureWork;
    }

    public void setNoStructureWork(String noStructureWork) {
        this.noStructureWork = noStructureWork;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
