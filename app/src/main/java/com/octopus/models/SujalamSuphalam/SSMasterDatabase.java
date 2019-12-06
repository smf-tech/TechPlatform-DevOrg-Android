package com.octopus.models.SujalamSuphalam;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class SSMasterDatabase {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Expose
    private int id;
    @ColumnInfo(name = "data")
    private String data;
    @ColumnInfo(name = "dateTime")
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
