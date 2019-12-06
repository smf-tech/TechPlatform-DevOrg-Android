package com.octopus.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopus.models.SujalamSuphalam.StructurePripretionData;

import java.util.List;

@Dao
public interface StructurePripretionDataDao {
    @Query("SELECT * FROM StructurePripretionData")
    List<StructurePripretionData> getAllStructure();

    @Query("DELETE FROM StructurePripretionData")
    void deleteAll();

    @Query("DELETE FROM StructurePripretionData WHERE id  = :id")
    void delete(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StructurePripretionData structureData);
}
