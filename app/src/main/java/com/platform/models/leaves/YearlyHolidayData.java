package com.platform.models.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YearlyHolidayData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("holiday_date")
    @Expose
    private long holidayDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(long holidayDate) {
        this.holidayDate = holidayDate;
    }

}
