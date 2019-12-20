package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorMachineData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private OperatorMachineCodeDataModel OperatorMachineCodeDataModel;
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

    public com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel getOperatorMachineCodeDataModel() {
        return OperatorMachineCodeDataModel;
    }

    public void setOperatorMachineCodeDataModel(com.octopusbjsindia.models.Operator.OperatorMachineCodeDataModel operatorMachineCodeDataModel) {
        OperatorMachineCodeDataModel = operatorMachineCodeDataModel;
    }
}
