package com.platform.models.Operator;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class OperatorRequestResponseModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    @ColumnInfo(name ="machine_id")
    @SerializedName("machine_id")
    @Expose
    private String machine_id;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_code")
    @Expose
    private String status_code;

    @SerializedName("workTime")
    @Expose
    private String workTime;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("meter_reading")
    @Expose
    private String meter_reading;
    @SerializedName("hours")
    @Expose
    private Integer hours;
    @SerializedName("totalHours")
    @Expose
    private Integer totalHours;
    @SerializedName("reason_id")
    @Expose
    private String reasonId;


    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMeter_reading() {
        return meter_reading;
    }

    public void setMeter_reading(String meter_reading) {
        this.meter_reading = meter_reading;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }
}
