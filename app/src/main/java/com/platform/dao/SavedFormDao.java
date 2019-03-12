package com.platform.dao;

import com.platform.models.SavedForm;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@SuppressWarnings("unused")
@Dao
public interface SavedFormDao {
    @Query("SELECT * FROM savedform WHERE is_synced = 0")
    List<SavedForm> getAllNonSyncedForms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SavedForm... savedForms);

    @Update
    void update(SavedForm savedForms);

    @Delete
    void delete(SavedForm savedForm);
}
