package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationSchedule {
    @SerializedName("regStartDateTime")
    @Expose
    private long regStartDateTime;
    @SerializedName("regEndDateTime")
    @Expose
    private long regEndDateTime;

    public long getRegStartDateTime() {
        return regStartDateTime;
    }

    public void setRegStartDateTime(long regStartDateTime) {
        this.regStartDateTime = regStartDateTime;
    }

    public long getRegEndDateTime() {
        return regEndDateTime;
    }

    public void setRegEndDateTime(long regEndDateTime) {
        this.regEndDateTime = regEndDateTime;
    }
}
