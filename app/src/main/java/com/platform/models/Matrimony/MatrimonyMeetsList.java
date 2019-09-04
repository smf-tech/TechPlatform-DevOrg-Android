package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatrimonyMeetsList {

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
