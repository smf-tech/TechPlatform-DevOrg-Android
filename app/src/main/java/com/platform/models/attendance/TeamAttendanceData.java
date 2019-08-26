package com.platform.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamAttendanceData {

    @SerializedName("name")
    @Expose
    private String name;
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
    private List<Object> checkIn = null;
    @SerializedName("check_out")
    @Expose
    private List<Object> checkOut = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Object> getCheckIn() {
        return checkIn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setCheckIn(List<Object> checkIn) {
        this.checkIn = checkIn;
    }

    public List<Object> getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(List<Object> checkOut) {
        this.checkOut = checkOut;
    }


}
