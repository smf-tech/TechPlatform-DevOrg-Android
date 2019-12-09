
package com.octopusbjsindia.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date_ implements Serializable
{

    @SerializedName("date")
    @Expose
    private Date__ date;
    private final static long serialVersionUID = -1710317645471141644L;

    public Date__ getDate() {
        return date;
    }

    public void setDate(Date__ date) {
        this.date = date;
    }

}
