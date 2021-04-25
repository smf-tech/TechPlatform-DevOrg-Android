package com.octopusbjsindia.models.MissionRahat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MachineModel implements Serializable {
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("machine_model")
    @Expose
    private String machineModel;
    @SerializedName("machine_capacity")
    @Expose
    private String mahineCapacity;
    @SerializedName("doner_name")
    @Expose
    private String donerName;
}
