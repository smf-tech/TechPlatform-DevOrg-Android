
package com.octopusbjsindia.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedOn implements Serializable
{

    @SerializedName("date")
    @Expose
    private Date date;
    private final static long serialVersionUID = -9120567158504790767L;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
