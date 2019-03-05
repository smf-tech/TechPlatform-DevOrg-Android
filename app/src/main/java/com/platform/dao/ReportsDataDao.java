package com.platform.dao;

import com.platform.models.reports.ReportData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@SuppressWarnings("unused")
@Dao
public interface ReportsDataDao {
    @Query("SELECT * FROM reportdata")
    List<ReportData> getAllReports();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ReportData... formData);

    @Update
    void update(ReportData formData);

    @Delete
    void delete(ReportData formData);
}
