package com.octopusbjsindia.models.profile;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
@Entity
public class JurisdictionLocation {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("_id")
    @Expose
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = "parent_id")
    @SerializedName("parent_id")
    @Expose
    private String parentLocationId;

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
