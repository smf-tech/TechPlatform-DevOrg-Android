package com.octopusbjsindia.models.SujalamSuphalam;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class StructurePripretionData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    private int id;
    @ColumnInfo(name = "structure_id")
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    @Expose
    private double lat;
    @ColumnInfo(name = "long")
    @SerializedName("long")
    @Expose
    private double _long;
    @ColumnInfo(name = "ff_identified")
    @SerializedName("ff_identified")
    @Expose
    private Boolean ffIdentified;
    @ColumnInfo(name = "ff_name")
    @SerializedName("ff_name")
    @Expose
    private String ffName;
    @ColumnInfo(name = "ff_mobile_number")
    @SerializedName("ff_mobile_number")
    @Expose
    private String ffMobileNumber;
    @ColumnInfo(name = "ff_traning_done")
    @SerializedName("ff_traning_done")
    @Expose
    private Boolean ffTraningDone;
    @ColumnInfo(name = "is_structure_fit")
    @SerializedName("is_structure_fit")
    @Expose
    private Boolean isStructureFit;
    @ColumnInfo(name = "reason")
    @SerializedName("reason")
    @Expose
    private String reason;
    @ColumnInfo(name = "structureImg1")
    private String structureImg1;
    @ColumnInfo(name = "structureImg2")
    private String structureImg2;
    @ColumnInfo(name = "beneficiary_id")
    @SerializedName("beneficiary_id")
    @Expose
    private String beneficiary_id;

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return _long;
    }

    public void setLong(double _long) {
        this._long = _long;
    }

    public Boolean getFfIdentified() {
        return ffIdentified;
    }

    public void setFfIdentified(Boolean ffIdentified) {
        this.ffIdentified = ffIdentified;
    }

    public String getFfName() {
        return ffName;
    }

    public void setFfName(String ffName) {
        this.ffName = ffName;
    }

    public String getFfMobileNumber() {
        return ffMobileNumber;
    }

    public void setFfMobileNumber(String ffMobileNumber) {
        this.ffMobileNumber = ffMobileNumber;
    }

    public Boolean getFfTraningDone() {
        return ffTraningDone;
    }

    public void setFfTraningDone(Boolean ffTraningDone) {
        this.ffTraningDone = ffTraningDone;
    }

    public Boolean getIsStructureFit() {
        return isStructureFit;
    }

    public void setIsStructureFit(Boolean isStructureFit) {
        this.isStructureFit = isStructureFit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStructureImg1() {
        return structureImg1;
    }

    public void setStructureImg1(String structureImg1) {
        this.structureImg1 = structureImg1;
    }

    public String getStructureImg2() {
        return structureImg2;
    }

    public void setStructureImg2(String structureImg2) {
        this.structureImg2 = structureImg2;
    }

    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(String beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }
}
