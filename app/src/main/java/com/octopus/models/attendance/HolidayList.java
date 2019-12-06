
package com.octopus.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HolidayList implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Date")
    @Expose
    private Date_ date;
    private final static long serialVersionUID = 1794223305409098069L;

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

    public Date_ getDate() {
        return date;
    }

    public void setDate(Date_ date) {
        this.date = date;
    }

}
