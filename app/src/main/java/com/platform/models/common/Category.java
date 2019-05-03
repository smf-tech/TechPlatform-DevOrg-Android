package com.platform.models.common;

import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.database.DataConverter;
import com.platform.models.LocaleData;

@SuppressWarnings("unused")
public class Category {

    @SerializedName("_id")
    @Expose
    private String id;

    @TypeConverters(DataConverter.class)
    @SerializedName("name")
    @Expose
    private LocaleData name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocaleData getName() {
        return name;
    }

    public void setName(LocaleData name) {
        this.name = name;
    }
}
