package com.octopusbjsindia.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamAttendanceData {

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("check_in")
    @Expose
    private CheckIn checkIn = null;
    @SerializedName("check_out")
    @Expose
    private CheckIn checkOut = null;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public CheckIn getCheckIn() {
        return checkIn;
    }



    public void setCheckIn(CheckIn checkIn) {
        this.checkIn = checkIn;
    }

    public CheckIn getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(CheckIn checkOut) {
        this.checkOut = checkOut;
    }


}
