package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.attendance.AttendaceCheckOut;

import java.util.List;
@Dao
public interface UserCheckOutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(AttendaceCheckOut... attendaceCheckOuts);
    @Query("SELECT * FROM UserChheckOutAttendance WHERE attendanceType=:type AND attendanceFormattedDate=:date AND mobileNumber=:mobile")
    public List<AttendaceCheckOut> getCheckOutData(String type, String date,String mobile);
}
