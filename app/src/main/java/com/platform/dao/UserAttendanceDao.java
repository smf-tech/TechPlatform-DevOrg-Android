package com.platform.dao;

import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.platform.models.attendance.AttendaceData;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserAttendanceDao {
    @Insert
    public void insert(AttendaceData...attendaceData);
    @Update
    public void update(AttendaceData...attendaceData);
    @Delete
    public void delete(AttendaceData... attendaceData);
    @Query("SELECT * FROM userattendance")
    public List<AttendaceData> getAttendanceList();
    @Query("SELECT * FROM userattendance WHERE uid =:userId")
    public List<AttendaceData> getUserAttendace(String userId);
    @Query("SELECT * FROM userattendance WHERE isSync=:isonline")
    public List<AttendaceData> getUserAttendace(Boolean isonline);
    @Query("SELECT * FROM userattendance WHERE attendanceType=:type AND attendanceFormattedDate=:formatdate")
    public List<AttendaceData> getUserAttendanceType(String type,String formatdate);
    @Query("SELECT uid FROM userattendance WHERE attendanceFormattedDate=:cdate")
    public String getUserId(String cdate);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  void insertAllCID(List<AttendaceData> cidVos);


}
