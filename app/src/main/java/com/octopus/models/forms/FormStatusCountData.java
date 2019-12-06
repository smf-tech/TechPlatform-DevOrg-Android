package com.octopus.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormStatusCountData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("count")
    @Expose
    private Integer count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
