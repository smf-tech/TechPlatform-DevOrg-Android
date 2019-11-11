package com.platform.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OperatorMachineDataResponseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private OperatorMachineData operatorMachineData;
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

    public OperatorMachineData getOperatorMachineData() {
        return operatorMachineData;
    }

    public void setOperatorMachineData(OperatorMachineData operatorMachineData) {
        this.operatorMachineData = operatorMachineData;
    }
}
