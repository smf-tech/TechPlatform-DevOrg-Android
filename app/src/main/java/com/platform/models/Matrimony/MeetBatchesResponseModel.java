package com.platform.models.Matrimony;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetBatchesResponseModel {

    @SerializedName("Group")
    @Expose
    private List<Group> group = null;

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

}