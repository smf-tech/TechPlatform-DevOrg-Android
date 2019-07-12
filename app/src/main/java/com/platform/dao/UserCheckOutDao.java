package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.platform.models.attendance.AttendaceCheckOut;

import java.util.List;
@Dao
public interface UserCheckOutDao {

    @Insert
    public void insert(AttendaceCheckOut... attendaceCheckOuts);
    @Query("SELECT * FROM UserChheckOutAttendance WHERE attendanceType=:type AND attendanceFormattedDate=:date")
    public List<AttendaceCheckOut> getCheckOutData(String type, String date);
}
