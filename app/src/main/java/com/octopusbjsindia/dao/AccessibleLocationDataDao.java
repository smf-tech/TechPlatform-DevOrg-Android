package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.profile.JurisdictionLocationV3;

import java.util.List;

@Dao
public interface AccessibleLocationDataDao {

    @Query("SELECT * FROM JurisdictionLocationV3 WHERE parent_id  = :parentId")
    List<JurisdictionLocationV3> getAccessibleLocationData(String parentId);

    @Query("DELETE FROM JurisdictionLocationV3")
    void deleteAccessibleLocationData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JurisdictionLocationV3 jurisdictionLocationV3);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<JurisdictionLocationV3> jurisdictionLocationV3);

}
