package com.octopus.models.home;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopus.database.DataConverter;
import com.octopus.models.LocaleData;

import java.io.Serializable;

@SuppressWarnings("unused")
@Entity
public class Modules implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    private int id;

    @ColumnInfo(name = "module_id")
    @SerializedName("_id")
    @Expose
    private String moduleId;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private LocaleData name;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String moduleType;

    @ColumnInfo(name = "weblink")
    @SerializedName("weblink")
    @Expose
    private String weblink;

    @ColumnInfo(name = "module")
    @Expose
    private String module;

    @Ignore
    private boolean isActive;
    @Ignore
    private int resId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String id) {
        this.moduleId = id;
    }

    public LocaleData getName() {
        return name;
    }

    public void setName(LocaleData name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }
}
