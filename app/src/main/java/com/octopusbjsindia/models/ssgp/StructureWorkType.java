package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructureWorkType {
    @SerializedName("structure_type")
    @Expose
    private String structureType;
    @SerializedName("number_structure_type")
    @Expose
    private String numberStructureType;

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getNumberStructureType() {
        return numberStructureType;
    }

    public void setNumberStructureType(String numberStructureType) {
        this.numberStructureType = numberStructureType;
    }
}
