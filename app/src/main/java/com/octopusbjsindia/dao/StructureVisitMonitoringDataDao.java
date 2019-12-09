package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.SujalamSuphalam.StructureVisitMonitoringData;

import java.util.List;

@Dao
public interface StructureVisitMonitoringDataDao {
    @Query("SELECT * FROM StructureVisitMonitoringData")
    List<StructureVisitMonitoringData> getAllStructure();

    @Query("DELETE FROM StructureVisitMonitoringData")
    void deleteAll();

    @Query("DELETE FROM StructureVisitMonitoringData WHERE id  = :id")
    void delete(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StructureVisitMonitoringData structureData);
}
