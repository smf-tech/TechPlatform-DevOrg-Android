package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GpStructureList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("code")
    @Expose
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
