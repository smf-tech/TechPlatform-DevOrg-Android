package com.octopus.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MasterDataList implements Serializable {
    @SerializedName("form")
    @Expose
    private String form;
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("data")
    @Expose
    private List<MasterDataValue> data = null;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<MasterDataValue> getData() {
        return data;
    }

    public void setData(List<MasterDataValue> data) {
        this.data = data;
    }

}
