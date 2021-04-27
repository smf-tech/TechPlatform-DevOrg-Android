package com.octopusbjsindia.models.MissionRahat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HospitalDetailsResponeModel {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private HospitalDetailsRespone hospitalDetailsRespone;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HospitalDetailsRespone getHospitalDetailsRespone() {
        return hospitalDetailsRespone;
    }

    public void setHospitalDetailsRespone(HospitalDetailsRespone hospitalDetailsRespone) {
        this.hospitalDetailsRespone = hospitalDetailsRespone;
    }
}