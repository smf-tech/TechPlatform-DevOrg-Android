package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.platform.models.pm.ProcessData;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface ProcessDataDao {
    @Query("SELECT * FROM processdata where id = :processId")
    ProcessData getProcessData(String processId);

    @Query("SELECT * FROM processdata")
    List<ProcessData> getAllProcesses();

    @Query("SELECT submit_count FROM processdata where id = :processId")
    String getSubmitCount(String processId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ProcessData... processData);

    @Query("DELETE FROM processdata")
    void deleteAllProcesses();

    @Query("UPDATE processdata SET submit_count = :count where id = :processId")
    void updateSubmitCount(String processId, String count);
}
