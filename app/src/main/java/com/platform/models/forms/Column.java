package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.LocaleData;

@SuppressWarnings("unused")
public class Column {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private LocaleData title;
    @SerializedName("cellType")
    @Expose
    private String cellType;
    @SerializedName("inputType")
    @Expose
    private String inputType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public LocaleData getTitle() {
        return title;
    }

    public void setTitle(LocaleData title) {
        this.title = title;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
