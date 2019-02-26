package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.platform.models.reports.ReportData;

import java.util.List;

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
