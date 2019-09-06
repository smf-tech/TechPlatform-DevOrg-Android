package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatrimonyMeetsList implements Serializable {

    @SerializedName("meets")
    @Expose
    private List<MatrimonyMeet> meets = null;

    public List<MatrimonyMeet> getMeets() {
        return meets;
    }

    public void setMeets(List<MatrimonyMeet> meets) {
        this.meets = meets;
    }

}
