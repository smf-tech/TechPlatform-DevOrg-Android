package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.SujalamSuphalam.StructureBoundaryData;

import java.util.List;

@Dao
public interface StructureBoundaryDao {
    @Query("SELECT * FROM StructureBoundaryData")
    List<StructureBoundaryData> getAllStructure();

    @Query("DELETE FROM StructureBoundaryData")
    void deleteAll();

    @Query("DELETE FROM StructureBoundaryData WHERE structureId  = :structureId")
    void delete(String structureId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StructureBoundaryData structureData);
}
