package com.octopusbjsindia.models.smartgirl;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Additional_master_trainer {

    @SerializedName("state_id")
    @Expose
    private String state_id;
    @SerializedName("district_id")
    @Expose
    private String district_id;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("user_phone")
    @Expose
    private String user_phone;
    @SerializedName("state_obj")
    @Expose
    private State_obj state_obj;
    @SerializedName("district_obj")
    @Expose
    private District_obj district_obj;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public State_obj getState_obj() {
        return state_obj;
    }

    public void setState_obj(State_obj state_obj) {
        this.state_obj = state_obj;
    }

    public District_obj getDistrict_obj() {
        return district_obj;
    }

    public void setDistrict_obj(District_obj district_obj) {
        this.district_obj = district_obj;
    }

}


    /*

    @SerializedName("state_id")
    @Expose
    private String state_id;
    @SerializedName("state_name")
    @Expose
    private String state_name;
    @SerializedName("district_id")
    @Expose
    private String district_id;
    @SerializedName("district_name")
    @Expose
    private String district_name;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("user_name")
    @Expose
    private String user_name;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
*/
