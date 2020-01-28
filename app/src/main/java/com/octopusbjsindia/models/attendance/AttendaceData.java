package com.octopusbjsindia.models.attendance;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "UserAttendance")
public class AttendaceData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int uid;
    @SerializedName("lattitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("attendanceId")
    public String attendanceId;
    public Long attendaceDate;
    @SerializedName("type")
    public String attendanceType;
    @SerializedName("dates")
    public Long date;
    public String attendanceFormattedDate;
    public String Address;
    @SerializedName("isSync")
    public Boolean isSync;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String mobileNumber;

    public String getAttendanceFormattedDate() {
        return attendanceFormattedDate;
    }

    public void setAttendanceFormattedDate(String attendanceFormattedDate) {
        this.attendanceFormattedDate = attendanceFormattedDate;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getTime() {
        return date;
    }

    public void setTime(Long date) {
        this.date = date;
    }

    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Long getAttendaceDate() {
        return attendaceDate;
    }

    public void setAttendaceDate(Long attendaceDate) {
        this.attendaceDate = attendaceDate;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }


}
