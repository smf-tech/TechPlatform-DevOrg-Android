package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructurePripretionRequest {
    @SerializedName("structure_id")
    @Expose
    private String structureId;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double _long;
    @SerializedName("ff_identified")
    @Expose
    private Boolean ffIdentified;
    @SerializedName("ff_name")
    @Expose
    private String ffName;
    @SerializedName("ff_mobile_number")
    @Expose
    private String ffMobileNumber;
    @SerializedName("ff_traning_done")
    @Expose
    private Boolean ffTraningDone;
    @SerializedName("is_structure_fit")
    @Expose
    private Boolean isStructureFit;
    @SerializedName("reason")
    @Expose
    private String reason;

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

}
