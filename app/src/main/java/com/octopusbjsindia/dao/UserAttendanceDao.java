package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.octopusbjsindia.models.attendance.AttendaceData;

import java.util.List;

@Dao
public interface UserAttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(AttendaceData...attendaceData);

    @Update
    public void update(AttendaceData...attendaceData);

    @Query("UPDATE userattendance SET attendanceId =:attendanceId, isSync =:isSync WHERE attendaceDate=:attendanceDate AND attendanceType=:attendanceType")
    public void updateUserAttendace(String attendanceId, Boolean isSync, long attendanceDate, String attendanceType);

    @Delete
    public void delete(AttendaceData... attendaceData);

    @Query("SELECT * FROM userattendance")
    public List<AttendaceData> getAttendance();

    @Query("SELECT * FROM userattendance WHERE isSync=:isonline")
    public List<AttendaceData> getUnsyncAttendance(Boolean isonline);

    @Query("SELECT * FROM userattendance WHERE attendanceType=:type AND attendanceFormattedDate=:formatdate AND mobileNumber=:mobile")
    public List<AttendaceData> getUserAttendanceType(String type,String formatdate,String mobile);

    @Query("SELECT * FROM userattendance WHERE attendaceDate=:attendanceDate AND attendanceType=:attendanceType")
    public AttendaceData getUserAttendace(long attendanceDate, String attendanceType);

    @Query("DELETE FROM userattendance")
    void deleteAllAttendance();

}
