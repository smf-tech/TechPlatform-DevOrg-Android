package com.octopusbjsindia.models.SujalamSuphalam;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class StructureBoundaryData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    private int id;
    @ColumnInfo(name = "structureId")
    @SerializedName("structureId")
    @Expose
    private String structureId;
    @ColumnInfo(name = "structureBoundary")
    @SerializedName("structureBoundary")
    @Expose
    private String structureBoundary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public String getStructureBoundary() {
        return structureBoundary;
    }

    public void setStructureBoundary(String structureBoundary) {
        this.structureBoundary = structureBoundary;
    }
}
