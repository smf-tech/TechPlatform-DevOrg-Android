package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.notifications.NotificationData;
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
