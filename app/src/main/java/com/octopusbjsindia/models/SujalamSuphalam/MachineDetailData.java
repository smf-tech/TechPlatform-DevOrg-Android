package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineDetailData {

    @SerializedName("machine")
    @Expose
    private MachineData machine;
    @SerializedName("provider_information")
    @Expose
    private ProviderInformation providerInformation;
    @SerializedName("operator_details")
    @Expose
    private OperatorDetails operatorDetails;
    @SerializedName("mou_details")
    @Expose
    private MouDetails mouDetails;
    @SerializedName("lat")
    @Expose
    private String formLat;
    @SerializedName("long")
    @Expose
    private String formLong;

    public MachineData getMachine() {
        return machine;
    }

    public void setMachine(MachineData machine) {
        this.machine = machine;
    }

    public ProviderInformation getProviderInformation() {
        return providerInformation;
    }

    public void setProviderInformation(ProviderInformation providerInformation) {
        this.providerInformation = providerInformation;
    }

    public OperatorDetails getOperatorDetails() {
        return operatorDetails;
    }

    public void setOperatorDetails(OperatorDetails operatorDetails) {
        this.operatorDetails = operatorDetails;
    }

    public MouDetails getMouDetails() {
        return mouDetails;
    }

    public void setMouDetails(MouDetails mouDetails) {
        this.mouDetails = mouDetails;
    }

    public String getFormLat() {
        return formLat;
    }

    public void setFormLat(String formLat) {
        this.formLat = formLat;
    }

    public String getFormLong() {
        return formLong;
    }

    public void setFormLong(String formLong) {
        this.formLong = formLong;
    }
}
