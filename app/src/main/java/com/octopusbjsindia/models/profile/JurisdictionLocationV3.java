package com.octopusbjsindia.models.profile;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
@Entity
public class JurisdictionLocationV3 {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "autoId")
    @NonNull
    @Expose
    private int autoId;

    @ColumnInfo(name = "id")
    @Expose
    @SerializedName("_id")
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "parent_id")
    @SerializedName("parent_id")
    @Expose
    private String parentLocationId;

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(String parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

}
