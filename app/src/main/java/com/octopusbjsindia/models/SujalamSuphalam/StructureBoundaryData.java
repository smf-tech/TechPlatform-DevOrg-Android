package com.octopusbjsindia.models.SujalamSuphalam;

import androidx.room.ColumnInfo;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StructureBoundaryData {

    @SerializedName("structureId")
    @Expose
    private String structureId;
    @SerializedName("structureBoundary")
    @Expose
    private ArrayList<LatLng> structureBoundary;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public ArrayList<LatLng> getStructureBoundary() {
        return structureBoundary;
    }

    public void setStructureBoundary(ArrayList<LatLng> structureBoundary) {
        this.structureBoundary = structureBoundary;
    }
}
