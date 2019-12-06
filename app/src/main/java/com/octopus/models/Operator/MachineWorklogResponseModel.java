package com.octopus.models.Operator;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineWorklogResponseModel {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("totalWorkHrs")
    @Expose
    private String totalWorkHrs;

    @SerializedName("data")
    @Expose
    private List<MachineWorklogList> machineWorklogList = null;
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

    public List<MachineWorklogList> getMachineWorklogList() {
        return machineWorklogList;
    }

    public void setMachineWorklogList(List<MachineWorklogList> machineWorklogList) {
        this.machineWorklogList = machineWorklogList;
    }

    public String getTotalWorkHrs() {
        return totalWorkHrs;
    }

    public void setTotalWorkHrs(String totalWorkHrs) {
        this.totalWorkHrs = totalWorkHrs;
    }
}