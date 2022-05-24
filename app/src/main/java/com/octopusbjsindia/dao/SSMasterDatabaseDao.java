package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;

import java.util.List;

@Dao
public interface SSMasterDatabaseDao {
    @Query("SELECT * FROM SSMasterDatabase where type = :type")
    List<SSMasterDatabase> getSSMasterData(String type);

    @Query("DELETE FROM SSMasterDatabase")
    void deleteSSMasterData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SSMasterDatabase ssMasterDatabase);

    @Update
    void update(SSMasterDatabase ssMasterDatabase);
}
