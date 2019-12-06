package com.octopus.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopus.models.SujalamSuphalam.StructureData;

import java.util.List;


@Dao
public interface StructureDataDao {

    @Query("SELECT * FROM StructureData")
    List<StructureData> getAllStructure();


    @Query("DELETE FROM StructureData WHERE structureId  = :id")
    void delete(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StructureData structureData);
}
