package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.profile.JurisdictionLocation;

import java.util.List;

@Dao
public interface AccessibleLocationDataDao {

    @Query("SELECT * FROM JurisdictionLocation WHERE parent_id  = :parentId")
    List<JurisdictionLocation> getAccessibleLocationData(String parentId);

    @Query("DELETE FROM JurisdictionLocation")
    void deleteAccessibleLocationData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JurisdictionLocation jurisdictionLocation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<JurisdictionLocation> jurisdictionLocation);

}
