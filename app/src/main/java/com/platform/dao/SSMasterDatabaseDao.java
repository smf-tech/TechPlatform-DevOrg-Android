package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.platform.models.SujalamSuphalam.SSMasterDatabase;

import java.util.List;

@Dao
public interface SSMasterDatabaseDao {
    @Query("SELECT * FROM SSMasterDatabase")
    List<SSMasterDatabase> getSSMasterData();

    @Query("DELETE FROM SSMasterDatabase")
    void deleteSSMasterData();

    @Query("DELETE FROM NotificationData WHERE id  = :id")
    void deleteNotification(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SSMasterDatabase ssMasterDatabase);

    @Update
    void update(SSMasterDatabase ssMasterDatabase);
}
