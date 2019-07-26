
package com.platform.models.attendance;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date implements Serializable
{

    @SerializedName("numberLong")
    @Expose
    private String numberLong;
    private final static long serialVersionUID = 8174785920741986217L;

    public String getNumberLong() {
        return numberLong;
    }

    public void setNumberLong(String numberLong) {
        this.numberLong = numberLong;
    }

}
