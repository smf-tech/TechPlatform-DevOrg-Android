package com.octopus.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.octopus.models.reports.ReportData;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface ReportsDataDao {
    @Query("SELECT * FROM reportdata")
    List<ReportData> getAllReports();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ReportData... formData);

    @Query("DELETE FROM reportdata")
    void deleteAllReports();

    @Update
    void update(ReportData formData);

    @Delete
    void delete(ReportData formData);
}
