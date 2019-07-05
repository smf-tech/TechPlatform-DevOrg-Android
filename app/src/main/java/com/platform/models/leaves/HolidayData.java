package com.platform.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("holiday_date")
    @Expose
    private Integer holidayDate;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Integer holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
