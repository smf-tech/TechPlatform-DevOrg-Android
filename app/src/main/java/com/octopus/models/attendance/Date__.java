
package com.octopus.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date__ implements Serializable
{

    @SerializedName("numberLong")
    @Expose
    private String numberLong;
    private final static long serialVersionUID = 685403576206082896L;

    public String getNumberLong() {
        return numberLong;
    }

    public void setNumberLong(String numberLong) {
        this.numberLong = numberLong;
    }

}
