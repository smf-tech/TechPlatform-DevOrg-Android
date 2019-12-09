package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorMachineData {

    @SerializedName("machine_id")
    @Expose
    private String machine_id;
    @SerializedName("machine_code")
    @Expose
    private String machine_code;
    @SerializedName("nonutilisationTypeData")
    @Expose
    private List<NonutilisationTypeDataList> nonutilisationTypeData = null;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public List<NonutilisationTypeDataList> getNonutilisationTypeData() {
        return nonutilisationTypeData;
    }

    public void setNonutilisationTypeData(List<NonutilisationTypeDataList> nonutilisationTypeData) {
        this.nonutilisationTypeData = nonutilisationTypeData;
    }

}
