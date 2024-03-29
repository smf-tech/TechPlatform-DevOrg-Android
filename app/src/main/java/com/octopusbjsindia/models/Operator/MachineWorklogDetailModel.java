package com.octopusbjsindia.models.Operator;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineWorklogDetailModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<MachineWorklogDetail> machineWorklogDetails = null;
    @SerializedName("code")
    @Expose
    private Integer code;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<MachineWorklogDetail> getMachineWorklogDetails() {
        return machineWorklogDetails;
    }

    public void setMachineWorklogDetails(List<MachineWorklogDetail> machineWorklogDetails) {
        this.machineWorklogDetails = machineWorklogDetails;
    }
}
